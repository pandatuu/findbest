package app.findbest.vip.project.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import org.jetbrains.anko.*


class ProjectApplicantsAdapter(
    context: Context,
    printedCrad: PrintedCrad,
    private var mData: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PrintedCrad {
        fun oneClick(str: String, b: Boolean)
    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var stars: LinearLayout
    private lateinit var imageList: HorizontalScrollView
    private var printedCrad: PrintedCrad = printedCrad

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mContext.UI {
            val linea = verticalLayout {
                backgroundResource = R.drawable.ffe4e4e4_bottom_line
                linearLayout {
                    headPic = imageView {
                    }.lparams(dip(45), dip(45)) {
                        setMargins(dip(5), dip(5), 0, dip(10))
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        name = textView {
                            textSize = 17f
                            textColor = Color.parseColor("#FF444444")
                            singleLine = true
                            ellipsize = TextUtils.TruncateAt.END
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(8)
                        }
                        stars = linearLayout {
                        }.lparams(wrapContent, dip(8)) {
                            topMargin = dip(5)
                        }
                    }.lparams(matchParent, wrapContent) {
                        setMargins(dip(10), 0, 0, dip(10))
                    }
                }.lparams(matchParent, dip(55))

                imageList = horizontalScrollView {
                    overScrollMode = View.OVER_SCROLL_NEVER
                    isScrollbarFadingEnabled = false
                }.lparams(matchParent, dip(85)) {
                    bottomMargin = dip(15)
                }
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linea.layoutParams = lp
        }.view
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        val model = mData[position].get("provider").asJsonObject
        val images = mData[position].get("workses").asJsonArray

        if (!model["avatar"].isJsonNull) {
            Glide.with(mContext)
                .load(model["avatar"].asString)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .placeholder(R.mipmap.default_avatar)
                .into(headPic)
        }else{
            Glide.with(mContext)
                .load(R.mipmap.default_avatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headPic)
        }

        if (!model["name"].isJsonNull) {
            name.text = model["name"].asString
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

        val view = with(mContext) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                images.forEach {
                    val imageObject = it.asJsonObject
                    val image = imageView {
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(10)
                    }
                    if (!imageObject["url"].isJsonNull || imageObject["url"].asString != "") {
                        Glide.with(mContext)
                            .load(imageObject["url"].asString)
                            .into(image)
                        image.setOnClickListener {
                            if(!imageObject["permise"].isJsonNull) {
                                if (imageObject["permise"].asBoolean) {
                                    printedCrad.oneClick(imageObject["url"].asString, true)
                                } else {
                                    printedCrad.oneClick(imageObject["url"].asString, false)
                                }
                            }
                        }
                    }else{
                        Glide.with(mContext)
                            .load(R.mipmap.not_look)
                            .into(image)
                        image.setOnClickListener {
                            toast(resources.getString(R.string.project_permission_work))
                        }
                    }
                }
            }
        }
        imageList.addView(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}