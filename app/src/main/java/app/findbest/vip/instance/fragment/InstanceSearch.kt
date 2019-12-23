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
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.instance.activity.InstanceActivity
import app.findbest.vip.instance.activity.InstanceSearchActivity
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


class InstanceSearch : FragmentParent() {
    private var mContext: Context? = null
    lateinit var outActivity: FragmentActivity


    lateinit var  editText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }

    }

    companion object {
        fun newInstance( activity: FragmentActivity): InstanceSearch {
            var f = InstanceSearch()
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

            frameLayout {
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
                            toolbar {
                                backgroundResource = R.color.transparent
                                isEnabled = true
                                title = ""

                            }.lparams() {
                                width = matchParent
                                height = dip(65)
                                alignParentBottom()
                                height = dip(65 - getStatusBarHeight(outActivity))
                            }



                            textView {
                                text = "搜索"
                                backgroundColor = Color.TRANSPARENT
                                gravity = Gravity.CENTER
                                textColor = Color.parseColor("#FF222222")
                                textSize = 17f
                                setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                            }.lparams() {
                                width = matchParent
                                height = wrapContent
                                height = dip(65 - getStatusBarHeight(outActivity))
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


                        linearLayout {


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



                                editText=    editText {
                                    hint = "搜索"
                                    hintTextColor = Color.parseColor("#ff666666")
                                    backgroundColor = Color.TRANSPARENT
                                    textColor = Color.BLACK
                                    singleLine = true
                                    gravity = Gravity.CENTER_VERTICAL
                                    textSize = 13f
                                    padding = dip(0)
                                    imeOptions= EditorInfo.IME_ACTION_SEARCH


                                    setOnEditorActionListener(object: TextView.OnEditorActionListener{
                                        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                                            //以下方法防止两次发送请求
                                            if (actionId === EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode === KeyEvent.KEYCODE_ENTER) {
                                                if(event!=null){


                                                    var mIntent = Intent()
                                                    mIntent.putExtra("content", text)

                                                    activity!!.setResult(222, mIntent)

                                                    activity!!.finish()//返回
                                                    activity!!.overridePendingTransition(R.anim.left_in,R.anim.right_out)

                                                }
                                            }
                                            return false
                                        }

                                    })



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

                                    setOnClickListener {

                                        closeSoftKeyboard(editText)

                                        activity!!.finish()
                                        activity!!.overridePendingTransition(
                                            R.anim.left_in,
                                            R.anim.right_out
                                        )

                                    }


                                    text = "取消"
                                    textSize = 15f
                                    textColor = Color.parseColor("#FF333333")
                                }.lparams() {
                                    rightMargin = dip(7)
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




                    }.lparams() {
                        height = dip(0)
                        weight = 1f
                        width = matchParent
                    }




                    //////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////






                }.lparams() {
                    width = matchParent
                    height = matchParent
                }
            }
        }.view



        return view

    }


}