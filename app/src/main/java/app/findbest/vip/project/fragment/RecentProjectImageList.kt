package app.findbest.vip.project.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.utils.FlowLayout
import app.findbest.vip.utils.flowLayout
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class RecentProjectImageList : Fragment() {

    interface ClickImage{
        fun clickImage(size: Int)
    }

    companion object{
        fun newInstance(context: Context, list: ArrayList<String>, clickImage: ClickImage): RecentProjectImageList{
            val fragment = RecentProjectImageList()
            fragment.mContext = context
            fragment.imageList = list
            fragment.clickImage = clickImage
            return fragment
        }
    }

    private lateinit var mContext: Context
    private var flow: FlowLayout? = null
    private lateinit var imageList: ArrayList<String>
    private lateinit var clickImage: ClickImage

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
                flow = flowLayout {}.lparams{
                    setMargins(dip(15),dip(15),dip(15),dip(15))
                }
            }
        }.view
    }

    @SuppressLint("RtlHardcoded")
    fun addItems(model: JsonArray){
        for (index in 0..model.size()){
            val imageUrl = model[index].asJsonObject["url"].asString
            var isClick = false
            val imageViews = UI {
                frameLayout {
                    frameLayout {
                        backgroundResource = R.drawable.around_rectangle_2
                        val image = imageView {}.lparams(matchParent, matchParent){
                            setMargins(dip(1),dip(1),dip(1),dip(1))
                        }
                        Glide.with(this.context)
                            .load(imageUrl)
                            .into(image)
                        imageView {
                            if(imageList.size>0){
                                for (i in 0 until imageList.size) {
                                    if (imageList[i] == imageUrl) {
                                        imageResource = R.mipmap.login_ico_checkbox_pre
                                        isClick = true
                                        break
                                    }else{
                                        imageResource = R.mipmap.login_ico_checkbox_nor
                                    }
                                }
                            }else{
                                imageResource = R.mipmap.login_ico_checkbox_nor
                            }
                            setOnClickListener {
                                if(isClick){
                                    imageResource = R.mipmap.login_ico_checkbox_nor
                                    imageList.remove(imageUrl)
                                    clickImage.clickImage(imageList.size)
                                    isClick = false
                                }else{
                                    if(imageList.size<4){
                                        imageResource = R.mipmap.login_ico_checkbox_pre
                                        imageList.add(imageUrl)
                                        clickImage.clickImage(imageList.size)
                                        isClick = true
                                    }else{
                                        toast(resources.getString(R.string.enlist_bigger_num))
                                    }
                                }
                            }
                        }.lparams(dip(20), dip(20)) {
                            gravity = Gravity.RIGHT
                            rightMargin = dip(5)
                            topMargin = dip(5)
                        }
                    }.lparams(dip(83),dip(83)){
                        leftMargin = dip(5)
                        topMargin = dip(5)
                    }
                }
            }.view
            flow?.addView(imageViews)
        }
    }
}