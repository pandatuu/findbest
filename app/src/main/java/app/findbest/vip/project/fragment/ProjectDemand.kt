package app.findbest.vip.project.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip

class ProjectDemand: Fragment() {

    companion object{
        fun newInstance(): ProjectDemand{
            val fragment = ProjectDemand()
            return fragment
        }
    }

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
                linearLayout {
                    textView {
                        text = "外国儿童文学小说插图+封面"
                        textSize = 21f
                        textColor = Color.parseColor("#FF202020")
                    }.lparams{
                        leftMargin = dip(15)
                        gravity = Gravity.CENTER_VERTICAL
                    }
                }.lparams(matchParent,dip(70))
                linearLayout {
                    backgroundColor = Color.parseColor("#FFF6F6F6")
                }.lparams(matchParent,dip(5))
                val details = 2
                frameLayout {
                    id = details
                    val de = ProjectDemandDetails.newInstance()
                    childFragmentManager.beginTransaction().add(details,de).commit()
                }.lparams(matchParent, matchParent)
            }
        }.view
    }
}