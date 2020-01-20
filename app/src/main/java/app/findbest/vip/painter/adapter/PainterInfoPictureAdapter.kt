package app.findbest.vip.painter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.provider.Contacts

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.instance.model.Instance
import app.findbest.vip.utils.shapeImageView
import click
import cn.jiguang.imui.view.ShapeImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

import withTrigger


class PainterInfoPictureAdapter(
    context: Context,
    private var pictureList: ArrayList<ArrayList<String>> = arrayListOf(),
    imageClick: ImageClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ImageClick {
        fun click(str: String)
    }
    private var mContext: Context = context
    private lateinit var leftImage: LinearLayout
    private lateinit var rightImage: LinearLayout
    private var imageClick: ImageClick = imageClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(parent.context) {
            frameLayout {
                scrollView {
//                    backgroundColor = Color.GREEN
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        leftImage = linearLayout {
                            orientation = LinearLayout.VERTICAL
                        }.lparams(dip(0), wrapContent) {
                            weight = 1f
                            rightMargin = dip(5)
                        }
                        rightImage = linearLayout {
                            orientation = LinearLayout.VERTICAL
                        }.lparams(dip(0), wrapContent) {
                            weight = 1f
                            leftMargin = dip(5)
                        }
                    }.lparams(dip(370), wrapContent){
                        leftMargin = dip(10)
                        rightMargin = dip(10)
                    }
                }.lparams(matchParent, matchParent)
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        for (index in 0 until pictureList[position].size){
            val view = with(mContext) {
                linearLayout {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        val image = imageView {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            adjustViewBounds = true
                            setOnClickListener {
                                //                                fragmentClick.clickImg(pictureList[position])
                            }
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                        }
                        Glide.with(mContext)
                            .load(pictureList[position][index])
                            .into(image)
                        image.setOnClickListener {
                                imageClick.click(pictureList[position][index])
                            }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(10)
                    }
                }
            }
            if (index % 2 == 0) {
                leftImage.addView(view)
            } else {
                rightImage.addView(view)
            }
        }

        //防止RecycleView数据刷新错乱
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun resetData(data: ArrayList<ArrayList<String>>) {
        pictureList.clear()
        pictureList.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: ArrayList<ArrayList<String>>) {
        pictureList[0].addAll(data[0])
        notifyDataSetChanged()
    }
}