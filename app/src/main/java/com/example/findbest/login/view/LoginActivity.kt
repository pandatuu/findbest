package com.example.findbest.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.findbest.R
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*

class LoginActivity: AppCompatActivity() {

    private lateinit var phoneNumber: EditText
    private lateinit var vCode: EditText

    @SuppressLint("SetTextI18n", "RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()

        frameLayout {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    textView {
                        padding = dip(10)
                        text = "注册"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 17f
                    }.lparams(wrapContent, wrapContent){
                        alignParentRight()
                        alignParentBottom()
                    }
                }.lparams(matchParent,dip(75)){
                    leftMargin = dip(15)
                    rightMargin = dip(10)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "登录"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_mobile
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            textView {
                                text = "+81"
                            }.lparams(wrapContent, wrapContent){
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            imageView {
                                imageResource = R.drawable.login_inverted_triangle
                            }.lparams(dip(12),dip(7)){
                                gravity = Gravity.CENTER_VERTICAL
                                leftMargin = dip(8)
                            }
                            phoneNumber = editText {
                                background = null
                                hint = "请输入手机号码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                            }.lparams(matchParent, matchParent){
                                leftMargin = dip(10)
                            }
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(70)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_password_nor
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            vCode = editText {
                                background = null
                                hint = "请输入密码"
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
                    textView {
                        padding = dip(10)
                        text = "忘记密码？"
                        textColor = Color.parseColor("#FF202020")
                        textSize = 13f
                        setOnClickListener {
                            startActivity<ResetPassword>()
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.RIGHT
                        topMargin = dip(5)
                    }
                    button {
                        backgroundResource = R.drawable.disable_around_button
                        text = "登录"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(14)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(40),dip(40),0)
                }
                setOnClickListener {
                    closeFocusjianpan()
                }
            }
        }
    }
    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        phoneNumber.clearFocus()
        vCode.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(phoneNumber.windowToken, 0)
        phone.hideSoftInputFromWindow(vCode.windowToken, 0)
    }
}