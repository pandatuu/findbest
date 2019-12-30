package app.findbest.vip.instance.fragment.artist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.individual.view.Feedback
import app.findbest.vip.individual.view.Head
import app.findbest.vip.individual.view.Help
import app.findbest.vip.individual.view.Us
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import withTrigger

class Terminal:FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context

    companion object {
        fun newInstance(context: Context): Terminal {
            val f = Terminal()
            f.activityInstance = context
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
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
                    backgroundResource = R.mipmap.image_message_png
                    relativeLayout {
                        toolbar1 = toolbar {
                            isEnabled = true
                            title = ""
                        }.lparams{
                            width = matchParent
                            alignParentBottom()
                        }
//
                        textView {
                            textResource= R.string.tl_title
                            gravity = Gravity.CENTER
                            textSize =  17f
                            textColor = Color.WHITE
                        }.lparams {
                            width = matchParent
                            height =dip(54-getStatusBarHeight(this@Terminal.context!!))
                            alignParentBottom()
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL

                            onClick {
                                toast("分享")
                            }
                            imageView {
                                imageResource = R.mipmap.ico_share_nor
                            }.lparams(width = dip(16),height = dip(16)){
                                rightMargin = dip(7)
                            }
                            textView {
                                textResource = R.string.tl_share
                                textSize = 15f
                                textColor = Color.WHITE
                            }.lparams(width = dip(31),height = dip(21))
                        }.lparams{
                            height = dip(54 - getStatusBarHeight(this@Terminal.context!!))
                            rightMargin = dip(17)
                            alignParentRight()
                            alignParentBottom()
                        }
                    }.lparams{
                        width = matchParent
                        height = dip(54)
                    }

                    verticalLayout {
                       linearLayout {
                           verticalLayout {
                               textView {
                                   text = "Amy Gonzalez"
                                   textSize = 30f
                                   textColor = Color.WHITE
                               }.lparams(width = dip(209),height = dip(42)){
                                   bottomMargin = dip(3)
                                   leftMargin = dip(2)
                               }

                               linearLayout {
                                   gravity = Gravity.CENTER_VERTICAL
                                   imageView {
                                       imageResource = R.mipmap.ico_certification_nor
                                   }.lparams(width = dip(26),height = dip(26)){
                                       rightMargin = dip(3)
                                   }

                                   imageView {
                                       imageResource = R.mipmap.retist
                                   }.lparams(width = dip(65),height = dip(21))
                               }
                           }.lparams(width = wrapContent,height = wrapContent){
                               weight = 1f
                               leftMargin = dip(22)
                           }

                           imageView {
                               imageResource = R.mipmap.default_avatar

                               this.withTrigger().click {
                                   var intent = Intent(context, Head::class.java)
                                   startActivity(intent)
                                   activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                               }
                           }.lparams(width = dip(64),height = dip(64)){

                               rightMargin = dip(20)
                           }
                       }.lparams(width = matchParent,height = wrapContent){
                           weight = 1f
                           topMargin = dip(17)
                       }

                        linearLayout {
                            verticalLayout {
                                textView {
                                    text = "3"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_application_items
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }
                            verticalLayout {
                                textView {
                                    text = "2"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_production_project
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            verticalLayout {
                                textView {
                                    text = "168"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_transaction_complete
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            verticalLayout {

                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            bottomMargin = dip(20)
                        }
                    }.lparams(width = matchParent,height = matchParent){
                        weight = 1f

                    }




                }.lparams(width = matchParent,height = dip(229))

                scrollView {
                    verticalLayout {
                        backgroundColor = Color.WHITE
                        linearLayout{
                            gravity = Gravity.CENTER
                            linearLayout {
                                backgroundColor = Color.parseColor("#F87F2D")
                            }.lparams(width = dip(4),height = dip(18)){
                                leftMargin = dip(4)
                                rightMargin = dip(9)
                            }

                            textView {
                                textResource = R.string.tl_account_balance
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }
                        }.lparams(width = wrapContent,height = dip(28)){
                            leftMargin = dip(20)
                            topMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_rmb
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "2600.00"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = matchParent){
                            topMargin = dip(10)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_frozen_amount
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "600.00"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = matchParent){
                            topMargin = dip(11)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_jpy
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "40681"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(15)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_won
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "432776"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(15)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_file_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_my_project
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(25)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_update_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_version_update
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            textView {
                                text = "V1.1.22"
                                textSize = 15f
                                textColor = Color.parseColor("#333333")
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                                leftMargin = dip(12)
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            this.withTrigger().click {
                                var intent = Intent(context, Help::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }

                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_help_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_use_help
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            this.withTrigger().click {
                                var intent = Intent(context, Feedback::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }
                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_opinion_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_feedback
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER

                            this.withTrigger().click {
                                var intent = Intent(context, Us::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }

                            imageView {
                                imageResource = R.mipmap.ico_aboutus_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_about_us
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            bottomMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }


                    }.lparams(width = matchParent)
                }
            }
        }.view
    }
}