package app.findbest.vip.project.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip

class ProjectDemandDetails: Fragment() {

    companion object{
        fun newInstance(): ProjectDemandDetails{
            val fragment = ProjectDemandDetails()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    @SuppressLint("SetTextI18n")
    private fun createV(): View {
        return UI {
            linearLayout {
                scrollView {
                    isVerticalScrollBarEnabled = false
                    linearLayout{
                        orientation = LinearLayout.VERTICAL
                        /**
                         * 制作费用
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "制作费用"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                verticalLayout {
                                    textView {
                                        text = "截稿日期"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "总金额"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "结算币种"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }
                                verticalLayout {
                                    textView {
                                        text = "2019-11-22"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "¥ 8000.00"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "人民币"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    leftMargin = dip(12)
                                }
                            }.lparams(wrapContent, matchParent){
                                alignParentLeft()
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                verticalLayout {
                                    textView {
                                        text = "原画数量"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "个人单价"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "源泉税率"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }
                                verticalLayout {
                                    textView {
                                        text = "8"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "¥ 800.00"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "15%"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    leftMargin = dip(12)
                                }
                            }.lparams(wrapContent, matchParent){
                                alignParentRight()
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 评价奖金
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "制作费用"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                verticalLayout {
                                    textView {
                                        text = "满意"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "超越期待"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }
                                verticalLayout {
                                    textView {
                                        text = "100"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "400"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    leftMargin = dip(12)
                                }
                            }.lparams(wrapContent, matchParent){
                                alignParentLeft()
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                verticalLayout {
                                    textView {
                                        text = "非常满意"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "惊为天人"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    topMargin
                                }
                                verticalLayout {
                                    textView {
                                        text = "200"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "800"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    leftMargin = dip(12)
                                }
                            }.lparams(wrapContent, matchParent){
                                alignParentRight()
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 需求详情
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "需求详情"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                verticalLayout {
                                    textView {
                                        text = "稿件类型"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "风格"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "规格"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "试稿要求"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "公开权限"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "颜色模式"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "稿件格式"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }
                                verticalLayout {
                                    textView {
                                        text = "儿童插画，擅长画儿童，童趣温馨细腻"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "儿童插画"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "1920*1080 px"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "不需要试稿"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "可公开"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "RGB"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                    }
                                    textView {
                                        text = "PSD"
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams{
                                        topMargin = dip(15)
                                        bottomMargin = dip(15)
                                    }
                                }.lparams{
                                    leftMargin = dip(12)
                                }
                            }.lparams(wrapContent, matchParent)
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 稿件构思
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "稿件构思"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            textView {
                                text = "需要单色线条稿18张以内，封面插图1张，用于外国儿童文学作品的封面和内插。" +
                                        "封面图价格单独计算。上面的报价是内文插图价格。"
                                textSize = 14f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                centerVertically()
                                topMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 补充内容
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "补充内容"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        relativeLayout {
                            textView {
                                text = "图书的目标读者为8-13岁儿童。人物形象较多，包括10岁左右的男孩女孩，" +
                                        "成年男人，老年男人、老年女人。擅长儿童插画和人物形象的插画师优先。"
                                textSize = 14f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                centerVertically()
                                topMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 参考图例
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "补充内容"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        verticalLayout {
                            textView {
                                text = "图书的目标读者为8-13岁儿童。人物形象较多，包括10岁左右的男孩女孩，" +
                                        "成年男人，老年男人、老年女人。擅长儿童插画和人物形象的插画师优先。"
                                textSize = 14f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                topMargin = dip(15)
                            }
                            horizontalScrollView {
                                linearLayout {
                                    for (index in 0..4){
                                        imageView {
                                            imageResource = R.mipmap.test_pic
                                        }.lparams(wrapContent, matchParent)
                                    }
                                }.lparams(matchParent, matchParent)
                            }.lparams(matchParent,dip(75)){
                                topMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))

                        /**
                         * 各阶段费用占比及交稿时间
                         */
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = "各阶段费用占比及交稿时间"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent,dip(55)){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        verticalLayout {
                            textView {
                                text = "图书的目标读者为8-13岁儿童。人物形象较多，包括10岁左右的男孩女孩，" +
                                        "成年男人，老年男人、老年女人。擅长儿童插画和人物形象的插画师优先。"
                                textSize = 14f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                topMargin = dip(15)
                            }
                            horizontalScrollView {
                                linearLayout {
                                    for (index in 0..4){
                                        imageView {
                                            imageResource = R.mipmap.test_pic
                                        }.lparams(wrapContent, matchParent)
                                    }
                                }.lparams(matchParent, matchParent)
                            }.lparams(matchParent,dip(75)){
                                topMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(15)
                            rightMargin = dip(15)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))
                    }.lparams(matchParent, matchParent)
                }
            }
        }.view
    }
}