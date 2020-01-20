package app.findbest.vip.message.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.KeyEvent
import android.widget.Toast
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.message.listener.RecieveMessageListener
import app.findbest.vip.message.listener.VideoListener
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.roundImageView
import com.bumptech.glide.Glide
import com.neovisionaries.ws.client.WebSocketState
import imui.jiguang.cn.imuisample.messages.JitsiMeetActivitySon
import io.github.sac.Socket

import org.jetbrains.anko.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

class VideoRequestActivity : BaseActivity() {

    private lateinit var jsonMsg: com.alibaba.fastjson.JSONObject
    private var sendMessageModel = ""
    private var hisId = ""
    lateinit var mVideoListener: VideoListener
    private var channelSend: Socket.Channel? = null
    var application: App? = null
    var socket: Socket? = null
    private val listhandler = @SuppressLint("HandlerLeak")

    object : Handler() {
        override fun handleMessage(msg: Message) {
            println("+++++++++++++++++++++++")
            println(jsonMsg)
            println("+++++++++++++++++++++++")
            val json = jsonMsg
            val type = json.getString("type")
            if (type != null && type == "video") {
//                val members: JSONArray = json.getJSONObject("content").getJSONArray("members")
//                isFirstGotGroup = true
//                MessageChatRecordFragment.chatRecordList = mutableListOf()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (socket != null && socket?.isconnected()!! && (WebSocketState.OPEN == socket?.currentState || WebSocketState.CREATED == socket?.currentState)) {
            println("socket有效!!!")
            Handler().postDelayed({
                socket?.emit(
                    "queryContactList", application!!.getMyToken()
                ) { name, error, data -> println("Got message for :$name error is :$error data is :$data") }
            }, 200)
        } else {
            println("socket失效，重连中！！！！！！！+")
            application?.closeMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = App.getInstance()
        socket = application?.socket

        //消息回调

        application?.setRecieveMessageListener(object : RecieveMessageListener {
            override fun getNormalMessage(str: String) {
                try {
                    val jsono = JSONObject(str)
                    showMessageOnScreen(jsono)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                println("普通消息")
            }

            override fun getHistoryMessage(str: String) {
                println("历史消息")
            }
        })

        hisId = intent.getStringExtra("otherId") ?: ""
        val otherName = intent.getStringExtra("otherName") ?: ""
        val otherAvatar = intent.getStringExtra("otherAvatar") ?: ""
        sendMessageModel = intent.getStringExtra("sendMessageModel") ?: ""

        verticalLayout {
            frameLayout {
                backgroundColor = Color.BLACK
                verticalLayout {
                    gravity = Gravity.CENTER
                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        roundImageView {
                            setBorderRadius(1250)
                            Glide.with(context)
                                .load(otherAvatar)
                                .centerCrop()
                                .skipMemoryCache(false)
                                .dontAnimate()
                                .placeholder(R.mipmap.default_avatar)
                                .into(this)

                        }.lparams {
                            width = dip(125)
                            height = dip(125)
                            topMargin = dip(150)
                        }
                        textView {
                            gravity = Gravity.CENTER
                            textSize = 27f
                            textColor = Color.WHITE
                            text = otherName
                        }.lparams {
                            topMargin = dip(20)
                        }
                        textView {
                            gravity = Gravity.CENTER
                            textSize = 17f
                            textColor = Color.parseColor("#FFCECECE")
                            text = "正在等待对方接听…"
                        }.lparams {
                            topMargin = dip(100)
                        }
                        imageView {
                            imageResource = R.mipmap.video_cancel
                            setOnClickListener {
                                cacelVideo()
//                                setResult(Activity.RESULT_OK)
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams {
                            width = dip(90)
                            height = dip(90)
                            topMargin = dip(25)
                        }
                    }.lparams {
                        height = matchParent
                        width = wrapContent
                    }
                }.lparams {
                    height = matchParent
                    width = matchParent
                }
            }.lparams {
                height = matchParent
                width = matchParent
            }
        }
    }

    private fun showMessageOnScreen(json: JSONObject) {
        val senderId = json.getJSONObject("sender").get("id").toString()
        if (senderId != null && senderId == hisId) {
            println(json)
            val content = json.getJSONObject("content")
            if ("video" == content.getString("type")) {
                if (content.getString("videoRoomId") == "1") {
                    //接收方拒绝视频
                    //解决在子线程中调用Toast的异常情况处理
                    Looper.prepare()
                    val toast =
                        Toast.makeText(this@VideoRequestActivity, "接收方拒绝视频", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    finish()
                    overridePendingTransition(R.anim.left_in, R.anim.right_out)
                    Looper.loop()
                } else if (content.getString("videoRoomId") == "4") {
                    //接收方接收视频
                    val message = JSONObject(sendMessageModel)
                    val user = JitsiMeetUserInfo()
                    user.displayName = message.getJSONObject("sender").getString("name").toString()


                    val defaultOptions = JitsiMeetConferenceOptions.Builder()
                        .setServerURL(URL(getString(R.string.jitsiUrl)))
                        .setWelcomePageEnabled(false)
                        .build()
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions)
                    val roomId = intent.getStringExtra("roomId") ?: ""
                    val options = JitsiMeetConferenceOptions.Builder()
                        .setRoom(roomId)
                        .setUserInfo(user)
                        .build()

//                    JitsiMeetActivitySon.launch(this@VideoRequestActivity, options, "")
                    val intent = Intent(this@VideoRequestActivity, JitsiMeetActivitySon::class.java)
                    intent.action = "org.jitsi.meet.CONFERENCE"
                    intent.putExtra("JitsiMeetConferenceOptions", options)
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    finish()
                }
            }
        }
    }

    private fun cacelVideo() {
        //视频中间页挂断
        try {
            val message = JSONObject(sendMessageModel)

            message.getJSONObject("content").put("type", "video")
            message.getJSONObject("content").put("msg", "已取消")
            message.getJSONObject("content").put("videoRoomId", 2)
            println("--------------------")
            println(message)
            println("--------------------")
            channelSend?.publish(message) { _, error, _ ->
                if (error == null) {
                    //成功 修改信息状态
                    println("发送取消视频成功")
                } else {
                    //失败
                    println("发送消息出错了")
                    println(error)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val intent = Intent()
                intent.putExtra("isVideo", false)
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(
                    R.anim.fade_in_out,
                    R.anim.fade_in_out
                )
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}