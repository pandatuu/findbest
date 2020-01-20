package app.findbest.vip.project.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class EnlistCheckTipsDialog : Fragment() {

    companion object {
        fun newInstance(buttomClick: ButtomClick, status: Int): EnlistCheckTipsDialog {
            val fragment = EnlistCheckTipsDialog()
            fragment.buttomClick = buttomClick
            fragment.status = status
            return fragment
        }
    }

    interface ButtomClick {
        fun click()
    }

    private lateinit var buttomClick: ButtomClick
    private var status = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    @SuppressLint("StringFormatInvalid")
    private fun createV(): View {
        var tipText = ""
        when (status) {
            1 -> {
                // 1为项目不存在
                tipText = resources.getString(R.string.enlist_status_one)
            }
            2 -> {
                // 2为项目并非发布阶段
                tipText = resources.getString(R.string.enlist_status_two)
            }
            3 -> {
                // 3为身份类型不匹配
                tipText = resources.getString(R.string.enlist_status_three)
            }
            4 -> {
                // 4为国家不匹配
                tipText = resources.getString(R.string.enlist_status_four)
            }
            5 -> {
                // 5为已经应征过
                tipText = resources.getString(R.string.enlist_status_five)
            }
            6 -> {
                // 6为个人用户只允许接两个项目
                tipText = resources.getString(R.string.enlist_status_six)
            }
            7 -> {
                // 7为用户不存在或者不是接包方
                tipText = resources.getString(R.string.enlist_status_seven)
            }
            8 -> {
                // 8用户未认证
                tipText = resources.getString(R.string.enlist_status_eight)
            }
            9 -> {
                // 9用户无vip
                tipText = resources.getString(R.string.enlist_status_nine)
            }
        }
        return UI {
            frameLayout {
                frameLayout {
                    backgroundResource = R.drawable.around_input_3
                    linearLayout {
                        backgroundResource = R.mipmap.image_message_to_remind
                    }.lparams(matchParent, dip(145))
                    verticalLayout {
                        linearLayout {
                            backgroundResource = R.drawable.around_input_5
                            textView {
                                text = tipText
                                textSize = 15f
                                textColor = Color.parseColor("#FF333333")
                            }.lparams(matchParent, wrapContent) {
                                setMargins(dip(25), dip(94), dip(25), dip(28))
                            }
                        }.lparams(matchParent, wrapContent) {
                            setMargins(dip(25), dip(82), dip(25), 0)
                        }
                        button {
                            backgroundResource = R.drawable.enable_around_button
                            text = resources.getString(R.string.enlist_know)
                            textSize = 18f
                            textColor = Color.parseColor("#FFFFFFFF")
                            setOnClickListener {
                                buttomClick.click()
                            }
                        }.lparams(matchParent, dip(50)) {
                            gravity = Gravity.BOTTOM
                            setMargins(dip(25), dip(20), dip(25), dip(20))
                        }
                    }
                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        textView {
                            text = resources.getString(R.string.enlist_tip)
                            textSize = 22f
                            textColor = Color.parseColor("#FFFFFFFF")
                        }.lparams(wrapContent, wrapContent) {
                            topMargin = dip(22)
                        }
                        imageView {
                            imageResource = R.mipmap.ico_the_message_nor
                        }.lparams(dip(100), dip(100)) {
                            topMargin = dip(8)
                        }
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER
                    leftMargin = dip(35)
                    rightMargin = dip(35)
                }
            }
        }.view
    }
}