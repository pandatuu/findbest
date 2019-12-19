package app.findbest.vip.commonfrgmant

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class BottomButton : Fragment() {


    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance(): BottomButton {
            return BottomButton()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = createView()
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }
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

                            imageView {

                                setImageResource(R.mipmap.tab_ico_project_nor)
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

                            imageView {
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

                            imageView {
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

                            imageView {
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

                            imageView {
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