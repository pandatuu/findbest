package app.findbest.vip.project.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
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
import app.findbest.vip.project.view.EnlistProject
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
import click
import com.alibaba.fastjson.JSON
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import retrofit2.HttpException
import withTrigger

class ProjectDemand : Fragment(), EnlistCheckTipsDialog.ButtomClick, BackgroundFragment.ClickBack, ProjectDemandDetails.ClickImage, BigImage2.ImageClick {

    companion object {
        fun newInstance(
            context: Context,
            id: String,
            applicants: ProjectApplicants
        ): ProjectDemand {
            val fragment = ProjectDemand()
            fragment.mContext = context
            fragment.projectId = id
            fragment.applicants = applicants
            return fragment
        }
    }

    lateinit var mContext: Context
    var demand: ProjectDemandDetails? = null
    var projectId = ""
    val mainId = 1

    private lateinit var applicants: ProjectApplicants
    private var backgroundFragment: BackgroundFragment? = null
    private var bigImage: BigImage2? = null
    private var tipsDialog: EnlistCheckTipsDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    override fun clickAll() {
//        closeAlertDialog()
    }

    override fun click() {
        closeAlertDialog()
    }
    //点击放大的图片
    override fun clickclose() {
        closeBigDialog()
    }

    override fun clickImage(str: String, b: Boolean) {
        val mainId = 1
        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectDemand)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@ProjectDemand, b)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, bigImage!!).commit()
        }
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE
                val details = 2
                frameLayout {
                    id = details
                    demand = ProjectDemandDetails.newInstance(this@ProjectDemand)
                    childFragmentManager.beginTransaction().add(details, demand!!).commit()
                }.lparams(matchParent, dip(0)) {
                    weight = 1f
                    bottomMargin = dip(20)
                }
                button {
                    backgroundResource = R.drawable.enable_around_button
                    text = resources.getString(R.string.project_info_button)
                    textSize = 16f
                    textColor = Color.parseColor("#FFFFFFFF")
                    this.withTrigger().click {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            getAppliesValidation(projectId)
                        }
                    }
                }.lparams(dip(300), dip(50)) {
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
                demand?.setInfomation(model)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun getAppliesValidation(id: String) {
        try {
            val params = mapOf(
                "projectId" to id
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getAppliesValidation(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val status = it.body()!!["status"].asInt
                if (status == 0) {
                    activity!!.startActivity<EnlistProject>("projectId" to id)
                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else {
                    openDialog(status)
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private fun openDialog(status: Int) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectDemand)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        tipsDialog = EnlistCheckTipsDialog.newInstance(this@ProjectDemand, status)
        mTransaction.add(mainId, tipsDialog!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (tipsDialog != null) {
            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)

            mTransaction.remove(tipsDialog!!)
            tipsDialog = null
        }

        if (backgroundFragment != null) {
            mTransaction.setCustomAnimations(
                R.anim.fade_in_out_a, R.anim.fade_in_out_a
            )
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }
        mTransaction.commit()
    }

    private fun closeBigDialog() {
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