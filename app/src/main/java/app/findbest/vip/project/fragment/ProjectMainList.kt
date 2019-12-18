package app.findbest.vip.project.fragment

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
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
import com.alibaba.fastjson.JSON
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity

class ProjectMainList : Fragment(),ProjectMainListAdapter.ListAdapter  {

    companion object {
        fun newInstance(context: Context): ProjectMainList {
            val fragment = ProjectMainList()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context
    lateinit var smart: SmartRefreshLayout
    lateinit var recycle: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //选择recycle里的单个card
    override fun oneClick() {
        startActivity<ProjectInformation>()
        activity?.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    override fun onResume() {
        super.onResume()

        smart.autoRefresh()
    }

    private fun createV(): View {
        val list = ProjectMainListAdapter(mContext, this@ProjectMainList)
        return UI {
            linearLayout {
                backgroundColor = Color.parseColor("#FFF6F6F6")
                smart = smartRefreshLayout {
                    setEnableAutoLoadMore(false)
                    setRefreshHeader(MaterialHeader(activity))
                    setOnRefreshListener {
                        toast("刷新....")
                        it.finishRefresh(3000)

//                        list.setItems(a)
                    }
                    recycle = recyclerView {
                        layoutManager = LinearLayoutManager(mContext)

                        adapter = list
                    }
                }.lparams(matchParent, matchParent) {
                    setMargins(dip(10), 0, dip(10), 0)
                }
            }
        }.view
    }

    private suspend fun getProjectList(){
        try {
            val params = mapOf(
                "size" to 5
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.testRegisterUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectList(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                //完善信息成功


            }
        } catch (throwable: Throwable) {

        }
    }
}