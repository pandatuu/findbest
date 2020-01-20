package app.findbest.vip.instance.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.findbest.vip.R
import app.findbest.vip.instance.adapter.InstanceDetailListAdapter
import app.findbest.vip.instance.model.InstanceModel
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.DialogUtils
import app.findbest.vip.utils.MyDialog
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager
import withTrigger
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.fragment.InstanceDetail
import app.findbest.vip.utils.RetrofitUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import retrofit2.HttpException


class InstanceDetailPagers : BaseActivity() {

    var toolbar1: Toolbar? = null
    lateinit var viewpager: ViewPager
    private val viewPagerId = 2
    private var list = mutableListOf<InstanceModel>()
    var touchDownX = 0f
    var nowPage = 0
    private var myPageAdapter: InstanceDetailListAdapter? = null
    private val datas = ArrayList<Fragment>()

    var thisDialog: MyDialog? = null
    var mHandler = Handler()
    var r: Runnable = Runnable {
        //do something
        if (thisDialog?.isShowing == true && applicationContext!=null) {
            val toast = Toast.makeText(applicationContext, "ネットワークエラー", Toast.LENGTH_SHORT)//网路出现问题
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        DialogUtils.hideLoading(thisDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = intent.getSerializableExtra("nowInstanceDetailList") as MutableList<InstanceModel>
        val nowDetail = intent.getSerializableExtra("nowDetail") as InstanceModel
        nowPage = intent.getIntExtra("nowPage",0)

        list.forEach {
            val applicants =
                InstanceDetail.newInstance(this@InstanceDetailPagers, it)
            datas.add(applicants)
        }

        frameLayout {
            verticalLayout {
                backgroundColor = Color.WHITE
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    toolbar {
                        isEnabled = true
                        title = ""
                        navigationIconResource = R.mipmap.nav_ico_return
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(45), dip(30)) {
                        alignParentBottom()
                        alignParentLeft()
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent, dip(65))
                linearLayout {
                    viewpager = viewPager {
                        id = viewPagerId
                        myPageAdapter = InstanceDetailListAdapter(supportFragmentManager, datas)
                        adapter = myPageAdapter
                        if (nowDetail != null)
                            currentItem = list.indexOf(nowDetail)
                        setOnTouchListener(object: View.OnTouchListener{
                            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                                if(event?.action == MotionEvent.ACTION_DOWN){
                                    if(currentItem == (list.size-1)) {//是否是最后一页
                                        touchDownX = event.x  //获取当前点击坐标
                                        return true
                                    }
                                }
                                if(event?.action == MotionEvent.ACTION_UP){
                                    if(currentItem == (list.size-1)){//是否是最后一页
                                        if(touchDownX-event.x>0){
                                            //向左滑
                                            val rightScroll = touchDownX-event.x
                                            if(rightScroll>200){//向左滑大于200时加载下一分页
                                                thisDialog = DialogUtils.showLoading(this@InstanceDetailPagers)
                                                mHandler.postDelayed(r, 12000)
                                                GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                                                    getList(nowPage+1)
                                                }
                                            }
                                            return true
                                        }
                                    }
                                }
                                return false
                            }
                        })
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent)
            }
        }
    }
    private suspend fun getList(page: Int) {
        try {
            val retrofitUils =
                RetrofitUtils(this@InstanceDetailPagers, resources.getString(R.string.developmentUrl))
            val it =
                retrofitUils.create(InstanceApi::class.java)
                    .instanceList(page, 6)
                    .subscribeOn(Schedulers.io())
                    .awaitSingle()

            if (it.code() in 200..299) {
                DialogUtils.hideLoading(thisDialog)

                val item = it.body()!!.data
                if (item.size() == 0) {
                    toast(resources.getString(R.string.common_no_list_data))
                    return
                }
                item.forEach {
                    val model = Gson().fromJson(it as JsonObject, InstanceModel::class.java)
                    list.add(model)
                    val applicants =
                        InstanceDetail.newInstance(this@InstanceDetailPagers, model)
                    datas.add(applicants)
                }
                myPageAdapter?.notifyDataSetChanged()
                nowPage = page
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
        DialogUtils.hideLoading(thisDialog)
    }
}