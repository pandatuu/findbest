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
import android.text.style.BackgroundColorSpan
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.activity.InstanceActivity
import app.findbest.vip.instance.adapter.InstanceListAdapter
import app.findbest.vip.instance.adapter.MyProjectListAdapter
import app.findbest.vip.instance.api.InstanceApi
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.instance.model.ProjectItem
import app.findbest.vip.utils.*
import click
import cn.jiguang.imui.view.ShapeImageView
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


class MessageDialog : FragmentParent() {
    private var mContext: Context? = null
    lateinit var manager: FragmentTransaction
    lateinit var outActivity: FragmentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }

    }

    companion object {
        fun newInstance(manager: FragmentTransaction, activity: FragmentActivity): MessageDialog {
            var f = MessageDialog()
            f.manager = manager
            f.outActivity = activity
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


        var view = UI {
            verticalLayout {
                backgroundColor = Color.parseColor("#88000000")
                gravity = Gravity.CENTER
                frameLayout {
                    backgroundColor = Color.WHITE

                    imageView() {

                        setImageResource(R.mipmap.image_message_to_remind)

                    }.lparams() {
                        height = dip(144)
                    }

                    verticalLayout {

                        backgroundColor = Color.WHITE
                    }.lparams() {
                        width = matchParent
                        height = dip(187)
                        rightMargin = dip(25)
                        topMargin = dip(82)
                        leftMargin = dip(25)
                    }


                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        imageView() {

                            setImageResource(R.mipmap.ico_the_message_nor)

                        }.lparams() {
                            height = dip(117)
                            width = dip(117)
                        }


                        textView {
                            text = "发送邀请成功"
                            textColor = Color.parseColor("#FF333333")
                            textSize = 18f
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams() {
                            topMargin = dip(8)
                        }

                        textView {
                            text = "请等候对方同意"
                            textColor = Color.parseColor("#FF666666")
                            textSize = 15f
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams() {
                            topMargin = dip(12)
                        }

                        verticalLayout {
                            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                            imageView() {
                                setImageResource(R.mipmap.button_confirm_nor)

                                this.withTrigger().click {
                                    manager.remove(this@MessageDialog).commit()

                                    outActivity.finish()
                                    activity!!.overridePendingTransition(
                                        R.anim.left_in,
                                        R.anim.right_out
                                    )
                                }

                            }.lparams() {
                                bottomMargin = dip(10)
                                width = dip(276)
                                height = dip(60)
                            }


                        }.lparams() {
                            height = dip(0)
                            weight = 1f
                            width = matchParent
                        }


                    }.lparams() {
                        width = matchParent
                        height = matchParent
                        topMargin = dip(54)
                    }

                }.lparams() {
                    width = dip(306)
                    height = dip(350)
                }


            }
        }.view



        return view

    }


}