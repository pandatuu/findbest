package app.findbest.vip.project.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class ProjectMainTitle: Fragment() {

    interface ChildrenClick{
        fun clickFragment()
    }

    companion object{
        fun newInstance(child:ChildrenClick): ProjectMainTitle{
            val fragment = ProjectMainTitle()
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
                        text = "项目"
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams(wrapContent, wrapContent){
                        alignParentBottom()
                        centerHorizontally()
                        bottomMargin = dip(8)
                    }
                }.lparams(matchParent,dip(65))
                linearLayout {
                    linearLayout {
                        backgroundResource = R.drawable.fff6f6f6_around_button
                        imageView {
                            padding = dip(5)
                            imageResource = R.mipmap.tab_icon_search_nor
                        }
                        textView {
                            text = "搜索"
                            textSize = 15f
                            textColor = Color.parseColor("#FF666666")
                        }.lparams(wrapContent, wrapContent){
                            leftMargin = dip(10)
                            gravity = Gravity.CENTER_VERTICAL
                        }
                    }.lparams(dip(0), dip(30)){
                        weight = 1f
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        textView {
                            text = "筛选"
                            textSize = 15f
                            textColor= Color.parseColor("#FF333333")
                        }
                        imageView {
                            imageResource = R.mipmap.tab_icon_screening_nor
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        setOnClickListener {
                            child.clickFragment()
                        }
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(20)
                    }
                }.lparams(matchParent,dip(40)){
                    setMargins(dip(10),0,dip(10),0)
                }
            }
        }.view
    }
}