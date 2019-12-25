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
class PainterPersonList : Fragment(), PainterMainListAdapter.ImageClick, BigImage2.ImageClick {
    // 点击关闭图片
    override fun clickclose() {
        closeAlertDialog()
    }
    // 点击放大图片
    override fun click(str: String) {
        openDialog(str)
    }

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
    var mWeight = 1
    private var bigImage: BigImage2? = null
    val mainId = 1
    var mCategory = 0
    var mStyle = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mPerferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        mWeight += mPerferences.getInt("painterSort",0)
        return createV()
    }

    private fun createV(): View {
        painterMain = PainterMainListAdapter(mContext,this@PainterPersonList)
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

            val it = when(mWeight) {
                1 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,1,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,1)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                2 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,2,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,2)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                3 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,3,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,3)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                4 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,4,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,4)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                else -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,1,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,1,1)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
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

            val it = when(mWeight) {
                1 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,1,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,1)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                2 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,2,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,2)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                3 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,3,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,3)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                4 -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,4,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,4)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
                }
                else -> {
                    if (mStyle != 0 && mCategory != 0) {
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,1,mCategory,mStyle)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }else{
                        retrofitUils.create(PainterApi::class.java)
                            .getPainterList(1,5,page,1)//个人画师传1
                            .subscribeOn(Schedulers.io())
                            .awaitSingle()
                    }
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


    fun setCategoryList(category: Int, style: Int) {
        mCategory = category
        mStyle = style
        smart.autoRefresh()
    }

    fun setSortList(index: Int) {
        mWeight += index
        smart.autoRefresh()
    }


    private fun openDialog(pic: String) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@PainterPersonList)
        mTransaction.add(mainId, bigImage!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (bigImage != null) {
            mTransaction.setCustomAnimations(R.anim.fade_in_out, R.anim.fade_in_out)

            mTransaction.remove(bigImage!!)
            bigImage = null
        }

        mTransaction.commit()
    }
}