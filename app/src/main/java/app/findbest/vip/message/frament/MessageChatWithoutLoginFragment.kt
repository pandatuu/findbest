package cgland.job.sk_android.mvp.view.fragment.message

import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R

class MessageChatWithoutLoginFragment : Fragment() {

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
        fun newInstance(): MessageChatWithoutLoginFragment {
            return MessageChatWithoutLoginFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }
    private fun createView(): View {
        return UI {
            linearLayout {

                gravity=Gravity.CENTER_VERTICAL
                verticalLayout  {
                    gravity=Gravity.CENTER_HORIZONTAL


                    imageView {
                        imageResource= R.mipmap.icon_jianying
                    }.lparams {
                        width=dip(132)
                        height=dip(105)
                    }


                    textView {
                        text="ログインしてからメッセージを閲覧してください"
                        textSize=13f
                        textColorResource=R.color.black33
                    }.lparams {
                        topMargin=dip(25)
                    }


                    textView {
                        text="ログイン"
                        textSize=15f
                        textColorResource=R.color.white
                        gravity=Gravity.CENTER
                        backgroundResource=R.drawable.radius_button_theme

                    }.lparams {
                        topMargin=dip(25)
                        width=dip(165)
                        height=dip(45)
                    }


                }.lparams() {
                    width = matchParent
                    height = dip(220)
                    bottomMargin=dip(70)
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




