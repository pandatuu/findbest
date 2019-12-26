package app.findbest.vip.individual.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout
import withTrigger

class ProjectListStatus: Fragment() {

    interface ClickBack{
        fun clicksome()
    }

    private lateinit var clickback: ClickBack

    companion object {
        fun newInstance(clickback: ClickBack): ProjectListStatus {
            val fragment = ProjectListStatus()
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
                    clickback.clicksome()
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

}