package app.findbest.vip.individual.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
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


class PainterInviteListAdapter(
    context: Context,
    private var mData: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    interface PrintedCrad {
//        fun oneClick(str: String)
//    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var stars: LinearLayout
    private lateinit var statusLayout: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(mContext) {
            verticalLayout {
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
                        }.lparams {
                            topMargin = dip(8)
                        }
                        stars = linearLayout {
                        }.lparams(wrapContent, dip(8)) {
                            topMargin = dip(5)
                        }
                    }.lparams(matchParent, wrapContent) {
                        setMargins(dip(10), 0, 0, dip(10))
                    }
                    linearLayout {
                        backgroundColor = Color.parseColor("#979797FF")
                    }.lparams(matchParent,dip(0.5f))
                }.lparams(matchParent, dip(77.5f)){
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                statusLayout = linearLayout {  }.lparams(matchParent, wrapContent)
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        val model = mData[position]

        if (!model["consumerLogo"].isJsonNull) {
            Glide.with(mContext)
                .load(model["consumerLogo"].asString)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headPic)
        }

        if (!model["consumerName"].isJsonNull) {
            name.text = model["consumerName"].asString
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

        if(model["status"].isJsonNull){
            when(model["status"].asInt){
                // 0待接受
                0 ->{
                    val view = with(mContext){
                        linearLayout {
                            verticalLayout {
                                textView {
                                    text = "邀请您加入本项目，您是否同意？"
                                    textSize = 15f
                                    textColor = Color.parseColor("#666666FF")
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    button {
                                        text = ""
                                        textSize = 15f
                                        textColor = Color.parseColor("#FFFFFFFF")
                                        backgroundResource = R.drawable.enable_around_button
                                    }.lparams(dip(0),dip(47)){
                                        weight = 1f
                                    }
                                    button {
                                        text = ""
                                        textSize = 15f
                                        textColor = Color.parseColor("#FFFFFFFF")
                                        backgroundResource = R.drawable.black_around_button
                                    }.lparams(dip(0),dip(47)){
                                        weight = 1f
                                        leftMargin = dip(25)
                                    }
                                }.lparams(matchParent, wrapContent){
                                    topMargin = dip(20)
                                }
                            }.lparams(matchParent, wrapContent){
                                setMargins(dip(15),dip(15),dip(15),dip(19))
                            }
                        }
                    }
                    statusLayout.addView(view)
                }
                // 100已接受
                100 -> {
                    val view = with(mContext){
                        linearLayout {
                            linearLayout {
                                linearLayout {
                                    backgroundResource = R.drawable.grey_around_button
                                    gravity = Gravity.CENTER
                                    textView {
                                        text = "已加入该项目"
                                        textSize = 15f
                                        textColor = Color.parseColor("#666660FF")
                                    }
                                }.lparams(matchParent,dip(47))
                            }.lparams(matchParent, wrapContent){
                                setMargins(dip(10),dip(15),dip(10),dip(19))
                            }
                        }
                    }
                    statusLayout.addView(view)
                }
                // 25个人拒绝
                25 ->{
                    val view = with(mContext){
                        linearLayout {
                            linearLayout {
                                linearLayout {
                                    backgroundResource = R.drawable.grey_around_button
                                    gravity = Gravity.CENTER
                                    textView {
                                        text = "已拒绝加入该项目"
                                        textSize = 15f
                                        textColor = Color.parseColor("#666660FF")
                                    }
                                }.lparams(matchParent,dip(47))
                            }.lparams(matchParent, wrapContent){
                                setMargins(dip(10),dip(15),dip(10),dip(19))
                            }
                        }
                    }
                    statusLayout.addView(view)
                }
                // 50发包方拒绝
                50 -> {
                    val view = with(mContext){
                        linearLayout {
                            linearLayout {
                                linearLayout {
                                    backgroundResource = R.drawable.grey_around_button
                                    gravity = Gravity.CENTER
                                    textView {
                                        text = "被拒绝加入该项目"
                                        textSize = 15f
                                        textColor = Color.parseColor("#666660FF")
                                    }
                                }.lparams(matchParent,dip(47))
                            }.lparams(matchParent, wrapContent){
                                setMargins(dip(10),dip(15),dip(10),dip(19))
                            }
                        }
                    }
                    statusLayout.addView(view)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}