package app.findbest.vip.painter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BigImage2
import app.findbest.vip.painter.adapter.PainterMainListAdapter
import app.findbest.vip.painter.view.PainterInfomation
import app.findbest.vip.utils.recyclerView
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView

class PainterListFragment : Fragment(), PainterMainListAdapter.ImageClick, BigImage2.ImageClick {

    companion object{
        fun newInstance(
            context: Context
        ): PainterListFragment{
            val fragment = PainterListFragment()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var painterMain: PainterMainListAdapter? = null
    private var bigImage: BigImage2? = null
    val mainId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun jumpToInfo(id: String) {
        activity!!.startActivity<PainterInfomation>("userId" to id)
        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    // 点击关闭图片
    override fun clickclose() {
        closeAlertDialog()
    }
    // 点击放大图片
    override fun click(str: String) {
        openDialog(str)
    }

    private fun createV(): View {
        painterMain = PainterMainListAdapter(mContext, this@PainterListFragment)
        return UI{
            nestedScrollView {
                recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = painterMain
                }.lparams(matchParent, matchParent)
            }
        }.view
    }

    fun resetItems(list: MutableList<JsonObject>){
        painterMain?.resetItems(list)
    }

    fun addItems(list: MutableList<JsonObject>){
        painterMain?.addItems(list)
    }

    private fun openDialog(pic: String) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)
        bigImage = BigImage2.newInstance(pic,this@PainterListFragment, true)
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