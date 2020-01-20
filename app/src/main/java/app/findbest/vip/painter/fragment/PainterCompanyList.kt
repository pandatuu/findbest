package app.findbest.vip.painter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BigImage2
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

class PainterCompanyList : Fragment() {

    companion object {
        fun newInstance(context: Context): PainterCompanyList {
            val fragment = PainterCompanyList()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var bigImage: BigImage2? = null
    lateinit var smart: SmartRefreshLayout
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: PainterListFragment
    private var nullData: NullDataPageFragment? = null
    var nowPage = 0
    var mWeight = 1
    var mCategory = 0
    var mStyle = 0
    private val nullId = 4


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mPerferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        mWeight += mPerferences.getInt("painterSort", 0)
        return createV()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                smart = smartRefreshLayout {
                    setEnableAutoLoadMore(false)
                    setRefreshHeader(MaterialHeader(activity))
                    setOnRefreshListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getPainters()
                            it.finishRefresh(1000)
                        }
                    }
                    setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))
                    setOnLoadMoreListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getPainters(nowPage + 1)
                            it.finishLoadMore(1000)
                        }
                    }
                    listFram = frameLayout {
                        id = nullId
                        listFragment = PainterListFragment.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(nullId, listFragment).commit()
                    }
                    val listFramlp = listFram.layoutParams
                    listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                    listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(10)
                }
            }
        }.view
        smart.autoRefresh()
        return view
    }

    private suspend fun getPainters() {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))

            val it = if (mStyle != 0 && mCategory != 0) {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, 1, mWeight, mCategory, mStyle)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else if (mStyle == 0 && mCategory != 0) {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, 1, mWeight, mCategory, null)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, 1, mWeight)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val array = it.body()!!.data
                if (array.size() > 0) {
                    if (nullData != null) {
                        childFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    val mutableList = mutableListOf<JsonObject>()
                    array.forEach {
                        mutableList.add(it.asJsonObject)
                    }
                    nowPage = 1
                    listFragment.resetItems(mutableList)
                } else {
                    if (nullData == null) {
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().add(nullId, nullData!!).commit()
                    }
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun getPainters(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))

            val it = if (mStyle != 0 && mCategory != 0) {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, page, mWeight, mCategory, mStyle)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else if (mStyle == 0 && mCategory != 0) {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, page, mWeight, mCategory, null)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(PainterApi::class.java)
                    .getCompanyPainterList(2, 5, page, mWeight)//个人画师传1
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                if (array.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                if (nullData != null) {
                    childFragmentManager.beginTransaction().remove(nullData!!).commit()
                    nullData = null
                }
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                nowPage = page
                listFragment.addItems(mutableList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    fun setCategoryList(category: Int, style: Int) {
        mCategory = category
        mStyle = style
        smart.autoRefresh()
    }

    fun setSortList(index: Int) {
        mWeight = index
        smart.autoRefresh()
    }
}