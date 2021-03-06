package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.individual.api.IndividualApi
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.fragment.EnlistCheckTipsDialog
import app.findbest.vip.project.view.EnlistProject
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
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

class PainterSideProjectInvite : Fragment(), PainterSideInvite.ChooseStatus, BackgroundFragment.ClickBack,
    ChooseRefuse.DialogSelect, EnlistCheckTipsDialog.ButtomClick{

    companion object {
        fun newInstance(context: Context, id: String): PainterSideProjectInvite {
            val fragment = PainterSideProjectInvite()
            fragment.mContext = context
            fragment.projectId = id
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var name = ""
    private var painterSideInvite: PainterSideInvite? = null
    private var nullData: NullDataPageFragment? = null
    private var tipsDialog: EnlistCheckTipsDialog? = null
    private var backgroundFragment: BackgroundFragment? = null
    private var chooseRefuse: ChooseRefuse? = null

    var projectId = ""
    private val inviteId = 2
    private var painterInviterId = ""
    val mainId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }
    //同意邀请
    override fun agree() {
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getAppliesValidation(projectId)
        }
    }
    //拒绝邀请
    override fun refuse() {
        openDialog()
    }
    override fun clickAll() {
        closeDialog()
        closeTipsDialog()
    }
    //拒绝理由
    override fun getSelectedItem(str: String) {
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            refuseInvite(painterInviterId, str)
        }
        closeDialog()
    }
    override fun click() {
        closeTipsDialog()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.parseColor("#FFF6F6F6")
                verticalLayout {
                    linearLayout {
                        backgroundColor = Color.WHITE
                        textView {
                            text = name
                            textSize = 21f
                            textColor = Color.parseColor("#FF202020")
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                            setMargins(dip(15),dip(20),dip(15),dip(20))
                        }
                    }.lparams(matchParent, wrapContent)
                    frameLayout {
                        id = inviteId
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent)
            }
        }.view
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getPainterSideInviteById(projectId)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if(resultCode==1001){
                painterSideInvite?.updateSatuts(true)
            }
        }
    }
    //获取收到的邀请
    private suspend fun getPainterSideInviteById(id: String) {
        try {
            val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val systemCountry = mPerferences.getString("systemCountry", "").toString()
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .getPainterSideInviteById(id,systemCountry)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                val model = it.body()!!
                if(model.size()>0){
                    painterInviterId = model["id"].asString
                    painterSideInvite = PainterSideInvite.newInstance(mContext,this@PainterSideProjectInvite,model)
                    childFragmentManager.beginTransaction().add(inviteId,painterSideInvite!!).commit()
                }else{
                    if(nullData==null){
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().replace(inviteId,nullData!!).commit()
                    }
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
    //同意邀请
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
                    val intent = Intent(mContext, EnlistProject::class.java)
                    intent.putExtra("projectId", id)
                    activity!!.startActivityForResult(intent, 1001)
                    activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else {
                    openTipsDialog(status)
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
    //拒绝邀请
    private suspend fun refuseInvite(inviteId: String, str: String) {
        try {
            val reason = when(str){
                resources.getString(R.string.my_project_invite_refuse_one) -> 42
                resources.getString(R.string.my_project_invite_refuse_two) -> 44
                resources.getString(R.string.my_project_invite_refuse_three) -> 46
                resources.getString(R.string.my_project_invite_refuse_four) -> 49
                else -> 0
            }
            val params = if(reason==49){
                mapOf(
                    "reason" to reason,
                    "content" to resources.getString(R.string.my_project_invite_refuse_four)
                )
            }else{
                mapOf(
                    "reason" to reason
                )
            }
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(IndividualApi::class.java)
                .painterRefuseInvite(inviteId,body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if (it.code() in 200..299) {
                painterSideInvite?.updateSatuts(false)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.message())
            }
        }
    }

    fun setProjectName(str: String) {
        name = str
    }
    private fun openTipsDialog(status: Int) {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@PainterSideProjectInvite)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        tipsDialog = EnlistCheckTipsDialog.newInstance(this@PainterSideProjectInvite, status)
        mTransaction.add(mainId, tipsDialog!!)

        mTransaction.commit()
    }

    private fun closeTipsDialog() {
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
    private fun openDialog(){
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@PainterSideProjectInvite)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.bottom_in_a, R.anim.bottom_in_a)

        chooseRefuse = ChooseRefuse.newInstance(mContext,this@PainterSideProjectInvite)
        mTransaction.add(mainId, chooseRefuse!!)

        mTransaction.commit()
    }

    private fun closeDialog(){
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (chooseRefuse != null) {
            mTransaction.setCustomAnimations(R.anim.bottom_out_a, R.anim.bottom_out_a)

            mTransaction.remove(chooseRefuse!!)
            chooseRefuse = null
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
}