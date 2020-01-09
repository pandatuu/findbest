package app.findbest.vip.instance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.instance.adapter.MyProjectListAdapter
import app.findbest.vip.instance.model.ProjectItem
import app.findbest.vip.utils.DataDictionary
import app.findbest.vip.utils.recyclerView
import com.scwang.smart.refresh.layout.util.SmartUtil.px2dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class InstanceListFragment : Fragment() {

    interface InstanceList{
        fun clickInvite(f: Boolean, item: ProjectItem)
    }

    companion object{
        fun newInstance(
            context: Context,
            instanceList: InstanceList
        ): InstanceListFragment{
            val fragment = InstanceListFragment()
            fragment.mContext = context
            fragment.instanceList = instanceList
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var recycler: RecyclerView
    private lateinit var instanceList: InstanceList
    var adapter: MyProjectListAdapter? = null
    private var sdf = SimpleDateFormat("yyyy-MM-dd")
    private var screenWidth = 0
    private var picWidth = 0
    val mainId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        screenWidth = px2dp(FragmentParent().getDisplay(mContext)!!.width).toInt()
        picWidth = (screenWidth - 18) / 2
        if (picWidth >= 180) {
            picWidth = 180
        }
        return UI{
            nestedScrollView {
                recycler = recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    overScrollMode = View.OVER_SCROLL_NEVER

                }
            }
        }.view
    }



    fun appendRecyclerData(
        list: MutableList<ProjectItem>,
        screenWidth: Int,
        picWidth: Int
    ) {
        if (adapter == null) {
            //适配器
            adapter = MyProjectListAdapter(recycler, screenWidth, picWidth, list) { item, f ->
                instanceList.clickInvite(f,item)
            }
            //设置适配器
            recycler.adapter = adapter
        } else {
            adapter!!.addDataList(list)
        }
    }

    fun refresh(){
        adapter?.clear()
    }

    suspend fun addView(array: JSONArray) {
        val dataList = mutableListOf<ProjectItem>()
        for (i in 0 until array.length()) {
            val ob = array.getJSONObject(i)

            dataList.add(
                ProjectItem(
                    DataDictionary.getFormat(ob.getString("format").toInt()),
                    ob.getString("size"),
                    ob.getString("name"),
                    ob.getString("id"),
                    sdf.format(Date(ob.getString("createAt").toLong()))
                )
            )
        }

        withContext(Dispatchers.Main) {
            appendRecyclerData(dataList, screenWidth, picWidth)
        }
    }
}