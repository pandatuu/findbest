package app.findbest.vip.commonfrgmant

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

class NullDataPageFragment: Fragment() {

    companion object{
        fun newInstance(): NullDataPageFragment{
            return NullDataPageFragment()
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
        return UI{
            linearLayout {
                gravity = Gravity.CENTER
                verticalLayout {
                    imageView {
                        imageResource = R.mipmap.null_data
                    }
                    textView {
                        text = "暂时没有相关内容哦！"
                        textSize = 14f
                        textColor = Color.parseColor("#FF5C5C5C")
                    }.lparams{
                        topMargin = dip(8)
                    }
                }
            }
        }.view
    }
}