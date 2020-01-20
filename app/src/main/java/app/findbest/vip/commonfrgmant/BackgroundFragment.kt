package app.findbest.vip.commonfrgmant

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout
import withTrigger

class BackgroundFragment: Fragment() {

    interface ClickBack{
        fun clickAll()
    }

    private lateinit var clickback: ClickBack

    companion object {
        fun newInstance(clickback: ClickBack): BackgroundFragment {
            val fragment = BackgroundFragment()
            fragment.clickback = clickback
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }

    private fun createView():View{
        return UI {
            verticalLayout {
                isClickable = true
                backgroundColorResource = R.color.black66000000
                this.withTrigger().click  {
                    clickback.clickAll()
                }
            }
        }.view
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

    fun onTouch(v: View, event: MotionEvent): Boolean {
        return true//消费掉点击事件,防止跑到下一层去
    }
}