package app.findbest.vip.project.fragment

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
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.project.adapter.ProjectApplicantsAdapter
import app.findbest.vip.utils.recyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip

class ProjectApplicants: Fragment(), ProjectApplicantsAdapter.PrintedCrad,BackgroundFragment.ClickBack,BigImage.ClickImg{

    companion object{
        fun newInstance(context: Context): ProjectApplicants{
            val fragment = ProjectApplicants()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var bigImage: BigImage? = null
    private var backgroundFragment: BackgroundFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //单独点击某一张画
    override fun oneClick(i: Int) {
        val mainId = 1
        if(backgroundFragment==null){
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId,backgroundFragment!!).commit()
        }
        if(bigImage==null){
            bigImage = BigImage.newInstance(i,this@ProjectApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId,bigImage!!).commit()
        }
    }
    //点击黑色背景
    override fun clickAll() {
        closeDialog()
    }
    //点击放大的图片
    override fun closeImg() {
        closeDialog()
    }
    private fun createV(): View {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                linearLayout {
                    textView {
                        text = "外国儿童文学小说插图+封面"
                        textSize = 21f
                        textColor = Color.parseColor("#FF202020")
                    }.lparams{
                        gravity = Gravity.CENTER_VERTICAL
                        leftMargin = dip(15)
                    }
                }.lparams(matchParent,dip(70))
                linearLayout {
                    backgroundColor = Color.parseColor("#FFF6F6F6")
                }.lparams(matchParent,dip(5))
                linearLayout {
                    recyclerView {
                        layoutManager = LinearLayoutManager(mContext)
                        val list = arrayListOf("1","2")
                        val applicants = ProjectApplicantsAdapter(mContext,this@ProjectApplicants,list)
                        adapter = applicants
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent){
                    leftMargin = dip(10)
                }
            }
        }.view
    }

    private fun closeDialog(){
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