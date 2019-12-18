package app.findbest.vip.project.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class BigImage: Fragment() {

    var imageInt: Int = 0
    lateinit var clickImg: ClickImg

    companion object{
        fun newInstance(i: Int, clickImg: ClickImg): BigImage{
            val fragment = BigImage()
            fragment.imageInt = i
            fragment.clickImg = clickImg
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
                imageView{
                    imageResource = imageInt
                    setOnClickListener {
                        clickImg.closeImg()
                    }
                }.lparams(matchParent, wrapContent){
                    gravity = Gravity.CENTER
                }
            }
        }.view
    }
    interface ClickImg{
        fun closeImg()
    }
}