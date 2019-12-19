package app.findbest.vip.instance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.view.*
import org.jetbrains.anko.*
import android.graphics.Typeface
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.utils.shapeImageView
import android.opengl.ETC1.getWidth
import android.content.Context.WINDOW_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.utils.RetrofitUtils
import com.biao.pulltorefresh.OnRefreshListener
import com.biao.pulltorefresh.PtrHandler
import com.biao.pulltorefresh.PtrLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.support.v4.toast
import retrofit2.http.Query


class InstanceDisplay : FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null

    lateinit var ptrLayout: PtrLayout
    lateinit var header: View
    lateinit var footer: View
    lateinit var glide: RequestManager

    var adapter: InstanceListAdapter? = null

    lateinit var recycler: RecyclerView

    private lateinit var search: EditText


    var pageNum=1
    var pageSize=10

    private lateinit var instanceApi: InstanceApi

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
        fun newInstance( glide: RequestManager): InstanceDisplay {
            var f = InstanceDisplay()
            f.glide=glide
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = createView()
        return fragmentView
    }

    private fun createView(): View {

        var screenWidth=px2dp(getDisplay(mContext!!)!!.width.toFloat())
        var picWidth = (screenWidth - 10) / 2
        if (picWidth < 180) {
        } else {
            picWidth = 180
        }


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
                            textSize = 16f
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


                    setOnClickListener {
                        search.requestFocus()
                        val imm =
                            search.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(search, 0)
                    }


                    gravity = Gravity.CENTER_VERTICAL

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL

                        backgroundResource = R.drawable.edit_text_background
                        imageView() {

                            setImageResource(R.mipmap.icon_search_nor)

                        }.lparams() {
                            width = dip(17)
                            height = dip(17)
                            leftMargin = dip(10)

                        }

                        editText {
                        }.lparams() {
                            width = dip(0)
                            height = dip(0)
                        }

                        isFocusable
                        search = editText {
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


                var pullToRefreshContainer =
                    LayoutInflater.from(context).inflate(R.layout.springback_recycler_view, null);
                ptrLayout = pullToRefreshContainer as PtrLayout



                recycler =
                    pullToRefreshContainer.findViewById(R.id.SBRecyclerView) as RecyclerView

                recycler.overScrollMode = View.OVER_SCROLL_NEVER
                recycler.setLayoutManager(LinearLayoutManager(pullToRefreshContainer.getContext()))

                //顶部刷新显示
                header =
                    LayoutInflater.from(context).inflate(R.layout.fresh_header, null)
                //底部刷新显示
                footer =
                    LayoutInflater.from(context).inflate(R.layout.fresh_footer, null)

                //顶部刷新，展示的文字
                var freshText = header.findViewById<TextView>(R.id.freshText)
                //底部刷新展示的文字
                var footerFreshText = footer.findViewById<TextView>(R.id.footerFreshText)

                ptrLayout.setHeaderView(header)
                ptrLayout.setFooterView(footer)



                ptrLayout.setHeaderPtrHandler(object : PtrHandler {
                    /** when refresh pulling  */
                    override fun onPercent(percent: Float) {

                        if (percent == 0.0f) {
                            freshText.setText("スライドでロード")//下拉刷新
                        }

                        if (percent == 1.0f) {
                            freshText.setText("リリースでロード")//释放更新
                        }

                    }

                    /** when refresh end  */
                    override fun onRefreshEnd() {


                    }

                    /** when refresh begin  */
                    override fun onRefreshBegin() {
                        freshText.setText("ローディング...")//加载中

                    }

                })


                ptrLayout.setFootererPtrHandler(object : PtrHandler {
                    /** when refresh pulling  */
                    override fun onPercent(percent: Float) {


                        if (percent == 0.0f) {
                            footerFreshText.setText("スライドでロード")//上拉刷新
                        }

                        if (percent == 1.0f) {
                            footerFreshText.setText("リリースでロード")//释放更新
                        }

                    }

                    /** when refresh end  */
                    override fun onRefreshEnd() {


                    }

                    /** when refresh begin  */
                    override fun onRefreshBegin() {
                        footerFreshText.setText("ローディング...")//加载中

                    }

                })


                ptrLayout.setMode(PtrLayout.MODE_ALL_MOVE)
                ptrLayout.setDuration(10)



                ptrLayout.setOnPullDownRefreshListener(object : OnRefreshListener {
                    override fun onRefresh() {

                        var sMainHandler = Handler(Looper.getMainLooper())
                        sMainHandler.post(Runnable {
                            Thread.sleep(1000)
                            header.postDelayed(Runnable {
                                ptrLayout.onRefreshComplete()
                            }, 2000)

                            footer.postDelayed(Runnable {
                                ptrLayout.onRefreshComplete()
                            }, 2000)
                        })
                }})


                ptrLayout.setOnPullUpRefreshListener(object : OnRefreshListener {
                    override fun onRefresh() {
                       var sMainHandler = Handler(Looper.getMainLooper())
                        sMainHandler.post(Runnable {
                            Thread.sleep(1000)
                            header.postDelayed(Runnable {
                                ptrLayout.onRefreshComplete()
                            }, 2000)

                            footer.postDelayed(Runnable {
                                ptrLayout.onRefreshComplete()
                            }, 2000)
                        })
                    }

                })



                linearLayout {

                    setOnClickListener {
                        closeSoftKeyboard(search)
                    }

                    linearLayout {

                        addView(ptrLayout)

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

        //appendRecyclerData(screenWidth,picWidth)

        requestPicData(null,null,null)

        return view

    }


    fun requestPicData( category :Int?,
                        styles :Int?,
                        content :String?){

        GlobalScope.launch {
          var response= instanceApi.instanceList(
                category,
                styles,
                pageNum,
                pageSize,
                content
            )
                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
                .awaitSingle()
            println("ssssssssssssssssssssssssssssssssssssssss")
            println(response)

        }

    }

    fun appendRecyclerData(
        screenWidth: Int,
        picWidth: Int
    ) {


        var list = mutableListOf<MutableList<Instance>>()
        var sonList = mutableListOf<Instance>()
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        sonList.add(Instance("x"))
        list.add(sonList)

        if (adapter == null) {

            //适配器
            adapter = InstanceListAdapter(recycler, screenWidth,picWidth, glide,list, { item ->

                toast("xx")
            })
            //设置适配器
            recycler.setAdapter(adapter)
        } else {

            // adapter!!.addRecruitInfoList(list)

        }


    }


}