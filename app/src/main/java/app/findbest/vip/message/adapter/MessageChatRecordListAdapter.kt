package cgland.job.sk_android.mvp.view.adapter.message

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cgland.job.sk_android.mvp.model.message.ChatRecordModel
import org.jetbrains.anko.*
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.utils.roundImageView
import click
import cn.jiguang.imui.utils.SpannableStringUtil
import com.bumptech.glide.Glide
import withTrigger


class MessageChatRecordListAdapter(
    private val context: RecyclerView,
    private val chatRecord: MutableList<ChatRecordModel>,
    private val listener: (ChatRecordModel) -> Unit
) : RecyclerView.Adapter<MessageChatRecordListAdapter.ViewHolder>() {

    fun setChatRecords(chatRecords: List<ChatRecordModel>) {
        chatRecord.clear()
        chatRecord.addAll(chatRecords)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var imageV: ImageView
        var userName: TextView? = null
        var message: TextView? = null
        var number: TextView? = null
        var position: TextView? = null


        var view = with(parent.context) {
            relativeLayout {
                linearLayout {
                    backgroundResource= R.drawable.text_view_bottom_border
                    imageV = roundImageView {
                        setImageResource(R.mipmap.default_avatar)
                    }.lparams {
                        width=dip(44)
                        height=dip(44)
                        topMargin=dip(15)
                    }

                    linearLayout{
                        gravity=Gravity.CENTER_VERTICAL
                        orientation=LinearLayout.HORIZONTAL
                        verticalLayout {
                            linearLayout{
                                userName=textView {
                                    text="清水さん"
                                    textSize=16f
                                    textColorResource=R.color.normalTextColor
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }

                                position= textView {
                                    text="ジャさん·社長"
                                    textSize=12f
                                    maxLines=1
                                    maxEms=15
                                    ellipsize= TextUtils.TruncateAt.END
                                    textColorResource=R.color.grayb3
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }.lparams {
                                    leftMargin=dip(7)
                                }
                            }

                            linearLayout{
                                gravity=Gravity.CENTER_VERTICAL
                                textView {
                                    visibility=View.GONE
                                    backgroundResource=R.drawable.ellipse_border_grayb3
                                    text="已读"
                                    textSize=11f
                                    textColorResource=R.color.gray99
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                                    topPadding=dip(2)
                                    bottomPadding=dip(2)
                                    leftPadding=dip(5)
                                    rightPadding=dip(5)
                                }

                                message=textView {
                                    text="是非御社で働きたいと思います。"
                                    textSize=14f
                                    textColorResource=R.color.black33
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }.lparams {
                                    leftMargin=dip(7)
                                }
                            }.lparams {
                                topMargin=dip(12)
                            }
                        }.lparams {
                            width=0
                            weight=1f
                        }

                        number=textView {
                            backgroundResource=R.drawable.circle_button_red
                            textColor=Color.WHITE
                            text="-100"
                            leftPadding=dip(8)
                            rightPadding=dip(8)
                            topPadding=dip(3)
                            bottomPadding=dip(3)
                            addTextChangedListener(object :TextWatcher{

                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                }

                                override fun afterTextChanged(s: Editable?) {

                                    if(text.toString().equals("0")){
                                        visibility=View.GONE
                                    }else{
                                        visibility=View.VISIBLE
                                    }
                                }
                            })
                        }.lparams {
                            width= wrapContent
                            height= wrapContent

                        }


                    }.lparams {
                        height= matchParent
                        width= matchParent
                        leftMargin=dip(14)
                        rightMargin=dip(14)
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(95)
                }
            }

        }
        return ViewHolder(view, userName, message, number, imageV,position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var spannablestring=  SpannableStringUtil.stringToSpannableString(context.context, chatRecord[position].massage)

        holder.message?.text= if(spannablestring.length>13) "${spannablestring.subSequence(0,13)}..." else spannablestring
        //holder.message?.setMovementMethod(LinkMovementMethod.getInstance())

        holder.position?.text=chatRecord[position].companyName+" · "+chatRecord[position].position
        holder.userName?.text=chatRecord[position].userName
        holder.number?.text=chatRecord[position].number






        //imageUri="https://static.dingtalk.com/media/lALPDgQ9qdWUaQfMyMzI_200_200.png_200x200q100.jpg"
        imageUri=chatRecord[position].avatar
        println("加载图片")
        println(imageUri)

//        var option= ImageOptions.Builder()
//            .setCrossFade(false)
//            .setPriority(ImageOptions.LoadPriority.IMMEDIATE)
//            .setDiskCacheStrategy(ImageOptions.DiskCache.ALL)
//            .setSkipMemoryCache(false)
//            .setRoundedCorners(true)
//            .setRoundRadius(100)
//            .build()

        if(imageUri!=null && !"".equals(imageUri) && imageUri.contains("http")){


            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .skipMemoryCache(false)
                .dontAnimate()
                .placeholder(R.mipmap.default_avatar)
                .into(holder.imageView)

//
//
//            loadImage(imageUri,holder.imageView,object : OnImageListener{
//                /**
//                 * 图片加载失败
//                 * @param msg 加载失败的原因
//                 */
//                override fun onFail(msg: String?) {
//                    holder.imageView!!.setImageResource(R.mipmap.default_avatar)
//                    chatRecord[position].avatar=""
//                    println("失败")
//
//                }
//
//                /**
//                 * 图片加载成功
//                 * @param bitmap 加载成功生成的bitmap对象
//                 */
//                override fun onSuccess(bitmap: Bitmap?) {
//                    println("成功")
//
//                }
//
//            },R.mipmap.default_avatar,R.mipmap.default_avatar,option)
        }else{
            chatRecord[position].avatar=""
        }


        holder.bindItem(chatRecord[position],position,listener,context)
        holder.setIsRecyclable(false);

    }


    override fun getItemCount(): Int = chatRecord.size

    class ViewHolder(
            view: View,
            val userName: TextView?,
            val message: TextView?,
            val number: TextView?,
            val imageView: ImageView,
            val position: TextView?
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("ResourceType")
        fun bindItem(chatRecord:ChatRecordModel,position:Int,listener: (ChatRecordModel) -> Unit,context: RecyclerView) {
            itemView.withTrigger().click {
                listener(chatRecord)
            }
        }
    }
    var imageUri:String=""

}