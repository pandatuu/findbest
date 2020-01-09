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
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.application.App
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.instance.view.InstanceActivity
import app.findbest.vip.instance.view.InstanceSearchActivity
import app.findbest.vip.message.activity.VideoResultActivity
import app.findbest.vip.painter.adapter.PainterInfoPictureAdapter
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.smartRefreshLayout
import com.google.gson.JsonObject
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.HttpException


class InstanceDisplay : FragmentParent(),PainterInfoPictureAdapter.ImageClick, BackgroundFragment.ClickBack,InstanceScreen.ProjectScreen {

    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var smart: SmartRefreshLayout
    var adapter: InstanceListAdapter? = null
    private lateinit var search: EditText

    val mainId = 1
    var list = mutableListOf<MutableList<Instance>>()
    var sonList = mutableListOf<Instance>()
    private var backgroundFragment: BackgroundFragment? = null
    private var instanceMainScreen: InstanceScreen? = null
    private lateinit var instanceApi: InstanceApi
    var screenWidth = 0
    var picWidth = 0
    var nowPage = 0
    var mCategory = 0
    var mStyle = 0
    private lateinit var recycler: RecyclerView
    private var painterInfoPic: PainterInfoPictureAdapter? = null
    val imageList: MutableList<JsonObject> = mutableListOf()

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
            val f = InstanceDisplay()
            f.mContext = context
            return f
        }
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
        closeAlertDialog()
    }

    override fun click(str: String) {
        imageList.forEach {
            if(str == it["url"].asString){
                val intent = Intent(mContext, InstanceActivity::class.java)
                //跳转详情
                intent.putExtra("url", if(!it["url"].isJsonNull) it["url"].asString else "")
                intent.putExtra("logo", if(!it["logo"].isJsonNull) it["logo"].asString else "")
                intent.putExtra("name", if(!it["name"].isJsonNull) it["name"].asString else "")
                intent.putExtra("id", if(!it["id"].isJsonNull) it["id"].asString else "")
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        }
    }

    private fun createView(): View {
        screenWidth = px2dp(getDisplay(mContext!!)!!.width.toFloat())
        picWidth = (screenWidth - 18) / 2
        if (picWidth < 180) {
        } else {
            picWidth = 180
        }
        list.add(sonList)

        return UI {
            verticalLayout {
                relativeLayout {
                    setOnClickListener {
                        toast("123")
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
                            text = activity!!.getString(R.string.sample)
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
                            hint = "搜索"
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
                            text = "筛选"
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
                            setRefreshHeader(MaterialHeader(activity))
                            setOnRefreshListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    getList()
                                    it.finishRefresh(300)
                                }
                            }
                            setRefreshFooter(BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale))
                            setOnLoadMoreListener {
                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                    getList(nowPage + 1)
                                    it.finishRefresh(300)
                                }
                            }

                            recycler = recyclerView {
                                layoutManager = LinearLayoutManager(mContext)
                                painterInfoPic = PainterInfoPictureAdapter(
                                    mContext!!,
                                    arrayListOf(),this@InstanceDisplay)
                                adapter = painterInfoPic
                            }
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
            val it = if (mStyle != 0 && mCategory != 0) {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(1, 5, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(1, 5)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val item = it.body()!!.data
                val list1 = arrayListOf<String>()
                imageList.clear()
                item.forEach {
                    imageList.add(it.asJsonObject)
                    list1.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.resetData(arrayListOf(list1))

                nowPage = 1
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
                    .instanceList(page, 5, mCategory, mStyle)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            } else {
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(page, 5)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()
            }

            if (it.code() in 200..299) {
                val item = it.body()!!.data
                val list1 = arrayListOf<String>()
                item.forEach {
                    imageList.add(it.asJsonObject)
                    list1.add(it.asJsonObject["url"].asString)
                }
                painterInfoPic?.addData(arrayListOf(list1))
                nowPage = page
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