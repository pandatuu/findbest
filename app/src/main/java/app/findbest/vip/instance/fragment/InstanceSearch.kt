package app.findbest.vip.instance.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.view.*
import org.jetbrains.anko.*
import android.graphics.Typeface
import android.os.Handler
import android.os.Message
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.FragmentActivity
import app.findbest.vip.application.App
import app.findbest.vip.message.fragment.MessageChatRecordFragment
import app.findbest.vip.message.frament.MessageChatRecordListFragment
import app.findbest.vip.message.fragment.MessageChatRecordSearchActionBarFragment
import app.findbest.vip.message.listener.ChatRecord
import app.findbest.vip.message.model.ChatRecordModel
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils
import com.neovisionaries.ws.client.WebSocketState
import io.github.sac.Ack
import io.github.sac.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class InstanceSearch : FragmentParent() {

    companion object {
        fun newInstance( activity: FragmentActivity): InstanceSearch {
            val f = InstanceSearch()
            f.outActivity = activity
            return f
        }
    }

    var chatRecordList: MutableList<ChatRecordModel> = mutableListOf()
    private var mContext: Context? = null
    lateinit var outActivity: FragmentActivity
    var application: App? = null
    var socket: Socket? = null

    lateinit var editText:EditText
    private lateinit var recordList: FrameLayout
    private lateinit var messageChatRecordListFragment: MessageChatRecordListFragment
    private var messageChatRecordSearchActionBarFragment: MessageChatRecordSearchActionBarFragment? = null
    private val listhandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            println("+++++++++++++++++++++++")
            println(MessageChatRecordFragment.json)
            println("+++++++++++++++++++++++")
            val type = MessageChatRecordFragment.json.getString("type")
            if (type != null && type == "contactList") {
                val members: JSONArray = MessageChatRecordFragment.json.getJSONObject("content").getJSONArray("members")
                val isFirstGotGroup = true
                MessageChatRecordFragment.chatRecordList = mutableListOf()
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
                        createdTime = SimpleDateFormat("yyyy-MM-dd").format(Date(lastMsg.get("created").toString().toLong()))
                        val year = SimpleDateFormat("yyyy-MM-dd").format(Date()).substring(0,4)
                        if (year == createdTime.substring(0,4)) {
                            createdTime=createdTime.substring(5,createdTime.length)
                        }

                    }

                    val chatRecordModel =
                        ChatRecordModel(
                            uid,
                            if(item["uid"].toString() == "000000000000000000000000") outActivity.resources.getString(R.string.system_msg) else name,
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    override fun onResume() {
        super.onResume()

        if( socket!=null && socket?.isconnected()!!   &&(WebSocketState.OPEN == socket?.currentState || WebSocketState.CREATED == socket?.currentState)) {
            println("搜索-socket有效!!!")
            Handler().postDelayed({
                socket?.emit("queryContactList", application!!.getMyToken(),
                    object : Ack {
                        override fun call(name: String?, error: Any?, data: Any?) {
                            println("Got message for :" + name + " error is :" + error + " data is :" + data)
                        }
                    })
            }, 200)
        } else {
            println("搜索-socket失效，重连中！！！！！！！+")
            application?.closeMessage()
        }
    }

    private fun createView(): View {

        //接受
        application = App.getInstance()
        socket = application?.socket

        //消息回调
        application?.setChatRecord(object : ChatRecord {
            override fun requestContactList() {
            }
            override fun getContactList(str: String) {
                MessageChatRecordFragment.json = JSONObject(str)
                val message = Message()
                listhandler.sendMessage(message)
                //(activity as PagesActivity).recruitInfoBottomMenuFragment.showData(str)
            }
        })
        val view = UI {
            frameLayout {
                verticalLayout {
                    relativeLayout {
                        textView {
                            backgroundColor = Color.parseColor("#FFE3E3E3")
                        }.lparams {
                            width = matchParent
                            height = dip(1)
                            alignParentBottom()

                        }
                        relativeLayout {
                            toolbar {
                                backgroundResource = R.color.transparent
                                isEnabled = true
                                title = ""
                            }.lparams {
                                width = matchParent
                                height = dip(65)
                                alignParentBottom()
                                height = dip(65 - getStatusBarHeight(outActivity))
                            }
                            textView {
                                text = outActivity.resources.getText(R.string.chat_sreach)
                                backgroundColor = Color.TRANSPARENT
                                gravity = Gravity.CENTER
                                textColor = Color.parseColor("#FF222222")
                                textSize = 17f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams {
                                width = matchParent
                                height = wrapContent
                                height = dip(65 - getStatusBarHeight(outActivity))
                                alignParentBottom()
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(65)
                        }
                    }.lparams {
                        width = matchParent
                        height = dip(65)
                    }
                    verticalLayout {
                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL
                                backgroundResource = R.drawable.edit_text_background
                                imageView {
                                    setImageResource(R.mipmap.icon_search_nor)
                                }.lparams {
                                    width = dip(17)
                                    height = dip(17)
                                    leftMargin = dip(10)

                                }
                                editText = editText {
                                    hint = outActivity.resources.getText(R.string.chat_sreach)
                                    hintTextColor = Color.parseColor("#ff666666")
                                    backgroundColor = Color.TRANSPARENT
                                    textColor = Color.BLACK
                                    singleLine = true
                                    gravity = Gravity.CENTER_VERTICAL
                                    textSize = 13f
                                    padding = dip(0)
                                    imeOptions = EditorInfo.IME_ACTION_SEARCH

                                    setOnEditorActionListener(object: TextView.OnEditorActionListener{
                                        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                                            //以下方法防止两次发送请求
                                            val newList = mutableListOf<ChatRecordModel>()
                                            if (actionId === EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode === KeyEvent.KEYCODE_ENTER) {
                                                if(event!=null){
                                                    val result = editText.text.toString().trim()
                                                    chatRecordList.forEach {
                                                        if(it.userName == result || it.userName.indexOf(result)!=-1){
                                                            newList.add(it)
                                                        }
                                                    }
                                                    messageChatRecordListFragment.setRecyclerAdapter(newList)
                                                }
                                            }
                                            return false
                                        }
                                    })
                                }.lparams {
                                    leftMargin = dip(9)
                                    width = matchParent
                                    height = matchParent
                                }
                            }.lparams {
                                width = dip(280)
                                height = dip(30)
                            }
                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
                                textView {
                                    setOnClickListener {
                                        closeSoftKeyboard(editText)
                                        activity!!.finish()
                                        activity!!.overridePendingTransition(
                                            R.anim.left_in,
                                            R.anim.right_out
                                        )
                                    }
                                    text = outActivity.resources.getText(R.string.chat_cancel)
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams {
                                    rightMargin = dip(7)
                                }
                            }.lparams {
                                width = dip(0)
                                weight = 1f
                                height = matchParent
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(40)
                            leftMargin = dip(10)
                            rightMargin = dip(10)
                        }

                        val listId = 5
                        recordList = frameLayout {
                            backgroundColor = Color.RED
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
                            height = matchParent
                            width = matchParent
                        }
                    }.lparams {
                        height = dip(0)
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

    override fun onDestroy() {
        super.onDestroy()
        application!!.setMessageChatRecordListFragment(null)
    }
}