package app.findbest.vip.individual.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.individual.adapter.PersonAccountAdapter
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.individual.view.*
import app.findbest.vip.login.api.LoginApi
import app.findbest.vip.login.view.LoginActivity
import app.findbest.vip.message.activity.VideoResultActivity
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException
import withTrigger


class Terminal : FragmentParent() {

    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context
    private lateinit var personName: TextView
    private lateinit var beforeNumber: TextView
    private lateinit var makingNumber: TextView
    private lateinit var finishNumber: TextView
    private lateinit var headImage: ImageView
    private lateinit var vipImage: ImageView
    private lateinit var stateImage: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userAccount: RecyclerView
    private var accountAdapter: PersonAccountAdapter? = null
    private var role = ""

    companion object {
        fun newInstance(context: Context): Terminal {
            val f = Terminal()
            f.activityInstance = context
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = if (parentFragment != null) {
            parentFragment?.context
        } else {
            activity
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun onResume() {
        super.onResume()
//        val bool = App.getInstance()?.getInviteVideoBool()
//        if(bool!!){
//            startActivity<VideoResultActivity>()
//            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        role = sharedPreferences.getString("role", "").toString()
        return createView()
    }

    @SuppressLint("SetTextI18n")
    private fun createView(): View {
//        accountAdapter = PersonAccountAdapter(mContext!!, mutableListOf())
        val view = UI {
            verticalLayout {
                backgroundColor = Color.WHITE
                verticalLayout {
                    backgroundResource = R.mipmap.image_message_png
                    relativeLayout {
                        toolbar1 = toolbar {
                            isEnabled = true
                            title = ""
                        }.lparams(matchParent, wrapContent) {
                            alignParentBottom()
                        }
//
                        textView {
                            textResource = R.string.tl_title
                            gravity = Gravity.CENTER
                            textSize = 17f
                            textColor = Color.WHITE
                        }.lparams(matchParent,dip(54 - getStatusBarHeight(this@Terminal.context!!))) {
                            alignParentBottom()
                        }
                    }.lparams(matchParent,dip(54))

                    verticalLayout {
                        //我的名字，头像，是否认证那一排
                        linearLayout {
                            verticalLayout {
                                personName = textView {
                                    textSize = 30f
                                    textColor = Color.WHITE
                                    ellipsize = TextUtils.TruncateAt.END
                                }.lparams(matchParent, dip(42)) {
                                    bottomMargin = dip(3)
                                    leftMargin = dip(2)
                                    rightMargin = dip(5)
                                }

                                linearLayout {
                                    gravity = Gravity.CENTER_VERTICAL
                                    vipImage = imageView {

                                    }.lparams(dip(26), dip(26)) {
                                        rightMargin = dip(3)
                                    }

                                    stateImage = imageView {

                                    }.lparams(dip(65), dip(21))
                                }
                            }.lparams(dip(0), wrapContent) {
                                weight = 1f
                                leftMargin = dip(22)
                            }

                            headImage = imageView {
                                imageResource = R.mipmap.default_avatar

//                                this.withTrigger().click {
//                                    val intent = Intent(context, Head::class.java)
//                                    startActivity(intent)
//                                    activity?.overridePendingTransition(
//                                        R.anim.right_in,
//                                        R.anim.left_out
//                                    )
//                                }
                            }.lparams(dip(64), dip(64)) {

                                rightMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent) {
                            weight = 1f
                            topMargin = dip(17)
                        }
                        //应征项目那一排
                        linearLayout {
                            verticalLayout {
                                setOnClickListener {
                                    activity!!.startActivity<MyProjectList>("status" to "beforeNumber")
                                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                }
                                beforeNumber = textView {
                                    text = "0"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_application_items
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(wrapContent, wrapContent) {
                                weight = 1f
                            }
                            verticalLayout {
                                setOnClickListener {
                                    activity!!.startActivity<MyProjectList>("status" to "makingNumber")
                                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                }
                                makingNumber = textView {
                                    text = "0"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_production_project
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(wrapContent, wrapContent) {
                                weight = 1f
                            }

                            verticalLayout {
                                setOnClickListener {
                                    activity!!.startActivity<MyProjectList>("status" to "finishNumber")
                                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                }
                                finishNumber = textView {
                                    text = "0"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_transaction_complete
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams {
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(wrapContent, wrapContent) {
                                weight = 1f
                            }

                            verticalLayout {

                            }.lparams(wrapContent, wrapContent) {
                                weight = 1f
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(20)
                        }
                    }.lparams(matchParent, matchParent) {
                        weight = 1f
                    }


                }.lparams(matchParent, dip(229))

                scrollView {
                    overScrollMode = View.OVER_SCROLL_NEVER
                    verticalLayout {
                        backgroundColor = Color.WHITE
                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            orientation = LinearLayout.HORIZONTAL
                            linearLayout {
                                backgroundColor = Color.parseColor("#F87F2D")
                            }.lparams(dip(4), dip(18)) {
                                leftMargin = dip(4)
                            }

                            textView {
                                textResource = R.string.tl_account_balance
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams{
                                leftMargin = dip(10)
                            }
                        }.lparams(matchParent, dip(62)) {
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        //账户余额显示
                        linearLayout {
                            userAccount = recyclerView {
                                layoutManager = LinearLayoutManager(context)
                                overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                            }.lparams(matchParent, wrapContent){
                                leftMargin = dip(20)
                                rightMargin = dip(20)
                            }
                        }.lparams(matchParent, wrapContent)


                        //我的项目
                        relativeLayout {
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            this.withTrigger().click {
                                activity!!.startActivity<MyProjectList>("role" to role)
                                activity?.overridePendingTransition(
                                    R.anim.right_in,
                                    R.anim.left_out
                                )
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.ico_file_nor
                                }.lparams(dip(19), dip(18))

                                textView {
                                    textResource = R.string.tl_my_project
                                    textSize = 13f
                                    textColor = Color.parseColor("#333333")
                                }.lparams(wrapContent, wrapContent) {
                                    leftMargin = dip(10)
                                }
                            }.lparams{
                                centerVertically()
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(dip(6), dip(11)) {
                                alignParentRight()
                                centerVertically()
                            }
                        }.lparams(matchParent, dip(62)) {
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

//                        linearLayout {
//                            gravity = Gravity.CENTER
//                            imageView {
//                                imageResource = R.mipmap.ico_update_nor
//                            }.lparams(dip(19),dip(18))
//
//                            textView {
//                                textResource = R.string.tl_version_update
//                                textSize = 13f
//                                textColor = Color.parseColor("#333333")
//                            }.lparams(wrapContent,matchParent){
//                                leftMargin = dip(10)
//                                rightMargin = dip(15)
//                                weight = 1f
//                            }
//
//                            textView {
//                                text = "V1.1.22"
//                                textSize = 15f
//                                textColor = Color.parseColor("#333333")
//                            }
//
//                            imageView {
//                                imageResource = R.mipmap.btn_slect_nor
//                            }.lparams(dip(6),dip(11)){
//                                leftMargin = dip(12)
//                            }
//                        }.lparams(matchParent,wrapContent){
//                            topMargin = dip(16)
//                            leftMargin = dip(20)
//                            rightMargin = dip(20)
//                        }
//                        view {
//                            backgroundColor = Color.parseColor("#E3E3E3")
//                        }.lparams(matchParent, dip(1)) {
//                            topMargin = dip(16)
//                        }

                        //使用帮助
//                        relativeLayout {
//                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
//                            this.withTrigger().click {
//                                val intent = Intent(context, Help::class.java)
//                                startActivity(intent)
//                                activity?.overridePendingTransition(
//                                    R.anim.right_in,
//                                    R.anim.left_out
//                                )
//                            }
//                            linearLayout {
//                                orientation = LinearLayout.HORIZONTAL
//                                imageView {
//                                    imageResource = R.mipmap.ico_help_nor
//                                }.lparams(dip(19), dip(18))
//
//                                textView {
//                                    textResource = R.string.tl_use_help
//                                    textSize = 13f
//                                    textColor = Color.parseColor("#333333")
//                                }.lparams(wrapContent, wrapContent) {
//                                    leftMargin = dip(10)
//                                }
//                            }.lparams{
//                                centerVertically()
//                            }
//
//                            imageView {
//                                imageResource = R.mipmap.btn_slect_nor
//                            }.lparams(dip(6), dip(11)) {
//                                alignParentRight()
//                                centerVertically()
//                            }
//                        }.lparams(matchParent, dip(62)) {
//                            leftMargin = dip(20)
//                            rightMargin = dip(20)
//                        }


                        //意见反馈
                        relativeLayout {
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            this.withTrigger().click {
                                val intent = Intent(context, Feedback::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(
                                    R.anim.right_in,
                                    R.anim.left_out
                                )
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.ico_opinion_nor
                                }.lparams(dip(19), dip(18))

                                textView {
                                    textResource = R.string.tl_feedback
                                    textSize = 13f
                                    textColor = Color.parseColor("#333333")
                                }.lparams(wrapContent, wrapContent) {
                                    leftMargin = dip(10)
                                }
                            }.lparams{
                                centerVertically()
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(dip(6), dip(11)) {
                                alignParentRight()
                                centerVertically()
                            }
                        }.lparams(matchParent, dip(62)) {
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        //账户退出
                        relativeLayout {
                            backgroundResource = R.drawable.ffe3e3e3_bottom_line
                            this.withTrigger().click {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    logout()
                                }
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                imageView {
                                    imageResource = R.mipmap.ico_exit_nor
                                }.lparams(dip(19), dip(18))

                                textView {
                                    textResource = R.string.tl_loginout
                                    textSize = 13f
                                    textColor = Color.parseColor("#333333")
                                }.lparams(wrapContent, wrapContent) {
                                    leftMargin = dip(10)
                                }
                            }.lparams{
                                centerVertically()
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(dip(6), dip(11)) {
                                alignParentRight()
                                centerVertically()
                            }
                        }.lparams(matchParent, dip(62)) {
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        //关于我们
                        linearLayout {
                            gravity = Gravity.CENTER

                            this.withTrigger().click {
                                val intent = Intent(context, Us::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(
                                    R.anim.right_in,
                                    R.anim.left_out
                                )
                            }

                            imageView {
                                imageResource = R.mipmap.ico_aboutus_nor
                            }.lparams(dip(19), dip(18))

                            textView {
                                textResource = R.string.tl_about_us
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(wrapContent, matchParent) {
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(dip(6), dip(11)) {
                            }
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(16)
                            bottomMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }
                    }.lparams(matchParent)
                }
            }
        }.view
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            initData()
        }
        return view
    }

    private suspend fun logout() {
        try {
            val retrofitUils =
                RetrofitUtils(activityInstance, this.getString(R.string.developmentUrl))
            val it = retrofitUils.create(LoginApi::class.java)
                .logout()
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if(it.code() in 200..299){
                //清除token
                val mPerferences: SharedPreferences =
                    androidx.preference.PreferenceManager.getDefaultSharedPreferences(mContext)
                val mEditor = mPerferences.edit()
                mEditor.clear()

                activity!!.startActivity<LoginActivity>()
                activity!!.finish()
                activity!!.overridePendingTransition(R.anim.left_in, R.anim.right_out)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun initData() {
        try {
            val retrofitUils =
                RetrofitUtils(activityInstance, this.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .personInformation()
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val result = it.body()
                val testName = result?.get("userName").toString().trim().replace("\"", "")
                val logo = result?.get("logo")
                val vip = result?.get("vip")?.asBoolean
                val auditState = result?.get("auditState")?.asBoolean
                val before = result?.get("before").toString().trim().replace("\"", "")
                val making = result?.get("making").toString().trim().replace("\"", "")
                val finish = result?.get("finish").toString().trim().replace("\"", "")

                // 公司方
                if (auditState!!) {
                    stateImage.imageResource = R.mipmap.authen
                } else {
                    stateImage.imageResource = R.mipmap.not_certified
                }

                // 金额数据
                val account = result.get("account")?.asJsonArray
                val accountList = mutableListOf<JsonObject>()
                account?.forEach {
                    accountList.add(it as JsonObject)
                }
//                accountAdapter?.resetItems(accountList)
                userAccount.adapter = PersonAccountAdapter(mContext!!,accountList)

                println(account)
                personName.text = testName
                beforeNumber.text = before
                makingNumber.text = making
                finishNumber.text = finish
                if (!logo?.isJsonNull!!) {
                    val headLogo = logo.toString().trim().replace("\"", "")
                    Glide.with(this)
                        .asBitmap()
                        .load(headLogo)
                        .placeholder(R.mipmap.default_avatar)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(headImage)
                }

                if (vip!!) {
                    vipImage.imageResource = R.mipmap.ico_certification_nor
                } else {
                    vipImage.imageResource = R.mipmap.ico_unauthorized_nor
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}