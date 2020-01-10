package app.findbest.vip.individual.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import withTrigger


class ProjectSideInviteAdapter(
    context: Context,
    printedCrad: PrintedCrad,
    private var mData: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PrintedCrad {
        fun oneClick(str: String)
        fun cacel(id: String)
    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var status: TextView
    private lateinit var stars: LinearLayout
    private lateinit var imageList: HorizontalScrollView
    private lateinit var isInvite: RelativeLayout
    private lateinit var inviteButton: LinearLayout
    private var printedCrad: PrintedCrad = printedCrad

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mContext.UI {
            val linea = verticalLayout {
                backgroundResource = R.drawable.around_input_5
                linearLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    headPic = imageView {
                    }.lparams(dip(45), dip(45)) {
                        setMargins(dip(5), dip(5), 0, dip(10))
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        relativeLayout {
                            name = textView {
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                backgroundResource = R.drawable.around_button_10
                                gravity = Gravity.CENTER
                                status = textView {
                                    textSize = 11f
                                    textColor = Color.parseColor("#FFF8791A")
                                }.lparams{
                                    setMargins(dip(10),dip(2.5f),dip(10),dip(2.5f))
                                }
                            }.lparams(dip(53),dip(20)) {
                                alignParentRight()
                            }
                        }.lparams {
                            topMargin = dip(8)
                        }
                        stars = linearLayout {
                        }.lparams(wrapContent, dip(8)) {
                            topMargin = dip(8)
                        }
                    }.lparams(matchParent, wrapContent) {
                        setMargins(dip(10), 0, 0, dip(10))
                    }
                }.lparams(matchParent, dip(75)){
                    setMargins(dip(10), dip(5), dip(10),0)
                }
                imageList = horizontalScrollView {
                    isHorizontalScrollBarEnabled = false
                }.lparams(matchParent, dip(85)) {
                    topMargin = dip(15)
                    bottomMargin = dip(20)
                }
                isInvite = relativeLayout {
                    inviteButton = linearLayout {
                        backgroundResource = R.drawable.around_button_24
                        gravity = Gravity.CENTER
                        textView {
                            text = resources.getString(R.string.my_project_invite_cancel)
                            textSize = 14f
                            textColor = Color.WHITE
                        }
                    }.lparams(dip(100),dip(24)){
                        alignParentRight()
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent, wrapContent){
                    bottomMargin = dip(23)
                }
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linea.layoutParams = lp
        }.view
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        //应征人员信息
        val model = mData[position]
        //应征作品
        val images = mData[position].get("workes").asJsonArray

        if (!model["consumerLogo"].isJsonNull) {
            Glide.with(mContext)
                .load(model["consumerLogo"].asString)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headPic)
        }

        if (!model["painterName"].isJsonNull) {
            name.text = model["painterName"].asString
        }else{
            name.text = mContext.getString(R.string.painter_list_anonymous_name)
        }

        //0待接收，100已同意，25,50拒绝
        if(!model["status"].isJsonNull){
            when(model["status"].asInt){
                0 -> {
                    status.text = mContext.getString(R.string.my_project_invite_await_agree)
                }
                100 -> {
                    status.text = mContext.getString(R.string.my_project_invite_already_agree)
                    isInvite.visibility = RelativeLayout.GONE
                }
                25,50 -> {
                    status.text = mContext.getString(R.string.my_project_invite_already_cancel)
                    isInvite.visibility = RelativeLayout.GONE
                }
            }
        }

        val star = model["star"].asInt
        for (index in 0..4) {
            val view = with(mContext) {
                linearLayout {
                    imageView {
                        imageResource = if (index < star) {
                            R.mipmap.ico_star_select
                        } else {
                            R.mipmap.ico_star_no
                        }
                    }.lparams(dip(8), dip(8)) {
                        if (index != 0) {
                            leftMargin = dip(8)
                        }
                    }
                }
            }
            stars.addView(view)
        }

        val view = with(mContext) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                images.forEach {
                    val imageObject = it.asJsonObject
                    val image = imageView {
                        withTrigger().click {
                            printedCrad.oneClick(imageObject["url"].asString)
                        }
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(10)
                    }
                    if (!imageObject["url"].isJsonNull) {
                        Glide.with(mContext)
                            .load(imageObject["url"].asString)
                            .into(image)
                    }
                }
            }
        }
        imageList.addView(view)

        inviteButton.withTrigger().click {
            printedCrad.cacel(model["id"].asString)
        }

        //防止RecycleView数据刷新错乱
        h.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun resetView(data: MutableList<JsonObject>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<JsonObject>){
        mData.addAll(data)
        notifyDataSetChanged()
    }
}