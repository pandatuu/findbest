package app.findbest.vip.project.adapter

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
import click
import org.jetbrains.anko.*
import withTrigger
import java.text.SimpleDateFormat
import java.util.*


class ProjectMainListAdapter(
    context: Context,
    listAdapter: ListAdapter,
    private val mDataSet: MutableList<ProjectListModel> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListAdapter{
        fun oneClick(id: String)
    }

    private var mContext: Context = context
    private lateinit var title: TextView
    private lateinit var defend: ImageView
    private lateinit var pixel: TextView
    private lateinit var format: TextView
    private lateinit var date: TextView
    private lateinit var country: ImageView
    private lateinit var style: LinearLayout
    private lateinit var countryPrice: TextView
    private lateinit var maxPrice: TextView
    private lateinit var rela: RelativeLayout
    private val listAdapter: ListAdapter = listAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(parent.context) {
            frameLayout {
                rela = relativeLayout {
                    linearLayout {
                        backgroundResource = R.drawable.raduis_card
                        orientation = LinearLayout.VERTICAL
                        linearLayout {
                            title = textView {
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
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_size_nor
                                }
                                pixel = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                    singleLine = true
                                    ellipsize = TextUtils.TruncateAt.END
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }.lparams(dip(0), wrapContent){
                                weight = 1f
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_format_nor
                                }
                                format = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                    singleLine = true
                                    ellipsize = TextUtils.TruncateAt.END
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }.lparams(dip(0), wrapContent){
                                weight = 1f
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_data_nor
                                }
                                date = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                    singleLine = true
                                    ellipsize = TextUtils.TruncateAt.END
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }.lparams(dip(0), wrapContent){
                                weight = 1f
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
                                    textSize = 18f
                                    textColor = Color.parseColor("#FFFF7C00")
                                    typeface = Typeface.DEFAULT_BOLD
                                }.lparams {
                                    leftMargin = dip(5)
                                }
                            }.lparams(wrapContent, dip(20)) {
                                topMargin = dip(15)
                                bottomMargin = dip(18)
                            }
                        }.lparams(matchParent, matchParent) {
                            setMargins(dip(11), dip(15), dip(20), 0)
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(5)
                    }
                    defend = imageView {
                        imageResource = R.mipmap.project_ico_ensure_nor
                    }.lparams(dip(50), dip(50)) {
                        alignParentRight()
                        alignParentTop()
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(15)
                }
            }
        }
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        title.text = mDataSet[position].title
        if(!mDataSet[position].isDefend) defend.visibility = RelativeLayout.GONE
        pixel.text = mDataSet[position].size
        format.text = mDataSet[position].format
        date.text = longToString(mDataSet[position].commitAt)
        when(mDataSet[position].country){
            "china" -> {
                country.imageResource = R.mipmap.image_china
                countryPrice.text = "CNY"
            }
            "japan" -> {
                country.imageResource = R.mipmap.image_japan
                countryPrice.text = "JPY"
            }
            "korea" -> {
                country.imageResource = R.mipmap.image_korea
                countryPrice.text = "KRW"
            }
        }
        title.text = mDataSet[position].title
        //风格标签最多三个
        for(index in 0 until mDataSet[position].styleList.size()){
            if(index < 3){
                val styleString = mDataSet[position].styleList[index].asString
                val view = with(mContext){
                    relativeLayout {
                        relativeLayout {
                            backgroundColor = Color.parseColor("#FFF7F7F7")
                            textView {
                                text = styleString
                                textSize = 11f
                                textColor = Color.parseColor("#FF555555")
                            }.lparams {
                                setMargins(dip(7),dip(2.5f),dip(7),dip(2.5f))
                            }
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                        }
                    }
                }
                style.addView(view)
            }
        }
        maxPrice.text = mDataSet[position].maxPrice.toString()
        rela.withTrigger().click {
            listAdapter.oneClick(mDataSet[position].id)
        }
        //防止RecycleView数据刷新错乱
        h.setIsRecyclable(false)

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun resetItems(items: MutableList<ProjectListModel>) {
        mDataSet.clear()
        mDataSet.addAll(items)
        notifyDataSetChanged()
    }
    fun addItems(items: MutableList<ProjectListModel>) {
        mDataSet.addAll(items)
        notifyDataSetChanged()
    }

    // 类型转换
    @SuppressLint("SimpleDateFormat")
    private fun longToString(long: Long): String {
        return SimpleDateFormat("yyyy/MM/dd").format(Date(long))
    }
}