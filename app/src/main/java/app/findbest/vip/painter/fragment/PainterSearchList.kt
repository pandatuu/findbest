package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.painter.adapter.PainterMainListAdapter
import app.findbest.vip.painter.api.PainterApi
import app.findbest.vip.painter.fragment.BigImage2
import app.findbest.vip.painter.view.PainterInfomation
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

class PainterSearchList : Fragment(), PainterMainListAdapter.ImageClick, BigImage2.ImageClick {

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
    lateinit var recycle: RecyclerView
    lateinit var painterMain: PainterMainListAdapter
    var nowPage = 0
    val mainId = 1
    var searchText = ""
    private var bigImage: BigImage2? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun jumpToInfo(id: String) {
        activity!!.startActivity<PainterInfomation>("userId" to id)
        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }
    // 点击关闭图片
    override fun clickclose() {
        closeAlertDialog()
    }
    // 点击放大图片
    override fun click(str: String) {
        openDialog(str)
    }

    fun setText(s: String){
        searchText = s
        smart.autoRefresh()
    }

    private fun createV(): View {
        painterMain = PainterMainListAdapter(mContext, this@PainterSearchList)
        return UI {
            linearLayout {
                backgroundColor = Color.parseColor("#FFF6F6F6")
                smart = smartRefreshLayout {
                    setEnableAutoLoadMore(false)
                    setRefreshHeader(MaterialHeader(activity))
                    setOnRefreshListener {
                        toast("刷新....")
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getProjectList()
                            it.finishRefresh(1000)
                        }
//                        list.setItems(a)
                    }
                    setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))
                    setOnLoadMoreListener {
                        toast("刷新....")
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getProjectList(nowPage + 1)
                            it.finishLoadMore(1000)
                        }
                    }
                    recycle = recyclerView {
                        layoutManager = LinearLayoutManager(mContext)
                        adapter = painterMain
                        setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                            clickback.clickback()
                        }
                    }
                    setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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

    private fun openDialog(pic: String) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@PainterSearchList)
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