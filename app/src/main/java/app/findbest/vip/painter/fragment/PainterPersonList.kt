package app.findbest.vip.painter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.painter.adapter.PainterMainListAdapter
import app.findbest.vip.painter.api.PainterApi
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
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
import retrofit2.HttpException

@Suppress("IMPLICIT_CAST_TO_ANY")
class PainterPersonList : Fragment() {

    companion object {
        fun newInstance(context: Context): PainterPersonList {
            val fragment = PainterPersonList()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var recycle: RecyclerView? = null
    lateinit var painterMain: PainterMainListAdapter
    lateinit var smart: SmartRefreshLayout
    var nowPage = 0
    var mWeight = 0
    var mStar = 0
    var mAmountCompleted = 0
    var mTimeOfEntity = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mPerferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        when(mPerferences.getInt("painterSort",0)){
            0 -> {
                mWeight = 1
            }
            1 -> {
                mStar = 1
            }
            2 -> {
                mAmountCompleted = 1
            }
            3 -> {
                mTimeOfEntity = 1
            }
        }
        return createV()
    }

    private fun createV(): View {
        painterMain = PainterMainListAdapter(this@PainterPersonList.context!!)
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
                    recycle = recyclerView {
                        layoutManager = LinearLayoutManager(mContext)
                        adapter = painterMain
                    }
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

            val it = when {
                mWeight==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,1,null,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mStar==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,null,1,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mAmountCompleted==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,null,null,1,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mTimeOfEntity==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,null,null,null,1)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,1,null,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                nowPage = 1
                painterMain.resetItems(mutableList)
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

            val it = when {
                mWeight==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,page,1,null,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mStar==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,page,null,1,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mAmountCompleted==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,page,null,null,1,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                mTimeOfEntity==1 -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,page,null,null,null,1)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
                else -> {
                    retrofitUils.create(PainterApi::class.java)
                        .getPainterList(1,5,page,1,null,null,null)//个人画师传1
                        .subscribeOn(Schedulers.io())
                        .awaitSingle()
                }
            }
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                nowPage = page
                painterMain.addItems(mutableList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    fun setSortList(index: Int) {
        when(index){
            0 -> {
                mWeight = 1
                mStar = 0
                mAmountCompleted = 0
                mTimeOfEntity = 0
            }
            1 -> {
                mWeight = 0
                mStar = 1
                mAmountCompleted = 0
                mTimeOfEntity = 0
            }
            2 -> {
                mWeight = 0
                mStar = 0
                mAmountCompleted = 1
                mTimeOfEntity = 0
            }
            3 -> {
                mWeight = 0
                mStar = 0
                mAmountCompleted = 0
                mTimeOfEntity = 1
            }
        }
        smart.autoRefresh()
    }
}