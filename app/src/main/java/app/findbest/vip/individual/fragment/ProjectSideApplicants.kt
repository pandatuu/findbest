package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.individual.adapter.ProjectSideApplicantsAdapter
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

class ProjectSideApplicants : Fragment(), ProjectSideApplicantsAdapter.PrintedCrad,
    BackgroundFragment.ClickBack, BigImage2.ImageClick {

    companion object {
        fun newInstance(context: Context, id: String): ProjectSideApplicants {
            val fragment = ProjectSideApplicants()
            fragment.mContext = context
            fragment.projectId = id
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var name: TextView
    private lateinit var smart: SmartRefreshLayout
    private var bigImage: BigImage2? = null
    private var backgroundFragment: BackgroundFragment? = null
    private var recycle: RecyclerView? = null

    private var nowPage = 0
    var projectId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //单独点击某一张画
    override fun oneClick(str: String) {
        val mainId = 1
        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectSideApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@ProjectSideApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, bigImage!!).commit()
        }
    }

    //点击黑色背景
    override fun clickAll() {
        closeDialog()
    }

    //点击放大的图片
    override fun clickclose() {
        closeDialog()
    }
    //聊一聊
    override fun chat(commitId: String) {

    }
    //发送委托
    override fun send(commitId: String) {
        toast("请前往web端发布委托")
    }
    //拒绝应征
    override fun refuse(commitId: String) {
        toast("拒绝")
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            refusePerson(commitId)
        }
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                verticalLayout {
                    linearLayout {
                        name = textView {
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
                                getPrinters(projectId)
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
                                getPrinters(projectId, nowPage + 1)
                                it.finishLoadMore(1000)
                            }
                        }
                        recycle = recyclerView {
                            layoutManager = LinearLayoutManager(mContext)
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(5)
                        rightMargin = dip(5)
                    }
                }
            }
        }.view
        smart.autoRefresh()
        return view
    }

    private suspend fun getPrinters(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideApplies(id,1,5)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                val applicants =
                    ProjectSideApplicantsAdapter(this@ProjectSideApplicants.context!!, this@ProjectSideApplicants, mutableList)
                recycle?.adapter = applicants
                nowPage = 1
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
    private suspend fun getPrinters(id: String,page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideApplies(id,page,5)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                val applicants =
                    ProjectSideApplicantsAdapter(this@ProjectSideApplicants.context!!, this@ProjectSideApplicants, mutableList)
                recycle?.adapter = applicants
                nowPage = page
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun refusePerson(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .refuseApplies(id)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {

                smart.autoRefresh()
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
    private fun closeDialog() {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (backgroundFragment != null) {
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }

        if (bigImage != null) {
            mTransaction.remove(bigImage!!)
            bigImage = null
        }
        mTransaction.commit()
    }

    fun setProjectName(str: String) {
        name.text = str
    }
}