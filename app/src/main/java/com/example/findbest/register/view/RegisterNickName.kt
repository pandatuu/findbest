package com.example.findbest.register.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.findbest.R
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*

class RegisterNickName: AppCompatActivity() {

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
                    imageView {
                        imageResource = R.mipmap.nav_ico_return
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(10),dip(18)){
                        alignParentLeft()
                        alignParentBottom()
                        bottomMargin = dip(10)
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
                        text = "设置团队名称"
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
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(35)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(17),dip(40),0)
                }
            }
        }
    }
}