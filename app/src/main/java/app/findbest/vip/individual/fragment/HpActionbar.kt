package app.findbest.vip.individual.fragment


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class HpActionbar:FragmentParent() {

    var TrpToolbar: Toolbar?=null
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
        fun newInstance(): HpActionbar {
            return HpActionbar()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }

    private fun createView():View{
        return UI {
            verticalLayout {
                relativeLayout {
                    textView {
                        backgroundColor = Color.WHITE
                    }

                    TrpToolbar = toolbar {
                        backgroundColor = Color.WHITE
                        isEnabled = true
                        title = ""
                        navigationIconResource= R.mipmap.nav_ico_return
                    }.lparams {
                        width = matchParent
                        height =dip(65)
                        alignParentBottom()
                    }

                    textView {
                        textResource = R.string.hb_title
                        backgroundColor = Color.TRANSPARENT
                        gravity = Gravity.CENTER
                        textColor = Color.parseColor("#222222")
                        textSize = 16f
                        typeface = Typeface.defaultFromStyle(Typeface.BOLD)

                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        height =dip(65-getStatusBarHeight(this@HpActionbar.context!!))
                        alignParentBottom()
                    }
                }.lparams {
                    width = matchParent
                    height = dip(65)
                }
            }
        }.view
    }

    override fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
            val scale = context.resources.displayMetrics.density
            result = ((result / scale + 0.5f).toInt())
        }
        return result
    }

}