package app.findbest.vip.painter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import withTrigger


class PainterMainListAdapter(
    context: Context,
    imageClick: ImageClick,
    private var mData: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ImageClick {
        fun click(str: String)
        fun jumpToInfo(id: String)
    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var stars: LinearLayout
    private lateinit var country: ImageView
    private lateinit var imageList: HorizontalScrollView
    private lateinit var mainLinea: LinearLayout
    private var imageClick: ImageClick = imageClick

    @SuppressLint("WrongConstant")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mContext.UI {
            mainLinea = verticalLayout {
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
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
                                }.lparams(wrapContent, wrapContent) {
                                    alignParentLeft()
                                    topMargin = dip(8)
                                }
                                country = imageView {

                                }.lparams(dip(30), dip(20)) {
                                    rightMargin = dip(15)
                                    alignParentRight()
                                    alignParentBottom()
                                }
                            }.lparams(matchParent, dip(30))
                            stars = linearLayout {
                            }.lparams(wrapContent, dip(8)) {
                                topMargin = dip(5)
                            }
                        }.lparams(matchParent, wrapContent) {
                            setMargins(dip(10), 0, 0, dip(10))
                        }
                    }.lparams(matchParent, dip(55)) {
                        topMargin = dip(15)
                    }

                    imageList = horizontalScrollView {
                        isHorizontalScrollBarEnabled = false
                    }.lparams(matchParent, dip(85)) {
                        bottomMargin = dip(15)
                    }
                    linearLayout {
                        backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    }.lparams(matchParent, dip(1)) {
                        rightMargin = dip(15)
                    }
                }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            mainLinea.layoutParams = lp
        }.view
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        val model = mData[position].asJsonObject
        val images =
            if (!model["works"].isJsonNull) model.get("works").asJsonArray else arrayListOf<JsonObject>()

        mainLinea.withTrigger().click {
            imageClick.jumpToInfo(model["id"].asString)
        }

        if (!model["logo"].isJsonNull) {
            Glide.with(mContext)
                .load(model["logo"].asString)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headPic)
        }

        if (!model["name"].isJsonNull) {
            name.text = model["name"].asString
        } else {
            name.text = "******(匿名用户)"
        }

        val star = model["star"].asInt
        for (index in 0..4) {
            val view = with(mContext) {
                linearLayout {
                    imageView {
                        if (index < star) {
                            imageResource = R.mipmap.ico_star_select
                        } else {
                            imageResource = R.mipmap.ico_star_no
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

        if (!model["country"].isJsonNull) {
            when (model["country"].asString) {
                "86" -> country.imageResource = R.mipmap.image_china
                "81" -> country.imageResource = R.mipmap.image_japan
                "82" -> country.imageResource = R.mipmap.image_korea
            }
        }

        val imgList = arrayListOf<String>()
        images.forEach {
            imgList.add(it.asJsonObject["url"].asString)
        }
        val view = with(mContext) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                images.forEach {
                    val imageObject = it.asJsonObject
                    val image = imageView {
                        withTrigger().click {
                            imageClick.click(imageObject["url"].asString)
                        }
                    }.lparams(wrapContent, matchParent) {
                        leftMargin = dip(10)
                    }
                    Glide.with(mContext)
                        .load(imageObject["url"].asString)
                        .into(image)
                }
            }
        }
        imageList.addView(view)

        //防止RecycleView数据刷新错乱
        h.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun resetItems(items: MutableList<JsonObject>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items: MutableList<JsonObject>) {
        mData.addAll(items)
        notifyDataSetChanged()
    }
}