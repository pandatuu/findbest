package app.findbest.vip.instance.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.painter.fragment.PainterScreenType
import app.findbest.vip.utils.FlowLayout
import app.findbest.vip.utils.flowLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast

class InstanceScreenStyle : Fragment() {

    interface ScreenAll {
        fun clickMore()
    }

    companion object {
        fun newInstance(
            screenAll: ScreenAll,
            styleList: MutableList<String>
        ): InstanceScreenStyle {
            val fragment = InstanceScreenStyle()
            fragment.screenAll = screenAll
            fragment.mStyleList = styleList
            return fragment
        }
    }

    private lateinit var screenAll: ScreenAll
    private var styleFlow: FlowLayout? = null
    private lateinit var mStyleList: MutableList<String>
    private var styleSomeView: LinearLayout? = null
    private var styleText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE
                linearLayout {
                    styleText = textView {
                        visibility = LinearLayout.GONE
                        text = resources.getString(R.string.srceen_style)
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams {
                        bottomMargin = dip(12)
                    }
                }.lparams(matchParent, dip(40)) {
                    topMargin = dip(40)
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_top_line
                    styleFlow = flowLayout {
                    }.lparams {
                        topMargin = dip(2)
                    }
                }.lparams {
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
            }
        }.view
    }

    fun setStyleList(styleList: MutableList<String>) {
        if(isAdded){
            styleSomeView = null
            styleFlow?.removeAllViews()
            mStyleList = styleList

            //分别给flow添加全部标签
            for (index in mStyleList.indices) {
                val view = UI {
                    linearLayout {
                        linearLayout {
                            id = index + 1
                            backgroundColor = Color.parseColor("#FFF8F8F8")
                            textView {
                                text = mStyleList[index]
                                textSize = 12f
                                textColor = Color.parseColor("#FF555555")
                            }.lparams {
                                setMargins(dip(10), dip(7), dip(10), dip(7))
                            }
                            setOnClickListener {
                                if (styleSomeView != null) {
                                    if (styleSomeView!!.id != it.id) {
                                        styleSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                                        styleSomeView = it as LinearLayout
                                        styleSomeView!!.backgroundColor = Color.parseColor("#FFFF7C00")
                                    }
                                } else {
                                    styleSomeView = it as LinearLayout
                                    styleSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                                }
                            }
                        }.lparams(wrapContent, dip(30)) {
                            leftMargin = dip(10)
                            topMargin = dip(10)
                        }
                    }
                }.view
                when (index) {
                    0 -> {
                        val firstView = UI {
                            linearLayout {
                                val linea = linearLayout {
                                    id = index
                                    backgroundColor = Color.parseColor("#FFFF7C00")
                                    textView {
                                        text = resources.getString(R.string.srceen_all)
                                        textSize = 12f
                                        textColor = Color.parseColor("#FF555555")
                                    }.lparams {
                                        setMargins(dip(10), dip(7), dip(10), dip(7))
                                    }
                                    setOnClickListener {
                                        if (styleSomeView != null) {
                                            if (styleSomeView!!.id != it.id) {
                                                styleSomeView!!.backgroundColor =
                                                    Color.parseColor("#FFF8F8F8")
                                                styleSomeView = it as LinearLayout
                                                styleSomeView!!.backgroundColor =
                                                    Color.parseColor("#FFFF7C00")
                                            }
                                        } else {
                                            styleSomeView = it as LinearLayout
                                            styleSomeView!!.backgroundColor =
                                                Color.parseColor("#FFF8F8F8")
                                        }
                                    }
                                }.lparams(wrapContent, dip(30)) {
                                    leftMargin = dip(10)
                                    topMargin = dip(10)
                                }
                                styleSomeView = linea
                            }
                        }.view
                        styleFlow?.addView(firstView)
                        styleFlow?.addView(view)
                    }
                    9 -> {
                        styleFlow?.addView(view)
                        val lastView = UI {
                            linearLayout {
                                linearLayout {
                                    id = index + 2
                                    backgroundColor = Color.parseColor("#FFF8F8F8")
                                    textView {
                                        text = resources.getString(R.string.srceen_more)
                                        textSize = 12f
                                        textColor = Color.parseColor("#FF555555")
                                    }.lparams {
                                        setMargins(dip(10), dip(7), 0, dip(7))
                                    }
                                    imageView {
                                        imageResource = R.mipmap.ico_pack_up_arrow_nor
                                    }.lparams(dip(8), dip(4)) {
                                        setMargins(dip(5), dip(14), dip(10), dip(12))
                                    }
                                    setOnClickListener {
                                        if (styleSomeView != null) {
                                            if (styleSomeView!!.id != it.id) {
                                                styleSomeView!!.backgroundColor =
                                                    Color.parseColor("#FFF8F8F8")
                                                styleSomeView = it as LinearLayout
                                                styleSomeView!!.backgroundColor =
                                                    Color.parseColor("#FFFF7C00")
                                            }
                                        } else {
                                            styleSomeView = it as LinearLayout
                                            styleSomeView!!.backgroundColor =
                                                Color.parseColor("#FFF8F8F8")
                                        }
                                        screenAll.clickMore()
                                    }
                                }.lparams(wrapContent, dip(30)) {
                                    leftMargin = dip(10)
                                    topMargin = dip(10)
                                }
                            }
                        }.view
                        styleFlow?.addView(lastView)
                    }
                    in 1..8 -> {
                        styleFlow?.addView(view)
                    }
                }
            }
            styleText!!.visibility = LinearLayout.VISIBLE
        }
    }
    fun setTextGone(){
        styleSomeView = null
        styleFlow?.removeAllViews()
        styleText?.visibility = LinearLayout.GONE
    }

    fun getStyleItem(): String {
        if (styleSomeView != null) {
            val style = styleSomeView?.getChildAt(0) as TextView

            return style.text.toString()
        }
        return ""
    }

    fun setMore() {
        styleSomeView = null
        styleFlow?.removeViewAt(11)
        for (index in mStyleList.indices) {
            val view = UI {
                linearLayout {
                    linearLayout {
                        id = index + 1
                        backgroundColor = Color.parseColor("#FFF8F8F8")
                        textView {
                            text = mStyleList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            setMargins(dip(10), dip(7), dip(10), dip(7))
                        }
                        setOnClickListener {
                            if (styleSomeView != null) {
                                if (styleSomeView!!.id != it.id) {
                                    styleSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                                    styleSomeView = it as LinearLayout
                                    styleSomeView!!.backgroundColor = Color.parseColor("#FFFF7C00")
                                }
                            } else {
                                styleSomeView = it as LinearLayout
                                styleSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                            }
                            backgroundColor = Color.parseColor("#FFFF7C00")
                        }
                    }.lparams(wrapContent, dip(30)) {
                        leftMargin = dip(10)
                        topMargin = dip(10)
                    }
                }
            }.view
            if (index > 9) {
                styleFlow?.addView(view)
            }
        }
    }
}