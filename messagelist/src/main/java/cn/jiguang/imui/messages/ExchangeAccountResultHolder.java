package cn.jiguang.imui.messages;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class ExchangeAccountResultHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;
    private int icoType;
    private int showType;
    private final RoundImageView mImageAvatar_receive;
    private final RoundImageView mImageAvatar_send;

    private RoundTextView mDateTv;
    private ImageView communication_type;
    private TextView communication_content;
    private LinearLayout communication_parent;

    private TextView exchangeAccountCenterButton;
    private boolean messageHandled;

    public ExchangeAccountResultHolder(View itemView, boolean isSender, int showType, int icoType,boolean handled) {
        super(itemView);
        this.mIsSender = isSender;
        this.icoType=icoType;
        this.showType=showType;
        this.messageHandled=handled;

        mImageAvatar_receive = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar_receive);
        mImageAvatar_send = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar_send);

        mDateTv =  itemView.findViewById(R.id.aurora_tv_msgitem_date);
        communication_type =  itemView.findViewById(R.id.communication_type);
        communication_content=  itemView.findViewById(R.id.communication_content);

        communication_parent=  itemView.findViewById(R.id.communication_parent);

        exchangeAccountCenterButton=itemView.findViewById(R.id.exchangeAccountCenterButton);

    }

    @Override
    public void onBind(final MESSAGE message) {

        if(icoType==MsgListAdapter.PHONE){
            communication_type.setImageResource(R.drawable.ico_phone);
            exchangeAccountCenterButton.setText("呼び出す");
            exchangeAccountCenterButton.setOnClickListener(new phoneListener());
        }else if(icoType==MsgListAdapter.LINE){
            communication_type.setImageResource(R.drawable.ico_line);
            exchangeAccountCenterButton.setText("コピー");
            exchangeAccountCenterButton.setOnClickListener(new View.OnClickListener(){
                // 复制Line账号
                @Override
                public void onClick(View v) {
                    String linePhone = communication_content.getText().toString().trim();
                    int last = linePhone.lastIndexOf("：");
                    String resultLine = linePhone.substring(last+1);
                    System.out.println(resultLine);
                    ClipboardManager copy = (ClipboardManager) mContext
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    copy.setText(resultLine);


                    mMsgClickListener.onMessageClick(message);

                }
            });
        }else if(icoType==MsgListAdapter.VIDEO){
            communication_type.setImageResource(R.drawable.ico_video);
        }


        communication_content.setText(message.getText());

        String timeString = message.getTimeString();
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();


        if (mImageLoader != null) {
            if (isAvatarExists) {
                if(showType==MsgListAdapter.RECEIVE){
                    communication_parent.setGravity(Gravity.LEFT);
                    mImageAvatar_send.setVisibility(View.GONE);
                    mImageAvatar_receive.setVisibility(View.VISIBLE);
                    mImageLoader.loadAvatarImage(mImageAvatar_receive, message.getFromUser().getAvatarFilePath(),"CIRCLE");
                }
                else{
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


        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());

        mImageAvatar_receive.setBorderRadius(style.getAvatarRadius());
        mImageAvatar_send.setBorderRadius(style.getAvatarRadius());


    }




    // 跳转到拨打电话界面
    class phoneListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String phone = communication_content.getText().toString().trim();
            int last = phone.lastIndexOf("：");
            String resultPhone = phone.substring(last+1);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + resultPhone);
            intent.setData(data);
            mContext.startActivity(intent);
        }
    }

}