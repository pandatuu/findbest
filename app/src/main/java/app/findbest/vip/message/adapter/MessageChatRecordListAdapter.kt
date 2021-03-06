package app.findbest.vip.message.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.message.model.ChatRecordModel
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
        var isRead: TextView? = null
        var position: TextView? = null
        var time: TextView? = null

        val view = with(parent.context) {
            relativeLayout {
                linearLayout {
                    backgroundResource = R.drawable.text_view_bottom_border
                    gravity = Gravity.CENTER_VERTICAL
                    imageV = roundImageView {
                        setImageResource(R.mipmap.default_avatar)
                    }.lparams {
                        width = dip(44)
                        height = dip(44)
                    }

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        orientation = LinearLayout.HORIZONTAL
                        verticalLayout {

                            linearLayout {
                                userName = textView {
                                    maxEms=10
                                    singleLine=true
                                    ellipsize= TextUtils.TruncateAt.END

                                    text = "清水さん"
                                    textSize = 16f
                                    textColorResource = R.color.normalTextColor
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }

                                position = textView {
                                    text = "ジャさん·社長"
                                    textSize = 12f
                                    maxLines = 1
                                    maxEms = 15
                                    ellipsize = TextUtils.TruncateAt.END
                                    textColorResource = R.color.grayb3
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }.lparams {
                                    leftMargin = dip(7)
                                }
                            }

                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL
                                isRead = textView {
                                    //  visibility=View.GONE
                                    backgroundResource=R.drawable.raduis_tag
                                    text = "已读"
                                    textSize = 11f
                                    textColorResource = R.color.gray99
                                    textColor = Color.WHITE
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                                    topPadding = dip(2)
                                    bottomPadding = dip(2)
                                    leftPadding = dip(5)
                                    rightPadding = dip(5)
                                }

                                message = textView {
                                    maxEms=12
                                    singleLine=true
                                    ellipsize= TextUtils.TruncateAt.END
                                    text = "是非御社で働きたいと思います。"
                                    textSize = 14f
                                    textColor = Color.parseColor("#FF666666")
                                    setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                                }.lparams {
                                    leftMargin = dip(7)
                                }
                            }.lparams {
                                topMargin = dip(12)
                            }
                        }.lparams {
                            width = 0
                            weight = 1f
                            height = wrapContent
                        }


                        verticalLayout {
                            gravity=Gravity.RIGHT
                            time = textView {
                                gravity=Gravity.CENTER
                                textSize = 12f
                                textColor = Color.parseColor("#FF999999")

                            }.lparams(){
                                width = wrapContent
                                height=dip(22)
                            }

                            number = textView {
                                backgroundResource = R.drawable.circle_button_red
                                textColor = Color.WHITE
                                text = "-100"
                                gravity=Gravity.CENTER
                                leftPadding = dip(8)
                                rightPadding = dip(8)
                                topPadding = dip(2)
                                bottomPadding = dip(2)
                                addTextChangedListener(object : TextWatcher {

                                    override fun beforeTextChanged(
                                        s: CharSequence?,
                                        start: Int,
                                        count: Int,
                                        after: Int
                                    ) {


                                    }

                                    override fun onTextChanged(
                                        s: CharSequence?,
                                        start: Int,
                                        before: Int,
                                        count: Int
                                    ) {


                                    }

                                    override fun afterTextChanged(s: Editable?) {


                                        if(!text.contains("+") && text.toString().toInt()>=100){
                                            setText("99+")
                                        }

                                        if (text.toString().equals("0")) {
                                            visibility = View.INVISIBLE
                                        } else {
                                            visibility = View.VISIBLE
                                        }
                                    }
                                })
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(12)
                            }


                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                        }


                    }.lparams {
                        height = matchParent
                        width = matchParent
                        leftMargin = dip(14)
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(95)
                }
            }

        }
        return ViewHolder(view, userName, message, number,isRead, imageV, position, time)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var spannablestring = SpannableStringUtil.stringToSpannableString(
            context.context,
            chatRecord[position].massage
        )

        holder.message?.text = if (spannablestring.length > 13) "${spannablestring.subSequence(
            0,
            13
        )}..." else spannablestring
        //holder.message?.setMovementMethod(LinkMovementMethod.getInstance())

        holder.position?.text =
            chatRecord[position].companyName + "  " + chatRecord[position].position
        holder.userName?.text = chatRecord[position].userName
        holder.number?.text = chatRecord[position].number.toString()
        holder.time?.text = chatRecord[position].time
        //已读
        if(chatRecord[position].number>0){
            holder.isRead?.text = context.resources.getText(R.string.notRead)
        }else{
            holder.isRead?.text = context.resources.getText(R.string.isRead)
            holder.isRead?.backgroundResource = R.drawable.raduis_tag_notread
            holder.isRead?.textColor = Color.parseColor("#FF666666")
        }

        imageUri = chatRecord[position].avatar
        if (imageUri != null && "" != imageUri && imageUri.contains("http")) {
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .skipMemoryCache(false)
                .dontAnimate()
                .placeholder(R.mipmap.default_avatar)
                .into(holder.imageView)
        } else if(imageUri == "0") {
            Glide.with(context)
                .load(R.mipmap.message_ico_message_nor)
                .centerCrop()
                .skipMemoryCache(false)
                .dontAnimate()
                .placeholder(R.mipmap.message_ico_message_nor)
                .into(holder.imageView)
        }else{
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .skipMemoryCache(false)
                .dontAnimate()
                .placeholder(R.mipmap.default_avatar)
                .into(holder.imageView)
        }


        holder.bindItem(chatRecord[position], position, listener, context)
        holder.setIsRecyclable(false)

    }


    override fun getItemCount(): Int = chatRecord.size

    class ViewHolder(
        view: View,
        val userName: TextView?,
        val message: TextView?,
        val number: TextView?,
        val isRead: TextView?,
        val imageView: ImageView,
        val position: TextView?,
        val time: TextView?
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("ResourceType")
        fun bindItem(
            chatRecord: ChatRecordModel,
            position: Int,
            listener: (ChatRecordModel) -> Unit,
            context: RecyclerView
        ) {
            itemView.withTrigger().click {
                listener(chatRecord)
            }
        }
    }

    var imageUri: String = ""

}