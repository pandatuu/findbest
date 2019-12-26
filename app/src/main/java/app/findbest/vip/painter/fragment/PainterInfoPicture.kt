package app.findbest.vip.painter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class PainterInfoPicture: Fragment() {

    interface FragmentClick{
        fun clickImg(str: String)
    }

    companion object{
        fun newInstance(context: Context,fragmentClick: FragmentClick): PainterInfoPicture{
            val fragment = PainterInfoPicture()
            fragment.mContext = context
            fragment.fragmentClick = fragmentClick
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var fragmentClick: FragmentClick
    private lateinit var leftImage: LinearLayout
    private lateinit var rightImage: LinearLayout
    private var imageList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            scrollView {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    leftImage = linearLayout {
                        orientation = LinearLayout.VERTICAL
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        rightMargin = dip(5)
                    }
                    rightImage = linearLayout {
                        orientation = LinearLayout.VERTICAL
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        leftMargin = dip(5)
                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view
    }

}