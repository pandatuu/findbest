package app.findbest.vip.instance.view

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.instance.model.InstanceModel
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.appCompatTextView
import app.findbest.vip.utils.roundImageView
import click
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import withTrigger


class InstanceSreachDetail : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = intent.getSerializableExtra("nowDetail") as InstanceModel
        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@InstanceSreachDetail)
        val role =  mPerferences.getString("role", "").toString()

        verticalLayout {
            relativeLayout {
                backgroundResource = R.drawable.ffe3e3e3_bottom_line
                toolbar {
                    isEnabled = true
                    title = ""
                    navigationIconResource = R.mipmap.nav_ico_return
                    setOnClickListener {
                        finish()
                        overridePendingTransition(R.anim.left_in, R.anim.right_out)
                    }
                }.lparams(dip(45), dip(30)) {
                    alignParentBottom()
                    alignParentLeft()
                    bottomMargin = dip(10)
                }
            }.lparams(matchParent, dip(65))
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                roundImageView {
                    Glide.with(this@InstanceSreachDetail)
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
                gravity = Gravity.CENTER
                val image = imageView {}.lparams {
                    width = matchParent
                    height = matchParent
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
                    val intent = Intent(this@InstanceSreachDetail, InvitationActivity::class.java)
                    //跳转详情
                    //画师/团队的id
                    intent.putExtra("id", model.id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                }
            }.lparams(matchParent,dip(50))
        }
    }
}