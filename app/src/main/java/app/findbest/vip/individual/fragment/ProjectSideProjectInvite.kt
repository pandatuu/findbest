package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
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
import app.findbest.vip.individual.adapter.ProjectSideInviteAdapter
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.painter.fragment.BigImage2
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
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class ProjectSideProjectInvite : Fragment(), ProjectSideProjectInviteList.ProjectSideList{

    companion object {
        fun newInstance(context: Context, id: String): ProjectSideProjectInvite {
            val fragment = ProjectSideProjectInvite()
            fragment.mContext = context
            fragment.projectId = id
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var name = ""
    private var bigImage: BigImage2? = null
    private var recycle: RecyclerView? = null
    private lateinit var smart: SmartRefreshLayout
    var projectId = ""
    val mainId = 1
    private var nowPage = 0
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: ProjectSideProjectInviteList
    private var nullData: NullDataPageFragment? = null
    private val nullId = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun cancel(id: String) {
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            cancelInvite(id)
        }
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.parseColor("#FFF6F6F6")
                verticalLayout {
                    linearLayout {
                        backgroundColor = Color.WHITE
                        textView {
                            text = name
                            textSize = 21f
                            textColor = Color.parseColor("#FF202020")
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                            leftMargin = dip(15)
                        }
                    }.lparams(matchParent, dip(70))

                    smart = smartRefreshLayout {
                        setEnableAutoLoadMore(false)
                        setRefreshHeader(MaterialHeader(mContext))
                        setOnRefreshListener {
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                getProjectSideInviteById(projectId)
                                it.finishRefresh(1000)
                            }
                        }
                        setRefreshFooter(
                            BallPulseFooter(mContext).setSpinnerStyle(
                                SpinnerStyle.Scale
                            )
                        )
                        setOnLoadMoreListener {
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                getProjectSideInviteById(projectId, nowPage + 1)
                                it.finishLoadMore(1000)
                            }
                        }
                        listFram = frameLayout {
                            id = nullId
                        }
                        val listFramlp = listFram.layoutParams
                        listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                        listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(5)
                        rightMargin = dip(5)
                    }
                }.lparams(matchParent, matchParent)
            }
        }.view

        listFragment = ProjectSideProjectInviteList.newInstance(mContext,this@ProjectSideProjectInvite)
        childFragmentManager.beginTransaction().replace(nullId, listFragment).commit()
        smart.autoRefresh()
        return view
    }
    //获取发送的邀请
    private suspend fun getProjectSideInviteById(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideInviteById(id,1,5)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val array = it.body()!!["invites"].asJsonObject["data"].asJsonArray
                if(array.size()>0){
                    nowPage = 1
                    val mutableList = mutableListOf<JsonObject>()
                    array.forEach {
                        mutableList.add(it.asJsonObject)
                    }
                    listFragment.resetView(mutableList)
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
    //获取发送的邀请
    private suspend fun getProjectSideInviteById(id: String,page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideInviteById(id,page,5)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val array = it.body()!!["invites"].asJsonObject["data"].asJsonArray
                if(array.size() ==0){
                    toast("没有数据了。。。")
                    return
                }
                nowPage = page
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                listFragment.addView(mutableList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    //取消邀请
    private suspend fun cancelInvite(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .cancelInvite(id)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {

                listFragment = ProjectSideProjectInviteList.newInstance(mContext,this@ProjectSideProjectInvite)
                childFragmentManager.beginTransaction().replace(nullId, listFragment).commit()
                smart.autoRefresh()
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
    fun setProjectName(str: String) {
        name = str
    }
}