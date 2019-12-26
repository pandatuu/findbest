package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class UsMain:FragmentParent() {
    private var mContext: Context? = null
    lateinit var myEdit:EditText
    lateinit var myText:TextView

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
                        val version = "1.2.3"
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
                        text = "致力于打造全球顶尖的"
                        textColor = Color.parseColor("#222222")
                        textSize = 17f
                        gravity = Gravity.CENTER
                    }.lparams(width = matchParent,height = wrapContent){
                        topMargin = dip(30)
                    }

                    textView {
                        text = "原画创作交易平台"
                        textColor = Color.parseColor("#222222")
                        textSize = 17f
                        gravity = Gravity.CENTER
                    }.lparams(width = matchParent,height = wrapContent){
                        topMargin = dip(2)
                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                text = "平台网址"
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                rightMargin = dip(20)
                            }

                            textView {
                                text = "www.findbest.vip"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            val phoneNum = "028-61114932"
                            textView {
                                text = "客服电话"
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                rightMargin = dip(20)
                            }

                            textView {
                                text = "$phoneNum"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                            }
//                            this.withTrigger().click {
////                                val intent = Intent(context!!, CompanyWebSiteActivity::class.java)
////                                intent.putExtra("webUrl", "https://astar2020.jp/")
////                                intent.putExtra("companyName", "株式会社アジアスター")
////                                startActivity(intent)
////                                overridePendingTransition(R.anim.right_in, R.anim.left_out)
//                            }

                            this.withTrigger().click {
                                val intent = Intent(Intent.ACTION_DIAL)
                                val data = Uri.parse("tel:$phoneNum")
                                intent.data = data
                                startActivity(intent)
                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
//                            backgroundResource = R.drawable.aboutas_bottom_border
//                            val phoneNum = "03-6806-0908"
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                text = "E-Mail"
                                textSize = 15f
                                textColor = Color.parseColor("#666666")
                            }.lparams(wrapContent, wrapContent){
                                rightMargin = dip(20)
                            }

                            textView {
                                text = "info@findbest.vip"
                                textSize = 15f
                                textColor = Color.parseColor("#202020")
                            }
                        }.lparams(matchParent, dip(55)) {
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(31)
                        leftMargin = dip(35)
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