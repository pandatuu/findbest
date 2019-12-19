package app.findbest.vip.instance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.view.*
import org.jetbrains.anko.*
import android.graphics.Typeface
import app.findbest.vip.R


class InstanceDisplay : Fragment() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }
    }

    companion object {
        fun newInstance(): InstanceDisplay {
            var f = InstanceDisplay()
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
        return UI {
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

                    gravity=Gravity.CENTER_VERTICAL

                    linearLayout {

                    }.lparams(){
                        width=dip(280)
                        height=dip(30)
                    }


                    linearLayout {
                        gravity=Gravity.CENTER_VERTICAL or Gravity.RIGHT

                        imageView(){

                        }.lparams(){
                            width=dip(20)
                            height=dip(20)
                        }
                    }.lparams(){
                        width=dip(0)
                        weight=1f
                        height= matchParent
                    }

                }.lparams(){
                    width = matchParent
                    height = dip(40)
                    leftMargin=dip(10)
                    rightMargin=dip(10)
                }

            }
        }.view

    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
            var scale = context.getResources().getDisplayMetrics().density;
            result = ((result / scale + 0.5f).toInt());
        }
        return result
    }
}