package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.view.InstanceActivity
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
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
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class InstanceSearchList : Fragment(), PainterInfoPictureAdapter.ImageClick {

    interface ClickBack{
        fun clickback()
    }

    companion object {
        fun newInstance(context: Context, clickback: ClickBack): InstanceSearchList {
            val fragment = InstanceSearchList()
            fragment.mContext = context
            fragment.clickback = clickback
            return fragment
        }
    }

    lateinit var clickback: ClickBack
    lateinit var mContext: Context
    lateinit var smart: SmartRefreshLayout
    lateinit var recycle: RecyclerView
    lateinit var projectMain: ProjectMainListAdapter
    private var painterInfoPic: PainterInfoPictureAdapter? = null
    var imageList = mutableListOf<JsonObject>()
    var nowPage = 0
    var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun click(str: String) {
        imageList.forEach {
            if(str == it["url"].asString){
                val intent = Intent(mContext, InstanceActivity::class.java)
                //跳转详情
                intent.putExtra("url", if(!it["url"].isJsonNull) it["url"].asString else "")
                intent.putExtra("logo", if(!it["logo"].isJsonNull) it["logo"].asString else "")
                intent.putExtra("name", if(!it["name"].isJsonNull) it["name"].asString else "")
                intent.putExtra("id", if(!it["id"].isJsonNull) it["id"].asString else "")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        }
    }

    fun setText(s: String){
        searchText = s
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
                        painterInfoPic = PainterInfoPictureAdapter(
                            mContext!!,
                            arrayListOf(),this@InstanceSearchList)
                        adapter = painterInfoPic

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
            val it = retrofitUils.create(InstanceApi::class.java)
                .instanceListSearch(1, 5, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()


            if (it.code() in 200..299) {
                //完善信息成功
                val item = it.body()!!.data
                val list1 = arrayListOf<String>()
                imageList.clear()
                item.forEach {
                    imageList.add(it.asJsonObject)
                    list1.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.resetData(arrayListOf(list1))
                nowPage = 1
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
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectListBySearch(page, 5, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val item = it.body()!!.data
                val list1 = arrayListOf<String>()
                item.forEach {
                    imageList.add(it.asJsonObject)
                    list1.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.addData(arrayListOf(list1))
                nowPage = page
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

}