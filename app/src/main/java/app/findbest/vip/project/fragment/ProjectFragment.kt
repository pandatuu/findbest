package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.ChooseCountry
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast

class ProjectFragment : Fragment(), ProjectMainTitle.ChildrenClick,BackgroundFragment.ClickBack, ProjectMainScreen.ProjectScreen {

    companion object {
        fun newInstance(context: Context): ProjectFragment {
            val fragment = ProjectFragment()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context

    private var mainId = 1
    private var backgroundFragment: BackgroundFragment? = null
    private var projectMainScreen: ProjectMainScreen? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

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

    private fun createV(): View {
        return UI {
            frameLayout {
                id = mainId
                //上部分
                verticalLayout{
                    val top = 2
                    frameLayout {
                        id = top
                        val title = ProjectMainTitle.newInstance(this@ProjectFragment)
                        childFragmentManager.beginTransaction().add(top, title).commit()
                    }
                    //中间adapter
                    val list = 3
                    frameLayout {
                        backgroundColor = Color.GREEN
                        id = list
                        val main = ProjectMainList.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(list, main).commit()
                    }.lparams(matchParent, matchParent)
                }
            }
        }.view
    }

    private fun openDialog() {
        val mTransaction = childFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectFragment)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        projectMainScreen = ProjectMainScreen.newInstance(this@ProjectFragment)
        mTransaction.add(mainId, projectMainScreen!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = childFragmentManager.beginTransaction()
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