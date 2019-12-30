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
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.anko.*


class ProjectSideApplicantsAdapter(
    context: Context,
    printedCrad: PrintedCrad,
    private var mData: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PrintedCrad {
        fun oneClick(str: String)
        fun chat(commitId: String)
        fun send(commitId: String)
        fun refuse(commitId: String)
    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var stars: LinearLayout
    private lateinit var commit: TextView
    private lateinit var imageList: HorizontalScrollView
    private lateinit var isRefuse: LinearLayout
    private lateinit var chat: LinearLayout
    private lateinit var leftLine: LinearLayout
    private lateinit var send: LinearLayout
    private lateinit var rightLine: LinearLayout
    private lateinit var refuse: LinearLayout
    private var printedCrad: PrintedCrad = printedCrad

    var isCommit = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(mContext) {
            verticalLayout {
                backgroundResource = R.drawable.around_input_5
                linearLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
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
                        relativeLayout {
                            stars = linearLayout {
                            }.lparams(wrapContent, dip(8)) {
                                centerVertically()
                            }
                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL
                                imageView {
                                    imageResource = R.mipmap.ico_leave_message_nor
                                }
                                textView {
                                    text = "画师留言"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FF666666")
                                }.lparams {
                                    leftMargin = dip(5)
                                }
                                setOnClickListener {
                                    if (isCommit) {
                                        commit.visibility = LinearLayout.GONE
                                    } else {
                                        commit.visibility = LinearLayout.VISIBLE
                                    }
                                    isCommit = !isCommit
                                }
                            }.lparams(wrapContent, matchParent) {
                                alignParentRight()
                            }
                        }.lparams(matchParent, dip(18)) {
                            topMargin = dip(5)
                        }
                    }.lparams(matchParent, wrapContent) {
                        setMargins(dip(10), 0, 0, dip(10))
                    }
                }.lparams(matchParent, dip(75)) {
                    setMargins(dip(10), dip(5), dip(10), 0)
                }

                commit = textView {
                    textSize = 14f
                    textColor = Color.parseColor("#FF333333")
                    visibility = LinearLayout.GONE
                }.lparams {
                    topMargin = dip(15)
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                }
                imageList = horizontalScrollView {
                    isHorizontalScrollBarEnabled = false
                }.lparams(dip(500), dip(85)) {
                    topMargin = dip(15)
                }
                isRefuse = linearLayout {
                    chat = verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.ico_chat_nor
                        }.lparams(dip(25), dip(25))
                        textView {
                            text = "聊一聊"
                            textSize = 14f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            topMargin = dip(5)
                        }
                    }.lparams(dip(0), matchParent) {
                        weight = 1f
                    }
                    leftLine = linearLayout {
                        backgroundColor = Color.parseColor("#FFE1E1E1")
                    }.lparams(dip(0.5f), dip(30)) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    send = verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.ico_send_nor
                        }.lparams(dip(25), dip(25))
                        textView {
                            text = "发送委托"
                            textSize = 14f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            topMargin = dip(5)
                        }
                    }.lparams(dip(0), matchParent) {
                        weight = 1f
                    }
                    rightLine = linearLayout {
                        backgroundColor = Color.parseColor("#FFE1E1E1")
                    }.lparams(dip(0.5f), dip(30)) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    refuse = verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.ico_refused_nor
                        }.lparams(dip(25), dip(25))
                        textView {
                            text = "拒绝"
                            textSize = 14f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            topMargin = dip(5)
                        }
                    }.lparams(dip(0), matchParent) {
                        weight = 1f
                    }
                }.lparams(matchParent, dip(50)) {
                    setMargins(dip(10), dip(12), dip(10), dip(12))
                }
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        //应征人员信息
        val model = mData[position].get("provider").asJsonObject
        //应征作品
        var images: JsonArray? = null
        if (!mData[position].get("applyWorks").isJsonNull)
            images = mData[position].get("applyWorks").asJsonArray

        if (!model["avatar"].isJsonNull) {
            Glide.with(mContext)
                .load(model["avatar"].asString)
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

        if (!mData[position]["comment"].isJsonNull) {
            commit.text = mData[position]["comment"].asString
        }

        val view = with(mContext) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                images?.forEach {
                    val imageObject = it.asJsonObject
                    val image = imageView {
                        setOnClickListener {
                            printedCrad.oneClick(imageObject["url"].asString)
                        }
                    }.lparams(wrapContent, matchParent) {
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


        //0待接收，100已同意，25,50拒绝
        if (!mData[position]["status"].isJsonNull) {
            if (mData[position]["status"].asInt != 0) {
                send.visibility = LinearLayout.GONE
                refuse.visibility = LinearLayout.GONE
                leftLine.visibility = LinearLayout.GONE
                rightLine.visibility = LinearLayout.GONE
            }
        }

        chat.setOnClickListener {
            printedCrad.chat(mData[position]["id"].asString)
        }
        send.setOnClickListener {
            printedCrad.send(mData[position]["id"].asString)
        }
        refuse.setOnClickListener {
            printedCrad.refuse(mData[position]["id"].asString)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}