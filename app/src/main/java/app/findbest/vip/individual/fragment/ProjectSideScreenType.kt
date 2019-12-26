package app.findbest.vip.individual.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.utils.FlowLayout
import app.findbest.vip.utils.flowLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast

class ProjectSideScreenType : Fragment() {

    interface ScreenAll {
        fun clickType(s: String)
    }

    companion object {
        fun newInstance(
            screenAll: ScreenAll,
            typeList: MutableList<String> = mutableListOf()
        ): ProjectSideScreenType {
            val fragment = ProjectSideScreenType()
            fragment.screenAll = screenAll
            fragment.mTypeList = typeList
            return fragment
        }
    }

    private lateinit var screenAll: ScreenAll
    private var typeFlow: FlowLayout? = null
    private lateinit var mTypeList: MutableList<String>
    private var typeSomeView: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun onResume() {
        super.onResume()
        if(typeSomeView!=null){
            typeSomeView = null
        }
        if(typeFlow!!.childCount > 0){
            typeFlow?.removeAllViews()
        }
        //分别给flow添加全部标签
        for (index in mTypeList.indices) {
            val view = UI {
                linearLayout {
                    linearLayout {
                        id = index + 1
                        backgroundColor = Color.parseColor("#FFF8F8F8")
                        textView {
                            text = mTypeList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            setMargins(dip(10), dip(7), dip(10), dip(7))
                        }
                        setOnClickListener {
                            if (typeSomeView != null) {
                                if (typeSomeView!!.id != it.id) {
                                    typeSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                                    typeSomeView = it as LinearLayout
                                    typeSomeView!!.backgroundColor = Color.parseColor("#FFFF7C00")
                                }
                            } else {
                                typeSomeView = it as LinearLayout
                                typeSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                            }
                            backgroundColor = Color.parseColor("#FFFF7C00")
                            screenAll.clickType(mTypeList[index])
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
                                    text = "全部"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF555555")
                                }.lparams {
                                    setMargins(dip(10), dip(7), dip(10), dip(7))
                                }
                                setOnClickListener {
                                    if (typeSomeView != null) {
                                        if (typeSomeView!!.id != it.id) {
                                            typeSomeView!!.backgroundColor =
                                                Color.parseColor("#FFF8F8F8")
                                            typeSomeView = it as LinearLayout
                                            typeSomeView!!.backgroundColor =
                                                Color.parseColor("#FFFF7C00")
                                        }
                                    } else {
                                        typeSomeView = it as LinearLayout
                                        typeSomeView!!.backgroundColor =
                                            Color.parseColor("#FFF8F8F8")
                                    }
                                    backgroundColor = Color.parseColor("#FFFF7C00")
                                    screenAll.clickType("全部")
                                }
                            }.lparams(wrapContent, dip(30)) {
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                            typeSomeView = linea
                        }
                    }.view
                    typeFlow?.addView(firstView)
                    typeFlow?.addView(view)
                }
                9 -> {
                    typeFlow?.addView(view)
                    val lastView = UI {
                        linearLayout {
                            linearLayout {
                                id = index + 2
                                backgroundColor = Color.parseColor("#FFF8F8F8")
                                textView {
                                    text = "更多"
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
                                    if (typeSomeView != null) {
                                        if (typeSomeView!!.id != it.id) {
                                            typeSomeView!!.backgroundColor =
                                                Color.parseColor("#FFF8F8F8")
                                            typeSomeView = it as LinearLayout
                                            typeSomeView!!.backgroundColor =
                                                Color.parseColor("#FFFF7C00")
                                        }
                                    } else {
                                        typeSomeView = it as LinearLayout
                                        typeSomeView!!.backgroundColor =
                                            Color.parseColor("#FFF8F8F8")
                                    }
                                    screenAll.clickType("更多")
                                }
                            }.lparams(wrapContent, dip(30)) {
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                        }
                    }.view
                    typeFlow?.addView(lastView)
                }
                in 1..8 -> {
                    typeFlow?.addView(view)
                }
            }
        }

    }

    private fun createV(): View {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE
                linearLayout {
                    textView {
                        text = "类型"
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
                    typeFlow = flowLayout {
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

    fun getTypeItem(): String {
        if (typeSomeView != null) {
            val type = typeSomeView?.getChildAt(0) as TextView

            return type.text.toString()
        }
        return ""
    }


    fun setMore() {
        typeSomeView = null
        typeFlow?.removeViewAt(11)
        for (index in mTypeList.indices) {
            val view = UI {
                linearLayout {
                    linearLayout {
                        id = index + 1
                        backgroundColor = Color.parseColor("#FFF8F8F8")
                        textView {
                            text = mTypeList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            setMargins(dip(10), dip(7), dip(10), dip(7))
                        }
                        setOnClickListener {
                            if (typeSomeView != null) {
                                if (typeSomeView!!.id != it.id) {
                                    typeSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                                    typeSomeView = it as LinearLayout
                                    typeSomeView!!.backgroundColor = Color.parseColor("#FFFF7C00")
                                }
                            } else {
                                typeSomeView = it as LinearLayout
                                typeSomeView!!.backgroundColor = Color.parseColor("#FFF8F8F8")
                            }
                            backgroundColor = Color.parseColor("#FFFF7C00")
                            screenAll.clickType(mTypeList[index])
                        }
                    }.lparams(wrapContent, dip(30)) {
                        leftMargin = dip(10)
                        topMargin = dip(10)
                    }
                }
            }.view
            if (index > 9) {
                typeFlow?.addView(view)
            }
        }
    }
}