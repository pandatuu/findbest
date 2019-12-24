package cgland.job.sk_android.mvp.view.fragment.message

import android.graphics.Color
import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.graphics.Typeface
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R

class MessageChatWithoutLoginActionBarFragment : Fragment() {

    var toolbar1: Toolbar?=null
    private var mContext: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }
    companion object {
        fun newInstance(): MessageChatWithoutLoginActionBarFragment {
            return MessageChatWithoutLoginActionBarFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }
    private fun createView(): View {
        return UI {
            linearLayout { relativeLayout() {
                textView() {
                    backgroundResource = R.drawable.actionbar_bottom_border
                }.lparams() {
                    width = matchParent
                    height = dip(65)

                }
                relativeLayout() {

                    toolbar1 = toolbar {
                        isEnabled = true
                        title = ""
                        navigationIconResource = R.mipmap.icon_back
                    }.lparams() {
                        width = matchParent
                        height = dip(65)
                        alignParentBottom()

                    }


                    textView {
                        text = "マイ"
                        backgroundColor = Color.TRANSPARENT
                        gravity = Gravity.CENTER
                        textColorResource = R.color.toolBarTextColor
                        textSize = 16f
                        setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                    }.lparams() {
                        width = matchParent
                        height = wrapContent
                        height = dip(65 - getStatusBarHeight(this@MessageChatWithoutLoginActionBarFragment.context!!))
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
            }
        }.view

    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
            var scale = context.getResources().getDisplayMetrics().density;
            result = ((result / scale + 0.5f).toInt());
        }
        return result
    }




}




