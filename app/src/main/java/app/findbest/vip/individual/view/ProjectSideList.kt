package app.findbest.vip.individual.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.individual.adapter.ProjectSideListAdapter
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.individual.fragment.ProjectSideListScreen
import app.findbest.vip.utils.BaseActivity
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
import retrofit2.HttpException

class ProjectSideList: BaseActivity(),ProjectSideListAdapter.ListAdapter, BackgroundFragment.ClickBack, ProjectSideListScreen.ProjectScreen {

    private lateinit var recycle: RecyclerView
    private lateinit var smart: SmartRefreshLayout
    private var backgroundFragment: BackgroundFragment? = null
    private var projectSideScreen: ProjectSideListScreen? = null
    private var projectSideList: ProjectSideListAdapter? = null

    var nowPage = 0
    val mainId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            id = mainId
            verticalLayout {
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        gravity = Gravity.CENTER
                        toolbar {
                            navigationIconResource = R.mipmap.icon_back
                        }.lparams(dip(10),dip(18))
                    }.lparams(dip(20),dip(20)){
                        alignParentBottom()
                        alignParentLeft()
                        leftMargin = dip(15)
                        bottomMargin = dip(10)
                    }
                    textView {
                        text = "我的项目"
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams{
                        centerHorizontally()
                        alignParentBottom()
                        bottomMargin = dip(10)
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER
                        textView {
                            text = "筛选"
                            textSize = 15f
                            textColor = Color.parseColor("#FF333333")
                        }
                        imageView {
                            imageResource = R.mipmap.tab_icon_screening_nor
                        }.lparams{
                            leftMargin = dip(8)
                        }
                        setOnClickListener {
                            openScreenDialog()
                        }
                    }.lparams(){
                        rightMargin = dip(15)
                        alignParentBottom()
                        alignParentRight()
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent,dip(65))
                linearLayout {
                    smart = smartRefreshLayout {
                        setEnableAutoLoadMore(false)
                        setRefreshHeader(MaterialHeader(this@ProjectSideList))
                        setOnRefreshListener {
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                getProjectSideList()
                                it.finishRefresh(1000)
                            }
                        }
                        setRefreshFooter(BallPulseFooter(this@ProjectSideList).setSpinnerStyle(SpinnerStyle.Scale))
                        setOnLoadMoreListener {
                            GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                getProjectSideList(nowPage + 1)
                                it.finishLoadMore(1000)
                            }
                        }
                        recycle = recyclerView {
                            layoutManager = LinearLayoutManager(this@ProjectSideList)
                            projectSideList = ProjectSideListAdapter(this@ProjectSideList,this@ProjectSideList)
                            adapter = projectSideList
                        }
                        val lp = recycle.layoutParams
                        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
                        lp.height = LinearLayout.LayoutParams.MATCH_PARENT
                    }.lparams(matchParent, matchParent) {
                        setMargins(dip(10), 0, dip(10), 0)
                    }
                }.lparams(matchParent, matchParent)
            }
        }

        smart.autoRefresh()
    }


    override fun clickAll() {
        closeScreenDialog()
    }

    override fun backgroundClick() {
        closeScreenDialog()
    }

    override fun confirmClick(array: ArrayList<Int>) {

    }
    //点击某一项目
    override fun oneClick(id: String) {

    }


    private suspend fun getProjectSideList() {
        try {
            val retrofitUils =
                RetrofitUtils(this@ProjectSideList, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideList(1,"zh")
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                nowPage = 1

                val list = mutableListOf<JsonObject>()
                model.forEach {
                    list.add(it.asJsonObject)
                }
                projectSideList?.resetItems(list)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }

    private suspend fun getProjectSideList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(this@ProjectSideList, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideList(page,"zh")
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!.data
                nowPage = page

                val list = mutableListOf<JsonObject>()
                model.forEach {
                    list.add(it.asJsonObject)
                }
                projectSideList?.addItems(list)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }

    private fun openScreenDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()
        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectSideList)
            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)
        projectSideScreen = ProjectSideListScreen.newInstance(this@ProjectSideList,this@ProjectSideList)
        mTransaction.add(mainId, projectSideScreen!!)

        mTransaction.commit()
    }

    private fun closeScreenDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()
        if (projectSideScreen != null) {
            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)
            mTransaction.remove(projectSideScreen!!)
            projectSideScreen = null
        }

        if (backgroundFragment != null) {
            mTransaction.setCustomAnimations(
                R.anim.fade_in_out_a, R.anim.fade_in_out_a
            )
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }
        mTransaction.commit()
    }
}