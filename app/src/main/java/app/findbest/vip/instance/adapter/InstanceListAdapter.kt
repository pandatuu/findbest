package app.findbest.vip.instance.adapter

import android.annotation.SuppressLint
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
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

import withTrigger


class InstanceListAdapter
    (
    private val context: RecyclerView,
    private val screenWidth: Int,
    private val picWidth: Int,
    private val instanceList: MutableList<MutableList<Instance>>,
    private val listener: (Instance) -> Unit
) : RecyclerView.Adapter<InstanceListAdapter.ViewHolder>() {


   lateinit var theHolder: ViewHolder

    //清空数据
    fun clearRecruitInfoList() {
        instanceList.clear()
        notifyDataSetChanged()
    }


    //添加数据
    fun addRecruitInfoList(list: MutableList<Instance>,pageNum:Int,pageSize:Int) {
//        recruitInfo.addAll(list)
//        notifyDataSetChanged()

        var endIndex=pageNum*pageSize
        if(list.size<pageNum*pageSize){
            endIndex=list.size
        }

        for(i in (pageNum-1)*pageSize until  endIndex){
            if(i%2==0){
                theHolder.verticalLeft.addView(getView(instanceList.get(0).get(i)))

            }else{
                theHolder.verticalRight.addView(getView(instanceList.get(0).get(i)))
            }
        }
        notifyDataSetChanged()
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        lateinit var verticalRight: LinearLayout
        lateinit var verticalLeft: LinearLayout


        var view = with(parent.context) {

            linearLayout {
                linearLayout {
                    textView {
                    }.lparams() {
                        width = dip((screenWidth-picWidth*2-10)/4)
                        height = dip(10)
                    }
                    verticalLeft = verticalLayout() {



                    }.lparams() {
                        width = dip(picWidth)
                        height = wrapContent

                    }

                    textView {
                    }.lparams() {
                        width = dip((screenWidth-picWidth*2-10)/2)
                        height = dip(10)
                    }

                    verticalRight = verticalLayout() {



                    }.lparams() {
                        width = dip(picWidth)
                        height = wrapContent


                    }

                    textView {
                    }.lparams() {
                        width = dip((screenWidth-picWidth*2-10)/4)
                        height = dip(10)
                    }

                }.lparams() {
                    width = matchParent
                    height = wrapContent
                }


            }


        }

        return ViewHolder(
            view,
            verticalLeft,
            verticalRight

        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        if(position==0){

            for(i in 0 until  instanceList.get(0).size){
                if(i%2==0){
                    holder.verticalLeft.addView(getView(instanceList.get(0).get(i)))

                }else{
                    holder.verticalRight.addView(getView(instanceList.get(0).get(i)))
                }
            }
            holder.bindItem(
                instanceList.get(0),
                position

            )
            theHolder=holder

        }

        holder.setIsRecyclable(false);


    }


    override fun getItemCount(): Int = instanceList.size

    class ViewHolder(
        view: View,
        val verticalLeft: LinearLayout,
        val verticalRight: LinearLayout


    ) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        fun bindItem(
            list: MutableList<Instance>,
            position: Int
        ) {
//            //主体点击
//            itemView.withTrigger().click {
//            }

        }
    }


    fun getView(mode:Instance):View{

        lateinit var image: ShapeImageView
        var view= with(context) {
            linearLayout {

                this.withTrigger().click {
                    listener(mode)
                    
                }

                image= shapeImageView {
                    //setImageResource(R.drawable.pic2)
                    setTheWidth(picWidth)

                }.lparams() {
                    width = matchParent
                    topMargin = dip(2)
                }
            }
        }

        Glide.with(context)
            .load(mode.url)
            .centerCrop()
            .placeholder(R.mipmap.no_pic_show)
            .into(image)




        context.removeView(view)

        return view
    }


}