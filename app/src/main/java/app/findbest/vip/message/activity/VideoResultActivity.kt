package app.findbest.vip.message.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.KeyEvent
import android.widget.LinearLayout
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.message.listener.VideoListener
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.roundImageView
import com.bumptech.glide.Glide
import com.neovisionaries.ws.client.WebSocketState
import io.github.sac.Socket

import org.jetbrains.anko.*
import org.json.JSONException
import org.json.JSONObject

class VideoResultActivity : BaseActivity() {

    private lateinit var jsonMsg: com.alibaba.fastjson.JSONObject
    lateinit var mVideoListener: VideoListener
    private var sendMessageModel = ""
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

        if( socket!=null && socket?.isconnected()!!   &&(WebSocketState.OPEN == socket?.currentState || WebSocketState.CREATED == socket?.currentState)) {
            println("socket有效!!!")
            Handler().postDelayed({
                socket?.emit("queryContactList", application!!.getMyToken()
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
        application?.setVideoListener(object: VideoListener{
            override fun awaitVideo(json: com.alibaba.fastjson.JSONObject) {
                jsonMsg = json
                val message = Message()
                listhandler.sendMessage(message)
            }
        })
        val otherId = intent.getStringExtra("otherId") ?: ""
        val otherName = intent.getStringExtra("otherName") ?: ""
        val otherAvatar = intent.getStringExtra("otherAvatar") ?: ""
        sendMessageModel = intent.getStringExtra("sendMessageModel") ?: ""

        if (channelSend == null ) {
            channelSend = socket?.createChannel("f_$otherId")
        }
        verticalLayout {
            frameLayout {
                backgroundColor =  Color.BLACK
                verticalLayout{
                    gravity = Gravity.CENTER
                    verticalLayout{
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

                        }.lparams{
                            width = dip(125)
                            height = dip(125)
                            topMargin = dip(150)
                        }
                        textView {
                            gravity = Gravity.CENTER
                            textSize = 27f
                            textColor = Color.WHITE
                            text = otherName
                        }.lparams{
                            topMargin = dip(20)
                        }
                        textView {
                            gravity = Gravity.CENTER
                            textSize = 17f
                            textColor = Color.parseColor("#FFCECECE")
                            text = "邀请您接听…"
                        }.lparams{
                            topMargin = dip(100)
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.video_cancel
                                setOnClickListener {
                                    toast("false")
                                    cacelVideo()
//                                    setResult(Activity.RESULT_OK)
                                    finish()
                                    overridePendingTransition(R.anim.fade_in_out, R.anim.fade_in_out)
                                }
                            }
                            imageView{
                                imageResource = R.mipmap.ico_video
                                setOnClickListener {

                                }
                            }.lparams{
                                leftMargin = dip(60)
                            }
                        }.lparams(wrapContent,dip(90)){
                            topMargin = dip(40)
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }.lparams {
                        height  =  matchParent
                        width  =  matchParent
                    }
                }.lparams {
                    height  =  matchParent
                    width  =  matchParent
                }
            }.lparams {
                height  =  matchParent
                width  =  matchParent
            }
        }
    }

    private fun cacelVideo() {
        //视频中间页挂断
        try {
            val message = JSONObject(sendMessageModel)

            message.getJSONObject("content").put("type", "video")
            message.getJSONObject("content").put("msg", "已拒绝")
            message.getJSONObject("content").put("videoRoomId", 1)
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
                finish()
                overridePendingTransition(R.anim.fade_in_out, R.anim.fade_in_out)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}