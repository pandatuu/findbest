package app.findbest.vip.individual.fragment

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
import app.findbest.vip.R
import app.findbest.vip.individual.adapter.MyProjectListAdapter
import app.findbest.vip.individual.view.PainterSideProjectInfo
import app.findbest.vip.individual.view.ProjectSideProjectInfo
import app.findbest.vip.utils.recyclerView
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class MyProjectListFragment: Fragment(), MyProjectListAdapter.ListAdapter {
    override fun oneClick(id: String, status: Int) {
        if (isPainter) {
            activity!!.startActivity<PainterSideProjectInfo>("projectId" to id, "status" to status)
            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
        } else {
            activity!!.startActivity<ProjectSideProjectInfo>("projectId" to id, "status" to status)
            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }
    }

    companion object{
        fun newInstance(context: Context, isPainter: Boolean): MyProjectListFragment{
            val fragment = MyProjectListFragment()
            fragment.mContext = context
            fragment.isPainter = isPainter
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var isPainter: Boolean = false

    private var projectSideList: MyProjectListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI{
            linearLayout {
                recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    projectSideList = MyProjectListAdapter(
                        mContext, this@MyProjectListFragment,
                        arrayListOf()
                    )
                    adapter = projectSideList
                }.lparams(matchParent, matchParent)
            }
        }.view
    }

    fun resetItems(list: MutableList<JsonObject>){
        projectSideList?.resetItems(list)
    }

    fun addItems(list: MutableList<JsonObject>){
        projectSideList?.addItems(list)
    }
}