package app.findbest.vip.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.ChooseCountry
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.register.api.RegisterApi
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
import click
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
import withTrigger
import java.util.regex.Pattern

class ResetPassword: BaseActivity(), BackgroundFragment.ClickBack, ChooseCountry.DialogSelect {
    override fun clickAll() {
        closeAlertDialog()
    }

    @SuppressLint("SetTextI18n")
    override fun getSelectedItem(index: Int) {
        when(index){
            0 -> {
                countryCode.text = "+81"
            }
            1 -> {
                countryCode.text = "+86"
            }
            2 -> {
                countryCode.text = "+82"
            }
        }
        closeAlertDialog()
    }

    private lateinit var countryCode: TextView
    private lateinit var phoneNumber: EditText
    private lateinit var vCode: EditText
    private lateinit var code: TextView
    private lateinit var newPwd: EditText
    private lateinit var againPwd: EditText
    private lateinit var button: Button

    private var backgroundFragment: BackgroundFragment? = null
    private var chooseCountry: ChooseCountry? = null

    private val mainId = 1
    private var runningDownTimer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            id = mainId
            backgroundColor = Color.WHITE
            linearLayout {
                orientation = LinearLayout.VERTICAL
                linearLayout {
                    gravity = Gravity.BOTTOM
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.nav_ico_close
                            setOnClickListener {
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams(dip(16),dip(16)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        textView {
                            text = resources.getString(R.string.common_toolbar_back)
                            textSize = 17f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                        }

                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(wrapContent, wrapContent)
                }.lparams(matchParent,dip(65)){
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = resources.getString(R.string.login_reset_pwd)
                        textColor = Color.parseColor("#FF333333")
                        textSize = 19f
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_mobile
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                                countryCode = textView {
                                    text = "+86"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams(dip(26), wrapContent)
                                imageView {
                                    imageResource = R.mipmap.inverted_triangle
                                }.lparams(dip(12),dip(7)){
                                    gravity = Gravity.CENTER_VERTICAL
                                    leftMargin = dip(8)
                                    rightMargin = dip(5)
                                }
                                setOnClickListener {
                                    closeFocusjianpan()
                                    openDialog()
                                }
                            }.lparams(wrapContent, matchParent)
                            phoneNumber = editText {
                                background = null
                                hint = resources.getString(R.string.common_input_phone)
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                addTextChangedListener(object: TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val vCode = vCode.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(vCode.isNotBlank() && newPwd.isNotBlank() && againPwd.isNotBlank()){
                                                    button.backgroundResource = R.drawable.enable_around_button
                                                }
                                            }else{
                                                button.backgroundResource = R.drawable.disable_around_button
                                            }
                                        }
                                    }
                                })
                            }.lparams(dip(0), matchParent){
                                weight = 1f
                                leftMargin = dip(10)
                            }
                            imageView {
                                imageResource = R.mipmap.login_ico_close
                                setOnClickListener {
                                    closeFocusjianpan()
                                    phoneNumber.setText("")
                                }
                            }.lparams(dip(18),dip(18)){
                                leftMargin = dip(5)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(70)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_validation_nor
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            vCode = editText {
                                background = null
                                hint = resources.getString(R.string.common_input_vcode)
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                addTextChangedListener(object: TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phoneNumber = phoneNumber.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(phoneNumber.isNotBlank() && newPwd.isNotBlank() && againPwd.isNotBlank()){
                                                    button.backgroundResource = R.drawable.enable_around_button
                                                }
                                            }else{
                                                button.backgroundResource = R.drawable.disable_around_button
                                            }
                                        }
                                    }
                                })
                            }.lparams(dip(0), matchParent){
                                weight = 1f
                            }
                            relativeLayout {
                                backgroundResource = R.drawable.around_button_5
                                code = textView {
                                    text = resources.getString(R.string.common_get_vcode)
                                    textSize = 12f
                                    textColor = Color.parseColor("#FFFFFFFF")
                                }.lparams(wrapContent, wrapContent){
                                    centerInParent()
                                }
                                setOnClickListener {
                                    val phoneNum = phoneNumber.text.toString()
                                    if(phoneNum.isNullOrBlank()){
                                        val toast =
                                            Toast.makeText(applicationContext, resources.getString(R.string.common_input_phone), Toast.LENGTH_SHORT)
                                        toast.setGravity(Gravity.CENTER, 0, 0)
                                        toast.show()
                                        return@setOnClickListener
                                    }

                                    if(phoneNum.length !in 10..11){
                                        val toast =
                                            Toast.makeText(applicationContext, resources.getString(R.string.common_input_right_phone), Toast.LENGTH_SHORT)
                                        toast.setGravity(Gravity.CENTER, 0, 0)
                                        toast.show()
                                        return@setOnClickListener
                                    }

                                    GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                        val sendBool = sendvCode()
                                        if (sendBool){
                                            onPcode()
                                        }
                                    }
                                }
                            }.lparams(dip(72),dip(22)){
                                gravity = Gravity.CENTER_VERTICAL
                                leftMargin = dip(5)
                            }
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(35)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_password_nor
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            newPwd = editText {
                                background = null
                                hint = resources.getString(R.string.common_input_new_pwd)
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                transformationMethod = PasswordTransformationMethod()
                                addTextChangedListener(object: TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phoneNumber = phoneNumber.text.toString()
                                                val vCode = vCode.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(phoneNumber.isNotBlank() && vCode.isNotBlank() && againPwd.isNotBlank()){
                                                    button.backgroundResource = R.drawable.enable_around_button
                                                }
                                            }else{
                                                button.backgroundResource = R.drawable.disable_around_button
                                            }
                                        }
                                    }
                                })
                            }.lparams(matchParent, matchParent)
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(35)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView {
                            imageResource = R.mipmap.login_ico_password_nor
                        }.lparams(dip(16), dip(20)){
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            backgroundResource = R.drawable.login_input_bottom
                            againPwd = editText {
                                background = null
                                hint = resources.getString(R.string.common_input_new_pwd_again)
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                transformationMethod = PasswordTransformationMethod()
                                addTextChangedListener(object: TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phoneNumber = phoneNumber.text.toString()
                                                val vCode = vCode.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                if(phoneNumber.isNotBlank() && vCode.isNotBlank() && newPwd.isNotBlank()){
                                                    button.backgroundResource = R.drawable.enable_around_button
                                                }
                                            }else{
                                                button.backgroundResource = R.drawable.disable_around_button
                                            }
                                        }
                                    }
                                })
                            }.lparams(matchParent, matchParent)
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(14)
                        }
                    }.lparams(matchParent, dip(40)){
                        topMargin = dip(35)
                    }
                    button = button {
                        backgroundResource = R.drawable.disable_around_button
                        text = resources.getString(R.string.login_title)
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            closeFocusjianpan()
                            val phoneNum = phoneNumber.text.toString()
                            val vCode = vCode.text.toString()
                            val nPwd = newPwd.text.toString()
                            val aPwd = againPwd.text.toString()
                            if(phoneNum.isNullOrBlank()){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_phone), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(phoneNum.length !in 10..11){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_right_phone), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(vCode.isNullOrBlank()){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_vcode), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(nPwd.isNullOrBlank()){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_pwd), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(!pwdMatch(nPwd)){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_right_pwd), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(aPwd.isNullOrBlank()){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_new_pwd_again), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            if(nPwd != aPwd){
                                val toast =
                                    Toast.makeText(applicationContext, resources.getString(R.string.common_input_pwd_disaccord), Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()
                                return@setOnClickListener
                            }

                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                resetPwd()
                            }
                        }
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(24)
                    }
                }.lparams(matchParent, wrapContent){
                    setMargins(dip(40),dip(40),dip(40),0)
                }
                setOnClickListener {
                    closeFocusjianpan()
                }
            }
        }
    }

    private suspend fun sendvCode(): Boolean{
        try {
            val country = countryCode.text.toString().substring(1)
            val params = mapOf(
                "country" to country,
                "phone" to phoneNumber.text.toString(),
                "type" to "RESETLOGINPWD"
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@ResetPassword, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(RegisterApi::class.java)
                .sendvCode(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val toast =
                    Toast.makeText(applicationContext, resources.getString(R.string.common_tips_send_vcode), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

                return true
            }else{
                val errorJson = JSONObject(it.errorBody()!!.string())
                val errorMsg = errorJson.getString("message")
                if("phone_not_exist" == errorMsg){
                    toast(resources.getString(R.string.common_tips_phone_noexist))
                }
                return false
            }
        }catch (throwable: Throwable){
            return false
        }
    }

    private suspend fun resetPwd(){
        try {
            val country = countryCode.text.toString().substring(1)
            val params = mapOf(
                "country" to country,
                "phone" to phoneNumber.text.toString(),
                "code" to vCode.text.toString(),
                "password" to newPwd.text.toString()
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@ResetPassword, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(LoginApi::class.java)
                .resetPwd(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                //修改成功
                toast(resources.getString(R.string.common_tips_update_success))
                startActivity<LoginActivity>()
                overridePendingTransition(R.anim.left_in, R.anim.right_out)
            }else{
                val errorJson = JSONObject(it.errorBody()!!.string())
                val errorMsg = errorJson.getString("error")
                if("ValidtionError" == errorMsg){
                    toast(resources.getString(R.string.common_tips_vcode_wrong))
                }
            }
        } catch (throwable: Throwable) {
            if(throwable is HttpException){
                println(throwable.message())
            }
        }
    }

    private fun openDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ResetPassword)

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


    private fun pwdMatch(text: String): Boolean{
        val patter = Pattern.compile("^(?![0-9]+\$)(?![a-z]+\$)(?![A-Z]+\$)(?![,\\.#%'\\+\\*\\-:;^_`]+\$)[,\\.#%'\\+\\*\\-:;^_`0-9A-Za-z]{8,16}\$")
        val match = patter.matcher(text)
        return match.find()
    }

    //发送验证码按钮
    fun onPcode() {
        //如果60秒倒计时没有结束
        if (runningDownTimer) {
            return
        }
        downTimer.start()  // 倒计时开始
    }

    /**
     * 倒计时
     */
    private val downTimer = object : CountDownTimer((60 * 1000).toLong(), 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(l: Long) {
            runningDownTimer = true
            code.text = (l / 1000).toString() + "s"

            code.withTrigger().click  {
                toast(resources.getString(R.string.common_countdown_rest))

            }
        }

        override fun onFinish() {
            runningDownTimer = false
            code.text = resources.getString(R.string.common_get_vcode)


            code.withTrigger().click  {
                onPcode()
            }
        }
    }

    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        phoneNumber.clearFocus()
        vCode.clearFocus()
        newPwd.clearFocus()
        againPwd.clearFocus()
        //关闭键盘事件
        val phone = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(phoneNumber.windowToken, 0)
        phone.hideSoftInputFromWindow(vCode.windowToken, 0)
        phone.hideSoftInputFromWindow(newPwd.windowToken, 0)
        phone.hideSoftInputFromWindow(againPwd.windowToken, 0)
    }
}