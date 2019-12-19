package cn.jiguang.imui.messages;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class CommunicationViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;
    private int icoType;
    private int showType;
    private final RoundImageView mImageAvatar_receive;
    private final RoundImageView mImageAvatar_send;

    private RoundTextView mDateTv;
    private ImageView communication_type;
    private TextView communication_content;


    private TextView exchangeRefuse;
    private TextView exchangeReceive;

    private int messageType;

    private LinearLayout communication_parent;
    private LinearLayout buttonParent;

    private LinearLayout messageBody;

    private boolean messageHandled=false;

    public CommunicationViewHolder(View itemView, boolean isSender, int showType, int icoType,boolean handled) {
        super(itemView);
        this.mIsSender = isSender;
        this.icoType = icoType;
        this.showType = showType;
        this.messageHandled=handled;
        mImageAvatar_receive = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar_receive);
        mImageAvatar_send = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar_send);

        mDateTv = itemView.findViewById(R.id.aurora_tv_msgitem_date);
        communication_type = itemView.findViewById(R.id.communication_type);
        communication_content = itemView.findViewById(R.id.communication_content);

        communication_parent = itemView.findViewById(R.id.communication_parent);

        exchangeRefuse = itemView.findViewById(R.id.exchangeRefuse);
        exchangeReceive = itemView.findViewById(R.id.exchangeReceive);


        messageBody= itemView.findViewById(R.id.messageBody);

        buttonParent = itemView.findViewById(R.id.buttonParent);
    }

    @Override
    public void onBind(final MESSAGE message) {


        if (icoType == MsgListAdapter.PHONE) {
            communication_type.setImageResource(R.drawable.ico_phone);
            messageType=EXCHANGE_PHONE;
        } else if (icoType == MsgListAdapter.LINE) {
            communication_type.setImageResource(R.drawable.ico_line);
            messageType=EXCHANGE_LINE;
        } else if (icoType == MsgListAdapter.VIDEO) {
            communication_type.setImageResource(R.drawable.online_invite);
            messageType=INVITE_VIDEO;
        } else if (icoType == MsgListAdapter.INTERVIEW_VIDEO) {
            communication_type.setImageResource(R.drawable.video_in);
            messageType=INTERVIEW_VIDEO;
        }else if(icoType == MsgListAdapter.INVITE_NORMAL_INTERVIEW){
            communication_type.setImageResource(R.drawable.offline_invite);
            messageType=INVITE_NORMAL_INTERVIEW;
        }else if(icoType == MsgListAdapter.REQUEST_RESUME){
            communication_type.setImageResource(R.drawable.self_introduce_icon);
            messageType=REQUEST_RESUME;
        }





        if(messageHandled){
            //已经被处理了
            exchangeRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //什么也不做
                    if (mMsgClickListener != null) {
                        mMsgClickListener.onConfirmMessageClick(message,false,DO_THING);
                    }
                }
            });

            exchangeReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //什么也不做
                    if (mMsgClickListener != null) {
                        mMsgClickListener.onConfirmMessageClick(message,false,DO_THING);
                    }                }
            });
        }else{
            //没有被处理,添加点击事件
            //拒绝
            exchangeRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMsgClickListener != null) {
                        mMsgClickListener.onConfirmMessageClick(message,false,messageType);
                    }
                }
            });
            //同意
            exchangeReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMsgClickListener != null) {
                        mMsgClickListener.onConfirmMessageClick(message,true,messageType);
                    }
                }
            });
        }



        messageBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
                }
            }
        });


        communication_content.setText(message.getText());

        String timeString = message.getTimeString();
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (mImageLoader != null) {
            if (isAvatarExists) {
                if (showType == MsgListAdapter.RECEIVE) {
                    communication_parent.setGravity(Gravity.LEFT);
                    mImageAvatar_send.setVisibility(View.GONE);
                    mImageAvatar_receive.setVisibility(View.VISIBLE);
                    mImageLoader.loadAvatarImage(mImageAvatar_receive, message.getFromUser().getAvatarFilePath(),"CIRCLE");
                } else {
                    communication_parent.setGravity(Gravity.RIGHT);
                    mImageAvatar_receive.setVisibility(View.GONE);
                    mImageAvatar_send.setVisibility(View.VISIBLE);
                    mImageLoader.loadAvatarImage(mImageAvatar_send, message.getFromUser().getAvatarFilePath(),"CIRCLE");
                }
            }
        }


        mDateTv.setVisibility(View.VISIBLE);

        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }




    }


    @Override
    public void applyStyle(MessageListStyle style) {


        if(messageHandled){
           // buttonParent.setBackgroundColor(ContextCompat.getColor(exchangeRefuse.getContext(),R.color.grayFE));

            exchangeReceive.setTextColor(ContextCompat.getColor(exchangeReceive.getContext(),R.color.grayCE));
            exchangeRefuse.setTextColor(ContextCompat.getColor(exchangeRefuse.getContext(),R.color.grayCE));
        }

        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());

        mImageAvatar_receive.setBorderRadius(style.getAvatarRadius());
        mImageAvatar_send.setBorderRadius(style.getAvatarRadius());


    }


}