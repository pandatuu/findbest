package app.findbest.vip.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import cgland.job.sk_android.mvp.listener.message.ChatRecord
import cgland.job.sk_android.mvp.listener.message.RecieveMessageListener
import cgland.job.sk_android.mvp.model.message.ChatRecordModel
import cgland.job.sk_android.mvp.view.activity.message.MessageChatRecordActivity
import cgland.job.sk_android.mvp.view.fragment.message.MessageChatRecordListFragment
import com.alibaba.fastjson.JSON
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import com.umeng.message.IUmengRegisterCallback
import io.github.sac.*
import org.json.JSONArray
import org.json.JSONObject


class App : Application() {

    private val appKey = "5dedfa5b4ca3579d01000010"
    private val appSecret = "245e0ab8fa4cada8d858591b214a2d21"




    companion object {
        private var instance: App? = null
        fun getInstance(): App? {
            return instance
        }


    }
    private var chatRecord: ChatRecord? = null


    lateinit var socket: Socket

    var mRecieveMessageListener: RecieveMessageListener? = null
    private var messageChatRecordListFragment: MessageChatRecordListFragment? = null


    private val defaultPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(this)

    private lateinit var channelRecieve: Socket.Channel
    private var messageLoginState = false

    override fun onCreate() {
        super.onCreate()

        instance = this

        UMConfigure.init(this, appKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, appSecret)

        //获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(p0: String?) {
                Log.i(TAG, "注册成功：deviceToken：-------->  $p0")
                val mPerferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val mEditor = mPerferences.edit()
                mEditor.putString("deviceToken", p0)
                mEditor.commit()
            }

            override fun onFailure(p0: String?, p1: String?) {
                Log.e(TAG, "注册失败：-------->  p0:$p0,p1:$p1")
            }
        })
    }


    fun initMessage() {


        mRecieveMessageListener = null

        socket = Socket("${getString(R.string.imUrl)}sk/")

        println("初始化消息系统")

        val token = getMyToken()
        println("token:$token")


        if (socket.isconnected()) {
            println("初始化消息系统")
            socket.disconnect()
        }
        socket.setListener(object : BasicListener {
            override fun onConnected(socket: Socket, headers: Map<String, List<String>>) {
                println("socket链接成功")

                val obj = JSONObject("{\"token\":\"$token\"}")

                socket.emit("login", obj, object : Ack {
                    override fun call(eventName: String, error: Any, data: Any) {
                        println("Got message for :$eventName error is :$error data is :$data")
                        //订阅通道
                        val uId = getMyId()

                        messageLoginState=true

                        println("用户id:$uId")
                        println("用户id:$token")

                        if (uId.isBlank()) {
//                        val toast = Toast.makeText(applicationContext, "ID取得失敗", Toast.LENGTH_SHORT)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        }

                        channelRecieve = socket.createChannel("p_${uId.replace("\"", "")}")
                        channelRecieve.subscribe(object : Ack {
                            override fun call(channelName: String, error: Any, data: Any) {
                                if (error == null) {
                                    println("Subscribed to channel $channelName successfully")
                                } else {
                                }
                            }
                        })


                        //接受消息
                        channelRecieve.onMessage(object : Emitter.Listener {
                            override fun call(name: String?, data: Any?) {
                                println("app收到消息内容")
                                println(obj)
                                println("app收到消息")

                                val json = JSON.parseObject(obj.toString())
                                val type = json.getString("type")
                                try {
                                    if (type != null && type == "contactList") {
                                        println("准备发送contactList")
                                        println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx================xxx")
                                        getContactList(obj.toString())
                                        chatRecord?.getContactList(obj.toString())
                                        println("发送contactList完毕")
                                    } else if (type != null && type == "setStatus") {


                                    } else if (type != null && type == "historyMsg") {
                                        mRecieveMessageListener?.getHistoryMessage(obj.toString())
                                    } else {
                                        mRecieveMessageListener?.getNormalMessage(obj.toString())
                                        socket.emit("queryContactList", token)

                                    }
                                } catch (e: UninitializedPropertyAccessException) {
                                    //e.printStackTrace()
                                    println("请求联系人列表")
                                    chatRecord?.requestContactList()
                                }
                            }
                        })




                        socket.emit("queryContactList", token)
                    }
                }
                )

            }

            override fun onDisconnected(
                socket: Socket,
                serverCloseFrame: WebSocketFrame,
                clientCloseFrame: WebSocketFrame,
                closedByServer: Boolean
            ) {

                channelRecieve.unsubscribe()
                channelRecieve.onMessage(object : Emitter.Listener {
                    override fun call(name: String?, data: Any?) {
                    }

                })

                initMessage()
                println("socket断开链接!!!!!!!!!!!!!!!!!!")

                Log.i("Success ", "Disconnected from end-point")
                println("onDisconnectedonDisconnectedonDisconnected")
//                toast("onDisconnectedonDisconnectedonDisconnected")

            }

            override fun onConnectError(socket: Socket, exception: WebSocketException) {
                Log.i("Success ", "Got connect error $exception")

//                出现这种情况的原因有很多，其中包括：
//
//                颁发服务器证书的 CA 未知
//                服务器证书不是 CA 签名的，而是自签名的
//                服务器配置缺少中间 CA
//

//                toast("onConnectErroronConnectErroronConnectError")
                println("可能没网onConnectErroronConnectErroronConnectError")

            }

            override fun onSetAuthToken(token: String, socket: Socket) {
                socket.setAuthToken(token)
                print("")

            }

            override fun onAuthentication(socket: Socket, status: Boolean?) {
                if (status!!) {
                    Log.i("Success ", "socket is authenticated")
                } else {
                    Log.i("Success ", "Authentication is required (optional)")
                }

                print("")

            }

        })

        // socket.connect()
        socket.setReconnection(ReconnectStrategy().setMaxAttempts(10).setDelay(3000))
        socket.connectAsync()


    }




    //解析联系人列表数据
    fun getContactList(s: String) {

        var chatRecordList: MutableList<ChatRecordModel> = mutableListOf()
        var groupArray: JSONArray = JSONArray()
        var map: MutableMap<String, Int> = mutableMapOf()
        var json: JSONObject = JSONObject(s)
        var isFirstGotGroup = true


        var array: JSONArray =
            json.getJSONObject("content").getJSONArray("groups")

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

            if (id == MessageChatRecordActivity.groupId) {
                println("现在groupId")

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
            //最后一条消息
            var lastMsg: JSONObject? = null
            if (item.has("lastMsg") && !item.getString("lastMsg").equals("") && !item.getString(
                    "lastMsg"
                ).equals(
                    "null"
                )
            ) {
                lastMsg = (item.getJSONObject("lastMsg"))
            }

            var msg = ""
            //对方ID
            var uid = item["uid"].toString()
            //对方职位
            var position = item["position"].toString()
            //对方头像
            var avatar = item["avatar"].toString()
            if (avatar != null) {
                var arra = avatar.split(";")
                if (arra != null && arra.size > 0) {
                    avatar = arra[0]
                }
            }

            //公司
            var companyName = item["companyName"].toString()
            // 显示的职位的id
            var lastPositionId = item.getString("lastPositionId")
            if (lastPositionId == null) {
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


        MessageChatRecordActivity.chatRecordList = chatRecordList
        MessageChatRecordActivity.groupArray = groupArray
        MessageChatRecordActivity.map = map
        MessageChatRecordActivity.json = json


        MessageChatRecordListFragment.thisGroupArray = groupArray

        if (messageChatRecordListFragment != null) {
            messageChatRecordListFragment?.setRecyclerAdapter(
                chatRecordList,
                groupArray
            )
        }
    }


    infix fun setChatRecord(chat: ChatRecord) {
        chatRecord = chat
    }

    fun setMessageChatRecordListFragment(con: MessageChatRecordListFragment?) {
        messageChatRecordListFragment = con
    }

    fun setRecieveMessageListener(listener: RecieveMessageListener) {
        mRecieveMessageListener = listener
    }

    fun getMyToken(): String {
        val token = defaultPreferences.getString("token", "") ?: ""
        println("--------------------------------------------------------")
        println("token->" + "Bearer ${token.replace("\"", "")}")

        if (token.isEmpty()) {
//            Thread(Runnable {
//                sleep(200)
//                if(count>15){
//                }else{
//                    getMyToken()
//                    count++
//                }
//            }).start()
        }

        return token.replace("\"", "")
    }


    fun getMyId(): String {
        return defaultPreferences.getString("id", "") ?: ""
    }


    fun closeMessage(){

        socket.disconnect()

    }


    fun getMyLogoUrl(): String {
        var avatarURL = defaultPreferences.getString("avatarURL", "") ?: ""
        val arra = avatarURL.split(",")
        if (arra.isNotEmpty()) {
            avatarURL = arra[0]
        }
        return avatarURL
    }

    fun getMessageLoginState(): Boolean {
        return messageLoginState
    }


}