package cgland.job.sk_android.mvp.view.activity.message

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.application.App
import cgland.job.sk_android.mvp.view.fragment.message.MessageChatWithoutLoginActionBarFragment
import cgland.job.sk_android.mvp.view.fragment.message.MessageChatWithoutLoginFragment
import click

import com.jaeger.library.StatusBarUtil
import com.umeng.message.PushAgent
import org.jetbrains.anko.*
import withTrigger
import java.lang.Thread.sleep


class MessageChatWithoutLoginActivity : AppCompatActivity() {


    lateinit var mainContainer: FrameLayout
    lateinit var centerScreen: FrameLayout
    lateinit var actionBar: FrameLayout


    var messageChatWithoutLoginActionBarFragment: MessageChatWithoutLoginActionBarFragment? = null


    companion object {
        var fatherActivity: Activity? = null
    }


    override fun onStart() {
        super.onStart()
        setActionBar(messageChatWithoutLoginActionBarFragment!!.toolbar1)
        StatusBarUtil.setTranslucentForImageView(
            this@MessageChatWithoutLoginActivity,
            0,
            messageChatWithoutLoginActionBarFragment!!.toolbar1
        )
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        messageChatWithoutLoginActionBarFragment!!.toolbar1!!.setNavigationOnClickListener {
            finish()//返回
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this@MessageChatWithoutLoginActivity, MessageChatRecordActivity::class.java)
        startActivity(intent)
        finish()



        PushAgent.getInstance(this).onAppStart();
        var mainContainerId = 1
        mainContainer = frameLayout {
            id = mainContainerId
            backgroundColorResource = R.color.white

            verticalLayout {
                //ActionBar
                var actionBarId = 2
                actionBar = frameLayout {
                    id = actionBarId
                    messageChatWithoutLoginActionBarFragment = MessageChatWithoutLoginActionBarFragment.newInstance();
                    supportFragmentManager.beginTransaction().replace(id, messageChatWithoutLoginActionBarFragment!!)
                        .commit()

                }.lparams {
                    height = wrapContent
                    width = matchParent
                }


                var centerScreenId = 3
                centerScreen = frameLayout {
                    id = centerScreenId
                    var messageChatWithoutLoginFragment = MessageChatWithoutLoginFragment.newInstance();
                    supportFragmentManager.beginTransaction().replace(id, messageChatWithoutLoginFragment!!).commit()


                    this.withTrigger().click {


                        var app=  App.getInstance()
                        app?.initMessage()

                        runOnUiThread(Runnable {

                            sleep(300)
                            lateinit var intent: Intent
                            if (App.getInstance()!!.getMessageLoginState()) {
                                intent = Intent(this@MessageChatWithoutLoginActivity, MessageChatRecordActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                intent = Intent(this@MessageChatWithoutLoginActivity, MessageChatWithoutLoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            overridePendingTransition(R.anim.fade_in_out, R.anim.fade_in_out)




                        })



//                        val mEditor: SharedPreferences.Editor =
//                            PreferenceManager.getDefaultSharedPreferences(this@MessageChatWithoutLoginActivity).edit()
//                        mEditor.putString("token", "")
//                        mEditor.apply()
//
//                        val intent = Intent(this@MessageChatWithoutLoginActivity, LoginActivity::class.java)
//                        intent.putExtra("condition", 1)
//                        startActivity(intent)
//                        if (fatherActivity != null)
//                            fatherActivity!!.finish()
//                        finish()
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out)

                    }

                }.lparams {
                    height = 0
                    weight = 1f
                    width = matchParent
                }

            }.lparams() {
                width = matchParent
                height = matchParent
            }

        }

    }
}
