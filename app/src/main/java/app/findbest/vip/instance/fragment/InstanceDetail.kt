package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.graphics.Typeface
import android.view.*
import org.jetbrains.anko.*
import android.text.TextUtils
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.widget.*
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import app.findbest.vip.instance.model.InstanceModel
import app.findbest.vip.instance.view.InvitationActivity
import app.findbest.vip.utils.*
import click
import com.bumptech.glide.Glide

import withTrigger


class InstanceDetail : FragmentParent() {

    companion object {
        fun newInstance(context: Context, model: InstanceModel): InstanceDetail {
            val f = InstanceDetail()
            f.activityInstance = context
            f.model = model
            return f
        }
    }

    lateinit var image: ImageView
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context
    lateinit var model: InstanceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = if (parentFragment != null) {
            parentFragment?.context
        } else {
            activity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView(): View {
        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        val role =  mPerferences.getString("role", "").toString()
        return UI {
            verticalLayout {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    roundImageView {
                        Glide.with(this@InstanceDetail)
                            .load(model.logo)
                            .skipMemoryCache(false)
                            .dontAnimate()
                            .centerCrop()
                            .placeholder(R.mipmap.default_avatar)
                            .into(this)
                    }.lparams {
                        width = dip(45)
                        height = dip(45)
                    }
                    appCompatTextView {
                        text = model.name
                        textSize = 17f
                        textColor = Color.parseColor("#FF444444")
                        singleLine = true
                        setAutoSizeTextTypeUniformWithConfiguration(
                            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                            dip(17),
                            1,
                            0
                        )
                    }.lparams(dip(0), dip(25)) {
                        weight = 1f
                        leftMargin = dip(8)
                    }
                }.lparams {
                    width = matchParent
                    height = dip(80)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                textView {
                    backgroundColor = Color.parseColor("#FFE3E3E3")
                }.lparams {
                    width = matchParent
                    height = dip(1)
                }
                linearLayout {
                    gravity =Gravity.CENTER
                    image = imageView {}.lparams {
                        width = matchParent
                        height =matchParent
                        margin = dip(5)
                        rightMargin = dip(10)
                        leftMargin = dip(10)
                    }
                    Glide.with(image.context)
                        .load(model.url)
                        .centerCrop()
                        .placeholder(R.mipmap.no_pic_show)
                        .into(image)
                }.lparams {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }

                linearLayout {
                    backgroundResource = R.drawable.enable_rectangle_button
                    gravity = Gravity.CENTER
                    visibility = if(role != "consumer"){  //consumer是发包方
                        LinearLayout.GONE
                    }else{
                        LinearLayout.VISIBLE
                    }
                    textView {
                        text = resources.getString(R.string.instance_invite_painter)
                        textSize =16f
                        textColor =Color.WHITE
                    }
                    setOnClickListener {
                        val intent = Intent(mContext, InvitationActivity::class.java)
                        //跳转详情
                        //画师/团队的id
                        intent.putExtra("id", model.id)
                        startActivity(intent)
                        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    }
                }.lparams(matchParent,dip(50))
            }
        }.view
    }
}