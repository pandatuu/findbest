package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.utils.RetrofitUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import retrofit2.HttpException

class ProjectSideProjectDetails : Fragment() {

    companion object {
        fun newInstance(
            context: Context,
            id: String,
            projectSideApplicants: ProjectSideApplicants?,
            projectInvite: ProjectSideProjectInvite?
        ): ProjectSideProjectDetails {
            val fragment = ProjectSideProjectDetails()
            fragment.mContext = context
            fragment.projectId = id
            fragment.projectSideApplicants = projectSideApplicants
            fragment.projectInvite = projectInvite
            return fragment
        }
    }

    var projectSideApplicants: ProjectSideApplicants? = null
    var projectInvite: ProjectSideProjectInvite? = null
    lateinit var mContext: Context
    private var demand: ProjectDetailsDetails? = null
    var projectId = ""
    val mainId = 1


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
                backgroundColor = Color.WHITE
                val details = 2
                frameLayout {
                    id = details
                    demand = ProjectDetailsDetails.newInstance(mContext)
                    childFragmentManager.beginTransaction().add(details, demand!!).commit()
                }.lparams(matchParent, dip(0)) {
                    weight = 1f
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
            val it = retrofitUils.create(IndividualApi::class.java)
                .getProjectSideDetails(id)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!
                projectSideApplicants?.setProjectName(model.name)
                projectInvite?.setProjectName(model.name)
                demand?.setInfomation(model)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}