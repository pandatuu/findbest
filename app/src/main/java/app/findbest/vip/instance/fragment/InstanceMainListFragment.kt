package app.findbest.vip.instance.fragment

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
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.view.InstanceActivity
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.painter.fragment.PainterListFragment
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.project.view.ProjectInformation
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException

class InstanceMainListFragment : Fragment(), PainterInfoPictureAdapter.ImageClick {

    companion object {
        fun newInstance(context: Context): InstanceMainListFragment {
            val fragment = InstanceMainListFragment()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context
    private var painterInfoPic: PainterInfoPictureAdapter? = null
    var imageList = mutableListOf<JsonObject>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun click(str: String) {
        imageList.forEach {
            if(str == it["url"].asString){
                val intent = Intent(mContext, InstanceActivity::class.java)
                //跳转详情
                intent.putExtra("url", if(!it["url"].isJsonNull) it["url"].asString else "")
                intent.putExtra("logo", if(!it["logo"].isJsonNull) it["logo"].asString else "")
                intent.putExtra("name", if(!it["name"].isJsonNull) it["name"].asString else "")
                intent.putExtra("id", if(!it["id"].isJsonNull) it["id"].asString else "")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        }
    }

    private fun createV(): View {
        return UI {
                recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    painterInfoPic = PainterInfoPictureAdapter(
                        mContext,
                        arrayListOf(),this@InstanceMainListFragment)
                    adapter = painterInfoPic

                }
        }.view
    }


    fun resetData(items: JsonArray) {
        imageList.clear()
        val list1 = arrayListOf<String>()
        items.forEach {
            imageList.add(it.asJsonObject)
            list1.add(it.asJsonObject["url"].asString)
        }
        painterInfoPic?.resetData(arrayListOf(list1))
    }

    fun addData(items: JsonArray) {
        val list1 = arrayListOf<String>()
        items.forEach {
            imageList.add(it.asJsonObject)
            list1.add(it.asJsonObject["url"].asString)
        }
        painterInfoPic?.addData(arrayListOf(list1))
    }

}