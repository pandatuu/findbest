package app.findbest.vip.individual.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.individual.view.CompanyWeb
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger
import anet.channel.util.Utils.context
import anet.channel.util.Utils.context





class UsMain:FragmentParent() {
    private var mContext: Context? = null
    companion object {
        fun newInstance(): UsMain {
            return UsMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    @SuppressLint("SetTextI18n")
    private fun createView():View {
        return UI {
            verticalLayout {
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    imageView {
                        imageResource = R.mipmap.image_logo
                    }.lparams {
                        width = dip(79)
                        height = dip(79)
                        topMargin = dip(46)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    textView {
                        text = "FindBest"
                        textColor = Color.parseColor("#333333")
                        textSize = 17f
                        gravity = Gravity.CENTER
                    }.lparams(width = matchParent,height = wrapContent){
                        topMargin = dip(7)
                    }
                    relativeLayout {
                        //获取包管理器
                        val pm = context.packageManager
                        //获取包信息
                        val packageInfo = pm.getPackageInfo(context.packageName, 0)
                        //返回版本号
                        val version = packageInfo.versionName
                        textView {
                            text = "Version：$version"
                            textSize = 14f
                            textColor = Color.parseColor("#666666")
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            centerHorizontally()
                        }
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        topMargin = dip(3)
                    }

                    textView {
                        textResource = R.string.us_prefix
                        textColor = Color.parseColor("#222222")
                        textSize = 17f
                        gravity = Gravity.CENTER
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(width = dip(180),height = wrapContent){
                        topMargin = dip(30)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

//                    textView {
//                        textResource = R.string.us_suffix
//                        textColor = Color.parseColor("#222222")
//                        textSize = 17f
//                        gravity = Gravity.CENTER
//                        typeface = Typeface.DEFAULT_BOLD
//                    }.lparams(width = matchParent,height = wrapContent){
//                        topMargin = dip(2)
//                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        backgroundResource = R.drawable.around_shadow_10
                        relativeLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            textView {
                                textResource = R.string.us_web
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                leftMargin = dip(5)
                            }

                            textView {
                                text = "www.findbest.vip"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                                withTrigger().click {
                                    startActivity<CompanyWeb>()
                                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                }
                            }.lparams{
                                leftMargin = dip(87)
                            }

                            // 预留，不确定是否开放
//                            this.withTrigger().click {
//
//                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            val phoneNum = "028-61114932"
                            textView {
                                textResource = R.string.us_phone
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                leftMargin = dip(5)
                            }

                            textView {
                                text = "$phoneNum"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                                setTextIsSelectable(true)
                            }.lparams{
                                leftMargin = dip(87)
                            }

                            // 预留，不确定是否开放
//                            this.withTrigger().click {
//                                val intent = Intent(Intent.ACTION_DIAL)
//                                val data = Uri.parse("tel:$phoneNum")
//                                intent.data = data
//                                startActivity(intent)
//                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
//                            val phoneNum = "03-6806-0908"
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                text = "E-Mail"
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                leftMargin = dip(5)
                            }

                            textView {
                                text = "info@findbest.vip"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                            }.lparams{
                                leftMargin = dip(87)
                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(31)
                        leftMargin = dip(15)
                        rightMargin = dip(15)
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                    backgroundColor = Color.parseColor("#FFFFFFFF")
                }
            }
        }.view
    }


}