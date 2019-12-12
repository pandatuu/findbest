package com.example.findbest.register.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.findbest.R
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*

class RegisterIdentity : AppCompatActivity() {


    //项目方
    private lateinit var project: RelativeLayout
    //制作方
    private lateinit var producer: RelativeLayout
    //团队
    private lateinit var team: RelativeLayout
    //个人
    private lateinit var person: RelativeLayout
    private var isProject = false
    private var isProducer = true
    private var isTeam = false
    private var isPerson = true

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true) //状态栏字体是深色，默认为亮色
            .init()


        frameLayout {
            backgroundColor = Color.WHITE
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
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
                    }
                }.lparams(matchParent, dip(65)) {
                    rightMargin = dip(15)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "3/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = "选择角色与主体"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    verticalLayout {
                        relativeLayout {
                            project = relativeLayout {
                                backgroundResource = R.drawable.rigister_country_input
                                imageView {
                                    imageResource = R.mipmap.project
                                }.lparams(dip(39), dip(42)) {
                                    centerHorizontally()
                                    topMargin = dip(23)
                                }
                                textView {
                                    text = "项目方"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams {
                                    alignParentBottom()
                                    centerHorizontally()
                                    bottomMargin = dip(12)
                                }
                                imageView {
                                    imageResource = R.mipmap.login_ico_triangle
                                    if (!isProject)
                                        visibility = RelativeLayout.GONE
                                }.lparams(dip(27), dip(27)) {
                                    setMargins(0, 0, dip(1), dip(1))
                                    alignParentBottom()
                                    alignParentRight()
                                }
                                setOnClickListener {
                                    val pjimage = project.getChildAt(2) as ImageView
                                    val pdimage = producer.getChildAt(2) as ImageView
                                    if (!isProject) {
                                        pjimage.visibility = RelativeLayout.VISIBLE
                                        pdimage.visibility = RelativeLayout.GONE
                                        isProducer = false
                                        isProject = true
                                    }
                                }
                            }.lparams(dip(110), dip(110)) {
                                alignParentLeft()
                            }
                            producer = relativeLayout {
                                backgroundResource = R.drawable.rigister_country_input
                                imageView {
                                    imageResource = R.mipmap.producer
                                }.lparams(dip(39), dip(42)) {
                                    centerHorizontally()
                                    topMargin = dip(23)
                                }
                                textView {
                                    text = "制作方"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams {
                                    alignParentBottom()
                                    centerHorizontally()
                                    bottomMargin = dip(12)
                                }
                                imageView {
                                    imageResource = R.mipmap.login_ico_triangle
                                    if (!isProducer)
                                        visibility = RelativeLayout.GONE
                                }.lparams(dip(27), dip(27)) {
                                    setMargins(0, 0, dip(1), dip(1))
                                    alignParentBottom()
                                    alignParentRight()
                                }
                                setOnClickListener {
                                    val pjimage = project.getChildAt(2) as ImageView
                                    val pdimage = producer.getChildAt(2) as ImageView
                                    if (!isProducer) {
                                        pdimage.visibility = RelativeLayout.VISIBLE
                                        pjimage.visibility = RelativeLayout.GONE
                                        isProject = false
                                        isProducer = true
                                    }
                                }
                            }.lparams(dip(110), dip(110)) {
                                alignParentRight()
                            }
                        }
                        relativeLayout {
                            team = relativeLayout {
                                backgroundResource = R.drawable.rigister_country_input
                                imageView {
                                    imageResource = R.mipmap.team
                                }.lparams(dip(39), dip(42)) {
                                    centerHorizontally()
                                    topMargin = dip(23)
                                }
                                textView {
                                    text = "团队"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams {
                                    alignParentBottom()
                                    centerHorizontally()
                                    bottomMargin = dip(12)
                                }
                                imageView {
                                    imageResource = R.mipmap.login_ico_triangle
                                    if (!isTeam)
                                        visibility = RelativeLayout.GONE
                                }.lparams(dip(27), dip(27)) {
                                    setMargins(0, 0, dip(1), dip(1))
                                    alignParentBottom()
                                    alignParentRight()
                                }
                                setOnClickListener {
                                    val timage = team.getChildAt(2) as ImageView
                                    val psimage = person.getChildAt(2) as ImageView
                                    if (!isTeam) {
                                        timage.visibility = RelativeLayout.VISIBLE
                                        psimage.visibility = RelativeLayout.GONE
                                        isPerson = false
                                        isTeam = true
                                    }
                                }
                            }.lparams(dip(110), dip(110)) {
                                alignParentLeft()
                            }
                            person = relativeLayout {
                                backgroundResource = R.drawable.rigister_country_input
                                imageView {
                                    imageResource = R.mipmap.person
                                }.lparams(dip(39), dip(42)) {
                                    centerHorizontally()
                                    topMargin = dip(23)
                                }
                                textView {
                                    text = "个人"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams {
                                    alignParentBottom()
                                    centerHorizontally()
                                    bottomMargin = dip(12)
                                }
                                imageView {
                                    imageResource = R.mipmap.login_ico_triangle
                                    if (!isPerson)
                                        visibility = RelativeLayout.GONE
                                }.lparams(dip(27), dip(27)) {
                                    setMargins(0, 0, dip(1), dip(1))
                                    alignParentBottom()
                                    alignParentRight()
                                }
                                setOnClickListener {
                                    val timage = team.getChildAt(2) as ImageView
                                    val psimage = person.getChildAt(2) as ImageView
                                    if (!isPerson) {
                                        psimage.visibility = RelativeLayout.VISIBLE
                                        timage.visibility = RelativeLayout.GONE
                                        isTeam = false
                                        isPerson = true
                                    }
                                }
                            }.lparams(dip(110), dip(110)) {
                                alignParentRight()
                            }
                        }.lparams {
                            topMargin = dip(25)
                        }
                    }.lparams {
                        setMargins(dip(18), dip(29), dip(18), 0)
                    }
                    button {
                        backgroundResource = R.drawable.enable_around_button
                        text = "下一步"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
//                            val saveTool = PreferenceManager.getDefaultSharedPreferences(this@RegisterIdentity)
//                            val mEditor: SharedPreferences.Editor = saveTool.edit()
                            when (true) {
                                isProject -> {
                                    when (true) {
                                        isTeam -> {
                                            toast("isProject && isTeam")
                                            startActivity<RegisterEmail>(
                                                "identity" to "team"
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                        isPerson -> {
                                            toast("isProject && isPerson")
                                            startActivity<RegisterEmail>(
                                                "identity" to "person"
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                    }
                                }
                                isProducer -> {
                                    when (true) {
                                        isTeam -> {
                                            toast("isProducer && isTeam")
//                                            mEditor.putString("identity","team")
                                            startActivity<RegisterEmail>(
                                                "identity" to "team"
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                        isPerson -> {
                                            toast("isProducer && isPerson")
//                                            mEditor.putString("identity","person")
                                            startActivity<RegisterEmail>(
                                                "identity" to "person"
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                    }
                                }
                            }
                        }
                    }.lparams(matchParent, dip(47)) {
                        topMargin = dip(39)
                    }
                }.lparams(matchParent, wrapContent) {
                    setMargins(dip(40), dip(17), dip(40), 0)
                }
            }
        }
    }
}