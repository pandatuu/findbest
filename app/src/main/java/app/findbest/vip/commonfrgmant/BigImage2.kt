package app.findbest.vip.commonfrgmant

import android.graphics.Color
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

    companion object{
        fun newInstance(url: String, imageClick: ImageClick, isLook: Boolean): BigImage2 {
            val fragment = BigImage2()
            fragment.imageUrl = url
            fragment.imageClick = imageClick
            fragment.isLook = isLook
            return fragment
        }
    }

    lateinit var imageClick: ImageClick
    var imageUrl: String = ""
    var isLook = false

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
                isClickable = true
                setOnClickListener {
                    imageClick.clickclose()
                }
                verticalLayout {
                    val image = photoView{
                        setOnClickListener {
                            imageClick.clickclose()
                        }
                    }.lparams(matchParent, wrapContent)
                    Glide.with(context)
                        .load(imageUrl)
                        .into(image)
                    imageView {
                        imageResource = R.mipmap.btn_delete
                        setOnClickListener {
                            imageClick.clickclose()
                        }
                    }.lparams(dip(30), dip(30)){
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = dip(40)
                    }
                }.lparams(matchParent, wrapContent){
                    gravity = Gravity.CENTER
                }

                if (!isLook){
                    textView {
                        text = resources.getString(R.string.project_permission_tips)
                        textSize = 11f
                        textColor = Color.parseColor("#FFFFFFFF")
                    }.lparams(matchParent, wrapContent){
                        gravity = Gravity.BOTTOM
                        setMargins(dip(13),0,dip(13),dip(30))
                    }
                }
            }
        }.view
    }
}