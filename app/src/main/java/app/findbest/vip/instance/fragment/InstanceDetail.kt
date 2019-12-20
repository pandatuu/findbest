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
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.utils.RetrofitUtils
import app.findbest.vip.utils.recyclerView
import app.findbest.vip.utils.roundImageView
import app.findbest.vip.utils.smartRefreshLayout
import click
import com.biao.pulltorefresh.OnRefreshListener
import com.biao.pulltorefresh.PtrHandler
import com.biao.pulltorefresh.PtrLayout
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import withTrigger
import java.lang.Runnable


class InstanceDetail : FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context


    var screenWidth = 0
    var picWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }


    }

    companion object {
        fun newInstance(context: Context): InstanceDetail {
            var f = InstanceDetail()
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
                                    R.anim.right_out,
                                    R.anim.right_out
                                )

                            }
                        }.lparams() {
                            width = matchParent
                            height = dip(65)
                            alignParentBottom()
                            height = dip(65 - getStatusBarHeight(this@InstanceDetail.context!!))
                        }



                        textView {
                            text = ""
                            backgroundColor = Color.TRANSPARENT
                            gravity = Gravity.CENTER
                            textColor = Color.parseColor("#FF222222")
                            textSize = 16f
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams() {
                            width = matchParent
                            height = wrapContent
                            height = dip(65 - getStatusBarHeight(this@InstanceDetail.context!!))
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

                linearLayout {

                    gravity = Gravity.CENTER_VERTICAL
                    roundImageView {

                        Glide.with(this@InstanceDetail)
                            .load(activity!!.intent.getStringExtra("logo"))
                            .skipMemoryCache(false)
                            .dontAnimate()
                            .centerCrop()
                            .placeholder(R.mipmap.no_pic_show)
                            .into(this)


                    }.lparams() {
                        width = dip(45)
                        height = dip(45)
                    }


                    textView {

                        text = activity!!.intent.getStringExtra("name")
                        textSize = 17f
                        textColor = Color.parseColor("#FF444444")

                    }.lparams() {

                        leftMargin = dip(8)
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(80)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }

                textView() {
                    backgroundColor = Color.parseColor("#FFE3E3E3")
                }.lparams() {
                    width = matchParent
                    height = dip(1)

                }

                //////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////


                linearLayout {


                }.lparams() {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }



                textView {

                    gravity=Gravity.CENTER
                    text="邀请画师"
                    textSize=16f
                    textColor=Color.WHITE
                    backgroundResource=R.drawable.enable_rectangle_button

                }.lparams() {
                    height = dip(50)
                    width = matchParent
                }


            }
        }.view




        return view

    }


}