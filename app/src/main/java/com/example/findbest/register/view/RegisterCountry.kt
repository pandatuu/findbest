package com.example.findbest.register.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.findbest.R
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*

class RegisterCountry: AppCompatActivity() {

    private lateinit var china: RelativeLayout
    private lateinit var japan: RelativeLayout
    private lateinit var korea: RelativeLayout
    private var isChina = true
    private var isJapan = false
    private var isKorea = false

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
                        text = "2/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = "选择国家和地区"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        val country = arrayListOf("中国","日本","韩国")
                        china = relativeLayout {
                            backgroundResource = R.drawable.rigister_country_input
                            textView {
                                text = "中国"
                                textSize = 15f
                                textColor = Color.parseColor("#FF333333")
                            }.lparams{
                                centerInParent()
                            }
                            imageView {
                                imageResource = R.mipmap.login_ico_triangle
                                if(!isChina)
                                    visibility = RelativeLayout.GONE
                            }.lparams(dip(27),dip(27)){
                                setMargins(0,0,dip(1),dip(1))
                                alignParentBottom()
                                alignParentRight()
                            }
                            setOnClickListener {
                                isChina = !isChina
                                val cimage = china.getChildAt(1) as ImageView
                                val jimage = japan.getChildAt(1) as ImageView
                                val kimage = korea.getChildAt(1) as ImageView
                                if(isChina){
                                    cimage.visibility = RelativeLayout.VISIBLE
                                    jimage.visibility = RelativeLayout.GONE
                                    kimage.visibility = RelativeLayout.GONE
                                    isKorea = false
                                    isJapan = false
                                }else{
                                    cimage.visibility = RelativeLayout.GONE
                                }
                            }
                        }.lparams(matchParent,dip(40)){
                            topMargin = dip(15)
                        }
                        japan = relativeLayout {
                            backgroundResource = R.drawable.rigister_country_input
                            textView {
                                text = "日本"
                                textSize = 15f
                                textColor = Color.parseColor("#FF333333")
                            }.lparams{
                                centerInParent()
                            }
                            imageView {
                                imageResource = R.mipmap.login_ico_triangle
                                if(!isJapan)
                                    visibility = RelativeLayout.GONE
                            }.lparams(dip(27),dip(27)){
                                setMargins(0,0,dip(1),dip(1))
                                alignParentBottom()
                                alignParentRight()
                            }
                            setOnClickListener {
                                isJapan = !isJapan
                                val cimage = china.getChildAt(1) as ImageView
                                val jimage = japan.getChildAt(1) as ImageView
                                val kimage = korea.getChildAt(1) as ImageView
                                if(isJapan){
                                    jimage.visibility = RelativeLayout.VISIBLE
                                    cimage.visibility = RelativeLayout.GONE
                                    kimage.visibility = RelativeLayout.GONE
                                    isChina = false
                                    isKorea = false
                                }else{
                                    jimage.visibility = RelativeLayout.GONE
                                }
                            }
                        }.lparams(matchParent,dip(40)){
                            topMargin = dip(15)
                        }
                        korea = relativeLayout {
                            backgroundResource = R.drawable.rigister_country_input
                            textView {
                                text = "韩国"
                                textSize = 15f
                                textColor = Color.parseColor("#FF333333")
                            }.lparams{
                                centerInParent()
                            }
                            imageView {
                                imageResource = R.mipmap.login_ico_triangle
                                if(!isKorea)
                                    visibility = RelativeLayout.GONE
                            }.lparams(dip(27),dip(27)){
                                setMargins(0,0,dip(1),dip(1))
                                alignParentBottom()
                                alignParentRight()
                            }
                            setOnClickListener {
                                isKorea = !isKorea
                                val cimage = china.getChildAt(1) as ImageView
                                val jimage = japan.getChildAt(1) as ImageView
                                val kimage = korea.getChildAt(1) as ImageView
                                if(isKorea){
                                    kimage.visibility = RelativeLayout.VISIBLE
                                    cimage.visibility = RelativeLayout.GONE
                                    jimage.visibility = RelativeLayout.GONE
                                    isChina = false
                                    isJapan = false
                                }else{
                                    kimage.visibility = RelativeLayout.GONE
                                }
                            }
                        }.lparams(matchParent,dip(40)){
                            topMargin = dip(15)
                        }
                    }.lparams(matchParent, wrapContent){
                        setMargins(dip(18),dip(74),dip(18),0)
                    }
                    button {
                        backgroundResource = R.drawable.enable_around_button
                        text = "下一步"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            when(true){
                                isChina -> toast("china")
                                isJapan -> toast("japan")
                                isKorea -> toast("korea")
                            }
                        }
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(35)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(40),dip(40),0)
                }
            }
        }
    }
}