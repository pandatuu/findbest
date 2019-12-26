package app.findbest.vip.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.message.fragment.MessageChatRecordFragment

import app.findbest.vip.message.fragment.MessageChatRecordListFragment
import app.findbest.vip.message.listener.ChatRecord
import app.findbest.vip.message.listener.RecieveMessageListener
import app.findbest.vip.message.model.ChatRecordModel
import com.alibaba.fastjson.JSON
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import com.umeng.message.IUmengRegisterCallback
import io.github.sac.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


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

    var sdf= SimpleDateFormat("yyyy年MM月dd日")
    var year = sdf.format(Date()).substring(0,4)
    private var myId = ""

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
        initMessage()
    }


    fun initMessage() {


        mRecieveMessageListener = null

        socket = Socket("${getString(R.string.imUrl)}")

        println("初始化消息系统")

        val token = getMyToken()
        println("token:$token")

        if (socket.isconnected()) {
            println("断开连接,初始化消息系统")
            socket.disconnect()
        }
        socket.setListener(object : BasicListener {
            override fun onConnected(socket: Socket, headers: Map<String, List<String>>) {
                println("socket链接成功")

                //val obj = JSONObject("{\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5MTExMjQ0Yy02NmY3LTQ4MDUtODBiZC1kNTEwMzQ3M2Q5MjIiLCJ1c2VybmFtZSI6Ijg2MTU4ODIzMzUwMDciLCJ0aW1lc3RhbXAiOjE1NzQ5OTI3NTU2MDcsImRldmljZVR5cGUiOiJBTkRST0lEIiwiaWF0IjoxNTc0OTkyNzU1fQ.F4LpzLJoBwNY6-IcIBx_f3O1drgupyRMUMkVJOYvUPXllrGh4WVdAOZ3w91DTV8yKi4QbElS15ICt4qSBoNs1XWS0ucCcIy7kWVTf6LUo-jdG1l-U6wlB5ALVUm2CKJiYgXgY5F9piuzN8coSsTe84p_HdLgNM-eMjzM8h_ojNx_govWHKOke_QqIuha13kUwf48QSKAq8xrvL1nM6dB175_8_8Zc22a_8TGAhbBV17gmwEDExCz9-H1eK4uGX1yFhvTdDhnzKu3Qx9T2usvqKyjrIxXajwkkFyQoKRqIenCT7m0m_lKeGdDdUrixqyIGAltJeDFEg1kjRXLgriaGQ\"}")
                val obj = JSONObject("{\"token\":\"$token\"}")

                socket.emit("login", obj) { eventName, error, data ->

                    println("login接口返回信息:Got message for :$eventName error is :$error data is :$data")
                    //订阅通道


                    var json=JSONObject(data.toString())
                    myId=json.getString("uid")
                    val uId = getMyId()

                    messageLoginState=true

                    println("用户id:$uId")
                    println("用户id:$token")

                    if (uId.isBlank()) {
//                        val toast = Toast.makeText(applicationContext, "ID取得失敗", Toast.LENGTH_SHORT)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                    }

                    channelRecieve = socket.createChannel("f_${uId.replace("\"", "")}")


                    channelRecieve.subscribe { channelName, error, _ ->
                        if (error == null) {
                            println("Subscribed to channel $channelName successfully")
                        } else {

                        }
                    }



                    //接受
                    channelRecieve.onMessage { _, obj ->
                        println("app收到消息内容")
                        println(obj)
                        println("app收到消息")

                        val json = JSON.parseObject(obj.toString())
                        val type = json.getString("type")
                        try {
                            if (type != null && type == "contactList") {
                                println("准备发送contactList")
                                println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx================xxx")
                                println((chatRecord == null).toString())
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



                    socket.emit("queryContactList", token)


                }




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
        try {
            println("sssssssssssssssssssssssssssssssssssssssss")

            var chatRecordList: MutableList<ChatRecordModel> = mutableListOf()
            var map: MutableMap<String, Int> = mutableMapOf()
            var json: JSONObject = JSONObject(s)


            var members: JSONArray =
                json.getJSONObject("content").getJSONArray("members")

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

                var createdTime=""
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


            MessageChatRecordFragment.chatRecordList = chatRecordList
            MessageChatRecordFragment.map = map
            MessageChatRecordFragment.json = json



            if (messageChatRecordListFragment != null) {
                messageChatRecordListFragment?.setRecyclerAdapter(
                    chatRecordList
                )
            }

            println("sssssssssssssssssssssssssssssssssssssssss")
        }catch (e:Exception){
            e.printStackTrace()
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
        //9111244c-66f7-4805-80bd-d5103473d922
        return this.myId
        //return defaultPreferences.getString("id", "") ?: ""
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