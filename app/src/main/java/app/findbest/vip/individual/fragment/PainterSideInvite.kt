package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class PainterSideInvite : Fragment() {

    interface ChooseStatus{
        fun agree()
        fun refuse()
    }

    companion object {
        fun newInstance(context: Context, chooseStatus:ChooseStatus, json: JsonObject): PainterSideInvite {
            val fragment = PainterSideInvite()
            fragment.mContext = context
            fragment.chooseStatus = chooseStatus
            fragment.model = json
            return fragment
        }
    }

    private lateinit var chooseStatus:ChooseStatus
    private lateinit var model: JsonObject
    private lateinit var mContext: Context
    private lateinit var headPic: ImageView
    private lateinit var name: TextView
    private lateinit var stars: LinearLayout
    private lateinit var statusLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                verticalLayout {
                    backgroundResource = R.drawable.around_input_5
                    linearLayout {
                        backgroundResource = R.drawable.ff979797_bottom_line
                        headPic = imageView {
                        }.lparams(dip(50), dip(50)) {
                            setMargins(dip(5), dip(15), 0, dip(10))
                        }
                        verticalLayout {
                            name = textView {
                                textSize = 18f
                                textColor = Color.parseColor("#FF444444")
                                singleLine = true
                                ellipsize = TextUtils.TruncateAt.END
                            }.lparams(matchParent, wrapContent) {
                                topMargin = dip(17)
                            }
                            stars = linearLayout {
                            }.lparams(wrapContent, wrapContent) {
                                topMargin = dip(6)
                            }
                        }.lparams(matchParent, matchParent) {
                            leftMargin = dip(15)
                        }
                    }.lparams(matchParent, dip(77.5f)) {
                        leftMargin = dip(15)
                        rightMargin = dip(15)
                    }
                    statusLayout = linearLayout { }.lparams(matchParent, wrapContent)
                }.lparams{
                    topMargin = dip(15)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
            }
        }.view
        setInfo()
        return view
    }

    private fun setInfo() {
        if (!model["consumerLogo"].isJsonNull) {
            Glide.with(mContext)
                .load(model["consumerLogo"].asString)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headPic)
        }

        if (!model["consumerName"].isJsonNull) {
            name.text = model["consumerName"].asString
        }

        if(!model["star"].isJsonNull) {
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
                        }.lparams(dip(14.5f), dip(14.5f)) {
                            if (index != 0) {
                                leftMargin = dip(13)
                            }
                        }
                    }
                }
                stars.addView(view)
            }
        }

        if (!model["status"].isJsonNull) {
            when (model["status"].asInt) {
                // 0待接受
                0 -> {
                    val view = UI {
                        relativeLayout {
                            verticalLayout {
                                textView {
                                    text = resources.getString(R.string.my_project_invite_isagree)
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF666666")
                                }
                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    button {
                                        text = resources.getString(R.string.common_agree)
                                        textSize = 15f
                                        textColor = Color.parseColor("#FFFFFFFF")
                                        backgroundResource = R.drawable.enable_around_button
                                        withTrigger().click {
                                            chooseStatus.agree()
                                        }
                                    }.lparams(dip(0), dip(47)) {
                                        weight = 1f
                                    }
                                    button {
                                        text = resources.getString(R.string.common_refuse)
                                        textSize = 15f
                                        textColor = Color.parseColor("#FFFFFFFF")
                                        backgroundResource = R.drawable.black_around_button
                                        withTrigger().click {
                                            chooseStatus.refuse()
                                        }
                                    }.lparams(dip(0), dip(47)) {
                                        weight = 1f
                                        leftMargin = dip(25)
                                    }
                                }.lparams(matchParent, wrapContent) {
                                    topMargin = dip(20)
                                }
                            }.lparams(matchParent, wrapContent) {
                                setMargins(dip(15), dip(15), dip(15), dip(19))
                            }
                        }
                    }.view
                    statusLayout.addView(view)
                }
                // 100已接受
                100 -> {
                    val view = UI {
                        relativeLayout {
                            linearLayout {
                                backgroundResource = R.drawable.grey_around_button
                                gravity = Gravity.CENTER
                                textView {
                                    text = resources.getString(R.string.my_project_invite_join)
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF666660")
                                }
                            }.lparams(matchParent, dip(47)){
                                setMargins(dip(10), dip(15), dip(10), dip(19))
                            }
                        }
                    }.view
                    statusLayout.addView(view)
                }
                // 25个人拒绝
                25 -> {
                    val view = UI {
                        relativeLayout {
                            linearLayout {
                                backgroundResource = R.drawable.grey_around_button
                                gravity = Gravity.CENTER
                                textView {
                                    text = resources.getString(R.string.my_project_invite_refuse_join)
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF666660")
                                }
                            }.lparams(matchParent, dip(47)) {
                                setMargins(dip(10), dip(15), dip(10), dip(19))
                            }
                        }
                    }.view
                    statusLayout.addView(view)
                }
                // 50发包方拒绝
                50 -> {
                    val view = UI {
                        relativeLayout {
                            linearLayout {
                                backgroundResource = R.drawable.grey_around_button
                                gravity = Gravity.CENTER
                                textView {
                                    text = resources.getString(R.string.my_project_invite_quilt_refuse_join)
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF666660")
                                }
                            }.lparams(matchParent, dip(47)) {
                                setMargins(dip(10), dip(15), dip(10), dip(19))
                            }
                        }
                    }.view
                    statusLayout.addView(view)
                }
            }
        }

    }
    //拒绝项目，修改状态
    fun updateSatuts(boolean: Boolean) {
        statusLayout.removeAllViews()
        val statusText = if(boolean) resources.getString(R.string.my_project_invite_join) else resources.getString(R.string.my_project_invite_refuse_join)
        val view = UI {
            relativeLayout {
                linearLayout {
                    backgroundResource = R.drawable.grey_around_button
                    gravity = Gravity.CENTER
                    textView {
                        text = statusText
                        textSize = 15f
                        textColor = Color.parseColor("#FF666660")
                    }
                }.lparams(matchParent, dip(47)) {
                    setMargins(dip(10), dip(15), dip(10), dip(19))
                }
            }
        }.view
        statusLayout.addView(view)
    }
}