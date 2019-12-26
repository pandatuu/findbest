package app.findbest.vip.message.fragment

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

    //取消 搜索框
//    override fun cancle() {
//
//        messageChatRecordListFragment.setRecyclerAdapter(chatRecordList, groupArray)
//
//
//        var mTransaction = childFragmentManager.beginTransaction()
//        messageChatRecordActionBarFragment = MessageChatRecordActionBarFragment.newInstance();
//        mTransaction.replace(actionBar.id, messageChatRecordActionBarFragment!!)
//        mTransaction.commit()
//    }

    //搜索框输入的文字
//    override fun sendMessage(msg: String) {
//
//
//        var NewList: MutableList<ChatRecordModel> = mutableListOf()
//        for (item in chatRecordList) {
//            if (item.userName.contains(msg)) {
//                NewList.add(item)
//            }
//        }
//
//        messageChatRecordListFragment.setRecyclerAdapter(NewList, groupArray)
//
//    }


    //打开搜索框
//    override fun searchGotClick() {
//
//
//        var NewList: MutableList<ChatRecordModel> = mutableListOf()
//        messageChatRecordListFragment.setRecyclerAdapter(NewList, groupArray)
//
//
//        var mTransaction = childFragmentManager.beginTransaction()
//        messageChatRecordSearchActionBarFragment =
//            MessageChatRecordSearchActionBarFragment.newInstance();
//        mTransaction.replace(actionBar.id, messageChatRecordSearchActionBarFragment!!)
//        mTransaction.commit()
//    }


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
    var sdf=SimpleDateFormat("yyyy年MM月dd日")
    var year = sdf.format(Date()).substring(0,4)
    private val Listhandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            println("+++++++++++++++++++++++")
            println(json)
            println("+++++++++++++++++++++++")
            var type = json.getString("type")
            if (type != null && type.equals("contactList")) {


//                var array: JSONArray = json.getJSONObject("content").getJSONArray("groups")

                var members: JSONArray = json.getJSONObject("content").getJSONArray("members")
//                if (isFirstGotGroup) {
//                    groupArray = JSONArray()
//                }
//                for (i in 0..array.length() - 1) {
//                    var item = array.getJSONObject(i)
//
//                    var id = item.getInt("id")
//                    var name = item.getString("name")
//                    if (name == "全部") {
//                        name = "全て"
//                    }
//                    if (name != null && !name.equals("約束済み")) {
//                        map.put(name, id.toInt())
//                    }
//
//                    if (id == groupId) {
//
//                        println("现在groupId")
//                        println(groupId)
//
//                        members = item.getJSONArray("members")
//                    }
//                    if (isFirstGotGroup) {
//                        if (id == 4) {
//                            var group1 = item.getJSONArray("members")
//                            groupArray.put(group1)
//                        }
//                        if (id == 5) {
//                            var group2 = item.getJSONArray("members")
//                            groupArray.put(group2)
//                        }
//                        if (id == 6) {
//                            var group3 = item.getJSONArray("members")
//                            groupArray.put(group3)
//                        }
//
//
//                    }
//                }
                isFirstGotGroup = true
                chatRecordList = mutableListOf()
                for (i in 0..members.length() - 1) {
                    var item = members.getJSONObject(i)
                    println(item)
                    //未读条数
                    var unreads = item.getInt("unreads").toString()
                    //对方名
                    var name = item["name"].toString()

                    var lastMsg: JSONObject? = null
                    if (item.has("lastMsg") && item.getString("lastMsg") != null && !item.getString(
                            "lastMsg"
                        ).equals("") && !item.getString(
                            "lastMsg"
                        ).equals("null")
                    ) {
                        lastMsg = (item.getJSONObject("lastMsg"))
                    }

                    //最后一条消息

                    var msg = ""
                    //对方ID
                    var uid = item["uid"].toString()

                    //对方头像
                    var avatar = item["avatar"].toString()
                    if (avatar != null) {
                        var arra = avatar.split(";")
                        if (arra.size > 0) {
                            avatar = arra[0]
                        }
                    }

                    //公司
                    var companyName = item["companyName"].toString()
                    var  createdTime=""
                    if (lastMsg == null) {
                    } else {
                        var content = lastMsg.getJSONObject("content")
                        var contentType = content.getString("type")
                        if (contentType.equals("image")) {
                            msg = "[图片]"
                        } else if (contentType.equals("voice")) {
                            msg = "[语音]"
                        } else {
                            msg = content.getString("msg")
                        }
                        createdTime =sdf.format(Date(lastMsg!!.get("created").toString().toLong()))
                        if(year  !=  createdTime.substring(0,4)){

                        }else{
                            createdTime=createdTime.substring(5,11)
                        }

                    }




                    var ChatRecordModel = ChatRecordModel(
                        uid,
                        name,
                        "",
                        avatar,
                        msg,
                        unreads,
                        companyName,
                        "",
                        createdTime
                    )
                    chatRecordList.add(ChatRecordModel)
                }

            }
            println("xxxxxxxxxxxxxxx")
            println(chatRecordList)



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

        if (WebSocketState.OPEN == socket?.currentState || WebSocketState.CREATED == socket?.currentState) {


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
        var mainContainerId = 1
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
                    var actionBarId = 2
                    actionBar = frameLayout {
                        id = actionBarId
                        messageChatRecordActionBarFragment =
                            MessageChatRecordActionBarFragment.newInstance();
                        childFragmentManager.beginTransaction()
                            .replace(id, messageChatRecordActionBarFragment!!).commit()


                    }.lparams {
                        height = wrapContent
                        width = matchParent
                    }

                    //search
                    linearLayout {
                            gravity=Gravity.CENTER
                        //搜索框
                        linearLayout {

                            //去搜索
                            setOnClickListener {

                                var intent = Intent(activity, InstanceSearchActivity::class.java)
                                activity!!.startActivityForResult(intent,101)
                                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

                            }

                            gravity = Gravity.CENTER_VERTICAL

                            backgroundResource = R.drawable.edit_text_background
                            imageView() {

                                setImageResource(R.mipmap.icon_search_nor)

                            }.lparams() {
                                width = dip(17)
                                height = dip(17)
                                leftMargin = dip(10)

                            }

                            isFocusable

                            searchEditText = editText {


                                //去搜索
                                setOnClickListener {

                                    var intent = Intent(activity, InstanceSearchActivity::class.java)
                                    activity!!.startActivityForResult(intent,22)
                                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

                                }


                                setFocusable(false)
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


                        }.lparams() {
                            width = matchParent
                            height = dip(30)
                            leftMargin=dip(10)
                            rightMargin=dip(10)
                        }


                    }.lparams {
                        height = dip(40)
                        width = matchParent
                    }

                    //middle
                    var middleMenuId = 4
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
                    var listId = 5
                    recordList = frameLayout {
                        id = listId
                        messageChatRecordListFragment = MessageChatRecordListFragment.newInstance();
                        childFragmentManager.beginTransaction()
                            .replace(id, messageChatRecordListFragment).commit()






                        setOnClickListener(object : View.OnClickListener {

                            override fun onClick(v: View?) {

                                if (messageChatRecordSearchActionBarFragment != null) {
                                    EmoticonsKeyboardUtils.closeSoftKeyboard(
                                        messageChatRecordSearchActionBarFragment!!.editText
                                    )
                                }


                            }

                        })

                    }.lparams {
                        height = 0
                        weight = 1f
                        width = matchParent
                    }
//                    // bottom menu
//                    var bottomMenuId = 6
//                    bottomMenu = frameLayout {
//                        id = bottomMenuId
//                        bottomMenuFragment = BottomMenuFragment.newInstance(2, true);
//                        childFragmentManager.beginTransaction().replace(id, bottomMenuFragment!!)
//                            .commit()
//                    }.lparams {
//                        height = wrapContent
//                        width = matchParent
//                    }

                }.lparams() {
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
