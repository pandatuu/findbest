package app.findbest.vip.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonactivity.MainActivity
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.ChooseCountry
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.register.view.RegisterActivity
import app.findbest.vip.register.view.RegisterCountry
import app.findbest.vip.utils.*
import com.alibaba.fastjson.JSON
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.json.JSONObject
import retrofit2.HttpException

class LoginActivity : BaseActivity(), BackgroundFragment.ClickBack, ChooseCountry.DialogSelect {

    private lateinit var phoneNumber: EditText
    private lateinit var phoneNumberHint: TextView
    private lateinit var country: TextView
    private lateinit var pwd: EditText
    private lateinit var pwdHint: TextView
    private lateinit var button: Button

    private var backgroundFragment: BackgroundFragment? = null
    private var chooseCountry: ChooseCountry? = null

    private val mainId = 1
    private var exitTime: Long = 0

    @SuppressLint("SetTextI18n", "RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            id = mainId
            backgroundColor = Color.WHITE
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {

                    textView {
                        padding = dip(10)
                        text = resources.getString(R.string.register_title)
                        textColor = Color.parseColor("#FF333333")
                        textSize = 17f
                        setOnClickListener {
                            startActivity<RegisterActivity>()
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                    }.lparams(wrapContent, wrapContent) {
                        alignParentRight()
                        alignParentBottom()
                    }
                }.lparams(matchParent, dip(75)) {
                    leftMargin = dip(15)
                    rightMargin = dip(10)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = resources.getString(R.string.login_title)
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_mobile
                        }.lparams(dip(16), dip(20)) {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                                country = textView {
                                    text = "+86"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams(dip(26), wrapContent)
                                imageView {
                                    imageResource = R.mipmap.inverted_triangle
                                }.lparams(dip(12), dip(7)) {
                                    gravity = Gravity.CENTER_VERTICAL
                                    leftMargin = dip(8)
                                    rightMargin = dip(5)
                                }
                                setOnClickListener {
                                    closeFocusjianpan()
                                    openDialog()
                                }
                            }.lparams(wrapContent, matchParent)
                            relativeLayout {
                                phoneNumber = editText {
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
                                                if (s.isNotBlank()) {
                                                    val pwd = pwd.text.toString()
                                                    if (pwd.isNotBlank()) {
                                                        button.backgroundResource =
                                                            R.drawable.enable_around_button
                                                    }
                                                    phoneNumberHint.visibility = RelativeLayout.GONE
                                                } else {
                                                    phoneNumberHint.visibility = RelativeLayout.VISIBLE
                                                    button.backgroundResource =
                                                        R.drawable.disable_around_button
                                                }
                                            }
                                        }
                                    })
                                }.lparams(matchParent, matchParent)
                                phoneNumberHint = appCompatTextView {
                                    backgroundColor = Color.TRANSPARENT
                                    text = resources.getString(R.string.common_input_phone)
                                    textColor = Color.parseColor("#FFD0D0D0")
                                    maxLines = 1
                                    setAutoSizeTextTypeUniformWithConfiguration(
                                        TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                        dip(15),
                                        1,
                                        0
                                    )
                                    setOnClickListener {
                                        phoneNumber.isFocusable = true
                                        phoneNumber.isFocusableInTouchMode = true
                                        phoneNumber.requestFocus()
                                        val imm =
                                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.showSoftInput(phoneNumber, 0)
                                    }
                                }.lparams {
                                    centerVertically()
                                    leftMargin = dip(5)
                                }
                            }.lparams(matchParent, matchParent) {
                                leftMargin = dip(5)
                            }
                        }.lparams(matchParent, matchParent) {
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)) {
                        topMargin = dip(70)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_password_nor
                        }.lparams(dip(16), dip(20)) {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            relativeLayout {
                                pwd = editText {
                                    background = null
                                    textSize = 15f
                                    singleLine = true
                                    transformationMethod = PasswordTransformationMethod()
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
                                                if (s.isNotBlank()) {
                                                    val phone = phoneNumber.text.toString()
                                                    if (phone.isNotBlank()) {
                                                        button.backgroundResource =
                                                            R.drawable.enable_around_button
                                                    }
                                                    pwdHint.visibility = RelativeLayout.GONE
                                                } else {
                                                    pwdHint.visibility = RelativeLayout.VISIBLE
                                                    button.backgroundResource =
                                                        R.drawable.disable_around_button
                                                }
                                            }
                                        }
                                    })
                                }.lparams(matchParent, matchParent)
                                pwdHint = appCompatTextView {
                                    backgroundColor = Color.TRANSPARENT
                                    text = resources.getString(R.string.common_input_pwd)
                                    textColor = Color.parseColor("#FFD0D0D0")
                                    maxLines = 1
                                    setAutoSizeTextTypeUniformWithConfiguration(
                                        TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                        dip(15),
                                        1,
                                        0
                                    )
                                    setOnClickListener {
                                        pwd.isFocusable = true
                                        pwd.isFocusableInTouchMode = true
                                        pwd.requestFocus()
                                        val imm =
                                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.showSoftInput(pwd, 0)
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
                    textView {
                        padding = dip(10)
                        text = resources.getString(R.string.login_forget_pwd)
                        textColor = Color.parseColor("#FF202020")
                        textSize = 13f
                        setOnClickListener {
                            startActivity<ResetPassword>()
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.RIGHT
                        topMargin = dip(5)
                    }
                    button = button {
                        backgroundResource = R.drawable.disable_around_button
                        text = resources.getString(R.string.login_title)
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            closeFocusjianpan()

                            val phoneNum = phoneNumber.text.toString()
                            val pwd = pwd.text.toString()
                            if (phoneNum.isNullOrBlank()) {
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_phone), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }
                            if (pwd.isNullOrBlank()) {
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_pwd), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                login()
                            }
                        }
                    }.lparams(matchParent, dip(47)) {
                        topMargin = dip(14)
                    }
                }.lparams(matchParent, wrapContent) {
                    setMargins(dip(40), dip(40), dip(40), 0)
                }
                setOnClickListener {
                    closeFocusjianpan()
                }
            }
        }
    }

    private suspend fun login() {
        try {
            val countryCode = country.text.toString().substring(1)
            val deviceType = "android"
            val platform = "mobile"

            val mPerferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
            val deviceToken = mPerferences.getString("deviceToken", "") as String

            val params = mapOf(
                "country" to countryCode,
                "phone" to phoneNumber.text.toString(),
                "password" to pwd.text.toString(),
                "platform" to platform,
                "deviceType" to deviceType,
                "deviceToken" to deviceToken
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@LoginActivity, resources.getString(R.string.developmentUrl))
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

                val refresh = it.body()!!["refreshToken"].asString
                isAccount(token,refresh)
            }else if(it.code() == 400){
                val errorJson = JSONObject(it.errorBody()!!.string())
                val errorMsg = if(!errorJson.isNull("error")) errorJson.getString("error") else errorJson.getString("message")
                if("Phone not exist" == errorMsg){
                    toast(resources.getString(R.string.common_tips_phone_noexist))
                }else{ //"BadCredential" == errorMsg
                    toast(resources.getString(R.string.common_tips_accountpwd_wrong))
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }

    private fun isAccount(token: String, refreshToken: String){
        val account = CheckToken(this@LoginActivity).jwtParse(token)
        if(account == ""){
            //token验证失败
            return
        }
        //token验证成功获取到的status
        when (account) {
            AccountStatus.COMPLETED.arg -> {
                //已完善信息
                startActivity<MainActivity>()
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
            AccountStatus.UNCOMPLETED.arg -> {
                //未完善信息
                toast(resources.getString(R.string.common_tips_user_uncompleted))
                startActivity<RegisterCountry>("onlyCompleted" to true, "refreshToken" to refreshToken)
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
            AccountStatus.PENDING.arg -> {
                //审核中

            }
            AccountStatus.PASSED.arg -> {
                //审核通过

            }
            else -> {
                //审核失败

            }
        }
    }

    override fun clickAll() {
        closeAlertDialog()
    }

    @SuppressLint("SetTextI18n")
    override fun getSelectedItem(index: String) {
        when (index) {
            resources.getString(R.string.register_country_japan) -> {
                country.text = "+81"
            }
            resources.getString(R.string.register_country_china) -> {
                country.text = "+86"
            }
            resources.getString(R.string.register_country_korea) -> {
                country.text = "+82"
            }
        }
        closeAlertDialog()
    }

    private fun openDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@LoginActivity)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.bottom_in_a, R.anim.bottom_in_a)

        chooseCountry = ChooseCountry.newInstance()
        mTransaction.add(mainId, chooseCountry!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = supportFragmentManager.beginTransaction()
        if (chooseCountry != null) {
            mTransaction.setCustomAnimations(R.anim.bottom_out_a, R.anim.bottom_out_a)

            mTransaction.remove(chooseCountry!!)
            chooseCountry = null
        }

        if (backgroundFragment != null) {
            mTransaction.setCustomAnimations(
                R.anim.fade_in_out_a, R.anim.fade_in_out_a
            )
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }
        mTransaction.commit()
    }

    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        phoneNumber.clearFocus()
        pwd.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(phoneNumber.windowToken, 0)
        phone.hideSoftInputFromWindow(pwd.windowToken, 0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(applicationContext,  resources.getString(R.string.common_finish_second), Toast.LENGTH_SHORT)
                        .show()
                    exitTime = System.currentTimeMillis()
                } else {
                    val startMain = Intent(Intent.ACTION_MAIN)
                    startMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startMain.addCategory(Intent.CATEGORY_HOME)
                    startActivity(startMain)
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}

enum class AccountStatus(val arg: String){
    /*
         COMPLETED : 已完善信息
         UNCOMPLETED : 未完善信息
         PENDING : 审核中
         PASSED : 审核通过
         REJECT : 审核失败
     */
    COMPLETED("COMPLETED"),
    UNCOMPLETED("UNCOMPLETED"),
    PENDING("PENDING"),
    PASSED("PASSED"),
    REJECT("REJECT")
}