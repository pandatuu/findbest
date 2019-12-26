package app.findbest.vip.painter.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.utils.photoView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class BigImage2: Fragment() {

    interface ImageClick{
        fun clickclose()
    }

    lateinit var imageClick: ImageClick
    var imageUrl: String = ""

    companion object{
        fun newInstance(url: String, imageClick: ImageClick): BigImage2 {
            val fragment = BigImage2()
            fragment.imageUrl = url
            fragment.imageClick = imageClick
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
            frameLayout {
                backgroundColorResource = R.color.black66000000
                val image = photoView{
                    setOnClickListener {
                        imageClick.clickclose()
                    }
                }.lparams(matchParent, matchParent){
                    gravity = Gravity.CENTER
                }
                Glide.with(context)
                    .load(imageUrl)
                    .into(image)
            }
        }.view
    }
}