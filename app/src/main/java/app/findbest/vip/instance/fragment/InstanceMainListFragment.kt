package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import app.findbest.vip.instance.model.InstanceModel
import app.findbest.vip.instance.view.InstanceSreachDetail
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.utils.recyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.anko.support.v4.UI

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
                val model = Gson().fromJson(it, InstanceModel::class.java)
                val intent = Intent(mContext, InstanceSreachDetail::class.java)
                //跳转详情
                intent.putExtra("nowDetail", model)
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