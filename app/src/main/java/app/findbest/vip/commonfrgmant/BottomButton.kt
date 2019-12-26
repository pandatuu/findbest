package app.findbest.vip.commonfrgmant

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonactivity.MainActivity
import app.findbest.vip.instance.fragment.InstanceDisplay
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class BottomButton : Fragment() {


    private var mContext: Context? = null
    lateinit var main: MainActivity



    lateinit var imageView1:ImageView
    lateinit var imageView2:ImageView
    lateinit var imageView3:ImageView
    lateinit var imageView4:ImageView
    lateinit var imageView5:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }
    }

    companion object {
        fun newInstance(main: MainActivity): BottomButton {
            var f = BottomButton()
            f.main=main
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = createView()

        return fragmentView
    }

    private fun createView(): View {
        return UI {
            verticalLayout {
                linearLayout() {

                    //项目
                    linearLayout() {
                        gravity = Gravity.CENTER
                        verticalLayout {

                            this.withTrigger().click {
                                main.jumpPage(1)

                                imageView1.setImageResource(R.mipmap.tab_ico_project_yes)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)


                            }

                            imageView1=  imageView {

                                setImageResource(R.mipmap.tab_ico_project_yes)
                            }

                            textView {
                                textSize = 10f
                                text = "项目"
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams() {
                                topMargin = dip(2)
                            }

                        }.lparams() {
                            height = dip(35)
                            width = dip(21)
                        }

                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }

                    linearLayout() {

                        gravity = Gravity.CENTER
                        verticalLayout {


                            this.withTrigger().click {
                                main.jumpPage(2)

                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_yes)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }


                            imageView2=   imageView {
                                setImageResource(R.mipmap.tab_ico_case_nor)
                            }

                            textView {
                                textSize = 10f
                                text = "案例"
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams() {
                                topMargin = dip(2)
                            }

                        }.lparams() {
                            height = dip(35)
                            width = dip(21)
                        }


                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }


                    linearLayout() {

                        gravity = Gravity.CENTER
                        verticalLayout {


                            this.withTrigger().click {
                                main.jumpPage(3)

                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_yes)
                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }


                            imageView3=    imageView {
                                setImageResource(R.mipmap.tab_ico_painter_nor)
                            }

                            textView {
                                textSize = 10f
                                text = "画师"
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams() {
                                topMargin = dip(2)
                            }

                        }.lparams() {
                            height = dip(35)
                            width = dip(21)
                        }


                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }


                    linearLayout() {


                        gravity = Gravity.CENTER
                        verticalLayout {

                            this.withTrigger().click {
                                main.jumpPage(4)

                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
                                imageView4.setImageResource(R.mipmap.tab_ico_message_yes)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            imageView4=     imageView {
                                setImageResource(R.mipmap.tab_ico_message_nor)
                            }

                            textView {
                                textSize = 10f
                                text = "消息"
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams() {
                                topMargin = dip(2)
                            }

                        }.lparams() {
                            height = dip(35)
                            width = dip(21)
                        }


                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }


                    linearLayout() {


                        gravity = Gravity.CENTER
                        verticalLayout {

                            this.withTrigger().click {
                                main.jumpPage(5)

                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_yes)
                            }

                            imageView5=     imageView {
                                setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            textView {
                                textSize = 10f
                                text = "我的"
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams() {
                                topMargin = dip(2)
                            }

                        }.lparams() {
                            height = dip(35)
                            width = dip(21)
                        }


                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }


                }.lparams() {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view
    }


}