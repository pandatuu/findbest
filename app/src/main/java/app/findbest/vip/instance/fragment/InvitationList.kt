package app.findbest.vip.instance.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.view.*
import org.jetbrains.anko.*
import android.graphics.Typeface
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.widget.*
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.ProjectItem
import app.findbest.vip.utils.*
import click
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import withTrigger


class InvitationList : FragmentParent(), InstanceListFragment.InstanceList {
    override fun clickInvite(f: Boolean, item: ProjectItem) {
        if (f) {
            selectedItem.add(item.id)
        } else {
            selectedItem.remove(item.id)
        }
    }

    companion object {
        fun newInstance(): InvitationList {
            return InvitationList()
        }
    }

    var toolbar1: Toolbar? = null
    private var mContext: Context? = null

    private var pageNum = 1
    private var pageSize = 20
    lateinit var smart: SmartRefreshLayout
    @SuppressLint("SimpleDateFormat")
    private lateinit var instanceApi: InstanceApi
    private var selectedItem = mutableListOf<String>()
    private var outId = 111
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: InstanceListFragment
    private var nullData: NullDataPageFragment? = null
    private val nullId = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = if (parentFragment != null) {
            parentFragment?.context
        } else {
            activity
        }
        instanceApi = RetrofitUtils(context!!, this.getString(R.string.developmentUrl))
            .create(InstanceApi::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = createView()
        smart.autoRefresh()
        return fragmentView
    }


    private fun createView(): View {
        return UI {
            frameLayout {
                id = outId
                verticalLayout {
                    relativeLayout {
                        textView {
                            backgroundColor = Color.parseColor("#FFE3E3E3")
                        }.lparams {
                            width = matchParent
                            height = dip(1)
                            alignParentBottom()
                        }
                        relativeLayout {
                            toolbar1 = toolbar {
                                backgroundResource = R.color.transparent
                                isEnabled = true
                                navigationIconResource = R.mipmap.nav_ico_return
                                title = ""
                                withTrigger().click {
                                    activity!!.finish()
                                    activity!!.overridePendingTransition(
                                        R.anim.left_in,
                                        R.anim.right_out
                                    )
                                }
                            }.lparams {
                                width = matchParent
                                height = dip(65)
                                alignParentBottom()
                                height = dip(65 - getStatusBarHeight(this@InvitationList.context!!))
                            }

                            textView {
                                text = activity!!.getString(R.string.instance_chose_project)
                                backgroundColor = Color.TRANSPARENT
                                gravity = Gravity.CENTER
                                textColor = Color.parseColor("#FF222222")
                                textSize = 17f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams {
                                width = matchParent
                                height = wrapContent
                                height = dip(65 - getStatusBarHeight(this@InvitationList.context!!))
                                alignParentBottom()
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(65)
                        }
                    }.lparams {
                        width = matchParent
                        height = dip(65)
                    }

                    textView {
                        backgroundColor = Color.parseColor("#FFE3E3E3")
                    }.lparams {
                        width = matchParent
                        height = dip(1)
                    }

                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                        gravity = Gravity.CENTER
                        smart = smartRefreshLayout {
                            setEnableAutoLoadMore(false)
                            setRefreshHeader(MaterialHeader(activity))
                            setOnRefreshListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    refresh()
                                    it.finishRefresh(300)
                                }
                            }
                            setOnLoadMoreListener {
                                loadMore()
                                it.finishLoadMore(300)
                            }
                            setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))


                            listFram = frameLayout {
                                id = nullId
                                listFragment = InstanceListFragment.newInstance(mContext!!,this@InvitationList)
                                childFragmentManager.beginTransaction().add(nullId, listFragment).commit()
                            }
                            val listFramlp = listFram.layoutParams
                            listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                            listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                        }.lparams(matchParent, matchParent)
                    }.lparams {
                        height = dip(0)
                        weight = 1f
                        width = matchParent
                    }

                    textView {
                        gravity = Gravity.CENTER
                        text = activity!!.getString(R.string.instance_send_invitation)
                        textSize = 16f
                        textColor = Color.WHITE
                        backgroundResource = R.drawable.enable_rectangle_button
                        withTrigger().click {
                            if (selectedItem.size > 0) {
                                invite()
                            }
                        }
                    }.lparams {
                        height = dip(50)
                        width = matchParent
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view
    }

    fun refresh() {
        pageNum = 1
        listFragment.refresh()
        requestTheList()
    }

    private fun loadMore() {
        pageNum += 1
        requestTheList()
    }


    private fun requestTheList() {
        GlobalScope.launch {
            val response = instanceApi.getInviteProjectList(
                pageNum,
                pageSize,
                activity!!.intent.getStringExtra("id")
            )
                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
                .awaitSingle()

            if (response.code() == 200) {
                println("得到的数据")
                println(response.body())

                val jsonObject = JSONObject(response.body().toString())
                val array = jsonObject.getJSONArray("data")
                if(array.length()>0){
                    if (array.length() == 0 && pageNum > 1) {
                        pageNum -= 1
                    }
                    listFragment.addView(array)
                }else{
                    nullData = NullDataPageFragment.newInstance()
                    childFragmentManager.beginTransaction().replace(nullId,nullData!!).commit()
                }
            }
        }
    }


    private fun invite() {
        GlobalScope.launch {

            val json = JSONObject()
            json.put("providerId", activity!!.intent.getStringExtra("id"))

            val projectIds = JSONArray()

            for (i in 0 until selectedItem.size) {
                projectIds.put(selectedItem[i])
            }
            json.put("projectIds", projectIds)


            val body = RequestBody.create(MimeType.APPLICATION_JSON, json.toString())

            val response = instanceApi.invitePainterAndGroup(
                body
            )
                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
                .awaitSingle()
            println("得到的数据")
            println(response.body())
            println(response.code())
            if (response.code() == 200) {
                println("得到的数据")
                println(response.body())

                val manager = childFragmentManager.beginTransaction()
                val messageDialog = MessageDialog.newInstance(manager, activity!!)
                childFragmentManager.beginTransaction().add(outId, messageDialog).commit()
            }
        }


    }

}