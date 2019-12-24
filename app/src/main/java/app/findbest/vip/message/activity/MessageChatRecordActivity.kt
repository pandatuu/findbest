package cgland.job.sk_android.mvp.view.activity.message

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.FrameLayout
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.utils.BaseActivity
import cgland.job.sk_android.mvp.listener.message.ChatRecord
import cgland.job.sk_android.mvp.model.message.ChatRecordModel
import cgland.job.sk_android.mvp.view.fragment.message.*
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils

import com.jaeger.library.StatusBarUtil
import com.neovisionaries.ws.client.WebSocketState
import io.github.sac.Ack
import io.github.sac.Socket
import org.jetbrains.anko.*
import org.json.JSONArray
import org.json.JSONObject


class MessageChatRecordActivity : BaseActivity(), MessageChatRecordActionBarFragment.ActionBarSearch,
    MessageChatRecordSelectMenuFragment.MenuSelect, MessageChatRecordSearchActionBarFragment.SendSearcherText,
    MessageChatRecordFilterMenuFragment.FilterMenu {

    var mHandler = Handler()


    //筛选菜单
    override fun getFilterMenuselect(index: Int) {
        groupId = index
        val message = Message()
        Listhandler.sendMessage(message)
    }

    //取消 搜索框
    override fun cancle() {

        messageChatRecordListFragment.setRecyclerAdapter(chatRecordList, groupArray)


        var mTransaction = supportFragmentManager.beginTransaction()
        messageChatRecordActionBarFragment = MessageChatRecordActionBarFragment.newInstance();
        mTransaction.replace(actionBar.id, messageChatRecordActionBarFragment!!)
        mTransaction.commit()
        afterSearchList()
    }

    //搜索框输入的文字
    override fun sendMessage(msg: String) {


        var NewList: MutableList<ChatRecordModel> = mutableListOf()
        for (item in chatRecordList) {
            if (item.userName.contains(msg)) {
                NewList.add(item)
            }
        }

        messageChatRecordListFragment.setRecyclerAdapter(NewList, groupArray)

    }


    //顶部菜单(チャット履歴/連絡先)
    override fun getMenuSelect(index: Int) {
        var mTransaction = supportFragmentManager.beginTransaction()
        if (index == 1) {
            if (messageChatRecordFilterMenuFragment == null) {
                messageChatRecordFilterMenuFragment = MessageChatRecordFilterMenuFragment.newInstance(map);

                mTransaction.add(middleMenu.id, messageChatRecordFilterMenuFragment!!)
            }

        } else {
            if (messageChatRecordFilterMenuFragment != null) {
                mTransaction.remove(messageChatRecordFilterMenuFragment!!)
                messageChatRecordFilterMenuFragment = null
            }
            if (0 != groupId) {
                groupId = 0
                val message = Message()
                Listhandler.sendMessage(message)
            }

        }
        mTransaction.commit()

    }

    //打开搜索框
    override fun searchGotClick() {


        var NewList: MutableList<ChatRecordModel> = mutableListOf()
        messageChatRecordListFragment.setRecyclerAdapter(NewList, groupArray)


        var mTransaction = supportFragmentManager.beginTransaction()
        messageChatRecordSearchActionBarFragment = MessageChatRecordSearchActionBarFragment.newInstance();
        mTransaction.replace(actionBar.id, messageChatRecordSearchActionBarFragment!!)
        mTransaction.commit()
        whenSearchList()
    }


    lateinit var mainContainer: FrameLayout
    lateinit var middleMenu: FrameLayout
    lateinit var actionBar: FrameLayout
    lateinit var recordList: FrameLayout
    lateinit var selectMenu: FrameLayout
    lateinit var bottomMenu: FrameLayout

    var messageChatRecordActionBarFragment: MessageChatRecordActionBarFragment? = null
    var messageChatRecordSelectMenuFragment: MessageChatRecordSelectMenuFragment? = null
    lateinit var messageChatRecordListFragment: MessageChatRecordListFragment
    var messageChatRecordFilterMenuFragment: MessageChatRecordFilterMenuFragment? = null

    var messageChatRecordSearchActionBarFragment: MessageChatRecordSearchActionBarFragment? = null


    var isFirstGotGroup: Boolean = true


    companion object {
        var chatRecordList: MutableList<ChatRecordModel> = mutableListOf()
        var groupArray: JSONArray = JSONArray()
        var map: MutableMap<String, Int> = mutableMapOf()
        lateinit var json: JSONObject
        var groupId = 0;

    }


    private val Listhandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            println("+++++++++++++++++++++++")
            println(json)
            println("+++++++++++++++++++++++")
            var type = json.getString("type")
            if (type != null && type.equals("contactList")) {


                var array: JSONArray = json.getJSONObject("content").getJSONArray("groups")

                var members: JSONArray = JSONArray()
                if (isFirstGotGroup) {
                    groupArray = JSONArray()
                }
                for (i in 0..array.length() - 1) {
                    var item = array.getJSONObject(i)

                    var id = item.getInt("id")
                    var name = item.getString("name")
                    if (name == "全部") {
                        name = "全て"
                    }
                    if (name != null && !name.equals("約束済み")) {
                        map.put(name, id.toInt())
                    }

                    if (id == groupId) {

                        println("现在groupId")
                        println(groupId)

                        members = item.getJSONArray("members")
                    }
                    if (isFirstGotGroup) {
                        if (id == 4) {
                            var group1 = item.getJSONArray("members")
                            groupArray.put(group1)
                        }
                        if (id == 5) {
                            var group2 = item.getJSONArray("members")
                            groupArray.put(group2)
                        }
                        if (id == 6) {
                            var group3 = item.getJSONArray("members")
                            groupArray.put(group3)
                        }


                    }
                }
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
                    if (item.has("lastMsg") && item.getString("lastMsg") != null && !item.getString("lastMsg").equals("") && !item.getString(
                            "lastMsg"
                        ).equals("null")
                    ) {
                        lastMsg = (item.getJSONObject("lastMsg"))
                    }

                    //最后一条消息

                    var msg = ""
                    //对方ID
                    var uid = item["uid"].toString()
                    //对方职位
                    var position = item["position"].toString()
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
                    // 显示的职位的id
                    var lastPositionId = item.getString("lastPositionId")
                    if (lastPositionId == null || lastPositionId.equals("")) {
                        println("联系人信息中没有lastPositionId")
                        lastPositionId = ""
                    }

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
                    }
                    var ChatRecordModel = ChatRecordModel(
                        uid,
                        name,
                        position,
                        avatar,
                        msg,
                        unreads,
                        companyName,
                        lastPositionId
                    )
                    chatRecordList.add(ChatRecordModel)
                }

            }
            println("xxxxxxxxxxxxxxx")

            println(groupArray)


            messageChatRecordListFragment.setRecyclerAdapter(chatRecordList, groupArray)

        }
    }

    var application: App? = null
    var socket: Socket? = null


    override fun onStart() {
        super.onStart()
        setActionBar(messageChatRecordActionBarFragment!!.toolbar1)
        StatusBarUtil.setTranslucentForImageView(
            this@MessageChatRecordActivity,
            0,
            messageChatRecordActionBarFragment!!.toolbar1
        )
        window.decorView.systemUiVisibility =
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
            println("socket失效，重连中！！！！！！！***")



            application?.closeMessage()

        }
        // DialogUtils.hideLoading()
    }

//    override fun  onResume(){
//        super.onResume()
//        initRequest()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                //  Listhandler.sendMessage(message)
            }
        })

        mainContainer = frameLayout {
            id = mainContainerId
            backgroundColorResource = R.color.white
            verticalLayout {
                //ActionBar
                var actionBarId = 2
                actionBar = frameLayout {
                    id = actionBarId
                    messageChatRecordActionBarFragment = MessageChatRecordActionBarFragment.newInstance();
                    supportFragmentManager.beginTransaction().replace(id, messageChatRecordActionBarFragment!!).commit()


                }.lparams {
                    height = wrapContent
                    width = matchParent
                }

                //select menu
                var selectMenurId = 3
                selectMenu = frameLayout {
                    id = selectMenurId
                    messageChatRecordSelectMenuFragment = MessageChatRecordSelectMenuFragment.newInstance();
                    supportFragmentManager.beginTransaction().replace(id, messageChatRecordSelectMenuFragment!!)
                        .commit()


                }.lparams {
                    height = wrapContent
                    width = matchParent
                }

                //middle
                var middleMenuId = 4
                middleMenu = frameLayout {
                    id = middleMenuId
                    backgroundResource = R.color.originColor
                    textView {

                    }.lparams {
                        height = dip(8)
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
                    supportFragmentManager.beginTransaction().replace(id, messageChatRecordListFragment).commit()






                    setOnClickListener(object : View.OnClickListener {

                        override fun onClick(v: View?) {

                            if (messageChatRecordSearchActionBarFragment != null) {
                                EmoticonsKeyboardUtils.closeSoftKeyboard(messageChatRecordSearchActionBarFragment!!.editText)
                            }


                        }

                    })

                }.lparams {
                    height = 0
                    weight = 1f
                    width = matchParent
                }
                // bottom menu
                var bottomMenuId = 6
                bottomMenu = frameLayout {

                }.lparams {
                    height = wrapContent
                    width = matchParent
                }

            }.lparams() {
                width = matchParent
                height = matchParent
            }

        }

        application!!.setMessageChatRecordListFragment(messageChatRecordListFragment)
    }


    fun whenSearchList() {
        selectMenu.visibility = View.GONE
        middleMenu.visibility = View.GONE
        bottomMenu.visibility = View.GONE
    }

    fun afterSearchList() {
        selectMenu.visibility = View.VISIBLE
        middleMenu.visibility = View.VISIBLE
        bottomMenu.visibility = View.VISIBLE
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
