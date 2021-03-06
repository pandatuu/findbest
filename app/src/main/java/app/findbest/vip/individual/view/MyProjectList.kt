package app.findbest.vip.individual.view

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.individual.fragment.MyProjectListFragment
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.smartRefreshLayout
import com.google.gson.JsonObject
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
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class MyProjectList : BaseActivity(){

    private lateinit var smart: SmartRefreshLayout
    private var listFragment: MyProjectListFragment? = null
    private var nullData: NullDataPageFragment? = null
    private lateinit var status: TextView
    private lateinit var listFram: FrameLayout
    private var popup: PopupWindow? = null

    var nowPage = 0
    val mainId = 1
    private var isPainter = false
    private var screenStatus = 3
    val nullId = 4
    //弹窗是否可以点击
    private var isDialogClick = false
    private var systemCountry = "" 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MyProjectList)
        systemCountry = mPerferences.getString("systemCountry", "").toString()

        val statusText = intent.getStringExtra("status") ?: ""
        screenStatus = when(statusText){
            "beforeNumber" -> 0
            "makingNumber" -> 1
            "finishNumber" -> 2
            else -> 3
        }

        val role = intent.getStringExtra("role")
        isPainter = role != "consumer"

        frameLayout {
            id = mainId
            verticalLayout {
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        gravity = Gravity.CENTER
                        toolbar {
                            navigationIconResource = R.mipmap.icon_back
                            setOnClickListener {
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams(dip(10), dip(18))
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(30), dip(25)) {
                        alignParentBottom()
                        alignParentLeft()
                        leftMargin = dip(15)
                        bottomMargin = dip(10)
                    }
                    textView {
                        text = resources.getString(R.string.my_project_list)
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams {
                        centerHorizontally()
                        alignParentBottom()
                        bottomMargin = dip(10)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.BOTTOM
                        status = textView {
                            gravity = Gravity.BOTTOM
                            text = when(statusText){
                                "beforeNumber" -> resources.getString(R.string.project_status_release_stage)
                                "makingNumber" -> resources.getString(R.string.project_status_produce_stage)
                                "finishNumber" -> resources.getString(R.string.project_status_transaction_done)
                                else -> resources.getString(R.string.srceen_all)
                            }
                            textSize = 15f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                        imageView {
                            imageResource = R.mipmap.ico_pack_up_arrow_nor
                        }.lparams {
                            leftMargin = dip(8)
                            gravity = Gravity.CENTER
                        }
                        setOnClickListener {
                            if(!isDialogClick){
                                return@setOnClickListener
                            }
                            val la = View.inflate(this@MyProjectList, R.layout.screen_status, null)
                            val first = la.findViewById<LinearLayout>(R.id.firstNum)
                            val second = la.findViewById<LinearLayout>(R.id.secondNum)
                            val third = la.findViewById<LinearLayout>(R.id.thirdNum)
                            val forth = la.findViewById<LinearLayout>(R.id.forthNum)
                            if(popup==null){
                                popup = PopupWindow(la, wrapContent, wrapContent)
                                popup?.isTouchable = true
                                popup?.showAsDropDown(it)
                                popup?.isFocusable = true
                            }else{
                                popup?.dismiss()
                                popup = null
                            }

                            first.setOnClickListener {
                                if(popup!=null){
                                    status.text = resources.getString(R.string.srceen_all)
                                    screenStatus = 3
                                    popup?.dismiss()
                                    popup = null
                                    smart.autoRefresh()
                                }
                            }
                            second.setOnClickListener {
                                if(popup!=null){
                                    status.text = resources.getString(R.string.project_status_release_stage)
                                    screenStatus = 0
                                    popup?.dismiss()
                                    popup = null
                                    smart.autoRefresh()
                                }
                            }
                            third.setOnClickListener {
                                if(popup!=null){
                                    status.text = resources.getString(R.string.project_status_produce_stage)
                                    screenStatus = 1
                                    popup?.dismiss()
                                    popup = null
                                    smart.autoRefresh()
                                }
                            }
                            forth.setOnClickListener {
                                if(popup!=null){
                                    status.text = resources.getString(R.string.project_status_transaction_done)
                                    screenStatus = 2
                                    popup?.dismiss()
                                    popup = null
                                    smart.autoRefresh()
                                }
                            }
                        }
                    }.lparams {
                        rightMargin = dip(15)
                        alignParentBottom()
                        alignParentRight()
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent, dip(65))
                linearLayout {
                    smart = smartRefreshLayout {
                        setEnableAutoLoadMore(false)
                        setRefreshHeader(MaterialHeader(this@MyProjectList))
                        setOnRefreshListener {
                            isDialogClick = false
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                if (isPainter)
                                    getPainterSideList()
                                else
                                    getProjectSideList()
                                it.finishRefresh(1000)
                                isDialogClick = true
                            }
                        }
                        setRefreshFooter(
                            BallPulseFooter(this@MyProjectList).setSpinnerStyle(
                                SpinnerStyle.Scale
                            )
                        )
                        setOnLoadMoreListener {
                            isDialogClick = false
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                if (isPainter)
                                    getPainterSideList(nowPage + 1)
                                else
                                    getProjectSideList(nowPage + 1)
                                it.finishLoadMore(1000)
                                isDialogClick = true
                            }
                        }
                        listFram = frameLayout {
                            id = nullId
                            listFragment = MyProjectListFragment.newInstance(this@MyProjectList,isPainter)
                            supportFragmentManager.beginTransaction().add(nullId,listFragment!!).commit()
                        }
                        val listFramlp = listFram.layoutParams
                        listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                        listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                    }.lparams(matchParent, matchParent) {
                        setMargins(dip(10), 0, dip(10), 0)
                    }
                }.lparams(matchParent, matchParent)
            }
        }
        smart.autoRefresh()
    }

    //画师方接口
    private suspend fun getPainterSideList() {
        try {
            val retrofitUils =
                RetrofitUtils(this@MyProjectList, resources.getString(R.string.developmentUrl))
            val it = when (screenStatus) {
                0 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                1 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                2 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(1, systemCountry, null)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                if(model.size() > 0){
                    if(nullData!=null){
                        supportFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    nowPage = 1
                    val list = mutableListOf<JsonObject>()
                    model.forEach {
                        list.add(it.asJsonObject)
                    }
                    listFragment?.resetItems(list)
                }else{
                    if(nullData == null){
                        nullData = NullDataPageFragment.newInstance()
                        supportFragmentManager.beginTransaction().add(nullId,nullData!!).commit()
                    }
                }
            }
            if(it.code() == 403){
                println("forbidden")
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }
    private suspend fun getPainterSideList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(this@MyProjectList, resources.getString(R.string.developmentUrl))
            val it = when (screenStatus) {
                0 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                1 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                2 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getPainterSideList(page, systemCountry, null)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                if (model.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                if(nullData!=null){
                    supportFragmentManager.beginTransaction().remove(nullData!!).commit()
                    nullData = null
                }
                nowPage = page

                val list = mutableListOf<JsonObject>()
                model.forEach {
                    list.add(it.asJsonObject)
                }
                listFragment?.addItems(list)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }

    //项目方接口
    private suspend fun getProjectSideList() {
        try {
            val retrofitUils =
                RetrofitUtils(this@MyProjectList, resources.getString(R.string.developmentUrl))
            val it = when (screenStatus) {
                0 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                1 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                2 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(1, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(1, systemCountry, null)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                if(model.size() > 0){
                    if(nullData!=null){
                        supportFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    nowPage = 1
                    val list = mutableListOf<JsonObject>()
                    model.forEach {
                        list.add(it.asJsonObject)
                    }
                    listFragment?.resetItems(list)
                }else{
                    if(nullData == null){
                        nullData = NullDataPageFragment.newInstance()
                        supportFragmentManager.beginTransaction().add(nullId,nullData!!).commit()
                    }
                }
            }
            if(it.code() == 403){
                println("forbidden")
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }
    private suspend fun getProjectSideList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(this@MyProjectList, resources.getString(R.string.developmentUrl))
            val it = when (screenStatus) {
                0 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                1 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                2 -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(page, systemCountry, screenStatus)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(IndividualApi::class.java)
                        .getProjectSideList(page, systemCountry, null)
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                if (model.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                if(nullData!=null){
                    supportFragmentManager.beginTransaction().remove(nullData!!).commit()
                    nullData = null
                }
                nowPage = page
                val list = mutableListOf<JsonObject>()
                model.forEach {
                    list.add(it.asJsonObject)
                }
                listFragment?.addItems(list)

            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }
}