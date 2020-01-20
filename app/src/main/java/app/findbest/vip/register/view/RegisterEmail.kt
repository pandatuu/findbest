package app.findbest.vip.register.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import app.findbest.vip.R
import app.findbest.vip.register.model.RegisterModel
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.appCompatTextView
import com.gyf.immersionbar.ImmersionBar
import org.jetbrains.anko.*
import java.io.Serializable
import java.util.regex.Pattern

class RegisterEmail: BaseActivity() {

    private lateinit var email: EditText
    private lateinit var emailHint: TextView

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
                }.lparams(matchParent,dip(65)){
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "4/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = resources.getString(R.string.register_email)
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.email
                            }.lparams(dip(16), dip(20)){
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                backgroundResource = R.drawable.login_input_bottom
                                relativeLayout {
                                    email = editText {
                                        background = null
                                        textSize = 15f
                                        singleLine = true
                                        addTextChangedListener(object : TextWatcher {
                                            override fun afterTextChanged(s: Editable?) {}
                                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                                if (s != null) {
                                                    if (s.isNotEmpty()) {
                                                        emailHint.visibility = RelativeLayout.GONE
                                                    } else {
                                                        emailHint.visibility = RelativeLayout.VISIBLE
                                                    }
                                                }
                                            }
                                        })
                                    }.lparams(matchParent, matchParent)
                                    emailHint = appCompatTextView {
                                        backgroundColor = Color.TRANSPARENT
                                        text = resources.getString(R.string.register_email_hint)
                                        textColor = Color.parseColor("#FFD0D0D0")
                                        maxLines = 1
                                        setAutoSizeTextTypeUniformWithConfiguration(
                                            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                            dip(15),
                                            1,
                                            0
                                        )
                                        setOnClickListener {
                                            email.isFocusable = true
                                            email.isFocusableInTouchMode = true
                                            email.requestFocus()
                                            val imm =
                                                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                            imm.showSoftInput(email, 0)
                                        }
                                    }.lparams {
                                        centerVertically()
                                        leftMargin = dip(5)
                                    }
                                }.lparams(matchParent, matchParent)
                            }.lparams(matchParent, matchParent){
                                leftMargin = dip(14)
                            }
                        }.lparams(matchParent, dip(40)){
                            topMargin = dip(35)
                        }
                    }.lparams(matchParent, wrapContent)
                    button {
                        backgroundResource = R.drawable.enable_around_button
                        text = resources.getString(R.string.common_next)
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            closeFocusjianpan()
                            val emailText = email.text.toString()

                            if(emailText.isNullOrBlank()){
                                toast(resources.getString(R.string.register_input_email))
                                return@setOnClickListener
                            }

                            if(!emailMatch(emailText)){
                                toast(resources.getString(R.string.register_input_email_wrong))
                                return@setOnClickListener
                            }

                            val identity = intent.getIntExtra("identity", 1)

                            var user: RegisterModel? = null
                            if(intent.getSerializableExtra("user") != null){
                                user = intent.getSerializableExtra("user") as RegisterModel
                                user.email = emailText
                            }
                            var role = ""
                            if(intent.getSerializableExtra("role") != null){
                                role = intent.getStringExtra("role") as String
                            }
                            val refreshToken = if(intent.getStringExtra("refreshToken")!=null) intent.getStringExtra("refreshToken") else ""
                            startActivity<RegisterNickName>(
                                "role" to role,
                                "identity" to identity,
                                "user" to user as Serializable,
                                "refreshToken" to refreshToken
                            )
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(35)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(17),dip(40),0)
                }
                setOnClickListener {
                    closeFocusjianpan()
                }
            }
        }
    }

    private fun emailMatch(text: String): Boolean{
        val patter = Pattern.compile("[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9\\u4e00-\\u9fa5_-]+(\\.[a-zA-Z\\u4e00-\\u9fa5_-]{2,})+\$")
        val match = patter.matcher(text)
        return match.find()
    }
    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        email.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(email.windowToken, 0)
    }
}