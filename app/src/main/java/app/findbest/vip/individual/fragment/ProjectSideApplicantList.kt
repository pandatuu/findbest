package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.individual.adapter.ProjectSideApplicantsAdapter
import app.findbest.vip.commonfrgmant.BigImage2
import app.findbest.vip.utils.recyclerView
import com.google.gson.JsonObject
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.support.v4.toast

class ProjectSideApplicantList: Fragment(), ProjectSideApplicantsAdapter.PrintedCrad,
    BackgroundFragment.ClickBack, BigImage2.ImageClick  {

    interface ProjectSideList{
        fun refuse(commitId: String)
    }

    companion object{
        fun newInstance(context: Context, projectSideList: ProjectSideList): ProjectSideApplicantList{
            val fragment = ProjectSideApplicantList()
            fragment.mContext = context
            fragment.projectSideList = projectSideList
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var projectSideList: ProjectSideList
    private var bigImage: BigImage2? = null
    private var backgroundFragment: BackgroundFragment? = null
    private var recycle: RecyclerView? = null
    private lateinit var applicants: ProjectSideApplicantsAdapter

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
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectSideApplicantList)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@ProjectSideApplicantList, true)
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
    //聊一聊
    override fun chat(model: JsonObject) {
//        val provider = model["provider"].asJsonObject
//        val intent = Intent(mContext, MessageListActivity::class.java)
//        val id = if(!provider["id"].isJsonNull) provider["id"].asString else ""
//        val name = if(!provider["name"].isJsonNull) provider["name"].asString else ""
//        val avatar = if(!provider["avatar"].isJsonNull) provider["avatar"].asString else ""
//        intent.putExtra("hisId",id)
//        intent.putExtra("companyName","")
//        intent.putExtra("hisName",name)
//        intent.putExtra("hislogo",avatar)
//        intent.putExtra("position_id","")
//        intent.putExtra("isTranslate",false)
//
//        startActivity(intent)
//        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

        toast("coming soon,wait")
    }
    //发送委托
    override fun send(commitId: String) {
        toast(resources.getString(R.string.my_project_enlist_release_commission))
    }
    //拒绝应征
    override fun refuse(commitId: String) {
        projectSideList.refuse(commitId)
    }

    private fun createV(): View {
        applicants =
            ProjectSideApplicantsAdapter(this@ProjectSideApplicantList.context!!, this@ProjectSideApplicantList, mutableListOf())
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