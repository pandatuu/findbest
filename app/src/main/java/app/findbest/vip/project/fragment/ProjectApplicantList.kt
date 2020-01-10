package app.findbest.vip.project.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.painter.fragment.BigImage2
import app.findbest.vip.project.adapter.ProjectApplicantsAdapter
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
import app.findbest.vip.utils.recyclerView
import com.google.gson.JsonObject
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.support.v4.startActivity

class ProjectApplicantList: Fragment(), ProjectApplicantsAdapter.PrintedCrad,
    BackgroundFragment.ClickBack, BigImage2.ImageClick {

    companion object{
        fun newInstance(context: Context): ProjectApplicantList{
            val fragment = ProjectApplicantList()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var bigImage: BigImage2? = null
    private var backgroundFragment: BackgroundFragment? = null
    private var recycle: RecyclerView? = null

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
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectApplicantList)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@ProjectApplicantList)
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

    private fun createV(): View {
        return UI{
            nestedScrollView {
                recycle = recyclerView {
                    isHorizontalScrollBarEnabled = false
                    layoutManager = LinearLayoutManager(mContext)
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(10)
                }
            }
        }.view
    }

    fun resetItems(mutableList: MutableList<JsonObject>){
        val applicants =
            ProjectApplicantsAdapter(mContext, this@ProjectApplicantList, mutableList)
        recycle?.adapter = applicants
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
}