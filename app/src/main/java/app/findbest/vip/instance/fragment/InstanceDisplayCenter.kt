package app.findbest.vip.instance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import app.findbest.vip.instance.model.InstanceModel
import app.findbest.vip.instance.view.InstanceDetailPagers
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.utils.recyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity


class InstanceDisplayCenter: Fragment(),PainterInfoPictureAdapter.ImageClick {

    companion object{
        fun newInstance(context: Context): InstanceDisplayCenter {
            val fragment = InstanceDisplayCenter()
            fragment.mContext = context
            return fragment
        }
    }

    val imageList: ArrayList<InstanceModel> = arrayListOf()
    private lateinit var mContext: Context
    private lateinit var painterInfoPic: PainterInfoPictureAdapter
    var nowPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun click(str: String) {
        imageList.forEach {
            if(str == it.url){
                startActivity<InstanceDetailPagers>("nowInstanceDetailList" to imageList, "nowDetail" to it, "nowPage" to nowPage)
                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        }
    }

    private fun createV(): View {
        painterInfoPic = PainterInfoPictureAdapter( mContext,
            arrayListOf(),this@InstanceDisplayCenter)
        return UI{
            linearLayout {
                val recycler = recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = painterInfoPic
                }
                val lp = recycler.layoutParams
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT
            }
        }.view
    }

    fun resetItems(item: JsonArray, page :Int){
        nowPage = page
        val list1 = arrayListOf<String>()
        imageList.clear()
        item.forEach {
            val model = Gson().fromJson(it as JsonObject, InstanceModel::class.java)
            imageList.add(model)
            list1.add(it.asJsonObject["url"].asString)
        }
        painterInfoPic.resetData(arrayListOf(list1))
    }

    fun addItems(item: JsonArray, page :Int){
        nowPage = page
        val list1 = arrayListOf<String>()
        item.forEach {
            val model = Gson().fromJson(it as JsonObject, InstanceModel::class.java)
            imageList.add(model)
            list1.add(it.asJsonObject["url"].asString)
        }
        painterInfoPic.addData(arrayListOf(list1))
    }
}