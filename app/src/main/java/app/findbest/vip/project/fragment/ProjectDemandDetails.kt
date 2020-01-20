package app.findbest.vip.project.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.project.model.ProjectInfoModel
import app.findbest.vip.utils.appCompatTextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import java.text.SimpleDateFormat
import java.util.*

class ProjectDemandDetails: Fragment() {

    interface ClickImage{
        fun clickImage(str: String, b: Boolean)
    }

    companion object{
        fun newInstance(clickImage: ClickImage): ProjectDemandDetails{
            val fragment = ProjectDemandDetails()
            fragment.clickImage = clickImage
            return fragment
        }
    }

    private lateinit var clickImage: ClickImage
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
    private lateinit var samples: LinearLayout// 参考图例
    private lateinit var stages: LinearLayout// 项目阶段

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    @SuppressLint("SetTextI18n")
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
        bonuses.forEach {
            when(it.asJsonObject["level"].asInt){
                3 -> {
                    //满意
                    bountyFirst.text = it.asJsonObject["bounty"].asString
                }
                6 -> {
                    //非常满意
                    bountySecond.text = it.asJsonObject["bounty"].asString
                }
                9 -> {
                    //超越期待
                    bountyThrid.text = it.asJsonObject["bounty"].asString
                }
                12 -> {
                    //惊为天人
                    bountyForth.text = it.asJsonObject["bounty"].asString
                }
            }
        }

        //稿件类型
        category.text = model.category

        var styleText = ""
        for (index in 0 until model.styles.size()){
            styleText = if (index == 0){
                model.styles[index].asString.trim()
            }else{
                "$styleText,${model.styles[index].asString.trim()}"
            }
        }
        //风格
        style.text = styleText

        //规格
        size.text = model.size

        //试稿要求
        testing.text = when(model.testing){
            3 -> resources.getString(R.string.project_info_not_test)
            6 -> resources.getString(R.string.project_info_paid_test)
            9 -> resources.getString(R.string.project_info_not_paid_test)
            else -> "获取错误"
        }

        //公开权限
        publicity.text = when(model.pubPerm){
            3 -> resources.getString(R.string.project_info_open_not_authority)
            6 -> resources.getString(R.string.project_info_open_as_agreed)
            9 -> resources.getString(R.string.project_info_open_authority_all)
            else -> "获取错误"
        }

        //颜色模式
        color.text = when(model.color){
            3 -> "RGB"
            6 -> "CMYK"
            12 -> resources.getString(R.string.project_info_no_color_mode)
            else -> "获取错误"
        }

        //稿件格式
        format.text = when(model.format){
            3 -> "PSD"
            6 -> "JPEG"
            9 -> "PNG"
            12 -> resources.getString(R.string.project_info_other_format)
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
        }else{
            images.forEach { str ->
                val view = UI {
                    linearLayout {
                        val image = imageView {
                            setOnClickListener {
                                clickImage.clickImage(str,true)
                            }
                        }.lparams(wrapContent, matchParent){
                            leftMargin = dip(10)
                        }
                        Glide.with(this@ProjectDemandDetails)
                            .load(str)
                            .placeholder(R.mipmap.no_pic_show)
                            .into(image)
                    }
                }.view
                samples.addView(view)
            }
        }

        val stepList = arrayListOf(resources.getString(R.string.project_info_draft_acceptance),resources.getString(R.string.project_info_acceptance_line),resources.getString(R.string.project_info_acceptance_coloring))
        val stepPercent = arrayListOf(resources.getString(R.string.project_info_draft),resources.getString(R.string.project_info_line),resources.getString(R.string.project_info_coloring))
        for(index in 0 until model.stages.size()){
            val item = model.stages[index].asJsonObject

            val view = UI {
                val linea = linearLayout {
                    orientation = LinearLayout.VERTICAL
                    //实心圆
                    relativeLayout {
                        textView {
                            text = "${stepPercent[index]}${item["step"]}%"
                            textSize = 13f
                            textColor = Color.parseColor("#FF666666")
                        }.lparams {
                            if(index%2==0){
                                alignParentLeft()
                            }else{
                                alignParentRight()
                            }
                        }
                        imageView {
                            imageResource = R.mipmap.project_stage_scale
                        }.lparams(dip(8), dip(8)) {
                            centerInParent()
                        }
                    }.lparams(dip(130), dip(20)){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    //虚线
                    linearLayout {
                        backgroundResource = R.mipmap.dotted_line
                    }.lparams(dip(1), wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    //同心圆
                    relativeLayout {
                        textView {
                            text = stepList[index]
                            textSize = 15f
                            textColor = Color.parseColor("#FF444444")
                        }.lparams {
                            if(index%2==1){
                                alignParentLeft()
                            }else{
                                alignParentRight()
                            }
                        }
                        imageView {
                            imageResource = R.mipmap.project_stage
                        }.lparams(dip(15), dip(15)) {
                            centerInParent()
                        }
                    }.lparams(matchParent, dip(20))
                    //是否是最后阶段，是-隐藏，否-显示
                    //isLast
                    if(!item["last"].asBoolean){
                        linearLayout {
                            backgroundResource = R.mipmap.dotted_line
                        }.lparams(dip(1), wrapContent) {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }
                }
                val lp = LinearLayout.LayoutParams(dip(176),LinearLayout.LayoutParams.WRAP_CONTENT)
                linea.layoutParams = lp
            }.view
            stages.addView(view)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createV(): View {
        return UI {
            val linea = linearLayout {
                scrollView {
                    isVerticalScrollBarEnabled = false
                    overScrollMode = View.OVER_SCROLL_NEVER
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
                                    typeface = Typeface.DEFAULT_BOLD
                                }.lparams {
                                    setMargins(dip(15),dip(20),dip(15),dip(20))
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, wrapContent)
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 制作费用
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_production_cost)
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
                                            text = resources.getString(R.string.project_info_material_deadline)
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = resources.getString(R.string.project_info_unit_price)
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = resources.getString(R.string.project_info_total_sum)
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
                                }.lparams(wrapContent, matchParent){
                                    alignParentLeft()
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    verticalLayout {
                                        textView {
                                            text = resources.getString(R.string.project_info_currency)
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = resources.getString(R.string.project_info_paintings_num)
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
                                }.lparams(wrapContent, matchParent){
                                    alignParentRight()
                                    rightMargin = dip(16)
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 评价奖金
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_evaluation_bonus)
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
                                            text = resources.getString(R.string.project_info_satisfied)
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = resources.getString(R.string.project_info_beyond_expectations)
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
                                }.lparams(wrapContent, matchParent){
                                    alignParentLeft()
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    verticalLayout {
                                        textView {
                                            text = resources.getString(R.string.project_info_very_satisfied)
                                            textSize = 14f
                                            textColor = Color.parseColor("#FF999999")
                                        }.lparams {
                                            topMargin = dip(15)
                                        }
                                        textView {
                                            text = resources.getString(R.string.project_info_more_satisfied)
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
                                        bountySecond = appCompatTextView {
                                            textColor = Color.parseColor("#FF444444")
                                            singleLine = true
                                            setAutoSizeTextTypeUniformWithConfiguration(
                                                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                                dip(14),
                                                1,
                                                0
                                            )
                                        }.lparams(matchParent, wrapContent) {
                                            topMargin = dip(15)
                                        }
                                        bountyForth = appCompatTextView {
                                            textColor = Color.parseColor("#FF444444")
                                            singleLine = true
                                            setAutoSizeTextTypeUniformWithConfiguration(
                                                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                                dip(14),
                                                1,
                                                0
                                            )
                                        }.lparams {
                                            topMargin = dip(15)
                                            bottomMargin = dip(15)
                                        }
                                    }.lparams {
                                        leftMargin = dip(12)
                                    }
                                }.lparams(wrapContent, matchParent){
                                    alignParentRight()
                                    rightMargin = dip(16)
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 需求详情
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_demand_details)
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
                                //稿件类型
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_manuscripts_type)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    category = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //风格
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_style)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    style = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //规格
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_size)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    size = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //试稿要求
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_requirement)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    testing = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //公开权限
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_open_authority)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    publicity = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //颜色模式
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_color_mode)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    color = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                                //稿件格式
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    textView {
                                        text = resources.getString(R.string.project_info_format)
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF999999")
                                    }.lparams(dip(60), dip(20))
                                    format = textView {
                                        textSize = 14f
                                        textColor = Color.parseColor("#FF444444")
                                    }.lparams(dip(0), wrapContent){
                                        weight = 1f
                                        leftMargin = dip(12)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(15)
                                }
                            }.lparams(matchParent, wrapContent) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent)
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 稿件构思
                         */
                        conceptionParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_conception)
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
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 补充内容
                         */
                        supplementParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_supplementary)
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
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent, dip(5))
                        /**
                         * 参考图例
                         */
                        samplesParent = linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_reference_legend)
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
                                    overScrollMode = View.OVER_SCROLL_NEVER
                                    isScrollbarFadingEnabled = false
                                    samples = linearLayout {
                                        orientation = LinearLayout.HORIZONTAL
                                    }.lparams(matchParent, matchParent)
                                }.lparams(matchParent,dip(75)){
                                    topMargin = dip(15)
                                }
                            }.lparams(matchParent, wrapContent){
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                                bottomMargin = dip(20)
                            }
                        }
                        linearLayout {
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }.lparams(matchParent,dip(5))
                        /**
                         * 各阶段费用占比及交稿时间
                         */
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                                textView {
                                    text = resources.getString(R.string.project_info_proportion)
                                    textSize = 17f
                                    textColor = Color.parseColor("#FF444444")
                                }.lparams {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams(matchParent, dip(55)) {
                                leftMargin = dip(15)
                                rightMargin = dip(15)
                            }
                            stages = verticalLayout {
                                gravity = Gravity.CENTER_HORIZONTAL
                                /**
                                 * 1 开始制作
                                 */
                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    linearLayout {
                                        textView {
                                            text = resources.getString(R.string.project_info_making)
                                            textSize = 15f
                                            textColor = Color.parseColor("#FF444444")
                                        }
                                        imageView {
                                            imageResource = R.mipmap.project_stage
                                        }.lparams(dip(15), dip(15)) {
                                            gravity = Gravity.CENTER
                                            leftMargin = dip(8f)
                                        }
                                    }.lparams(matchParent, dip(20))
                                    linearLayout {
                                        backgroundResource = R.mipmap.dotted_line
                                    }.lparams(dip(1), wrapContent) {
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }
                                }.lparams(dip(176), wrapContent)
                                /**
                                 * 2 草稿验收
                                 */
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
                }.lparams(matchParent, matchParent)
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            linea.layoutParams = lp
        }.view
    }

    // 类型转换
    @SuppressLint("SimpleDateFormat")
    private fun longToString(long: Long): String {
        return SimpleDateFormat("yyyy/MM/dd").format(Date(long))
    }
}