package app.findbest.vip.commonfrgmant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.verticalLayout
import withTrigger

class BackgroundFragment: Fragment() {

    interface ClickBack{
        fun clickAll()
    }

    private var mContext: Context? = null
    private lateinit var clickback: ClickBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    companion object {
        fun newInstance(): BackgroundFragment {
            return BackgroundFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        clickback = activity as ClickBack
        val fragmentView=createView()
        mContext = activity
        return fragmentView
    }

    private fun createView():View{
        return with(mContext!!) {
            verticalLayout {
                isClickable = true
                backgroundColorResource = R.color.black66000000
                this.withTrigger().click  {
                    clickback.clickAll()
                }
            }
        }
    }

    fun getStatusBarHeight(context: Context): Int {
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