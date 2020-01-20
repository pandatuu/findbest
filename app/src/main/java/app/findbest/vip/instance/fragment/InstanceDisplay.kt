package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toolbar
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.view.InstanceSearchActivity
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.smartRefreshLayout
import com.google.gson.JsonObject
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import retrofit2.HttpException


class InstanceDisplay : FragmentParent(), BackgroundFragment.ClickBack,InstanceScreen.ProjectScreen {

    companion object {
        fun newInstance(context: Context): InstanceDisplay {
            val f = InstanceDisplay()
            f.mContext = context
            return f
        }
    }
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var smart: SmartRefreshLayout
    var adapter: InstanceListAdapter? = null
    private lateinit var search: EditText

    val mainId = 1
    private var backgroundFragment: BackgroundFragment? = null
    private var instanceMainScreen: InstanceScreen? = null
    private lateinit var instanceApi: InstanceApi
    var nowPage = 0
    var mCategory = 0
    var mStyle = 0
    val imageList: MutableList<JsonObject> = mutableListOf()
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: InstanceDisplayCenter
    private var nullData: NullDataPageFragment? = null
    private val nullId = 4

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

    override fun onResume() {
        super.onResume()
//        val bool = App.getInstance()?.getInviteVideoBool()
//        if(bool!!){
//            startActivity<VideoResultActivity>()
//            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = createView()
        smart.autoRefresh()
        return fragmentView
    }

    override fun clickAll() {
        closeAlertDialog()
    }

    override fun backgroundClick() {
        closeAlertDialog()
    }

    override fun confirmClick(array: ArrayList<Int>) {
        mCategory = array[0]
        mStyle = array[1]
        smart.autoRefresh()
        listFragment = InstanceDisplayCenter.newInstance(mContext!!)
        childFragmentManager.beginTransaction().replace(nullId,listFragment).commit()
        closeAlertDialog()
    }


    private fun createView(): View {
        return UI {
            verticalLayout {
                relativeLayout {
                    setOnClickListener {
                        closeSoftKeyboard(search)
                    }
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
                            title = ""
                        }.lparams {
                            width = matchParent
                            height = dip(65)
                            alignParentBottom()
                        }
                        textView {
                            text = activity!!.getString(R.string.instance_title)
                            backgroundColor = Color.TRANSPARENT
                            gravity = Gravity.CENTER
                            textColor = Color.parseColor("#FF222222")
                            textSize = 17f
                            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                            height = dip(65 - getStatusBarHeight(this@InstanceDisplay.context!!))
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

                linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    linearLayout {
                        //去搜索
                        setOnClickListener {
                            val intent = Intent(mContext, InstanceSearchActivity::class.java)
                            activity!!.startActivityForResult(intent, 100)
                            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
                        }
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundResource = R.drawable.edit_text_background
                        imageView {
                            setImageResource(R.mipmap.icon_search_nor)
                        }.lparams {
                            width = dip(17)
                            height = dip(17)
                            leftMargin = dip(10)
                        }

                        isFocusable
                        search = editText {
                            //去搜索
                            setOnClickListener {
                                val intent = Intent(mContext, InstanceSearchActivity::class.java)
                                activity!!.startActivityForResult(intent, 22)
                                activity!!.overridePendingTransition(
                                    R.anim.right_in,
                                    R.anim.left_out
                                )
                            }
                            isFocusable = false
                            hint = resources.getString(R.string.common_search)
                            hintTextColor = Color.parseColor("#ff666666")
                            backgroundColor = Color.TRANSPARENT
                            textColor = Color.BLACK
                            singleLine = true
                            gravity = Gravity.CENTER_VERTICAL
                            textSize = 13f
                            padding = dip(0)
                            clearFocus()
                        }.lparams {
                            leftMargin = dip(9)
                            width = matchParent
                            height = matchParent
                        }
                    }.lparams {
                        width = dip(280)
                        height = dip(30)
                    }

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
                        textView {
                            text = resources.getString(R.string.common_srceen)
                            textSize = 15f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams {
                            rightMargin = dip(7)
                        }
                        imageView {
                            setImageResource(R.mipmap.icon_screening_nor)
                        }.lparams {
                            width = dip(20)
                            height = dip(20)
                        }
                        setOnClickListener {
                            openDialog()
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }
                }.lparams {
                    width = matchParent
                    height = dip(40)
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                }

                linearLayout {
                    setOnClickListener {
                        closeSoftKeyboard(search)
                    }
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                        smart = smartRefreshLayout {
                            setEnableAutoLoadMore(false)
                            setRefreshHeader(MaterialHeader(mContext))
                            setOnRefreshListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    getList()
                                    it.finishRefresh(1000)
                                }
                            }
                            setRefreshFooter(
                                BallPulseFooter(mContext).setSpinnerStyle(
                                    SpinnerStyle.Scale
                                )
                            )
                            setOnLoadMoreListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    getList(nowPage + 1)
                                    it.finishLoadMore(1000)
                                }
                            }
//
//                            recycler = recyclerView {
//                                layoutManager = LinearLayoutManager(mContext)
//                                painterInfoPic = PainterInfoPictureAdapter(
//                                    mContext!!,
//                                    arrayListOf(),this@InstanceDisplay)
//                                adapter = painterInfoPic
//                            }
//                            val lp = recycler.layoutParams
//                            lp.width = LinearLayout.LayoutParams.MATCH_PARENT
//                            lp.height = LinearLayout.LayoutParams.MATCH_PARENT
                            listFram = frameLayout {
                                id = nullId
                                listFragment = InstanceDisplayCenter.newInstance(mContext!!)
                                childFragmentManager.beginTransaction().replace(nullId,listFragment).commit()
                            }
                            val listFramlp = listFram.layoutParams
                            listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                            listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                        }.lparams(matchParent, matchParent)
                    }.lparams {
                        width = matchParent
                        height = matchParent
                        leftMargin = dip(5)
                        rightMargin = dip(5)
                    }
                }.lparams {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }
            }
        }.view
    }

    private suspend fun getList() {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = if (mCategory != 0 && mStyle != 0) {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(1, 6, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else if(mCategory != 0 && mStyle == 0) {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(1, 6, mCategory, null)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }else{
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(1, 6)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val item = it.body()!!.data
                if(item.size()>0){
                    nowPage = 1
                    listFragment.resetItems(item,nowPage)
                }else{
                    if(nullData == null){
                        nullData = NullDataPageFragment.newInstance()
                        childFragmentManager.beginTransaction().add(nullId,nullData!!).commit()
                    }
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }


    private suspend fun getList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = if (mStyle != 0 && mCategory != 0) {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(page, 6, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else if(mCategory != 0 && mStyle == 0) {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(page, 6, mCategory, null)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(page, 6)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val item = it.body()!!.data
                if (item.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                nowPage = page
                listFragment.addItems(item,nowPage)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }


    private fun openDialog() {
        val mTransaction = activity!!.supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@InstanceDisplay)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        instanceMainScreen = InstanceScreen.newInstance(mContext!!,this@InstanceDisplay)
        mTransaction.add(mainId, instanceMainScreen!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (instanceMainScreen != null) {
            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)

            mTransaction.remove(instanceMainScreen!!)
            instanceMainScreen = null
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