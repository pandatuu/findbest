package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
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
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.activity.InstanceActivity
import app.findbest.vip.instance.activity.InstanceSearchActivity
import app.findbest.vip.instance.activity.InvitationActivity
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
import com.biao.pulltorefresh.OnRefreshListener
import com.biao.pulltorefresh.PtrHandler
import com.biao.pulltorefresh.PtrLayout
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.lang.Runnable


class InstanceDisplay : FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context


    lateinit var smart: SmartRefreshLayout


    var adapter: InstanceListAdapter? = null

    lateinit var recycler: RecyclerView

    private lateinit var search: EditText


    var pageNum = 1
    var pageSize = 20

    var list = mutableListOf<MutableList<Instance>>()
    var sonList = mutableListOf<Instance>()


    private lateinit var instanceApi: InstanceApi

    var screenWidth = 0
    var picWidth = 0

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
        fun newInstance(context: Context): InstanceDisplay {
            var f = InstanceDisplay()
            f.activityInstance = context


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


//    override fun onResume() {
//        super.onResume()
//    }


    private fun createView(): View {

        screenWidth = px2dp(getDisplay(mContext!!)!!.width.toFloat())
        picWidth = (screenWidth - 18) / 2
        if (picWidth < 180) {
        } else {
            picWidth = 180
        }
        list.add(sonList)

        var view = UI {
            verticalLayout {

                relativeLayout() {

                    setOnClickListener {
                        toast("123")
                        closeSoftKeyboard(search)
                    }


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
                            title = ""


                        }.lparams() {
                            width = matchParent
                            height = dip(65)
                            alignParentBottom()

                        }

                        textView {
                            text = "案例"
                            backgroundColor = Color.TRANSPARENT
                            gravity = Gravity.CENTER
                            textColor = Color.parseColor("#FF222222")
                            textSize = 17f
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams() {
                            width = matchParent
                            height = wrapContent
                            height = dip(65 - getStatusBarHeight(this@InstanceDisplay.context!!))
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


                linearLayout {


                    gravity = Gravity.CENTER_VERTICAL

                    linearLayout {


                        //去搜索
                        setOnClickListener {

                            var intent = Intent(activityInstance, InstanceSearchActivity::class.java)
                            activity!!.startActivityForResult(intent,100)
                            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

                        }

                        gravity = Gravity.CENTER_VERTICAL

                        backgroundResource = R.drawable.edit_text_background
                        imageView() {

                            setImageResource(R.mipmap.icon_search_nor)

                        }.lparams() {
                            width = dip(17)
                            height = dip(17)
                            leftMargin = dip(10)

                        }

                        isFocusable
                        search = editText {


                            //去搜索
                            setOnClickListener {

                                var intent = Intent(activityInstance, InstanceSearchActivity::class.java)
                                activity!!.startActivityForResult(intent,22)
                                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

                            }


                            setFocusable(false)
                            hint = "搜索"
                            hintTextColor = Color.parseColor("#ff666666")
                            backgroundColor = Color.TRANSPARENT

                            textColor = Color.BLACK
                            singleLine = true
                            gravity = Gravity.CENTER_VERTICAL
                            textSize = 13f
                            padding = dip(0)
                            clearFocus()

                        }.lparams() {
                            leftMargin = dip(9)
                            width = matchParent
                            height = matchParent
                        }


                    }.lparams() {
                        width = dip(280)
                        height = dip(30)
                    }


                    linearLayout {


                        setOnClickListener {


                        }



                        gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT



                        textView {
                            text = "筛选"
                            textSize = 15f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams() {
                            rightMargin = dip(7)
                        }

                        imageView() {

                            setImageResource(R.mipmap.icon_screening_nor)

                        }.lparams() {
                            width = dip(20)
                            height = dip(20)
                        }


                    }.lparams() {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(40)
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                }

                //////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////


                linearLayout {

                    setOnClickListener {
                        closeSoftKeyboard(search)
                    }

                    linearLayout {

                        backgroundColor = Color.parseColor("#FFF6F6F6")
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
                                overScrollMode = View.OVER_SCROLL_NEVER
                                layoutManager = LinearLayoutManager(mContext)
                            }

                        }.lparams(matchParent, matchParent) {
                        }

                    }.lparams {
                        width = matchParent
                        height = matchParent
                        leftMargin = dip(5)
                        rightMargin = dip(5)
                    }

                }.lparams() {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }


            }
        }.view




        return view

    }


    fun Refresh() {
        pageNum = 1
        sonList.clear()
        requestPicData(screenWidth, picWidth, null, null, null)

    }

    fun LoadMore() {
        pageNum = pageNum + 1
        requestPicData(screenWidth, picWidth, null, null, null)
    }


    fun requestPicData(
        screenWidth: Int,
        picWidth: Int,
        category: Int?,
        styles: Int?,
        content: String?
    ) {

        GlobalScope.launch {
            var response = instanceApi.instanceList(
                category,
                styles,
                pageNum,
                pageSize,
                content
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

                for (i in 0 until array.length()) {
                    var ob = array.getJSONObject(i)
                    sonList.add(
                        Instance(
                            ob.getString("url"),
                            ob.getString("logo"),
                            ob.getString("name"),
                            ob.getString("id")
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    appendRecyclerData(screenWidth, picWidth)
                }

            } else {

            }
        }
    }

    fun appendRecyclerData(
        screenWidth: Int,
        picWidth: Int
    ) {
        if (adapter == null) {
            //适配器
            adapter = InstanceListAdapter(recycler, screenWidth, picWidth, list, { item ->


                lateinit var intent: Intent
                //跳转详情
                intent = Intent(mContext, InstanceActivity::class.java)
                intent.putExtra("url", item.url)
                intent.putExtra("logo", item.logo)
                intent.putExtra("name", item.name)
                intent.putExtra("id", item.id)

                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)

            })
            //设置适配器
            recycler.setAdapter(adapter)
        } else {

            adapter!!.addInstanceList(sonList, pageNum, pageSize)

        }


    }



    fun searchByContent(content:String){

        pageNum = 1
        sonList.clear()
        requestPicData(screenWidth, picWidth, null, null, content)

    }


}