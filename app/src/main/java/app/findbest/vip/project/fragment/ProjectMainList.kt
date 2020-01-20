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
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.utils.RetrofitUtils
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
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class ProjectMainList : Fragment() {

    companion object {
        fun newInstance(context: Context): ProjectMainList {
            val fragment = ProjectMainList()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context
    lateinit var smart: SmartRefreshLayout
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: MyProjectListFragment
    private var nullData: NullDataPageFragment? = null
    var nowPage = 0
    private var mCategory = 0
    private var mStyle = 0
    private val nullId = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    fun setCategoryList(category: Int, style: Int) {
        mCategory = category
        mStyle = style
        smart.autoRefresh()
    }

    private fun createV(): View {
        val view = UI {
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
                        listFragment = MyProjectListFragment.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(nullId,listFragment).commit()
                    }
                    val listFramlp = listFram.layoutParams
                    listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                    listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                }.lparams(matchParent, matchParent) {
                    setMargins(dip(10), 0, dip(10), 0)
                }
            }
        }.view
        smart.autoRefresh()
        return view
    }

    private suspend fun getProjectList() {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = if (mStyle != 0 && mCategory != 0) {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectListByCategory(1, 5, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else if (mStyle == 0 && mCategory != 0) {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectListByCategory(1, 5, mCategory, null)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }else {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectList(1, 5)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val list = it.body()!!.data
                println(list)
                if(list.size()>0){
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
                            model["payCurrency"].asString,
                            model["minBonus"].asString,
                            model["maxBonus"].asString
                        )
                        projectList.add(projectListModel)
                    }
                    listFragment.resetItems(projectList)
                }else{
                    if(nullData == null){
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().add(nullId,nullData!!).commit()
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
            val it = if ( mCategory != 0 && mStyle != 0) {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectListByCategory(page, 5, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }else if ( mCategory != 0 && mStyle == 0) {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectListByCategory(page, 5, mCategory, null)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(ProjectApi::class.java)
                    .getProjectList(page, 5)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }
            if (it.code() in 200..299) {
                val list = it.body()!!.data
                if (list.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
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
                        model["payCurrency"].asString,
                        model["minBonus"].asString,
                        model["maxBonus"].asString
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