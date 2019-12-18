package app.findbest.vip.project.adapter

import android.content.Context
import android.graphics.Color
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
import app.findbest.vip.project.fragment.ProjectMainList
import app.findbest.vip.project.model.ProjectListModel
import org.jetbrains.anko.*


class ProjectMainListAdapter(
    context: Context,
    listAdapter: ListAdapter,
    private val mDataSet: MutableList<ProjectListModel> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListAdapter{
        fun oneClick()
    }

    private var mContext: Context = context
    private lateinit var title: TextView
    private lateinit var defend: ImageView
    private lateinit var pixel: TextView
    private lateinit var format: TextView
    private lateinit var date: TextView
    private lateinit var country: ImageView
    private lateinit var style: LinearLayout
    private lateinit var price: TextView
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
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_size_nor
                                }
                                pixel = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_format_nor
                                }
                                format = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }.lparams {
                                leftMargin = dip(48)
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.project_ico_data_nor
                                }
                                date = textView {
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                }.lparams {
                                    leftMargin = dip(8)
                                }
                            }.lparams {
                                leftMargin = dip(48)
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
                                textView {
                                    text = "¥"
                                    textSize = 11f
                                    textColor = Color.parseColor("#FFFF7C00")
                                }
                                price = textView {
                                    text = "800.00"
                                    textSize = 18f
                                    textColor = Color.parseColor("#FFFF7C00")
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

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        title.text = mDataSet[position].title
        if(!mDataSet[position].isDefend) defend.visibility = RelativeLayout.GONE
        pixel.text = mDataSet[position].pixel
        format.text = mDataSet[position].format
        date.text = mDataSet[position].date
        when(mDataSet[position].country){
            "china" -> country.imageResource = R.mipmap.image_china
            "japan" -> country.imageResource = R.mipmap.image_japan
            "korea" -> country.imageResource = R.mipmap.image_korea
        }
        title.text = mDataSet[position].title
        mDataSet[position].styleList.forEach {
            val view = with(mContext){
                relativeLayout {
                    relativeLayout {
                        backgroundColor = Color.parseColor("#FFF7F7F7")
                        textView {
                            text = it
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
        price.text = mDataSet[position].price.toString()
        rela.setOnClickListener {
            listAdapter.oneClick()
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setItems(items: MutableList<ProjectListModel>) {
        mDataSet.clear()
        mDataSet.addAll(items)

        notifyDataSetChanged()
    }
}