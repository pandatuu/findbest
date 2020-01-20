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

class NullPicturesFragment: Fragment() {

    companion object{
        fun newInstance(): NullPicturesFragment{
            return NullPicturesFragment()
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
                backgroundColor = Color.WHITE
                verticalLayout {
                    imageView {
                        imageResource = R.mipmap.null_data
                    }
                    textView {
                        text = resources.getString(R.string.common_no_list_works)
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