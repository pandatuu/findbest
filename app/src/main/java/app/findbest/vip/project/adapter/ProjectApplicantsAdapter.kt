package app.findbest.vip.project.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.*


class ProjectApplicantsAdapter(
    context: Context,
    printedCrad: PrintedCrad,
    private val mDataSet: MutableList<String> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PrintedCrad{
        fun oneClick(i: Int)
    }

    private var mContext: Context = context
    private lateinit var headPic: ImageView
    private var printedCrad: PrintedCrad = printedCrad

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(parent.context) {
            frameLayout {
                linearLayout {
                    backgroundResource = R.drawable.ffe4e4e4_bottom_line
                    orientation = LinearLayout.VERTICAL
                    linearLayout {
                        headPic = imageView {
                        }.lparams(dip(45),dip(45)){
                            setMargins(dip(5),dip(5),0,dip(10))
                        }
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            textView {
                                text = "小云画师"
                                textSize = 17f
                                textColor = Color.parseColor("#FF444444")
                            }.lparams{
                                topMargin = dip(8)
                            }
                            linearLayout {
                                for (index in 0..4){
                                    imageView {
                                        if(index == 4){
                                            imageResource = R.mipmap.ico_star_no
                                        }else{
                                            imageResource = R.mipmap.ico_star_select
                                        }
                                    }.lparams(dip(8),dip(8)){
                                        if(index!=0){
                                            leftMargin = dip(8)
                                        }
                                    }
                                }
                            }.lparams(wrapContent,dip(8)){
                                topMargin = dip(5)
                            }
                        }.lparams(matchParent, matchParent){
                            setMargins(dip(10),0,0,dip(10))
                        }
                    }.lparams(matchParent,dip(55))

                    horizontalScrollView {
                        isHorizontalScrollBarEnabled = false
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            for (index in 0..3){
                                imageView {
                                    imageResource = R.mipmap.test_pic
                                    setOnClickListener {
                                        printedCrad.oneClick(R.mipmap.test_pic)
                                    }
                                }.lparams(wrapContent, matchParent){
                                    if(index!=0){
//                                        leftMargin = dip(5)
                                    }
                                }
                            }
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent,dip(85)){
                        bottomMargin = dip(15)
                    }
                }
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        Glide.with(mContext)
            .load(R.mipmap.person)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(headPic)

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}