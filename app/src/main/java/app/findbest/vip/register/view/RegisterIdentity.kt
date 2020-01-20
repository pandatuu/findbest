package app.findbest.vip.register.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.register.model.RegisterModel
import app.findbest.vip.utils.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*
import java.io.Serializable

class RegisterIdentity : BaseActivity() {


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
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = resources.getString(R.string.register_identity)
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
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
                                    text = resources.getString(R.string.register_identity_project)
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
                                    text = resources.getString(R.string.register_identity_producer)
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
                                    text = resources.getString(R.string.register_identity_team)
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
                                    text = resources.getString(R.string.register_identity_person)
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
                        text = resources.getString(R.string.common_next)
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            val user = intent.getSerializableExtra("user") as RegisterModel
                            val refreshToken = if(intent.getStringExtra("refreshToken")!=null) intent.getStringExtra("refreshToken") else ""
                            when (true) {
                                isProject -> {
                                    when (true) {
                                        isTeam -> {
                                            startActivity<RegisterEmail>(
                                                "role" to "consumer",
                                                "identity" to 2,
                                                "user" to user as Serializable,
                                                "refreshToken" to refreshToken
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                        isPerson -> {
                                            startActivity<RegisterEmail>(
                                                "role" to "consumer",
                                                "identity" to 1,
                                                "user" to user as Serializable,
                                                "refreshToken" to refreshToken
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                    }
                                }
                                isProducer -> {
                                    when (true) {
                                        isTeam -> {
//                                            mEditor.putString("identity","team")
                                            startActivity<RegisterEmail>(
                                                "role" to "provider",
                                                "identity" to 2,
                                                "user" to user as Serializable,
                                                "refreshToken" to refreshToken
                                            )
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                        }
                                        isPerson -> {
//                                            mEditor.putString("identity","person")
                                            startActivity<RegisterEmail>(
                                                "role" to "provider",
                                                "identity" to 1,
                                                "user" to user as Serializable,
                                                "refreshToken" to refreshToken
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