package com.example.findbest.register.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.findbest.R
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*
import java.util.regex.Pattern

class RegisterEmail: AppCompatActivity() {

    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()

        frameLayout {
            backgroundColor = Color.WHITE
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    toolbar {
                        isEnabled = true
                        title = ""
                        navigationIconResource = R.mipmap.nav_ico_return
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(45), dip(30)) {
                        alignParentBottom()
                        alignParentLeft()
                    }
                }.lparams(matchParent,dip(65)){
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "4/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = "输入联系方式"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.email
                            }.lparams(dip(16), dip(20)){
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                backgroundResource = R.drawable.login_input_bottom
                                email = editText {
                                    background = null
                                    hint = "请输入电子邮箱，以便及时联系您"
                                    hintTextColor = Color.parseColor("#FFD0D0D0")
                                    textSize = 15f
                                    singleLine = true
                                }.lparams(matchParent, matchParent)
                            }.lparams(matchParent, matchParent){
                                leftMargin = dip(14)
                            }
                        }.lparams(matchParent, dip(40)){
                            topMargin = dip(35)
                        }
                    }.lparams(matchParent, wrapContent)
                    button {
                        backgroundResource = R.drawable.enable_around_button
                        text = "下一步"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {

                            val emailText = email.text.toString()

                            if(emailText.isNullOrBlank()){
                                toast("请填写邮箱地址")
                                return@setOnClickListener
                            }

                            if(!emailMatch(emailText)){
                                toast("邮箱格式不正确")
                                return@setOnClickListener
                            }

                            if(intent.getStringExtra("identity")!=null){
                                val identity = intent.getStringExtra("identity") as String
                                startActivity<RegisterNickName>(
                                    "identity" to identity
                                )
                                overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }else{
                                toast("页面出错")
                            }
                        }
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(35)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(17),dip(40),0)
                }
            }
        }
    }

    private fun emailMatch(text: String): Boolean{
        val patter = Pattern.compile("[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9\\u4e00-\\u9fa5_-]+(\\.[a-zA-Z\\u4e00-\\u9fa5_-]{2,})+\$")
        val match = patter.matcher(text)
        return match.matches()
    }

}