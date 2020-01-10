package app.findbest.vip.painter.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.painter.api.PainterApi
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
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
import app.findbest.vip.painter.fragment.BigImage2
import app.findbest.vip.painter.model.PainterInfo


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

        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@GuideView)
        systemCountry = mPerferences.getString("systemCountry", "")
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
                        relativeLayout {
                            name = textView {
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                                typeface = Typeface.DEFAULT_BOLD
                            }
                            starts = linearLayout {
                            }.lparams(wrapContent, dip(8)) {
                                alignParentRight()
                                centerVertically()
                                rightMargin = dip(15)
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
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_top_line
                    commit = textView {
                        textColor = Color.parseColor("#FF333333")
                        textSize = 15f
                    }.lparams(matchParent, wrapContent) {
                        setMargins(0, dip(20), dip(5), dip(20))
                    }
                }.lparams(matchParent, wrapContent) {
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                linearLayout {
                    backgroundColor = Color.parseColor("#FFF6F6F6")
                }.lparams(matchParent, dip(5))
                smart = smartRefreshLayout {
                    setEnableAutoLoadMore(false)
                    setRefreshHeader(MaterialHeader(this@PainterInfomation))
                    setOnRefreshListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getInfo(userId)
                            it.finishRefresh(1000)
                        }
                    }
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
                }.lparams(matchParent, wrapContent)
            }
        }
        smart.autoRefresh()
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
                .into(headImage)
        }

        name.text = model.userName

        starts.removeAllViews()
        for (index in 0..4) {
            val view = UI {
                linearLayout {
                    imageView {
                        imageResource = if (index < model.star) {
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

        commit.text = if(model.introduction!="") "无" else model.introduction

    }

    private fun openDialog(pic: String) {
        val mTransaction = supportFragmentManager.beginTransaction()

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@PainterInfomation)
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