package app.findbest.vip.painter.view

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.painter.api.PainterApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import retrofit2.HttpException
import app.findbest.vip.commonfrgmant.BigImage2
import app.findbest.vip.instance.view.InvitationActivity
import app.findbest.vip.painter.model.PainterInfo
import app.findbest.vip.utils.*
import org.jetbrains.anko.support.v4.nestedScrollView


class PainterInfomation : BaseActivity(), PainterInfoPictureAdapter.ImageClick, BigImage2.ImageClick{

    private lateinit var headImage: ImageView
    private lateinit var name: TextView
    private lateinit var starts: LinearLayout
    private lateinit var styles: LinearLayout
    private lateinit var country: ImageView
    private lateinit var commit: TextView
    private lateinit var smart: SmartRefreshLayout
    private lateinit var recycler: RecyclerView
    private var painterInfoPic: PainterInfoPictureAdapter? = null
    private var bigImage: BigImage2? = null
    val mainId = 1
    var nowPage = 0
    private var systemCountry = "" 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@PainterInfomation)
        systemCountry = mPerferences.getString("systemCountry", "").toString()
        val role =  mPerferences.getString("role", "").toString()

        val userId = intent.getStringExtra("userId") ?: ""

        frameLayout {
            id = mainId
            verticalLayout {
                backgroundColor = Color.WHITE
                linearLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        toolbar {
                            navigationIconResource = R.mipmap.nav_ico_return
                            setOnClickListener {
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams(dip(10), dip(17.5f))
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(30), dip(25)) {
                        gravity = Gravity.BOTTOM
                        leftMargin = dip(15)
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent, dip(65))
                linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    headImage = imageView {}.lparams(dip(60), dip(60))
                    verticalLayout {
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            name = appCompatTextView {
                                textColor = Color.parseColor("#FF444444")
                                typeface = Typeface.DEFAULT_BOLD
                                maxLines = 1
                                setAutoSizeTextTypeUniformWithConfiguration(
                                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                                    dip(17),
                                    1,
                                    0
                                )
                            }.lparams(dip(0), matchParent){
                                weight = 1f
                            }
                            starts = linearLayout {
                            }.lparams(dip(72), dip(8)) {
                                rightMargin = dip(15)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent, dip(25))
                        relativeLayout {
                            styles = linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                            }.lparams(wrapContent, matchParent)
                            country = imageView {
                            }.lparams(dip(30), dip(20)) {
                                alignParentRight()
                                rightMargin = dip(15)
                            }
                        }.lparams(matchParent, dip(20)) {
                            topMargin = dip(8)
                        }
                    }.lparams(wrapContent, dip(60)) {
                        weight = 1f
                        leftMargin = dip(10)
                    }
                }.lparams(matchParent, dip(100)) {
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
//                nestedScrollView {
//                }
                verticalLayout {
                    verticalLayout {
                        backgroundResource = R.drawable.ffe4e4e4_top_line
                        commit = textView {
                            textColor = Color.parseColor("#FF333333")
                            textSize = 15f
                        }.lparams(matchParent, dip(40)) {
                            setMargins(0, dip(20), dip(5), dip(20))
                        }
//                    linearLayout {
//                        val text = textView {
//                            text = resources.getString(R.string.srceen_more)
//                            textSize = 12f
//                            textColor = Color.parseColor("#FF555555")
//                        }.lparams {
//                            setMargins(dip(10), dip(7), 0, dip(7))
//                        }
//                        setOnClickListener {
//                            if(text.text == resources.getString(R.string.srceen_more)){
//                                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//                                commit.layoutParams = lp
//                                text.text = "收起"
//                            }else{
//                                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip(40))
//                                commit.layoutParams = lp
//                                text.text = resources.getString(R.string.srceen_more)
//                            }
//                        }
//                    }.lparams(wrapContent, dip(30)) {
//                        topMargin = dip(10)
//                        gravity = Gravity.CENTER_HORIZONTAL
//                    }
                    }.lparams(matchParent, wrapContent) {
                        leftMargin = dip(15)
                        rightMargin = dip(15)
                    }
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                    }.lparams(matchParent, dip(5))
                    smart = smartRefreshLayout {
                        setEnableAutoLoadMore(false)
                        setRefreshFooter(
                            BallPulseFooter(this@PainterInfomation).setSpinnerStyle(
                                SpinnerStyle.Scale
                            )
                        )
                        setOnLoadMoreListener {
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                getInfo(userId, nowPage + 1)
                                it.finishLoadMore(1000)
                            }
                        }
                        recycler = recyclerView {
                            layoutManager = LinearLayoutManager(this@PainterInfomation)
                            painterInfoPic = PainterInfoPictureAdapter(this@PainterInfomation,
                                arrayListOf(),this@PainterInfomation)
                            adapter = painterInfoPic
                        }
                        val lp = recycler.layoutParams
                        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
                        lp.height = LinearLayout.LayoutParams.MATCH_PARENT
                    }.lparams(matchParent, dip(0)){
                        weight = 1f
                    }
                }.lparams(matchParent, dip(0)){
                    weight = 1f
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
                        val intent = Intent(this@PainterInfomation, InvitationActivity::class.java)
                        //跳转详情
                        //画师/团队的id
                        intent.putExtra("id", userId)
                        startActivity(intent)
                        overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    }
                }.lparams(matchParent,dip(50))
            }
        }
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getInfo(userId)
        }
    }

    override fun click(str: String) {
        openDialog(str)
    }

    override fun clickclose() {
        closeAlertDialog()
    }

    private suspend fun getInfo(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(this@PainterInfomation, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(PainterApi::class.java)
                .getPainterInfo(id, systemCountry, 1)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!
                nowPage = 1
                setInfo(model)
                val imageList = arrayListOf<String>()
                model.works.data.forEach {
                    imageList.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.resetData(arrayListOf(imageList))
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun getInfo(id: String,page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(this@PainterInfomation, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(PainterApi::class.java)
                .getPainterInfo(id, systemCountry, page)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!
                nowPage = page
                val imageList = arrayListOf<String>()
                model.works.data.forEach {
                    imageList.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.addData(arrayListOf(imageList))
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private fun setInfo(model: PainterInfo){

        if (model.logo != null) {
            Glide.with(this@PainterInfomation)
                .load(model.logo)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .placeholder(R.mipmap.default_avatar)
                .into(headImage)
        }else{
            Glide.with(this@PainterInfomation)
                .load(R.mipmap.default_avatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(headImage)
        }

        name.text = model.userName

        starts.removeAllViews()
        for (index in 0..4) {
            val view = UI {
                linearLayout {
                    imageView {
                        imageResource = if (index < model.star.toInt()) {
                            R.mipmap.ico_star_select
                        } else {
                            R.mipmap.ico_star_no
                        }
                    }.lparams(dip(8), dip(8)) {
                        if (index != 0) {
                            leftMargin = dip(8)
                        }
                    }
                }
            }.view
            starts.addView(view)
        }

        styles.removeAllViews()
        for (index in 0 until model.styles.size) {
            val view = UI {
                relativeLayout {
                    relativeLayout {
                        backgroundColor = Color.parseColor("#FFF7F7F7")
                        textView {
                            text = model.styles[index]
                            textSize = 11f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            setMargins(dip(7),dip(2.5f),dip(7),dip(2.5f))
                        }
                    }.lparams(wrapContent, wrapContent) {
                        if (index > 0)
                            leftMargin = dip(10)
                    }
                }
            }.view
            styles.addView(view)
        }

        when (model.country) {
            "86" -> country.imageResource = R.mipmap.image_china
            "81" -> country.imageResource = R.mipmap.image_japan
            "82" -> country.imageResource = R.mipmap.image_korea
        }

        commit.text = if(model.introduction!=null){
           if(model.introduction=="")
               resources.getString(R.string.common_not_somethings)
           else
               model.introduction
        }else{
            resources.getString(R.string.common_not_somethings)
        }

    }

    private fun openDialog(pic: String) {
        val mTransaction = supportFragmentManager.beginTransaction()

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@PainterInfomation, true)
        mTransaction.add(mainId, bigImage!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = supportFragmentManager.beginTransaction()
        if (bigImage != null) {
//            mTransaction.setCustomAnimations(R.anim.fade_in_out, R.anim.fade_in_out)

            mTransaction.remove(bigImage!!)
            bigImage = null
        }

        mTransaction.commit()
    }
}