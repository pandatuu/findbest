package app.findbest.vip.project.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.project.model.ProjectInfoModel
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProjectDemandDetails: Fragment() {

    companion object{
        fun newInstance(): ProjectDemandDetails{
            val fragment = ProjectDemandDetails()
            return fragment
        }
    }

    private lateinit var projectName: TextView //项目名称
    private lateinit var commitAtDate: TextView //截稿日期
    private lateinit var userBounty: TextView //个人单价
    private lateinit var margin: TextView //总金额
    private lateinit var payCurrency: TextView //结算币种
    private lateinit var entityCount: TextView //原画数量
    private lateinit var bountyFirst: TextView //满意
    private lateinit var bountySecond: TextView //非常满意
    private lateinit var bountyThrid: TextView //超越期待
    private lateinit var bountyForth: TextView //惊为天人
    private lateinit var category: TextView //稿件类型
    private lateinit var style: TextView //风格
    private lateinit var size: TextView //规格
    private lateinit var testing: TextView //试稿要求
    private lateinit var publicity: TextView //公开权限
    private lateinit var color: TextView //颜色模式
    private lateinit var format: TextView //稿件格式
    private lateinit var conceptionParent: LinearLayout
    private lateinit var conception: TextView //稿件构思
    private lateinit var supplementParent	: LinearLayout
    private lateinit var supplement	: TextView //补充内容
    private lateinit var samplesParent: LinearLayout
    private lateinit var samples: ArrayList<String> //参考图例
    private lateinit var step: TextView //阶段名称
    private lateinit var stepPercent: TextView //阶段百分比
    private lateinit var isLast	: LinearLayout //是否是最后阶段，是-隐藏，否-显示

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    fun setInfomation(model: ProjectInfoModel){
        //项目名称
        projectName.text = model.name

        //截稿日期
        commitAtDate.text = longToString(model.commitAt)

        //个人单价
        userBounty.text = model.userBounty

        //总金额
        margin.text = model.margin

        //结算币种
        payCurrency.text = model.payCurrency["name"].asString

        //原画数量
        entityCount.text = model.entityCount.toString()

        val bonuses = model.bonuses.asJsonArray
        //满意
        bountyFirst.text = bonuses[0].asJsonObject["bounty"].asString

        //非常满意
        bountySecond.text = bonuses[1].asJsonObject["bounty"].asString

        //超越期待
        bountyThrid.text = bonuses[2].asJsonObject["bounty"].asString

        //惊为天人
        bountyForth.text = bonuses[3].asJsonObject["bounty"].asString

        //稿件类型
        category.text = model.category

        var styleText = ""
//        model.styles.forEach {
//            styleText = "$styleText ${it.toString().trim()}"
//        }
        for (index in 0 until model.styles.size()){
            styleText = if (index == 0){
                model.styles[index].asString.trim()
            }else{
                "$styleText ${model.styles[index].asString.trim()}"
            }
        }
        //风格
        style.text = styleText

        //规格
        size.text = model.size

        //试稿要求
        testing.text = when(model.testing){
            3 -> "无需试稿"
            6 -> "有偿试稿"
            9 -> "无偿试稿"
            else -> "获取错误"
        }

        //公开权限
        publicity.text = when(model.publicity){
            0 -> "不公开"
            1 -> "只允许个人"
            2 -> "只允许公司"
            3 -> "允许个人和公司"
            else -> "获取错误"
        }

        //颜色模式
        color.text = when(model.color){
            3 -> "RGB"
            6 -> "CMYK"
            12 -> "不限"
            else -> "获取错误"
        }

        //稿件格式
        format.text = when(model.format){
            3 -> "PSD"
            6 -> "JPEG"
            9 -> "PNG"
            12 -> "其他格式"
            else -> "获取错误"
        }

        //稿件构思
        if(model.conception.isNullOrBlank()){
            conceptionParent.visibility = LinearLayout.GONE
        }else{
            conception.text = model.conception
        }

        //补充内容
        if(model.supplement.isNullOrBlank()){
            supplementParent.visibility = LinearLayout.GONE
        }else{
            supplement.text = model.supplement
        }

        //参考图例
        val images = arrayListOf<String>()
        model.samples.forEach {
            val item = it.asJsonObject
            images.add(item["url"].asString)
        }
        if(images.size==0){
            samplesParent.visibility = LinearLayout.GONE
        }
//        //阶段名称
//        step =
//
//        //阶段百分比
//        stepPercent
//
//        //是否是最后阶段，是-隐藏，否-显示
//        isLast

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
                         * 项目名字
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                projectName = textView {
                                    textSize = 21f
                                    textColor = Color.parseColor("#FF202020")
                                }.lparams {
                                    leftMargin = dip(15)
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(70))
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 制作费用
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "制作费用"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
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
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "个人单价"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "总金额"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }
                                    verticalLayout {
                                        commitAtDate = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        userBounty = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        margin = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent) {
                                    alignParentLeft()
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    verticalLayout {
                                        textView {
                                            text = "结算币种"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "原画数量"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                    }
                                    verticalLayout {
                                        payCurrency = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        entityCount = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent) {
                                    alignParentRight()
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 评价奖金
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "制作费用"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
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
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "超越期待"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }
                                    verticalLayout {
                                        bountyFirst = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        bountyThrid = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent) {
                                    alignParentLeft()
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    verticalLayout {
                                        textView {
                                            text = "非常满意"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "惊为天人"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        topMargin
                                    }
                                    verticalLayout {
                                        bountySecond = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        bountyForth = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent) {
                                    alignParentRight()
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 需求详情
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "需求详情"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
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
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "风格"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "规格"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "试稿要求"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "公开权限"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "颜色模式"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = "稿件格式"
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }
                                    verticalLayout {
                                        category = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        style = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        size = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        testing = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        publicity = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        color = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        format = textView {
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent)
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 稿件构思
                         */
                        conceptionParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "稿件构思"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            relativeLayout {
                                conception = textView {
                                    textSize = 14f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    centerVertically()
                                    topMargin = dip(15)
                                    bottomMargin = dip(20)
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 补充内容
                         */
                        supplementParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "补充内容"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            relativeLayout {
                                supplement = textView {
                                    textSize = 14f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    centerVertically()
                                    topMargin = dip(15)
                                    bottomMargin = dip(20)
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                        /**
                         * 参考图例
                         */
                        samplesParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "参考图例"
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
                                horizontalScrollView {
                                    linearLayout {
                                    }.lparams(matchParent, matchParent)
                                }.lparams(matchParent,dip(75)){
                                    topMargin = dip(15)
                                }
                            }.lparams(matchParent, wrapContent){
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent,dip(5))
                        }
                        /**
                         * 各阶段费用占比及交稿时间
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = "各阶段费用占比及交稿时间"
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            verticalLayout {
                                /**
                                 * 1 开始制作
                                 */
                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    linearLayout {
                                        imageView {
                                            imageResource = R.mipmap.project_stage
                                        }.lparams(dip(15), dip(15)) {
                                            gravity = Gravity.CENTER_VERTICAL
                                        }
                                        textView {
                                            text = "1 开始制作"
                                            textSize = 15f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            leftMargin = dip(10)
                                        }
                                    }.lparams(wrapContent, dip(20))
                                    linearLayout {
                                        backgroundResource = R.mipmap.dotted_line
                                    }.lparams(dip(1), wrapContent) {
                                        leftMargin = dip(7)
                                    }
                                }
                                /**
                                 * 2 草稿验收
                                 */
                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    linearLayout {
                                        imageView {
                                            imageResource = R.mipmap.project_stage_scale
                                        }.lparams(dip(8), dip(8)) {
                                            gravity = Gravity.CENTER_VERTICAL
                                            leftMargin = dip(3.5f)
                                        }
                                        stepPercent = textView {
                                            text = "草稿15%"
                                            textSize = 13f
                                            textColor = Color.parseColor("#FF666666")
                                        }.lparams {
                                            leftMargin = dip(10)
                                        }
                                    }.lparams(wrapContent, dip(20))
                                    linearLayout {
                                        backgroundResource = R.mipmap.dotted_line
                                    }.lparams(dip(1), wrapContent) {
                                        leftMargin = dip(7)
                                    }
                                    linearLayout {
                                        imageView {
                                            imageResource = R.mipmap.project_stage
                                        }.lparams(dip(15), dip(15)) {
                                            gravity = Gravity.CENTER_VERTICAL
                                        }
                                        step = textView {
                                            text = "2 草稿验收"
                                            textSize = 15f
                                            textColor = Color.parseColor("#FF444444")
                                        }.lparams {
                                            leftMargin = dip(10)
                                        }
                                    }.lparams(wrapContent, dip(20))
                                    isLast = linearLayout {
                                        backgroundResource = R.mipmap.dotted_line
                                    }.lparams(dip(1), wrapContent) {
                                        leftMargin = dip(7)
                                    }
                                }
                                /**
                                 * 3 线稿验收
                                 */
                                /**
                                 * 4 上色验收
                                 */
                            }.lparams(matchParent, wrapContent) {
                                setMargins(dip(15), dip(15), dip(15), dip(20))
                            }
                            linearLayout {
                                backgroundColor = Color.parseColor("#FFF6F6F6")
                            }.lparams(matchParent, dip(5))
                        }
                    }.lparams(matchParent, matchParent)
                }
            }
        }.view
    }

    // 类型转换
    @SuppressLint("SimpleDateFormat")
    private fun longToString(long: Long): String {
        return SimpleDateFormat("yyyy/MM/dd").format(Date(long))
    }
}