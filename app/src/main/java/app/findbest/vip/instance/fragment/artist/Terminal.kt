package app.findbest.vip.instance.fragment.artist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.individual.api.individual
import app.findbest.vip.individual.view.*
import app.findbest.vip.utils.RetrofitUtils
import click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import retrofit2.HttpException
import withTrigger


class Terminal:FragmentParent() {

    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context
    lateinit var personName:TextView
    lateinit var beforeNumber:TextView
    lateinit var makingNumber:TextView
    lateinit var finishNumber:TextView
    lateinit var headImage:ImageView
    lateinit var vipImage:ImageView
    lateinit var stateImage:ImageView
    lateinit var sharedPreferences: SharedPreferences
    var role = ""

    companion object {
        fun newInstance(context: Context): Terminal {
            val f = Terminal()
            f.activityInstance = context
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        role = sharedPreferences.getString("role","").toString()
       return createView()
    }

    private fun createView():View {
        val view = UI {
            verticalLayout {
                verticalLayout {
                    backgroundResource = R.mipmap.image_message_png
                    relativeLayout {
                        toolbar1 = toolbar {
                            isEnabled = true
                            title = ""
                        }.lparams{
                            width = matchParent
                            alignParentBottom()
                        }
//
                        textView {
                            textResource= R.string.tl_title
                            gravity = Gravity.CENTER
                            textSize =  17f
                            textColor = Color.WHITE
                        }.lparams {
                            width = matchParent
                            height =dip(54-getStatusBarHeight(this@Terminal.context!!))
                            alignParentBottom()
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL

                            onClick {
                                toast("分享")
                            }
                            imageView {
                                imageResource = R.mipmap.ico_share_nor
                            }.lparams(width = dip(16),height = dip(16)){
                                rightMargin = dip(7)
                            }
                            textView {
                                textResource = R.string.tl_share
                                textSize = 15f
                                textColor = Color.WHITE
                            }.lparams(width = dip(31),height = dip(21))
                        }.lparams{
                            height = dip(54 - getStatusBarHeight(this@Terminal.context!!))
                            rightMargin = dip(17)
                            alignParentRight()
                            alignParentBottom()
                        }
                    }.lparams{
                        width = matchParent
                        height = dip(54)
                    }

                    verticalLayout {
                       linearLayout {
                           verticalLayout {
                               personName = textView {
                                   text = "Amy Gonzalez"
                                   textSize = 30f
                                   textColor = Color.WHITE
                               }.lparams(width = dip(209),height = dip(42)){
                                   bottomMargin = dip(3)
                                   leftMargin = dip(2)
                               }

                               linearLayout {
                                   gravity = Gravity.CENTER_VERTICAL
                                   vipImage = imageView {

                                   }.lparams(width = dip(26),height = dip(26)){
                                       rightMargin = dip(3)
                                   }

                                   stateImage = imageView {

                                   }.lparams(width = dip(65),height = dip(21))
                               }
                           }.lparams(width = wrapContent,height = wrapContent){
                               weight = 1f
                               leftMargin = dip(22)
                           }

                           headImage = imageView {
                               imageResource = R.mipmap.default_avatar

                               this.withTrigger().click {
                                   var intent = Intent(context, Head::class.java)
                                   startActivity(intent)
                                   activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                               }
                           }.lparams(width = dip(64),height = dip(64)){

                               rightMargin = dip(20)
                           }
                       }.lparams(width = matchParent,height = wrapContent){
                           weight = 1f
                           topMargin = dip(17)
                       }

                        linearLayout {
                            verticalLayout {
                                beforeNumber = textView {
                                    text = "0 "
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_application_items
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }
                            verticalLayout {
                                makingNumber = textView {
                                    text = "0"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_production_project
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            verticalLayout {
                                finishNumber = textView {
                                    text = "0"
                                    textSize = 25f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }

                                textView {
                                    textResource = R.string.tl_transaction_complete
                                    textSize = 12f
                                    textColor = Color.WHITE
                                }.lparams(){
                                    gravity = Gravity.CENTER
                                }
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            verticalLayout {

                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            bottomMargin = dip(20)
                        }
                    }.lparams(width = matchParent,height = matchParent){
                        weight = 1f

                    }




                }.lparams(width = matchParent,height = dip(229))

                scrollView {
                    verticalLayout {
                        backgroundColor = Color.WHITE
                        linearLayout{
                            gravity = Gravity.CENTER
                            linearLayout {
                                backgroundColor = Color.parseColor("#F87F2D")
                            }.lparams(width = dip(4),height = dip(18)){
                                leftMargin = dip(4)
                                rightMargin = dip(9)
                            }

                            textView {
                                textResource = R.string.tl_account_balance
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }
                        }.lparams(width = wrapContent,height = dip(28)){
                            leftMargin = dip(20)
                            topMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_rmb
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "2600.00"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = matchParent){
                            topMargin = dip(10)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_frozen_amount
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "600.00"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = matchParent){
                            topMargin = dip(11)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_jpy
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "40681"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(15)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            textView {
                                textResource = R.string.tl_won
                                textSize = 13f
                                textColor = Color.parseColor("#666666")
                            }.lparams(width = wrapContent,height = wrapContent){
                                weight = 1f
                            }

                            textView {
                                text = "432776"
                                textSize = 20f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = wrapContent){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(15)
                            leftMargin = dip(24)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(15)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_file_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_my_project
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                            setOnClickListener {
                                activity!!.startActivity<MyProjectList>("role" to role)
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(25)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

//                        linearLayout {
//                            gravity = Gravity.CENTER
//                            imageView {
//                                imageResource = R.mipmap.ico_update_nor
//                            }.lparams(width = dip(19),height = dip(18))
//
//                            textView {
//                                textResource = R.string.tl_version_update
//                                textSize = 13f
//                                textColor = Color.parseColor("#333333")
//                            }.lparams(width = wrapContent,height = matchParent){
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
//                            }.lparams(width = dip(6),height = dip(11)){
//                                leftMargin = dip(12)
//                            }
//                        }.lparams(width = matchParent,height = wrapContent){
//                            topMargin = dip(16)
//                            leftMargin = dip(20)
//                            rightMargin = dip(20)
//                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            this.withTrigger().click {
                                var intent = Intent(context, Help::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }

                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_help_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_use_help
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            this.withTrigger().click {
                                var intent = Intent(context, Feedback::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }
                            gravity = Gravity.CENTER
                            imageView {
                                imageResource = R.mipmap.ico_opinion_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_feedback
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }

                        view {
                            backgroundColor = Color.parseColor("#E3E3E3")
                        }.lparams(width = matchParent, height = dip(1)) {
                            topMargin = dip(16)
                        }

                        linearLayout {
                            gravity = Gravity.CENTER

                            this.withTrigger().click {
                                var intent = Intent(context, Us::class.java)
                                startActivity(intent)
                                activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            }

                            imageView {
                                imageResource = R.mipmap.ico_aboutus_nor
                            }.lparams(width = dip(19),height = dip(18))

                            textView {
                                textResource = R.string.tl_about_us
                                textSize = 13f
                                textColor = Color.parseColor("#333333")
                            }.lparams(width = wrapContent,height = matchParent){
                                leftMargin = dip(10)
                                rightMargin = dip(15)
                                weight = 1f
                            }

                            imageView {
                                imageResource = R.mipmap.btn_slect_nor
                            }.lparams(width = dip(6),height = dip(11)){
                            }
                        }.lparams(width = matchParent,height = wrapContent){
                            topMargin = dip(16)
                            bottomMargin = dip(16)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                        }


                    }.lparams(width = matchParent)
                }
            }
        }.view
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            initData()
        }
        return view
    }

    private suspend fun initData(){
        try {
            val retrofitUils = RetrofitUtils(activityInstance, this.getString(R.string.developmentUrl))
            val it = retrofitUils.create(individual::class.java)
                .personInformation()
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if(it.code() in 200..299) {
                val result = it.body()
                val testName = result?.get("userName").toString().trim().replace("\"","")
                val logo = result?.get("logo")
                val vip = result?.get("vip")?.asBoolean
                val auditState = result?.get("auditState")?.asBoolean
                val before= result?.get("before").toString().trim().replace("\"","")
                val making= result?.get("making").toString().trim().replace("\"","")
                val finish= result?.get("finish").toString().trim().replace("\"","")

                // 公司方
                if(auditState!!){
                    stateImage.imageResource =  R.mipmap.certified_enterprise
                }else{
                    stateImage.imageResource = R.mipmap.not_certified
                }
                if(auditState!!){
                    stateImage.imageResource =  R.mipmap.retist
                }else{
                    stateImage.imageResource = R.mipmap.not_certified
                }

                // 金额数据
                val account = result?.get("account")?.asJsonArray

                println(account)
                personName.text  = testName
                beforeNumber.text = before
                makingNumber.text = making
                finishNumber.text = finish
                if (logo?.isJsonNull!!){

                }else{
                    val headLogo = logo.toString().trim().replace("\"","")
                    Glide.with(this)
                        .asBitmap()
                        .load(headLogo)
                        .placeholder(R.mipmap.default_avatar)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(headImage)
                }

                if(vip!!){
                    vipImage.imageResource =  R.mipmap.ico_certification_nor
                }else{
                    vipImage.imageResource = R.mipmap.ico_unauthorized_nor
                }
            }
        }catch (throwable: Throwable){
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}