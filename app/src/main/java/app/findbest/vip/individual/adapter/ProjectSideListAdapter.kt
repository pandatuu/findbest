package app.findbest.vip.individual.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.project.model.ProjectListModel
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*


class ProjectSideListAdapter(
    context: Context,
    listAdapter: ListAdapter,
    private val mDataSet: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListAdapter{
        fun oneClick(id: String)
    }

    private var mContext: Context = context
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var status: TextView
    private lateinit var country: ImageView
    private lateinit var style: LinearLayout
    private lateinit var countryPrice: TextView
    private lateinit var maxPrice: TextView
    private val listAdapter: ListAdapter = listAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(parent.context) {
            linearLayout {
                linearLayout {
                    backgroundResource = R.drawable.raduis_card
                    orientation = LinearLayout.VERTICAL
                    linearLayout {
                        title = textView {
                            text = "乙女向帅哥立绘制作（乙女向きイケ…"
                            textSize = 16f
                            textColor = Color.parseColor("#FF202020")
                            singleLine = true
                            ellipsize = TextUtils.TruncateAt.END
                        }.lparams(matchParent, wrapContent) {
                            rightMargin = dip(50)
                            gravity = Gravity.CENTER_VERTICAL
                        }
                    }.lparams(matchParent, dip(50)) {
                        leftMargin = dip(11)
                    }
                    relativeLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.ico_time_nor
                            }
                            date = textView {
                                text = "截稿日期：2019-11-22"
                                textSize = 12f
                                textColor = Color.parseColor("#FF666666")
                            }.lparams {
                                leftMargin = dip(8)
                            }
                        }.lparams{
                            alignParentLeft()
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.around_button_10
                            gravity = Gravity.CENTER
                            status = textView {
                                text = "发布中"
                                textSize = 11f
                                textColor = Color.parseColor("#FFF8791A")
                            }
                        }.lparams(dip(55),dip(20)) {
                            alignParentRight()
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(15)
                        setMargins(dip(11), dip(15), dip(20), 0)
                    }
                    linearLayout {
                        backgroundResource = R.drawable.ffe4e4e4_top_line
                        linearLayout {
                            country = imageView {
                                imageResource = R.mipmap.image_china
                            }
                            style = linearLayout {
                                orientation = LinearLayout.HORIZONTAL

                            }.lparams{
                                leftMargin = dip(10)
                            }
                        }.lparams {
                            weight = 1f
                            topMargin = dip(15)
                            bottomMargin = dip(18)
                        }
                        linearLayout {
                            countryPrice = textView {
                                text = "CNY"
                                textSize = 15f
                                textColor = Color.parseColor("#FFFF7C00")
                            }
                            textView {
                                text = "¥"
                                textSize = 11f
                                textColor = Color.parseColor("#FFFF7C00")
                            }.lparams {
                                leftMargin = dip(5)
                            }
                            maxPrice = textView {
                                text = "800.00"
                                textSize = 18f
                                textColor = Color.parseColor("#FFFF7C00")
                                typeface = Typeface.DEFAULT_BOLD
                            }.lparams {
                                leftMargin = dip(3)
                            }
                        }.lparams(wrapContent, dip(20)) {
                            topMargin = dip(15)
                            bottomMargin = dip(18)
                        }
                    }.lparams(matchParent, matchParent) {
                        setMargins(dip(11), dip(15), dip(20), 0)
                    }
                }.lparams(matchParent, wrapContent){
                    topMargin = dip(10)
                }
            }
        }
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        val model = mDataSet[position].asJsonObject
        title.text = model["name"].asString ?: ""

        if(model["endAt"].isJsonNull){
            date.text = "截稿日期：${longToString(model["endAt"].asLong)}"
        }

        if(model["status"].isJsonNull){
            status.text = when(model["status"].asInt){
                // 待发布     CREATED = 0,
                0 -> "待发布"
                // 待审核     AUDITED = 10,
                10 -> "待审核"
                // 审核失败     AUDITFAIL = 11,
                11 -> "审核失败"
                // 发布中     ANNOUNCING = 231,
                231 -> "发布中"
                // 招募中     RECRUITING = 232,
                232 -> "招募中"
                // 委托待接受     COMMISSIONING = 233,
                233 -> "委托待接受"
                // 草稿制作阶段     CMAKING = 43,
                43 -> "草稿制作阶段"
                // 草稿验收中     CACCEPTANCE = 46,
                46 -> "草稿验收中"
                // 线稿制作中     XMAKING = 63,
                63 -> "线稿制作中"
                // 线稿验收中     XACCEPTANCE = 66,
                66 -> "线稿验收中"
                // 上色制作中     SMAKING = 83,
                83 -> "上色制作中"
                // 上色验收中     SACCEPTANCE = 86,
                86 -> "上色验收中"
                // 项目终止     PROJECTTERMINATION = 5,
                5 -> "项目终止"
                // 结算阶段     SETTLEMENT = 6,
                6 -> "结算阶段"
                // 验收通过     ACCEPTANCE = 100,
                100 -> "验收通过"
                // 账单待确认     BILLCONFIRMED = 106,
                106 -> "账单待确认"
                // 账单待付款     BILLPAY = 123,
                123 -> "账单待付款"
                // 已完成     COMPLETED = 200,
                200 -> "已完成"
                else -> ""
            }
        }

        if(model["country"].isJsonNull) {
            when (model["country"].asString) {
                "86" -> {
                    country.imageResource = R.mipmap.image_china
                    countryPrice.text = "CNY"
                }
                "81" -> {
                    country.imageResource = R.mipmap.image_japan
                    countryPrice.text = "JPY"
                }
                "82" -> {
                    country.imageResource = R.mipmap.image_korea
                    countryPrice.text = "KRW"
                }
            }
        }

        //风格标签最多三个
        for(index in 0 until model["style"].asJsonArray.size()){
            if(index < 3){
                val styleString = model["style"].asJsonArray[index].asString
                val view = with(mContext){
                    relativeLayout {
                        relativeLayout {
                            backgroundColor = Color.parseColor("#FFF7F7F7")
                            textView {
                                text = styleString
                                textSize = 11f
                                textColor = Color.parseColor("#FF555555")
                            }.lparams {
                                centerInParent()
                            }
                        }.lparams(dip(55), dip(20)){
                            leftMargin = dip(10)
                        }
                    }
                }
                style.addView(view)
            }
        }

        maxPrice.text = model["margin"].asString
//        rela.setOnClickListener {
//            listAdapter.oneClick(model.id)
//        }
        //防止RecycleView数据刷新错乱
        h.setIsRecyclable(false)

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun resetItems(items: MutableList<JsonObject>) {
        mDataSet.clear()
        mDataSet.addAll(items)
        notifyDataSetChanged()
    }
    fun addItems(items: MutableList<JsonObject>) {
        mDataSet.addAll(items)
        notifyDataSetChanged()
    }

    // 类型转换
    @SuppressLint("SimpleDateFormat")
    private fun longToString(long: Long): String {
        return SimpleDateFormat("yyyy/MM/dd").format(Date(long))
    }
}