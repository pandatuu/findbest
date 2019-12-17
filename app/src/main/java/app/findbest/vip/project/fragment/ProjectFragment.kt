package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class ProjectFragment : Fragment() {

    companion object {
        fun newInstance(context: Context): ProjectFragment {
            val fragment = ProjectFragment()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            verticalLayout {
                //上部分
                val top = 2
                frameLayout {
                    id = top
                    val title = ProjectMainTitle.newInstance()
                    childFragmentManager.beginTransaction().add(top, title).commit()
                }
                //中间adapter
                val list = 3
                frameLayout {
                    backgroundColor = Color.GREEN
                    id = list
                    val main = ProjectMainList.newInstance(mContext)
                    childFragmentManager.beginTransaction().add(list, main).commit()
                }.lparams(matchParent, matchParent)
            }
        }.view
    }
}