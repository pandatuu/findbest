package app.findbest.vip.painter.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class PainterMainTitle: Fragment() {

    interface ChildrenClick{
        fun clickSort()
        fun clickScreen()
        fun clickSearch()
    }

    companion object{
        fun newInstance(child:ChildrenClick): PainterMainTitle{
            val fragment = PainterMainTitle()
            fragment.child = child
            return fragment
        }
    }

    private lateinit var child:ChildrenClick

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    textView {
                        text = resources.getString(R.string.painter_title)
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                        typeface = Typeface.DEFAULT_BOLD
                    }.lparams(wrapContent, wrapContent){
                        alignParentBottom()
                        centerHorizontally()
                        bottomMargin = dip(8)
                    }
                }.lparams(matchParent,dip(65))
                linearLayout {
                    backgroundColor = Color.parseColor("#FFFFFF")
                    linearLayout {
                        backgroundResource = R.drawable.fff6f6f6_around_button
                        imageView {
                            padding = dip(5)
                            imageResource = R.mipmap.tab_icon_search_nor
                        }
                        textView {
                            text = resources.getString(R.string.common_search)
                            textSize = 15f
                            textColor = Color.parseColor("#FF666666")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        this.withTrigger().click {
                            child.clickSearch()
                        }
                    }.lparams(dip(0), dip(30)){
                        weight = 1f
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        textView {
                            text = resources.getString(R.string.common_sort)
                            textSize = 15f
                            textColor= Color.parseColor("#FF333333")
                        }
                        imageView {
                            imageResource = R.mipmap.tab_icon_ssorting_nor
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        setOnClickListener {
                            child.clickSort()
                        }
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(20)
                    }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        textView {
                            text = resources.getString(R.string.common_srceen)
                            textSize = 15f
                            textColor= Color.parseColor("#FF333333")
                        }
                        imageView {
                            imageResource = R.mipmap.tab_icon_screening_nor
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        setOnClickListener {
                            child.clickScreen()
                        }
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(20)
                    }
                }.lparams(matchParent,dip(40)){
                    setMargins(dip(10),0,dip(10),0)
                }
                linearLayout {
                    backgroundColor = Color.parseColor("#FFE3E3E3")
                }.lparams(matchParent,dip(0.5f))
            }
        }.view
    }
}