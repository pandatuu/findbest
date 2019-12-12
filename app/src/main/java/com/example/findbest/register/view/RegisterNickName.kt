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

class RegisterNickName: AppCompatActivity() {

    private lateinit var nickName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()
        var identity = ""
        if(intent.getStringExtra("identity")!=null)
            identity = intent.getStringExtra("identity") as String

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
                        text = "5/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        //设置昵称
                        text = if(identity == "team"){
                            "设置团队名称"
                        }else{
                            "设置个人昵称"
                        }
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.team_activity
                            }.lparams(dip(16), dip(20)){
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                backgroundResource = R.drawable.login_input_bottom
                                editText {
                                    background = null
                                    hint = "请输入团队名称"
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
                        text = "完成注册"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            val nickname = nickName.text.toString()

                            if(nickname.isNullOrBlank()){
                                toast("请填写昵称")
                                return@setOnClickListener
                            }

                            if(!nickeNameMatch(nickname)){
                                toast("昵称格式不正确")
                                return@setOnClickListener
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

    private fun nickeNameMatch(text: String): Boolean{
        val patter = Pattern.compile("[`~!@#\$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]")
        val match = patter.matcher(text)
        return match.matches()
    }
}