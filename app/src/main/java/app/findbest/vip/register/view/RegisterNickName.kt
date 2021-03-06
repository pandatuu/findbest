package app.findbest.vip.register.view

import android.content.Context
import android.content.SharedPreferences
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
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import com.alibaba.fastjson.JSON
import app.findbest.vip.R
import app.findbest.vip.commonactivity.MainActivity
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.register.api.RegisterApi
import app.findbest.vip.register.model.RegisterModel
import app.findbest.vip.utils.*
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.jetbrains.anko.*
import retrofit2.HttpException
import java.util.regex.Pattern

/**
 * 注册流程
 * 1.先注册用户 -- 注册第一页的
 * 2.登录用户Apass账号
 * 3.完善用户的findbest信息
 */
class RegisterNickName : BaseActivity() {

    private lateinit var nickName: EditText
    private lateinit var nickNameHint: TextView
    private lateinit var user: RegisterModel
    var identity = 0
    var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        identity = intent.getIntExtra("identity", 1)
        if (intent.getSerializableExtra("user") != null) {
            user = intent.getSerializableExtra("user") as RegisterModel
        }
        if (intent.getStringExtra("role") != null) {
            role = intent.getStringExtra("role") as String
        }

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
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "5/5"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        //设置昵称
                        text = if (identity == 2) {
                            resources.getString(R.string.register_team_nickname)
                        } else {
                            resources.getString(R.string.register_person_nickname)
                        }
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView {
                                imageResource = R.mipmap.team_activity
                            }.lparams(dip(16), dip(20)) {
                                gravity = Gravity.CENTER_VERTICAL
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                backgroundResource = R.drawable.login_input_bottom
                                relativeLayout {
                                    nickName = editText {
                                        background = null
                                        textSize = 15f
                                        singleLine = true
                                        addTextChangedListener(object : TextWatcher {
                                            override fun afterTextChanged(s: Editable?) {}
                                            override fun beforeTextChanged(
                                                s: CharSequence?,
                                                start: Int,
                                                count: Int,
                                                after: Int
                                            ) {
                                            }

                                            override fun onTextChanged(
                                                s: CharSequence?,
                                                start: Int,
                                                before: Int,
                                                count: Int
                                            ) {
                                                if (s != null) {
                                                    if (s.isNotEmpty()) {
                                                        nickNameHint.visibility = RelativeLayout.GONE
                                                    } else {
                                                        nickNameHint.visibility =
                                                            RelativeLayout.VISIBLE
                                                    }
                                                }
                                            }
                                        })
                                    }.lparams(matchParent, matchParent)
                                    nickNameHint = appCompatTextView {
                                        backgroundColor = Color.TRANSPARENT
                                        text = if (identity == 2) {
                                            resources.getString(R.string.register_team_nickname)
                                        } else {
                                            resources.getString(R.string.register_person_nickname)
                                        }
                                        textColor = Color.parseColor("#FFD0D0D0")
                                        maxLines = 1
                                        setAutoSizeTextTypeUniformWithConfiguration(
                                            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                            dip(15),
                                            1,
                                            0
                                        )
                                        setOnClickListener {
                                            nickName.isFocusable = true
                                            nickName.isFocusableInTouchMode = true
                                            nickName.requestFocus()
                                            val imm =
                                                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                            imm.showSoftInput(nickName, 0)
                                        }
                                    }.lparams {
                                        centerVertically()
                                        leftMargin = dip(5)
                                    }
                                }.lparams(matchParent, matchParent)
                            }.lparams(matchParent, matchParent) {
                                leftMargin = dip(14)
                            }
                        }.lparams(matchParent, dip(40)) {
                            topMargin = dip(35)
                        }
                    }.lparams(matchParent, wrapContent)
                    button {
                        backgroundResource = R.drawable.enable_around_button
                        text = resources.getString(R.string.register_done)
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            closeFocusjianpan()
                            val nickname = nickName.text.toString()
                            if (nickname.isNullOrBlank()) {
                                toast(resources.getString(R.string.register_input_nickname))
                                return@setOnClickListener
                            }

                            if (nickeNameMatch(nickname)) {
                                toast(resources.getString(R.string.register_input_nickname_wrong))
                                return@setOnClickListener
                            }

                            val refreshToken = if(intent.getStringExtra("refreshToken")!=null) intent.getStringExtra("refreshToken") else ""
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                if(user.onlyCompleted){
                                    setInformation(refreshToken)
                                }else{
                                    login()
                                }
                            }
                        }
                    }.lparams(matchParent, dip(47)) {
                        topMargin = dip(35)
                    }
                }.lparams(matchParent, wrapContent) {
                    setMargins(dip(40), dip(17), dip(40), 0)
                }
                setOnClickListener {
                    closeFocusjianpan()
                }
            }
        }
    }
//
//    private suspend fun register(){
//        try {
//            val params = mapOf(
//                "country" to user.country,
//                "phone" to user.phone,
//                "password" to user.pwd,
//                "code" to user.vCode
//            )
//            val userJson = JSON.toJSONString(params)
//            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)
//
//            val retrofitUils =
//                RetrofitUtils(this@RegisterNickName, resources.getString(R.string.developmentUrl))
//            val it = retrofitUils.create(RegisterApi::class.java)
//                .registerUser(body)
//                .subscribeOn(Schedulers.io())
//                .awaitSingle()
//            if (it.code() in 200..299) {
//                //注册成功
//                login()
//            }
//        } catch (throwable: Throwable) {
//
//        }
//    }

    private suspend fun login(){
        try {
            val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@RegisterNickName)
            val deviceType = "android"
            val deviceToken = mPerferences.getString("deviceToken","") as String
            val platform = "mobile"
            val params = mapOf(
                "country" to user.country,
                "phone" to user.phone,
                "password" to user.pwd,
                "platform" to platform,
                "deviceType" to deviceType,
                "deviceToken" to deviceToken
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@RegisterNickName, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(LoginApi::class.java)
                .loginApass(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                //登录成功
                val token = it.body()!!["accessToken"].asString
                val mEditor = mPerferences.edit()
                mEditor.putString("token", token)
                mEditor.commit()

                val refreshToken = it.body()!!["refreshToken"].asString
                setInformation(refreshToken)
            }
        } catch (throwable: Throwable) {
            if(throwable is HttpException){
                println(throwable.message())
            }
        }
    }

    private suspend fun setInformation(refreshToken: String) {
        try {
            val params = mapOf(
                "roleName" to role,
                "userType" to identity,
                "country" to user.country,
                "name" to nickName.text.toString(),
                "noOpen" to 1,
                "boss" to nickName.text.toString(),
                "email" to user.email
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@RegisterNickName, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(RegisterApi::class.java)
                .information(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                refreshToken(refreshToken)
            }
        } catch (throwable: Throwable) {
            println(throwable)
        }
    }

    private suspend fun refreshToken(refreshToken: String) {
        try {
            val params = mapOf(
                "refreshToken" to refreshToken
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@RegisterNickName, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(RegisterApi::class.java)
                .refreshToken(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@RegisterNickName)
                val token = it.body()!!["accessToken"].asString
                val mEditor = mPerferences.edit()
                mEditor.putString("token", token)
                mEditor.commit()

                val account = CheckToken(this@RegisterNickName).jwtParse(token)
                if(account != ""){
                    startActivity<MainActivity>()
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                }
                //token验证失败
            }
        } catch (throwable: Throwable) {

        }
    }


    private fun nickeNameMatch(text: String): Boolean {
        val patter =
            Pattern.compile("[`~!@#\$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]")
        val match = patter.matcher(text)
        return match.find()
    }
    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        nickName.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(nickName.windowToken, 0)
    }
}