package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.utils.RetrofitUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import retrofit2.HttpException

class ProjectDemand: Fragment() {

    companion object{
        fun newInstance(context: Context, id: String): ProjectDemand{
            val fragment = ProjectDemand()
            fragment.mContext = context
            fragment.projectId = id
            return fragment
        }
    }

    lateinit var mContext: Context
    var demand : ProjectDemandDetails? = null
    var projectId = ""

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
                val details = 2
                frameLayout {
                    id = details
                    demand = ProjectDemandDetails.newInstance()
                    childFragmentManager.beginTransaction().add(details,demand!!).commit()
                }.lparams(matchParent, dip(0)){
                    weight = 1f
                    bottomMargin = dip(20)
                }
                button {
                    backgroundResource = R.drawable.enable_around_button
                    text = "我要应征"
                    textSize = 16f
                    textColor = Color.parseColor("#FFFFFFFF")
                }.lparams(dip(300),dip(50)){
                    gravity = Gravity.CENTER_HORIZONTAL
                    bottomMargin = dip(30)
                }
            }
        }.view
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getInfo(projectId)
        }
        return view
    }


    private suspend fun getInfo(id: String) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectInfoById(id, "zh")
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if(it.code() in 200..299){
                val model = it.body()!!
                demand?.setInfomation(model)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}