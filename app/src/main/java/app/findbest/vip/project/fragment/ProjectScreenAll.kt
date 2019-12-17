package app.findbest.vip.project.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.utils.FlowLayout
import app.findbest.vip.utils.flowLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class ProjectScreenAll: Fragment() {

    companion object{
        fun newInstance(typeList: MutableList<String>, styleList: MutableList<String>): ProjectScreenAll{
            val fragment = ProjectScreenAll()
            fragment.mTypeList = typeList
            fragment.mStyleList = styleList
            return fragment
        }
    }

    private var typeFlow: FlowLayout? = null
    private var styleFlow: FlowLayout? = null
    private lateinit var mTypeList: MutableList<String>
    private lateinit var mStyleList: MutableList<String>
    private var isTypeFlow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun onResume() {
        super.onResume()

        //分别给flow添加全部标签
        for (index in mTypeList.indices){
            val view = UI {
                linearLayout {
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF8F8F8")
                        textView {
                            text = mTypeList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams{
                            setMargins(dip(10),dip(7),dip(10),dip(7))
                        }
                    }.lparams(wrapContent,dip(30)){
                        leftMargin = dip(10)
                        topMargin = dip(10)
                    }
                }
            }.view
            when (index) {
                0 -> {
                    val firstView = UI {
                        linearLayout {
                            linearLayout {
                                if(isTypeFlow)
                                    backgroundColor = Color.parseColor("#FFF8F8F8")
                                textView {
                                    text = "全部"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF555555")
                                }.lparams{
                                    setMargins(dip(10),dip(7),dip(10),dip(7))
                                }
                                setOnClickListener {
                                    backgroundColor = Color.parseColor("#FFFF7C00")
                                }
                            }.lparams(wrapContent,dip(30)){
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                        }
                    }.view
                    typeFlow?.addView(firstView)
                    typeFlow?.addView(view)
                }
                mTypeList.size-1 -> {
                    typeFlow?.addView(view)
                    val lastView = UI {
                        linearLayout {
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF8F8F8")
                                textView {
                                    text = "更多"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF555555")
                                }.lparams{
                                    setMargins(dip(10),dip(7),0,dip(7))
                                }
                                imageView {
                                    imageResource = R.mipmap.ico_pack_up_arrow_nor
                                }.lparams(dip(8),dip(4)){
                                    setMargins(dip(5),dip(14),dip(10),dip(12))
                                }
                            }.lparams(wrapContent,dip(30)){
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                        }
                    }.view
                    typeFlow?.addView(lastView)
                }
                else -> {
                    typeFlow?.addView(view)
                }
            }
        }

        for (index in mStyleList.indices){
            val view = UI {
                linearLayout {
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF8F8F8")
                        textView {
                            text = mStyleList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams{
                            setMargins(dip(10),dip(7),dip(10),dip(7))
                        }
                    }.lparams(wrapContent,dip(30)){
                        leftMargin = dip(10)
                        topMargin = dip(10)
                    }
                }
            }.view
            when (index) {
                0 -> {
                    val firstView = UI {
                        linearLayout {
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF8F8F8")
                                textView {
                                    text = "全部"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF555555")
                                }.lparams{
                                    setMargins(dip(10),dip(7),dip(10),dip(7))
                                }
                            }.lparams(wrapContent,dip(30)){
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                        }
                    }.view
                    styleFlow?.addView(firstView)
                    styleFlow?.addView(view)
                }
                mTypeList.size-1 -> {
                    styleFlow?.addView(view)
                    val lastView = UI {
                        linearLayout {
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF8F8F8")
                                textView {
                                    text = "更多"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF555555")
                                }.lparams{
                                    setMargins(dip(10),dip(7),0,dip(7))
                                }
                                imageView {
                                    imageResource = R.mipmap.ico_pack_up_arrow_nor
                                }.lparams(dip(8),dip(4)){
                                    setMargins(dip(5),dip(14),dip(10),dip(12))
                                }
                            }.lparams(wrapContent,dip(30)){
                                leftMargin = dip(10)
                                topMargin = dip(10)
                            }
                        }
                    }.view
                    styleFlow?.addView(lastView)
                }
                else -> {
                    styleFlow?.addView(view)
                }
            }
        }
    }

    private fun createV(): View {
        return UI {
            verticalLayout {
                linearLayout {
                    textView {
                        text = "类型"
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams{
                        bottomMargin = dip(12)
                    }
                }.lparams(matchParent,dip(40)){
                    topMargin = dip(40)
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_top_line
                    typeFlow = flowLayout {
                    }.lparams{
                        topMargin = dip(2)
                    }
                }.lparams{
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
                linearLayout {
                    textView {
                        text = "风格"
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams{
                        bottomMargin = dip(12)
                    }
                }.lparams(matchParent,dip(40)){
                    topMargin = dip(40)
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_top_line
                    styleFlow = flowLayout {
                    }.lparams{
                        topMargin = dip(12)
                    }
                }.lparams{
                    leftMargin = dip(20)
                    rightMargin = dip(20)
                }
            }
        }.view
    }
}