package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.utils.RetrofitUtils
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import retrofit2.HttpException

class ProjectApplicants : Fragment(){

    companion object {
        fun newInstance(context: Context, id: String): ProjectApplicants {
            val fragment = ProjectApplicants()
            fragment.mContext = context
            fragment.projectId = id
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var name: TextView
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: ProjectApplicantList
    private var nullData: NullDataPageFragment? = null

    var projectId = ""
    private val nullId = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                verticalLayout {
                    linearLayout {
                        name = textView {
                            textSize = 21f
                            textColor = Color.parseColor("#FF202020")
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                            setMargins(dip(15),dip(20),dip(15),dip(20))
                        }
                    }.lparams(matchParent, wrapContent)
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                    }.lparams(matchParent, dip(5))

                    listFram = frameLayout {
                        id = nullId
                        listFragment = ProjectApplicantList.newInstance(mContext)
                        childFragmentManager.beginTransaction().add(nullId, listFragment).commit()
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent)
            }
        }.view
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getPrinters(projectId)
        }
        return view
    }

    private suspend fun getPrinters(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getPrintersById(id)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val array = it.body()!!.data
                if(array.size()>0){
                    if(nullData!=null){
                        childFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    val mutableList = mutableListOf<JsonObject>()
                    array.forEach {
                        mutableList.add(it.asJsonObject)
                    }
                    listFragment.resetItems(mutableList)
                }else{
                    if(nullData==null){
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().replace(nullId,nullData!!).commit()
                    }
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }


    fun setProjectName(str: String) {
        name.text = str
    }
}