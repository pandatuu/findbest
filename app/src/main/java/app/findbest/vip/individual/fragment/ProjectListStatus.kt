package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class ProjectListStatus: Fragment() {

    interface ClickBack{
        fun clicksome(str: String)
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
        frameLayout {
            backgroundColor = Color.TRANSPARENT
            verticalLayout {
                backgroundResource = R.mipmap.bubble_dialog
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    gravity = Gravity.CENTER_VERTICAL
                    textView {
                        text = "全部"
                        textSize = 16f
                        textColor = Color.parseColor("#FF333333")
                    }
                    setOnClickListener {
                        clickback.clicksome("全部")
                    }
                }.lparams(matchParent,dip(47))
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    gravity = Gravity.CENTER_VERTICAL
                    textView {
                        text = "发布阶段"
                        textSize = 16f
                        textColor = Color.parseColor("#FF333333")
                    }
                    setOnClickListener {
                        clickback.clicksome("发布阶段")
                    }
                }.lparams(matchParent,dip(47))
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    gravity = Gravity.CENTER_VERTICAL
                    textView {
                        text = "制作阶段"
                        textSize = 16f
                        textColor = Color.parseColor("#FF333333")
                    }
                    setOnClickListener {
                        clickback.clicksome("制作阶段")
                    }
                }.lparams(matchParent,dip(47))
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    gravity = Gravity.CENTER_VERTICAL
                    textView {
                        text = "交易完成"
                        textSize = 16f
                        textColor = Color.parseColor("#FF333333")
                    }
                    setOnClickListener {
                        clickback.clicksome("交易完成")
                    }
                }.lparams(matchParent,dip(47))
            }.lparams{
                topMargin = dip(58)
                gravity = Gravity.RIGHT
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