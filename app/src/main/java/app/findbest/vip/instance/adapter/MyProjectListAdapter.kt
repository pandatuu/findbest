package app.findbest.vip.instance.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.provider.Contacts
import android.view.Gravity

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.instance.model.ProjectItem
import app.findbest.vip.utils.shapeImageView
import click
import cn.jiguang.imui.view.ShapeImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

import withTrigger


class MyProjectListAdapter
    (
    private val context: RecyclerView,
    private val screenWidth: Int,
    private val picWidth: Int,
    private val projectList: MutableList<ProjectItem>,
    private val listener: (ProjectItem, Boolean) -> Unit
) : RecyclerView.Adapter<MyProjectListAdapter.ViewHolder>() {


    //清除数据
    fun clear() {

        projectList.clear()
        notifyDataSetChanged()

    }

    //添加数据
    fun addDataList(list: MutableList<ProjectItem>) {
//        recruitInfo.addAll(list)
//        notifyDataSetChanged()

        projectList.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        lateinit var title: TextView
        lateinit var textView1: TextView
        lateinit var textView2: TextView
        lateinit var textView3: TextView

        lateinit var imageView1: ImageView
        lateinit var imageView2: ImageView
        lateinit var imageView3: ImageView

        lateinit var flagImage: ImageView

        lateinit var linearLayout: LinearLayout

        var view = with(parent.context) {

            linearLayout {

                linearLayout {
                    backgroundColor = Color.WHITE

                    verticalLayout {
                        leftPadding = dip(25)
                        title = textView {
                            textSize = 16f
                            textColor = Color.parseColor("#FF202020")
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams {
                            topMargin = dip(15)

                        }

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL
                            imageView1 = imageView {

                                setImageResource(R.mipmap.project_ico_size_nor)
                            }.lparams {
                                width = dip(15)
                                height = dip(12)
                            }

                            textView1 = textView {
                                textColor = Color.parseColor("#FF666666")
                                textSize = 12f
                            }.lparams {
                                leftMargin = dip(7)

                            }


                            imageView2 = imageView {
                                setImageResource(R.mipmap.project_ico_format_nor)

                            }.lparams {
                                width = dip(15)
                                height = dip(12)
                                leftMargin = dip(20)
                            }

                            textView2 = textView {
                                textColor = Color.parseColor("#FF666666")
                                textSize = 12f
                            }.lparams {
                                leftMargin = dip(7)

                            }


                            imageView3 = imageView {
                                setImageResource(R.mipmap.project_ico_data_nor)
                            }.lparams {
                                width = dip(15)
                                height = dip(12)
                                leftMargin = dip(20)
                            }

                            textView3 = textView {
                                textColor = Color.parseColor("#FF666666")
                                textSize = 12f
                            }.lparams {
                                leftMargin = dip(7)

                            }

                        }.lparams() {
                            width = matchParent
                            topMargin = dip(15)
                        }
                    }.lparams {
                        width = dip(0)
                        weight = 1f
                        height = matchParent
                    }



                    linearLayout {

                        gravity = Gravity.CENTER

                        linearLayout = linearLayout {
                            gravity = Gravity.CENTER
                            flagImage = imageView {
                                isSelected = false
                                setImageResource(R.mipmap.login_ico_checkbox_nor)
                            }.lparams() {
                                width = dip(20)
                                height = dip(20)
                            }
                        }.lparams {
                            width = dip(40)
                            height = dip(40)
                        }

                    }.lparams {
                        width = dip(40)
                        height = matchParent
                    }


                }.lparams() {
                    width = dip(screenWidth - 20)
                    height = dip(87)
                    topMargin = dip(10)
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                }


            }


        }

        return ViewHolder(
            view,
            title,
            textView1,
            textView2,
            textView3,
            flagImage,
            linearLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.title.text = projectList.get(position).name

        holder.textView1.text = projectList.get(position).size

        holder.textView2.text = projectList.get(position).format

        holder.textView3.text = projectList.get(position).time



        holder.bindItem(
            projectList,
            position,
            listener
        )
        holder.setIsRecyclable(false);


    }


    override fun getItemCount(): Int = projectList.size

    class ViewHolder(
        val view: View,
        val title: TextView,
        val textView1: TextView,
        val textView2: TextView,
        val textView3: TextView,
        val flagImage: ImageView,
        val linearLayout: LinearLayout

    ) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        fun bindItem(
            list: MutableList<ProjectItem>,
            position: Int,
            listener: (ProjectItem, Boolean) -> Unit
        ) {
            //主体点击
            this.linearLayout.withTrigger(10).click {
                if (flagImage.isSelected == false) {
                    flagImage.isSelected = true
                    listener(list.get(position), true)
                    flagImage.setImageResource(R.mipmap.login_ico_checkbox_pre)
                } else {
                    flagImage.isSelected = false
                    listener(list.get(position), false)
                    flagImage.setImageResource(R.mipmap.login_ico_checkbox_nor)
                }

            }


        }
    }


}