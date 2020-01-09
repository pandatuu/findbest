package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
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

class MainSearchList : Fragment(), ProjectMainListAdapter.ListAdapter {

    interface ClickBack {
        fun clickback()
    }

    companion object {
        fun newInstance(context: Context, clickback: ClickBack): MainSearchList {
            val fragment = MainSearchList()
            fragment.mContext = context
            fragment.clickback = clickback
            return fragment
        }
    }

    lateinit var clickback: ClickBack
    lateinit var mContext: Context
    lateinit var smart: SmartRefreshLayout
    lateinit var recycle: RecyclerView
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: MyProjectListFragment
    private var nullData: NullDataPageFragment? = null
    var nowPage = 0
    var searchText = ""
    private val nullId = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //选择recycle里的单个card
    override fun oneClick(id: String) {
        startActivity<ProjectInformation>("projectId" to id)
        activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    fun setText(s: String) {
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
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getProjectList()
                            it.finishRefresh(1000)
                        }
//                        list.setItems(a)
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
                        listFragment = MyProjectListFragment.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(nullId, listFragment).commit()
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
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectListBySearch(1, 5, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                //完善信息成功
                val list = it.body()!!.data
                println(list)
                if (list.size() > 0) {
                    if(nullData!=null){
                        childFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    nowPage = 1
                    val projectList = arrayListOf<ProjectListModel>()
                    list.forEach {
                        val model = it.asJsonObject
                        val projectListModel = ProjectListModel(
                            model["id"].asString,
                            model["name"].asString,
                            model["guaranteeType"].asInt == 6,
                            model["size"].asString,
                            model["format"].asString,
                            model["commitAt"].asLong,
                            model["consumer"].asJsonObject["country"].asString,
                            model["styles"].asJsonArray,
                            model["minBonus"].asFloat,
                            model["maxBonus"].asFloat
                        )
                        projectList.add(projectListModel)
                    }
                    listFragment.resetItems(projectList)
                }else{
                    nullData = NullDataPageFragment.newInstance()
                    childFragmentManager.beginTransaction().replace(nullId,nullData!!).commit()
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
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectListBySearch(page, 5, searchText)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                //完善信息成功
                val list = it.body()!!.data
                if (list.size() == 0) {
                    toast("没有数据啦...")
                    return
                }
                if(nullData!=null){
                    childFragmentManager.beginTransaction().remove(nullData!!).commit()
                    nullData = null
                }
                val projectList = arrayListOf<ProjectListModel>()
                list.forEach {
                    //                    val it = Gson().fromJson<ProjectListModel>(it, ProjectListModel::class.java)
                    val model = it.asJsonObject
                    val projectListModel = ProjectListModel(
                        model["id"].asString,
                        model["name"].asString,
                        model["guaranteeType"].asInt == 6,
                        model["size"].asString,
                        model["format"].asString,
                        model["commitAt"].asLong,
                        model["consumer"].asJsonObject["country"].asString,
                        model["styles"].asJsonArray,
                        model["minBonus"].asFloat,
                        model["maxBonus"].asFloat
                    )
                    projectList.add(projectListModel)
                }
                nowPage = page
                listFragment.addItems(projectList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

}