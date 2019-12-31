package app.findbest.vip.instance.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.adapter.MyProjectListAdapter
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
import java.text.SimpleDateFormat
import java.util.*


class InvitationList : FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null

    var pageNum = 1
    var pageSize = 20

    var screenWidth = 0
    var picWidth = 0
    lateinit var smart: SmartRefreshLayout


    var adapter: MyProjectListAdapter? = null

    lateinit var recycler: RecyclerView

    var sdf = SimpleDateFormat("yyyy-MM-dd")

    private lateinit var instanceApi: InstanceApi

    var selectedItem = mutableListOf<String>()
    var outId = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }

        instanceApi = RetrofitUtils(context!!, this.getString(R.string.developmentUrl))
            .create(InstanceApi::class.java)
    }

    companion object {
        fun newInstance(): InvitationList {
            var f = InvitationList()


            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = createView()
        smart.autoRefresh()

        return fragmentView
    }


    private fun createView(): View {

        screenWidth = px2dp(getDisplay(mContext!!)!!.width.toFloat())
        picWidth = (screenWidth - 18) / 2
        if (picWidth < 180) {
        } else {
            picWidth = 180
        }


        var view = UI {

            frameLayout {
                id = outId
                verticalLayout {

                    relativeLayout() {

                        textView() {
                            backgroundColor = Color.parseColor("#FFE3E3E3")
                        }.lparams() {
                            width = matchParent
                            height = dip(1)
                            alignParentBottom()

                        }
                        relativeLayout() {


                            toolbar1 = toolbar {
                                backgroundResource = R.color.transparent
                                isEnabled = true
                                navigationIconResource = R.mipmap.nav_ico_return

                                title = ""
                                this.withTrigger().click {

                                    activity!!.finish()
                                    activity!!.overridePendingTransition(
                                        R.anim.left_in,
                                        R.anim.right_out
                                    )

                                }
                            }.lparams() {
                                width = matchParent
                                height = dip(65)
                                alignParentBottom()
                                height = dip(65 - getStatusBarHeight(this@InvitationList.context!!))
                            }



                            textView {
                                text = activity!!.getString(R.string.chose_project)
                                backgroundColor = Color.TRANSPARENT
                                gravity = Gravity.CENTER
                                textColor = Color.parseColor("#FF222222")
                                textSize = 17f
                                setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                            }.lparams() {
                                width = matchParent
                                height = wrapContent
                                height = dip(65 - getStatusBarHeight(this@InvitationList.context!!))
                                alignParentBottom()
                            }

                        }.lparams() {
                            width = matchParent
                            height = dip(65)
                        }
                    }.lparams() {
                        width = matchParent
                        height = dip(65)
                    }

                    //////////////////////////////////////////////////////////////////
                    /////////////////////////////////////////////////////////////////


                    textView() {
                        backgroundColor = Color.parseColor("#FFE3E3E3")
                    }.lparams() {
                        width = matchParent
                        height = dip(1)

                    }

                    //////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////


                    linearLayout {

                        backgroundColor = Color.parseColor("#FFF6F6F6")

                        gravity = Gravity.CENTER




                        smart = smartRefreshLayout {
                            setEnableAutoLoadMore(false)
                            setRefreshHeader(MaterialHeader(activity))
                            setOnRefreshListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    Refresh()
                                    it.finishRefresh(300)
                                }
                            }
                            setOnLoadMoreListener {
                                LoadMore()
                                it.finishLoadMore(300)
                            }
                            setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))

                            recycler = recyclerView {
                                layoutManager = LinearLayoutManager(mContext)
                                overScrollMode = View.OVER_SCROLL_NEVER

                            }

                        }.lparams(matchParent, matchParent) {
                        }


                    }.lparams() {
                        height = dip(0)
                        weight = 1f
                        width = matchParent
                    }



                    textView {

                        gravity = Gravity.CENTER
                        text=activity!!.getString(R.string.send_invitation)
                        textSize = 16f
                        textColor = Color.WHITE
                        backgroundResource = R.drawable.enable_rectangle_button



                        this.withTrigger().click {
                            if (selectedItem.size > 0) {
                                invite()
                            }


                        }


                    }.lparams() {
                        height = dip(50)
                        width = matchParent
                    }


                }.lparams() {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view



        return view

    }


    fun Refresh() {
        pageNum = 1
        adapter?.clear()
        requestTheList()

    }

    fun LoadMore() {
        pageNum = pageNum + 1
        requestTheList()
    }


    fun requestTheList() {

        GlobalScope.launch {
            var response = instanceApi.getInviteProjectList(
                pageNum,
                pageSize,
                activity!!.intent.getStringExtra("id")
            )
                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
                .awaitSingle()

            if (response.code() == 200) {
                println("得到的数据")
                println(response.body())

                var jsonObject = JSONObject(response.body().toString())
                var array = jsonObject.getJSONArray("data")
                if (array.length() == 0 && pageNum > 1) {
                    pageNum = pageNum - 1
                }
                var dataList = mutableListOf<ProjectItem>()

                for (i in 0 until array.length()) {
                    var ob = array.getJSONObject(i)

                    dataList.add(
                        ProjectItem(
                            DataDictionary.getFormat(ob.getString("format").toInt()),
                            ob.getString("size"),
                            ob.getString("name"),
                            ob.getString("id"),
                            sdf.format(Date(ob.getString("createAt").toLong()))
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    appendRecyclerData(dataList, screenWidth, picWidth)
                }

            } else {

            }
        }


    }


    fun invite() {
        GlobalScope.launch {

            var json = JSONObject()
            json.put("providerId", activity!!.intent.getStringExtra("id"))

            val projectIds = JSONArray()

            for (i in 0 until selectedItem.size) {
                projectIds.put(selectedItem.get(i))
            }
            json.put("projectIds", projectIds)


            val body = RequestBody.create(MimeType.APPLICATION_JSON, json.toString())

            var response = instanceApi.invitePainterAndGroup(
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

                var manager = childFragmentManager.beginTransaction()
                val messageDialog = MessageDialog.newInstance(manager, activity!!)
                childFragmentManager.beginTransaction().add(outId, messageDialog).commit()

            } else {

            }
        }


    }

    fun appendRecyclerData(
        list: MutableList<ProjectItem>,
        screenWidth: Int,
        picWidth: Int
    ) {
        if (adapter == null) {
            //适配器
            adapter = MyProjectListAdapter(recycler, screenWidth, picWidth, list, { item, f ->

                if (f) {
                    selectedItem.add(item.id)
                } else {
                    selectedItem.remove(item.id)
                }
            })
            //设置适配器
            recycler.setAdapter(adapter)
        } else {

            adapter!!.addDataList(list)

        }


    }


}