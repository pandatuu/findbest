package cn.jiguang.imui.messages;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class InterviewResultHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;
    private int showType;
    private final RoundImageView mImageAvatar;

    private RoundTextView mDateTv;
    private ImageView communication_type;
    private TextView communication_content;


    private TextView interViewResult_label;
    private LinearLayout communication_parent;

    private TextView exchangeAccountCenterButton;


    private LinearLayout interViewResult_container;

    public InterviewResultHolder(View itemView, boolean isSender, int showType) {
        super(itemView);
        this.mIsSender = isSender;
        this.showType=showType;
        mImageAvatar = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);

        mDateTv =  itemView.findViewById(R.id.aurora_tv_msgitem_date);
        communication_type =  itemView.findViewById(R.id.communication_type);
        communication_content=  itemView.findViewById(R.id.communication_content);

        interViewResult_label=  itemView.findViewById(R.id.interViewResult_label);

        interViewResult_container=  itemView.findViewById(R.id.interViewResult_container);





    }

    @Override
    public void onBind(final MESSAGE message) {



        communication_content.setText(message.getText());

        String timeString = message.getTimeString();
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();

        if(showType==MsgListAdapter.SUCCESS){
            interViewResult_label.setText("面接结果");
            interViewResult_container.setBackgroundResource(R.drawable.mask_light);
            interViewResult_label.setBackgroundResource(R.drawable.label_light);
        }
        else{
            interViewResult_label.setText("面接结果");
            interViewResult_container.setBackgroundResource(R.drawable.mask_gray);
            interViewResult_label.setBackgroundResource(R.drawable.label_gray);

        }

        if (mImageLoader != null) {
            if (isAvatarExists) {
                mImageLoader.loadAvatarImage(mImageAvatar, message.getFromUser().getAvatarFilePath(),"CIRCLE");
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


        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());

        mImageAvatar.setBorderRadius(style.getAvatarRadius());


    }



}