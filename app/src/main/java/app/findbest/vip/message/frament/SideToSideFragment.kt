package app.findbest.vip.message.fragment

import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R

class SideToSideFragment : Fragment() {

    var toolbar1: Toolbar?=null
    private var mContext: Context? = null


    lateinit var textView1:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }
    companion object {
        fun newInstance(): SideToSideFragment {
            return SideToSideFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }
    private fun createView(): View {
        return UI {
            linearLayout {
                linearLayout() {



                        textView1=textView {
                            backgroundResource= R.drawable.bottom_border_yellow_3dp
                        }.lparams {
                            height=matchParent
                            width=dip(20)
                        }






                }.lparams() {
                    width = matchParent
                    height = dip(62)
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




