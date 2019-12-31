package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
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
import retrofit2.HttpException

class ProjectSideProjectInvite : Fragment(),ProjectSideInviteAdapter.PrintedCrad, BigImage2.ImageClick{

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
    private lateinit var smart: SmartRefreshLayout
    var projectId = ""
    val mainId = 1
    private var nowPage = 0
    private var recycle: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun oneClick(str: String) {
        openDialog(str)
    }

    override fun clickclose() {
        closeAlertDialog()
    }
    //取消邀请
    override fun cacel(id: String) {
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
                        recycle = recyclerView {
                            layoutManager = LinearLayoutManager(mContext)
                            backgroundColor = Color.parseColor("#FFF6F6F6")
                        }
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(5)
                        rightMargin = dip(5)
                    }
                }.lparams(matchParent, matchParent)
            }
        }.view
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
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                val applicants =
                    ProjectSideInviteAdapter(this@ProjectSideProjectInvite.context!!, this@ProjectSideProjectInvite, mutableList)
                recycle?.adapter = applicants
                nowPage = 1
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
                val model = it.body()!!

                nowPage = page
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


    private fun openDialog(pic: String) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@ProjectSideProjectInvite)
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