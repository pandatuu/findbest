package cn.jiguang.imui.messages;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class ResumeViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;
    private int icoType;
    private final RoundImageView mImageAvatar;

    private RoundTextView mDateTv;
    private ImageView resume_item_logo;
    private TextView resume_item_time;


    private TextView resume_item_size;
    private TextView resume_item_title;

    private int messageType;



    private LinearLayout resume_body_container;

    public ResumeViewHolder(View itemView, boolean isSender,  int icoType) {
        super(itemView);
        this.mIsSender = isSender;
        this.icoType = icoType;
        //头像
        mImageAvatar = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        //建立标题
        resume_item_title = itemView.findViewById(R.id.resume_item_title);
        //简历logo
        resume_item_logo = itemView.findViewById(R.id.resume_item_logo);
        //消息时间
        mDateTv = itemView.findViewById(R.id.aurora_tv_msgitem_date);
        //简历大小
        resume_item_size = itemView.findViewById(R.id.resume_item_size);
        //简历创建时间
        resume_item_time = itemView.findViewById(R.id.resume_item_time);
        //消息主体
        resume_body_container= itemView.findViewById(R.id.resume_body_container);
    }

    @Override
    public void onBind(final MESSAGE message) {


        messageType=EXCHANGE_PHONE;


        resume_item_title.setText(message.getText());
        resume_item_size.setText(message.getSize());

        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (mImageLoader != null) {
            if (isAvatarExists) {
                mImageAvatar.setVisibility(View.VISIBLE);
                mImageLoader.loadAvatarImage(mImageAvatar, message.getFromUser().getAvatarFilePath(),"CIRCLE");
            }
        }


        //时间
        String timeString = message.getTimeString();
        mDateTv.setVisibility(View.VISIBLE);
        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }



        resume_body_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
                }
            }
        });


    }


    @Override
    public void applyStyle(MessageListStyle style) {


        if( icoType ==IMessage.MIMETYPE_WORD){
            resume_item_logo.setImageResource(R.drawable.word_icon);
        }else if( icoType==IMessage.MIMETYPE_PDF){
            resume_item_logo.setImageResource(R.drawable.ico_pdf);
        }else if( icoType==IMessage.MIMETYPE_JPG){
            resume_item_logo.setImageResource(R.drawable.jpg_icon);
        }


        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());
        mImageAvatar.setBorderRadius(style.getAvatarRadius());

    }

}