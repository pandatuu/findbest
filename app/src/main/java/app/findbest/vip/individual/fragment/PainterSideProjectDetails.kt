package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.BigImage2
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.fragment.ProjectApplicants
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

class PainterSideProjectDetails : Fragment(), IndividualProjectDemandDetails.ClickImage, BigImage2.ImageClick, BackgroundFragment.ClickBack {

    companion object {
        fun newInstance(
            context: Context,
            id: String,
            applicants: ProjectApplicants,
            painterInvite: PainterSideProjectInvite
        ): PainterSideProjectDetails {
            val fragment = PainterSideProjectDetails()
            fragment.mContext = context
            fragment.projectId = id
            fragment.applicants = applicants
            fragment.painterInvite = painterInvite
            return fragment
        }
    }

    lateinit var applicants: ProjectApplicants
    lateinit var painterInvite: PainterSideProjectInvite
    lateinit var mContext: Context
    private var demand: IndividualProjectDemandDetails? = null
    private var bigImage: BigImage2? = null
    private var backgroundFragment: BackgroundFragment? = null
    var projectId = ""
    val mainId = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun clickImage(str: String, b: Boolean) {
        val mainId = 1
        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@PainterSideProjectDetails)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@PainterSideProjectDetails, b)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, bigImage!!).commit()
        }
    }

    //点击放大的图片
    override fun clickclose() {
        closeDialog()
    }
    override fun clickAll() {
        closeDialog()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE
                val details = 2
                frameLayout {
                    id = details
                    backgroundColor = Color.WHITE
                    demand = IndividualProjectDemandDetails.newInstance(this@PainterSideProjectDetails)
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
            val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val systemCountry = mPerferences.getString("systemCountry", "").toString()
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getProjectInfoById(id, systemCountry)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!
                applicants.setProjectName(model.name)
                painterInvite.setProjectName(model.name)
                demand?.setInfomation(model)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private fun closeDialog() {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (backgroundFragment != null) {
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }

        if (bigImage != null) {
            mTransaction.remove(bigImage!!)
            bigImage = null
        }
        mTransaction.commit()
    }
}