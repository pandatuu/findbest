package app.findbest.vip.message.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.instance.activity.InstanceSearchActivity
import app.findbest.vip.message.frament.MessageChatRecordListFragment
import app.findbest.vip.message.listener.ChatRecord
import app.findbest.vip.message.model.ChatRecordModel

import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils

import com.jaeger.library.StatusBarUtil
import com.neovisionaries.ws.client.WebSocketState
import io.github.sac.Ack
import io.github.sac.Socket
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MessageChatRecordFragment : Fragment(),
    MessageChatRecordFilterMenuFragment.FilterMenu {
    var mHandler = Handler()
    private lateinit var mContext: Context

    //筛选菜单
    override fun getFilterMenuselect(index: Int) {
        groupId = index
        // bottomMenuFragment!!.groupId == index
        val message = Message()
        Listhandler.sendMessage(message)
    }

    lateinit var mainContainer: FrameLayout
    lateinit var middleMenu: FrameLayout
    lateinit var actionBar: FrameLayout
    lateinit var recordList: FrameLayout
    lateinit var bottomMenu: FrameLayout
    lateinit var   searchEditText:EditText

    var messageChatRecordActionBarFragment: MessageChatRecordActionBarFragment? = null
    lateinit var messageChatRecordListFragment: MessageChatRecordListFragment
    var messageChatRecordFilterMenuFragment: MessageChatRecordFilterMenuFragment? = null

    var messageChatRecordSearchActionBarFragment: MessageChatRecordSearchActionBarFragment? = null

    var isFirstGotGroup: Boolean = true
    @SuppressLint("SimpleDateFormat")
    var sdf = SimpleDateFormat("yyyy-MM-dd")
    var year = sdf.format(Date()).substring(0,4)
    private val Listhandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            println("+++++++++++++++++++++++")
            println(json)
            println("+++++++++++++++++++++++")
            val type = json.getString("type")
            if (type != null && type == "contactList") {
                val members: JSONArray = json.getJSONObject("content").getJSONArray("members")
                isFirstGotGroup = true
                chatRecordList = mutableListOf()
                for (i in 0 until members.length()) {
                    val item = members.getJSONObject(i)
                    println(item)
                    //未读条数
                    val unreads = item.getInt("unreads")
                    //对方名
                    val name = item["name"].toString()

                    var lastMsg: JSONObject? = null
                    if (item.has("lastMsg") && item.getString("lastMsg") != null && item.getString(
                            "lastMsg"
                        ) != "" && item.getString(
                            "lastMsg"
                        ) != "null"
                    ) {
                        lastMsg = (item.getJSONObject("lastMsg"))
                    }

                    //最后一条消息

                    var msg = ""
                    //对方ID
                    val uid = item["uid"].toString()

                    //对方头像
                    var avatar = item["avatar"].toString()
                    if (avatar != null) {
                        val arra = avatar.split(";")
                        if (arra.size > 0) {
                            avatar = arra[0]
                        }
                    }

                    //公司
                    val companyName = item["companyName"].toString()
                    var  createdTime=""
                    if (lastMsg != null) {
                        val content = lastMsg.getJSONObject("content")
                        val contentType = content.getString("type")
                        msg = if (contentType == "image") {
                            "[图片]"
                        } else if (contentType == "voice") {
                            "[语音]"
                        } else {
                            content.getString("msg")
                        }
                        createdTime =sdf.format(Date(lastMsg!!.get("created").toString().toLong()))
                        if (year == createdTime.substring(0,4)) {
                            createdTime=createdTime.substring(5,createdTime.length)
                        }

                    }

                    val chatRecordModel =
                        ChatRecordModel(
                            uid,
                            if(item["uid"].toString() == "000000000000000000000000") mContext.resources.getString(R.string.system_msg) else name,
                            "",
                            if(item["uid"].toString() == "000000000000000000000000") "0" else avatar,
                            msg,
                            unreads,
                            item.getJSONObject("lastMsg"),
                            companyName,
                            "",
                            createdTime
                        )
                    chatRecordList.add(chatRecordModel)
                }

            }
            messageChatRecordListFragment.setRecyclerAdapter(chatRecordList)

        }
    }

    var application: App? = null
    var socket: Socket? = null


    override fun onStart() {
        super.onStart()
        activity!!.setActionBar(messageChatRecordActionBarFragment!!.toolbar1)
        StatusBarUtil.setTranslucentForImageView(
            activity,
            0,
            messageChatRecordActionBarFragment!!.toolbar1
        )
        activity!!.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        initRequest()

        isFirstGotGroup = true

        // DialogUtils.showLoading(this)

        if( socket!=null && socket?.isconnected()!!   &&(WebSocketState.OPEN == socket?.currentState || WebSocketState.CREATED == socket?.currentState)) {

            println("socket有效!!!")
            Handler().postDelayed({
                socket?.emit("queryContactList", application!!.getMyToken(),
                    object : Ack {
                        override fun call(name: String?, error: Any?, data: Any?) {
                            println("Got message for :" + name + " error is :" + error + " data is :" + data)
                        }
                    })
            }, 200)
        } else {
            println("socket失效，重连中！！！！！！！+")
            application?.closeMessage()
        }
        // DialogUtils.hideLoading()
    }

//    override fun  onResume(){
//        super.onResume()
//        initRequest()
//    }

    companion object {
        var chatRecordList: MutableList<ChatRecordModel> = mutableListOf()
//        var groupArray: JSONArray = JSONArray()
        var map: MutableMap<String, Int> = mutableMapOf()
        lateinit var json: JSONObject
        var groupId = 0;
        fun newInstance(): MessageChatRecordFragment {
            return MessageChatRecordFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView(): View {
        val mainContainerId = 1
        //接受
        application = App.getInstance()
        socket = application?.socket

        //消息回调
        application?.setChatRecord(object : ChatRecord {
            override fun requestContactList() {
            }
            override fun getContactList(str: String) {
                json = JSONObject(str)
                val message = Message()
                Listhandler.sendMessage(message)
                //(activity as PagesActivity).recruitInfoBottomMenuFragment.showData(str)
            }
        })

        val view = UI {
            mainContainer = frameLayout {
                id = mainContainerId
                backgroundColorResource = R.color.white
                verticalLayout {
                    //ActionBar
                    val actionBarId = 2
                    actionBar = frameLayout {
                        id = actionBarId
                        messageChatRecordActionBarFragment =
                            MessageChatRecordActionBarFragment.newInstance()
                        childFragmentManager.beginTransaction()
                            .replace(id, messageChatRecordActionBarFragment!!).commit()
                    }.lparams {
                        height = wrapContent
                        width = matchParent
                    }

                    //search
                    linearLayout {
                        gravity = Gravity.CENTER
                        //搜索框
                        linearLayout {
                            //去搜索
                            setOnClickListener {
                                val intent = Intent(activity, InstanceSearchActivity::class.java)
                                activity!!.startActivityForResult(intent, 101)
                                activity!!.overridePendingTransition(
                                    R.anim.right_in,
                                    R.anim.left_out
                                )
                            }

                            gravity = Gravity.CENTER_VERTICAL
                            backgroundResource = R.drawable.edit_text_background
                            imageView {
                                setImageResource(R.mipmap.icon_search_nor)
                            }.lparams {
                                width = dip(17)
                                height = dip(17)
                                leftMargin = dip(10)

                            }
                            isFocusable
                            searchEditText = editText {
                                //去搜索
                                setOnClickListener {
                                    val intent =
                                        Intent(activity, InstanceSearchActivity::class.java)
                                    activity!!.startActivityForResult(intent, 22)
                                    activity!!.overridePendingTransition(
                                        R.anim.right_in,
                                        R.anim.left_out
                                    )
                                }
                                isFocusable = false
                                hint = "搜索"
                                hintTextColor = Color.parseColor("#ff666666")
                                backgroundColor = Color.TRANSPARENT

                                textColor = Color.BLACK
                                singleLine = true
                                gravity = Gravity.CENTER_VERTICAL
                                textSize = 13f
                                padding = dip(0)
                                clearFocus()

                            }.lparams {
                                leftMargin = dip(9)
                                width = matchParent
                                height = matchParent
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(30)
                            leftMargin = dip(10)
                            rightMargin = dip(10)
                        }
                    }.lparams {
                        height = dip(40)
                        width = matchParent
                    }

                    //middle
                    val middleMenuId = 4
                    middleMenu = frameLayout {
                        id = middleMenuId
                        backgroundResource = R.color.originColor
                        textView {

                        }.lparams {
                            height = dip(1)
                            width = matchParent
                        }
                    }.lparams {
                        height = wrapContent
                        width = matchParent
                    }

                    //list
                    val listId = 5
                    recordList = frameLayout {
                        id = listId
                        messageChatRecordListFragment = MessageChatRecordListFragment.newInstance()
                        childFragmentManager.beginTransaction()
                            .replace(id, messageChatRecordListFragment).commit()

                        setOnClickListener {
                            if (messageChatRecordSearchActionBarFragment != null) {
                                EmoticonsKeyboardUtils.closeSoftKeyboard(
                                    messageChatRecordSearchActionBarFragment!!.editText
                                )
                            }
                        }
                    }.lparams {
                        height = 0
                        weight = 1f
                        width = matchParent
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view
        application!!.setMessageChatRecordListFragment(messageChatRecordListFragment)

        return view
    }

    fun initRequest() {
        //发送消息请求,获取联系人列表
//        app!!.setChatRecord(messageChatRecordListFragment)
//        app!!.sendRequest("queryContactList")

//        app!!setChatRecord(messageChatRecordListFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        application!!.setMessageChatRecordListFragment(null)
    }
}
