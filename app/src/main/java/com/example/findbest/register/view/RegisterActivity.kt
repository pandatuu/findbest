package com.example.findbest.register.view

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
import org.jetbrains.anko.sdk27.coroutines.onClick

class RegisterActivity: AppCompatActivity() {

    private lateinit var phoneNumber: EditText
    private lateinit var vCode: EditText
    private lateinit var newPwd: EditText
    private lateinit var againPwd: EditText

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
                        imageResource = R.mipmap.nav_ico_close
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(16),dip(16)){
                        alignParentLeft()
                        alignParentBottom()
                        bottomMargin = dip(10)
                    }
                    textView {
                        padding = dip(10)
                        text = "登录"
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
                        text = "注册"
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
                                imageResource = R.mipmap.inverted_triangle
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
                            }.lparams(dip(0), matchParent){
                                weight = 1f
                                leftMargin = dip(10)
                            }
                            imageView {
                                imageResource = R.mipmap.login_ico_close
                            }.lparams(dip(18),dip(18)){
                                leftMargin = dip(5)
                                gravity = Gravity.CENTER_VERTICAL
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
                            imageResource = R.mipmap.login_ico_validation_nor
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            vCode = editText {
                                background = null
                                hint = "请输入验证码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                            }.lparams(dip(0), matchParent){
                                weight = 1f
                            }
                            relativeLayout {
                                backgroundResource = R.drawable.around_button_5
                                textView {
                                    text = "获取验证码"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FFFFFFFF")
                                }.lparams(wrapContent, wrapContent){
                                    centerInParent()
                                }
                            }.lparams(dip(72),dip(22)){
                                gravity = Gravity.CENTER_VERTICAL
                                leftMargin = dip(5)
                            }
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(35)
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
                            newPwd = editText {
                                background = null
                                hint = "请输入新密码"
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
                            againPwd = editText {
                                background = null
                                hint = "请再次输入新密码"
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
                    button {
                        backgroundResource = R.drawable.disable_around_button
                        text = "注册"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(24)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        checkBox {
                            setButtonDrawable(R.drawable.checkbox)
                        }
                        textView {
                            text = "我同意"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                        }
                        textView {
                            text = "隐私协议 "
                            textSize = 13f
                            textColor = Color.parseColor("#FFFF7900")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                        textView {
                            text = "和"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                        textView {
                            text = "服务申明"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = dip(25)
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
        newPwd.clearFocus()
        againPwd.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(phoneNumber.windowToken, 0)
        phone.hideSoftInputFromWindow(vCode.windowToken, 0)
        phone.hideSoftInputFromWindow(newPwd.windowToken, 0)
        phone.hideSoftInputFromWindow(againPwd.windowToken, 0)
    }
}