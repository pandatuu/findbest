package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.individual.adapter.ProjectSideInviteAdapter
import app.findbest.vip.commonfrgmant.BigImage2
import app.findbest.vip.utils.recyclerView
import com.google.gson.JsonObject
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView

class ProjectSideProjectInviteList: Fragment(), ProjectSideInviteAdapter.PrintedCrad, BigImage2.ImageClick {

    interface ProjectSideList{
        fun cancel(id: String)
    }

    companion object{
        fun newInstance(context: Context, projectSideList: ProjectSideList): ProjectSideProjectInviteList{
            val fragment = ProjectSideProjectInviteList()
            fragment.mContext = context
            fragment.projectSideList = projectSideList
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var projectSideList: ProjectSideList
    private lateinit var applicants: ProjectSideInviteAdapter
    private var bigImage: BigImage2? = null
    private var recycle: RecyclerView? = null
    val mainId = 1

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
        projectSideList.cancel(id)
    }

    private fun createV(): View {
        applicants =
            ProjectSideInviteAdapter(this@ProjectSideProjectInviteList.context!!, this@ProjectSideProjectInviteList, mutableListOf())
        return UI{
            nestedScrollView {
                recycle = recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    backgroundColor = Color.parseColor("#FFF6F6F6")
                    adapter = applicants
                }
            }
        }.view
    }
    fun resetView(mutableList: MutableList<JsonObject>){
        applicants.resetView(mutableList)
    }

    fun addView(mutableList: MutableList<JsonObject>){
        applicants.addData(mutableList)
    }

    private fun openDialog(pic: String) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        bigImage = BigImage2.newInstance(pic,this@ProjectSideProjectInviteList,true)
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