package app.findbest.vip.painter.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.painter.api.PainterApi
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
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class PainterSearchList : Fragment(){

    interface ClickBack{
        fun clickback()
    }
    companion object {
        fun newInstance(context: Context,clickback: ClickBack): PainterSearchList {
            val fragment = PainterSearchList()
            fragment.mContext = context
            fragment.clickback = clickback
            return fragment
        }
    }

    lateinit var clickback: ClickBack
    lateinit var mContext: Context
    lateinit var smart: SmartRefreshLayout
    private lateinit var listFram: FrameLayout
    private var listFragment: PainterListFragment? = null
    private var nullData: NullDataPageFragment? = null
    var nowPage = 0
    val mainId = 1
    private var searchText = ""
    private val nullId = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    fun setText(s: String){
        searchText = s

        listFragment = PainterListFragment.newInstance(mContext)
        childFragmentManager.beginTransaction().replace(nullId, listFragment!!).commit()
        smart.autoRefresh()
    }

    private fun createV(): View {
        return UI {
            linearLayout {
                backgroundColor = Color.parseColor("#FFF6F6F6")
                smart = smartRefreshLayout {
                    setEnableAutoLoadMore(false)
                    setRefreshHeader(MaterialHeader(activity))
                    setOnRefreshListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getProjectList()
                            it.finishRefresh(1000)
                        }
                    }
                    setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))
                    setOnLoadMoreListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getProjectList(nowPage + 1)
                            it.finishLoadMore(1000)
                        }
                    }
                    listFram = frameLayout {
                        id = nullId
                        setOnScrollChangeListener { _, _, _, _, _ ->
                            clickback.clickback()
                        }
                    }
                    val listFramlp = listFram.layoutParams
                    listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                    listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                    setOnScrollChangeListener { _, _, _, _, _ ->
                        clickback.clickback()
                    }
                }.lparams(matchParent, matchParent) {
                    setMargins(dip(10), 0, dip(10), 0)
                }
            }
        }.view
    }

    private suspend fun getProjectList() {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(PainterApi::class.java)
                .getPainterListBySearch(3, 5,1,1, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val array = it.body()!!.data
                if(array.size()>0){
                    val mutableList = mutableListOf<JsonObject>()
                    nowPage = 1
                    array.forEach {
                        mutableList.add(it.asJsonObject)
                    }
                    listFragment?.resetItems(mutableList)
                }else{
                    if(nullData==null){
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().replace(nullId,nullData!!).commit()
                    }
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun getProjectList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(PainterApi::class.java)
                .getPainterListBySearch(3, 5,page,1, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val array = it.body()!!.data
                if (array.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                nowPage = page
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                listFragment?.addItems(mutableList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}