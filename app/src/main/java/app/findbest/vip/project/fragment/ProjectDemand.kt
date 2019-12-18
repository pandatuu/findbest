package app.findbest.vip.project.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI

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

            }
        }.view
    }
}