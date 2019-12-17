package app.findbest.vip.register.view

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
import app.findbest.vip.register.api.RegisterApi
import app.findbest.vip.register.model.RegisterModel
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
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import withTrigger
import java.io.Serializable
import java.util.regex.Pattern

class RegisterActivity: BaseActivity(), BackgroundFragment.ClickBack, ChooseCountry.DialogSelect {
    override fun clickAll() {
        closeAlertDialog()
    }

    @SuppressLint("SetTextI18n")
    override fun getSelectedItem(index: Int) {
        when(index){
            0 -> {
                country.text = "+81"
            }
            1 -> {
                country.text = "+86"
            }
            2 -> {
                country.text = "+82"
            }
        }
        closeAlertDialog()
    }

    private lateinit var country: TextView
    private lateinit var phoneNumber: EditText
    private lateinit var vCode: EditText
    private lateinit var code: TextView
    private lateinit var newPwd: EditText
    private lateinit var againPwd: EditText
    private lateinit var button: Button
    private lateinit var checkBox: CheckBox

    private var backgroundFragment: BackgroundFragment? = null
    private var chooseCountry: ChooseCountry? = null

    private var runningDownTimer: Boolean = false
    private val mainId = 1

    @SuppressLint("SetTextI18n")
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
                        text = "登录"
                        textColor = Color.parseColor("#FF333333")
                        textSize = 17f
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(wrapContent, wrapContent){
                        alignParentRight()
                        alignParentBottom()
                    }
                }.lparams(matchParent,dip(75)){
                    leftMargin = dip(15)
                    rightMargin = dip(10)
                }
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = "注册"
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
                                country = textView {
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
                                hint = "请输入手机号码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                addTextChangedListener(object: TextWatcher{
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val vCode = vCode.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(vCode.isNotBlank() && newPwd.isNotBlank()
                                                    && againPwd.isNotBlank() && checkBox.isChecked){
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
                                hint = "请输入验证码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                addTextChangedListener(object: TextWatcher{
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phone = phoneNumber.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(phone.isNotBlank() && newPwd.isNotBlank()
                                                    && againPwd.isNotBlank() && checkBox.isChecked){
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
                                    text = "获取验证码"
                                    textSize = 12f
                                    textColor = Color.parseColor("#FFFFFFFF")
                                }.lparams(wrapContent, wrapContent){
                                    centerInParent()
                                }
                                setOnClickListener {
                                    val phoneNum = phoneNumber.text.toString()
                                    if(phoneNum.isNullOrBlank()){
                                        toast("请填写手机号")
                                        return@setOnClickListener
                                    }

                                    if(phoneNum.length !in 10..11){
                                        toast("请填写正确手机号")
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
                                hint = "请输入新密码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                transformationMethod = PasswordTransformationMethod()
                                addTextChangedListener(object: TextWatcher{
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phone = phoneNumber.text.toString()
                                                val vCode = vCode.text.toString()
                                                val againPwd = againPwd.text.toString()
                                                if(phone.isNotBlank() && vCode.isNotBlank()
                                                    && againPwd.isNotBlank() && checkBox.isChecked){
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
                                hint = "请再次输入新密码"
                                hintTextColor = Color.parseColor("#FFD0D0D0")
                                textSize = 15f
                                singleLine = true
                                transformationMethod = PasswordTransformationMethod()
                                addTextChangedListener(object: TextWatcher{
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int ) {}
                                    override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) {
                                        if (s != null) {
                                            if(s.isNotBlank()){
                                                val phone = phoneNumber.text.toString()
                                                val vCode = vCode.text.toString()
                                                val newPwd = newPwd.text.toString()
                                                if(phone.isNotBlank() && vCode.isNotBlank()
                                                    && newPwd.isNotBlank() && checkBox.isChecked){
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
                        text = "注册"
                        textSize = 15f
                        textColor = Color.parseColor("#FFFFFFFF")
                        setOnClickListener {
                            closeFocusjianpan()
                            val phoneNum = phoneNumber.text.toString()
                            val code = vCode.text.toString()
                            val nPwd = newPwd.text.toString()
                            val aPwd = againPwd.text.toString()
                            if(phoneNum.isNullOrBlank()){
                                toast("请填写手机号")
                                return@setOnClickListener
                            }

                            if(phoneNum.length !in 10..11){
                                toast("请填写正确手机号")
                                return@setOnClickListener
                            }

                            if(code.isNullOrBlank()){
                                toast("请填写验证码")
                                return@setOnClickListener
                            }

                            if(nPwd.isNullOrBlank()){
                                toast("请填写密码")
                                return@setOnClickListener
                            }

                            if(!pwdMatch(nPwd)){
                                toast("密码格式不正确")
                                return@setOnClickListener
                            }

                            if(aPwd.isNullOrBlank()){
                                toast("请再次填写密码")
                                return@setOnClickListener
                            }

                            if(nPwd != aPwd){
                                toast("二次输入密码不一致")
                                return@setOnClickListener
                            }

                            if(!checkBox.isChecked){
                                toast("请勾选协议")
                                return@setOnClickListener
                            }

                            val user = RegisterModel(
                                phoneNum,
                                country.text.toString(),
                                code,
                                nPwd,
                                ""
                            )

                            startActivity<RegisterCountry>("user" to user as Serializable)
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                    }.lparams(matchParent,dip(47)){
                        topMargin = dip(24)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        checkBox = checkBox {
                            setButtonDrawable(R.drawable.checkbox)
                            onCheckedChange { _, isChecked ->
                                if(isChecked){
                                    val phone = phoneNumber.text.toString()
                                    val vCode = vCode.text.toString()
                                    val newPwd = newPwd.text.toString()
                                    val againPwd = againPwd.text.toString()
                                    if(phone.isNotBlank() && vCode.isNotBlank()
                                        && newPwd.isNotBlank() && againPwd.isNotBlank()){
                                        button.backgroundResource = R.drawable.enable_around_button
                                    }
                                }else{
                                    button.backgroundResource = R.drawable.disable_around_button
                                }
                            }
                        }
                        setOnClickListener {
                            checkBox.isChecked = !checkBox.isChecked
                        }
                        textView {
                            text = "我同意"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                        }
                        textView {
                            text = "隐私协议 "
                            textSize = 13f
                            textColor = Color.parseColor("#FFFF7900")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                        textView {
                            text = "和"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                        textView {
                            text = "服务申明"
                            textSize = 13f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(5)
                        }
                    }.lparams(wrapContent, wrapContent){
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = dip(25)
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
            val countryCode = country.text.toString().substring(1)
            val params = mapOf(
                "country" to countryCode,
                "phone" to phoneNumber.text.toString(),
                "type" to "REG"
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@RegisterActivity, resources.getString(R.string.testRegisterUrl))
            val it = retrofitUils.create(RegisterApi::class.java)
                .sendvCode(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
//                DialogUtils.hideLoading(thisDialog)
                val toast =
                    Toast.makeText(applicationContext, "已发送验证码", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

                return true
            }
            if(it.code() == 400){
                val toast =
                    Toast.makeText(applicationContext, "手机号已经注册过了", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

                return false
            }
            return false
        }catch (throwable: Throwable){
            return false
        }
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

            code. withTrigger().click  {
                toast("冷却中...")

            }
        }

        override fun onFinish() {
            runningDownTimer = false
            code.text = "获取"


            code.withTrigger().click  {
                onPcode()
            }
        }
    }

    private fun openDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance()

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