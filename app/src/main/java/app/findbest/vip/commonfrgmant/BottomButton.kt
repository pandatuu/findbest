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
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class BottomButton : Fragment() {

    private var mContext: Context? = null
    lateinit var main: MainActivity
    private lateinit var imageView1:ImageView
    private lateinit var imageView2:ImageView
    private lateinit var imageView3:ImageView
    private lateinit var imageView4:ImageView
    private lateinit var imageView5:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = if (parentFragment != null) {
            parentFragment?.context
        } else {
            activity
        }
    }

    companion object {
        fun newInstance(main: MainActivity): BottomButton {
            val f = BottomButton()
            f.main=main
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView(): View {
        return UI {
            verticalLayout {
                linearLayout {
                    //项目
                    linearLayout {
                        gravity = Gravity.CENTER
                        verticalLayout {
                            this.withTrigger().click {
                                main.jumpPage(1)
                                imageView1.setImageResource(R.mipmap.tab_ico_project_yes)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
//                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            imageView1=  imageView {
                                setImageResource(R.mipmap.tab_ico_project_yes)
                            }

                            textView {
                                textSize = 10f
                                text = activity!!.getString(R.string.project_xiangmu)
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams {
                                topMargin = dip(2)
                            }
                        }.lparams {
                            height = dip(35)
                            width = dip(21)
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }
                    //案例
                    linearLayout {
                        gravity = Gravity.CENTER
                        verticalLayout {
                            this.withTrigger().click {
                                main.jumpPage(2)
                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_yes)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
//                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            imageView2=   imageView {
                                setImageResource(R.mipmap.tab_ico_case_nor)
                            }

                            textView {
                                textSize = 10f
                                text = activity!!.getString(R.string.sample_anli)
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams {
                                topMargin = dip(2)
                            }
                        }.lparams {
                            height = dip(35)
                            width = dip(21)
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }
                    //画师
                    linearLayout {
                        gravity = Gravity.CENTER
                        verticalLayout {
                            this.withTrigger().click {
                                main.jumpPage(3)
                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_yes)
//                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            imageView3 = imageView {
                                setImageResource(R.mipmap.tab_ico_painter_nor)
                            }

                            textView {
                                textSize = 10f
                                text = activity!!.getString(R.string.painter_huashi)
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams {
                                topMargin = dip(2)
                            }
                        }.lparams {
                            height = dip(35)
                            width = dip(21)
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }
                    //消息
//                    linearLayout() {
//
//
//                        gravity = Gravity.CENTER
//                        verticalLayout {
//
//                            this.withTrigger().click {
//                                main.jumpPage(4)
//
//                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
//                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
//                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
//                                imageView4.setImageResource(R.mipmap.tab_ico_message_yes)
//                                imageView5.setImageResource(R.mipmap.tab_ico_my_nor)
//                            }
//
//                            imageView4=     imageView {
//                                setImageResource(R.mipmap.tab_ico_message_nor)
//                            }
//
//                            textView {
//                                textSize = 10f
//                                text = activity!!.getString(R.string.message_xiaoxi)
//                                textColor = Color.parseColor("#FF666666")
//                                gravity = Gravity.CENTER
//                            }.lparams() {
//                                topMargin = dip(2)
//                            }
//
//                        }.lparams() {
//                            height = dip(35)
//                            width = dip(21)
//                        }
//
//
//                    }.lparams() {
//                        width = dip(0)
//                        weight = 1f
//                        height = matchParent
//                    }
                    //我的
                    linearLayout {
                        gravity = Gravity.CENTER
                        verticalLayout {
                            this.withTrigger().click {
                                main.jumpPage(5)
                                imageView1.setImageResource(R.mipmap.tab_ico_project_nor)
                                imageView2.setImageResource(R.mipmap.tab_ico_case_nor)
                                imageView3.setImageResource(R.mipmap.tab_ico_painter_nor)
//                                imageView4.setImageResource(R.mipmap.tab_ico_message_nor)
                                imageView5.setImageResource(R.mipmap.tab_ico_my_yes)
                            }
                            imageView5 = imageView {
                                setImageResource(R.mipmap.tab_ico_my_nor)
                            }

                            textView {
                                textSize = 10f
                                text = activity!!.getString(R.string.mine_wode)
                                textColor = Color.parseColor("#FF666666")
                                gravity = Gravity.CENTER
                            }.lparams {
                                topMargin = dip(2)
                            }
                        }.lparams {
                            height = dip(35)
                            width = dip(21)
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view
    }
}