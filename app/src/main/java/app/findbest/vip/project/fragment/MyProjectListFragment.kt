package app.findbest.vip.project.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
import app.findbest.vip.utils.recyclerView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity

class MyProjectListFragment: Fragment(), ProjectMainListAdapter.ListAdapter {

    companion object{
        fun newInstance(context: Context): MyProjectListFragment{
            val fragment = MyProjectListFragment()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var projectMain: ProjectMainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //选择recycle里的单个card
    override fun oneClick(id: String) {
        startActivity<ProjectInformation>("projectId" to id)
        activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    private fun createV(): View {
        projectMain = ProjectMainListAdapter(mContext, this@MyProjectListFragment)
        return UI{
            linearLayout {
                recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = projectMain
                }
            }
        }.view
    }

    fun resetItems(list: MutableList<ProjectListModel>){
        projectMain.resetItems(list)
    }

    fun addItems(list: MutableList<ProjectListModel>){
        projectMain.addItems(list)
    }
}