package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.painter.fragment.BigImage2
import app.findbest.vip.project.adapter.ProjectApplicantsAdapter
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
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

class ProjectApplicants : Fragment(), ProjectApplicantsAdapter.PrintedCrad,
    BackgroundFragment.ClickBack, BigImage2.ImageClick {

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
    private var bigImage: BigImage2? = null
    private var backgroundFragment: BackgroundFragment? = null
    private var recycle: RecyclerView? = null

    var projectId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //单独点击某一张画
    override fun oneClick(str: String) {
        val mainId = 1
        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@ProjectApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, backgroundFragment!!)
                .commit()
        }
        if (bigImage == null) {
            bigImage = BigImage2.newInstance(str, this@ProjectApplicants)
            activity!!.supportFragmentManager.beginTransaction().add(mainId, bigImage!!).commit()
        }
    }

    //点击黑色背景
    override fun clickAll() {
        closeDialog()
    }

    //点击放大的图片
    override fun clickclose() {
        closeDialog()
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
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                            leftMargin = dip(15)
                        }
                    }.lparams(matchParent, dip(70))
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                    }.lparams(matchParent, dip(5))
                    recycle = recyclerView {
                        layoutManager = LinearLayoutManager(mContext)
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(10)
                    }
                }
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
                val mutableList = mutableListOf<JsonObject>()
                array.forEach {
                    mutableList.add(it.asJsonObject)
                }
                val applicants =
                    ProjectApplicantsAdapter(this@ProjectApplicants.context!!, this@ProjectApplicants, mutableList)
                recycle?.adapter = applicants
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

    fun setProjectName(str: String) {
        name.text = str
    }
}