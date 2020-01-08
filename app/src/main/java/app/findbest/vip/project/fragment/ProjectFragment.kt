package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.application.App
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.message.activity.VideoResultActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.verticalLayout
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.Observer
import io.github.sac.Ack
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*
import kotlin.collections.ArrayList


class ProjectFragment : Fragment(), ProjectMainTitle.ChildrenClick, BackgroundFragment.ClickBack,
    ProjectMainScreen.ProjectScreen {

    companion object {
        fun newInstance(context: Context): ProjectFragment {
            val fragment = ProjectFragment()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context

    private var mainId = 1
    private val top = 2
    val list = 3
    private var backgroundFragment: BackgroundFragment? = null
    private var projectMainScreen: ProjectMainScreen? = null
    var main: ProjectMainList? = null
    val app = App.getInstance()!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun onResume() {
        super.onResume()
//        stacktime()
//        LiveEventBus
//            .get("isVideo", MutableList::class.java)
//            .observe(this, Observer<MutableList<*>> {
//                toast("11111")
//                app.isVideo = false
//                startActivity<VideoResultActivity>(
//                    "otherName" to it[0].toString(),
//                    "otherAvatar" to it[1].toString()
//                )
//                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
//            })
    }
//    fun stacktime(){
//        val timer = Timer()
//        val timerTask = object: TimerTask() {
//            override fun run() {
//                if(app.isVideo){
//                    app.isVideo = false
//                    val list = app.getVideoOther()
//                    startActivity<VideoResultActivity>(
//                        "otherName" to list[0],
//                        "otherAvatar" to list[1]
//                    )
//                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
//                }
//            }
//        }
//        /**
//         *  参数一  表示当前要执行的任务
//         *  参数二  表示用户第一次启动渐变的时间
//         *  参数三  表示下次任务执行的时间
//         */
//        timer.schedule(timerTask,0,1000)
//    }

    //点击筛选按钮
    override fun clickFragment() {
        openDialog()
    }

    //点击dialog黑色背景色
    override fun backgroundClick() {
        closeAlertDialog()
    }

    //点击dialog黑色背景色
    override fun clickAll() {
        closeAlertDialog()
    }

    //点击dialog确认按钮
    override fun confirmClick(array: ArrayList<Int>) {
        main?.setCategoryList(array[0], array[1])
        closeAlertDialog()
    }

    private fun createV(): View {
        return UI {
            frameLayout {
                //上部分
                verticalLayout {
                    frameLayout {
                        id = this@ProjectFragment.top
                        val title = ProjectMainTitle.newInstance(this@ProjectFragment)
                        childFragmentManager.beginTransaction().add(this@ProjectFragment.top, title)
                            .commit()
                    }
                    //中间adapter
                    frameLayout {
                        backgroundColor = Color.GREEN
                        id = list
                        main = ProjectMainList.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(list, main!!).commit()
                    }.lparams(matchParent, matchParent)
                }
            }
        }.view
    }

    private fun openDialog() {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectFragment)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        projectMainScreen = ProjectMainScreen.newInstance(mContext, this@ProjectFragment)
        mTransaction.add(mainId, projectMainScreen!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (projectMainScreen != null) {
            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)

            mTransaction.remove(projectMainScreen!!)
            projectMainScreen = null
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