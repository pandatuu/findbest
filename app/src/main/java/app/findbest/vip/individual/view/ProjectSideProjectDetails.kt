package app.findbest.vip.individual.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.individual.adapter.MyProjectListAdapter
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.individual.fragment.ProjectListStatus
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.RetrofitUtils
import com.google.gson.JsonObject
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import retrofit2.HttpException

class ProjectSideProjectDetails : BaseActivity() {

    private lateinit var recycle: RecyclerView
    private lateinit var smart: SmartRefreshLayout
    private var backgroundFragment: BackgroundFragment? = null
    private var projectSideScreen: ProjectListStatus? = null
    private var projectSideList: MyProjectListAdapter? = null
    private lateinit var status: TextView

    var nowPage = 0
    val mainId = 1
    var isPainter = true

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
                        }.lparams(dip(10), dip(18))
                    }.lparams(dip(20), dip(20)) {
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
                    }.lparams {
                        centerHorizontally()
                        alignParentBottom()
                        bottomMargin = dip(10)
                    }
                    val linea = linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.BOTTOM
                        status = textView {
                            gravity = Gravity.BOTTOM
                            text = "全部"
                            textSize = 15f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams {
                            gravity = Gravity.CENTER
                        }
                        imageView {
                            imageResource = R.mipmap.ico_pack_up_arrow_nor
                        }.lparams {
                            leftMargin = dip(8)
                            gravity = Gravity.CENTER
                        }
                        setOnClickListener {
                            val la = View.inflate(this@ProjectSideProjectDetails, R.layout.screen_status, null)
                            val first = la.findViewById<LinearLayout>(R.id.firstNum)
                            val second = la.findViewById<LinearLayout>(R.id.secondNum)
                            val third = la.findViewById<LinearLayout>(R.id.thirdNum)
                            val forth = la.findViewById<LinearLayout>(R.id.forthNum)
                            val popup = PopupWindow(la, wrapContent, wrapContent)
                            popup.isOutsideTouchable = true
                            popup.isTouchable = true
                            popup.showAsDropDown(it)
                            popup.isFocusable = true
                            first.setOnClickListener {
                                status.text = "全部"
                                popup.dismiss()
                            }
                            second.setOnClickListener {
                                status.text = "发布阶段"
                                popup.dismiss()
                            }
                            third.setOnClickListener {
                                status.text = "制作阶段"
                                popup.dismiss()
                            }
                            forth.setOnClickListener {
                                status.text = "交易完成"
                                popup.dismiss()
                            }
                        }
                    }.lparams {
                        rightMargin = dip(15)
                        alignParentBottom()
                        alignParentRight()
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent, dip(65))
            }
        }

        smart.autoRefresh()
    }

    //画师方接口
//    private suspend fun getPainterSideList() {
//        try {
//            val retrofitUils =
//                RetrofitUtils(this@ProjectSideProjectDetails, resources.getString(R.string.developmentUrl))
//            val it = retrofitUils.create(IndividualApi::class.java)
//                .getPainterSideList(1, "zh")
//                .subscribeOn(Schedulers.io())
//                .awaitSingle()
//            if (it.code() in 200..299) {
//                val model = it.body()!!.data
//                nowPage = 1
//
//                val list = mutableListOf<JsonObject>()
//                model.forEach {
//                    list.add(it.asJsonObject)
//                }
//                projectSideList?.resetItems(list)
//            }
//        } catch (throwable: Throwable) {
//            if (throwable is HttpException) {
//                println(throwable.message())
//            }
//        }
//    }

    //项目方接口
//    private suspend fun getProjectSideList() {
//        try {
//            val retrofitUils =
//                RetrofitUtils(this@ProjectSideProjectDetails, resources.getString(R.string.developmentUrl))
//            val it = retrofitUils.create(IndividualApi::class.java)
//                .getProjectSideList(1, "zh")
//                .subscribeOn(Schedulers.io())
//                .awaitSingle()
//            if (it.code() in 200..299) {
//                val model = it.body()!!.data
//                nowPage = 1
//
//                val list = mutableListOf<JsonObject>()
//                model.forEach {
//                    list.add(it.asJsonObject)
//                }
//                projectSideList?.resetItems(list)
//            }
//        } catch (throwable: Throwable) {
//            if (throwable is HttpException) {
//                println(throwable.message())
//            }
//        }
//    }


//    private fun openScreenDialog() {
//        val mTransaction = supportFragmentManager.beginTransaction()
//        if (backgroundFragment == null) {
//            backgroundFragment = BackgroundFragment.newInstance(this@MyProjectList)
//            mTransaction.add(mainId, backgroundFragment!!)
//        }
//
//        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)
//        projectSideScreen = ProjectListStatus.newInstance(this@MyProjectList)
//        mTransaction.add(mainId, projectSideScreen!!)
//
//        mTransaction.commit()
//    }
//
//    private fun closeScreenDialog() {
//        val mTransaction = supportFragmentManager.beginTransaction()
//        if (projectSideScreen != null) {
//            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)
//            mTransaction.remove(projectSideScreen!!)
//            projectSideScreen = null
//        }
//
//        if (backgroundFragment != null) {
//            mTransaction.setCustomAnimations(
//                R.anim.fade_in_out_a, R.anim.fade_in_out_a
//            )
//            mTransaction.remove(backgroundFragment!!)
//            backgroundFragment = null
//        }
//        mTransaction.commit()
//    }
}