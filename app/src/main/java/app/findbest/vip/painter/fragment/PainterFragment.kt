package app.findbest.vip.painter.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout

class PainterFragment: Fragment(), PainterMainTitle.ChildrenClick, BackgroundFragment.ClickBack,
    PainterSort.SortClick{

    companion object {
        fun newInstance(context: Context): PainterFragment {
            val fragment = PainterFragment()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context
    private var mainId = 1
    private val toptitle = 2
    private val list = 3
    private var backgroundFragment: BackgroundFragment? = null
    private var painterSort: PainterSort? = null
    private var mainList: PainterMainList? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }
    // 点击排序
    override fun clickSort() {
        openDialog()
    }
    // 筛选
    override fun clickScreen() {

    }
    override fun clickAll() {
        closeAlertDialog()
    }
    // 排序选择
    override fun chooseSort(index: Int) {
        mainList?.setSortList(index)
        closeAlertDialog()
    }

    private fun createV(): View {
        return UI {
            frameLayout {
                //上部分
                verticalLayout{
                    frameLayout {
                        id = toptitle
                        val title = PainterMainTitle.newInstance(this@PainterFragment)
                        childFragmentManager.beginTransaction().add(toptitle, title).commit()
                    }
                    //中间adapter
                    frameLayout {
                        backgroundColor = Color.GREEN
                        id = list
                        mainList = PainterMainList.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(list, mainList!!).commit()
                    }.lparams(matchParent, matchParent)
                }
            }
        }.view
    }


    private fun openDialog() {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@PainterFragment)

            mTransaction.add(list, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.top_in, R.anim.top_in)

        if (painterSort == null) {
            painterSort = PainterSort.newInstance(mContext,this@PainterFragment)
            mTransaction.add(list, painterSort!!)
        }

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (painterSort != null) {
            mTransaction.setCustomAnimations(R.anim.top_out, R.anim.top_out)
            mTransaction.remove(painterSort!!)
            painterSort = null
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