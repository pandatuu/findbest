package cn.jiguang.imui.messages;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.JobInfoModel;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class JobInfoViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;


    private TextView positionName;
    private TextView companyName;


    private ImageView haveTraffic;
    private ImageView haveSocialInsurance;
    private ImageView haveClub;
    private ImageView haveCanteen;
    private ImageView  isNew;
    private RoundImageView   userlogo;
    private TextView  showSalaryMinToMax;
    private TextView  salaryType;
    private TextView  address;
    private TextView  workingExperience;
    private TextView  educationalBackground;
    private TextView  userPositionNameAndUserName;

    private TextView datetTime;

    private LinearLayout jobInfoContainer;
    public JobInfoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;



        positionName =  itemView.findViewById(R.id.positionName);
        companyName =  itemView.findViewById(R.id.companyName);
        showSalaryMinToMax =  itemView.findViewById(R.id.showSalaryMinToMax);
        salaryType=  itemView.findViewById(R.id.salaryType);
        address=  itemView.findViewById(R.id.address);
        workingExperience=  itemView.findViewById(R.id.workingExperience);
        educationalBackground=  itemView.findViewById(R.id.educationalBackground);
        userPositionNameAndUserName=  itemView.findViewById(R.id.userPositionNameAndUserName);

        datetTime=  itemView.findViewById(R.id.datetTime);



        haveTraffic =  itemView.findViewById(R.id.haveTraffic);
        haveSocialInsurance =  itemView.findViewById(R.id.haveSocialInsurance);
        haveClub =  itemView.findViewById(R.id.haveClub);
        haveCanteen =  itemView.findViewById(R.id.haveCanteen);
        isNew=  itemView.findViewById(R.id.isNew);
        userlogo=  itemView.findViewById(R.id.userlogo);

        jobInfoContainer=  itemView.findViewById(R.id.jobInfoContainer);




    }

    @Override
    public void onBind(final MESSAGE message) {


        JobInfoModel model= message.getJsobInfo();


        if(model!=null){
            positionName.setText(model.getName());
            companyName.setText(model.getCompanyName());
            showSalaryMinToMax.setText(model.getShowSalaryMinToMax());
            salaryType.setText(model.getSalaryType());
            userPositionNameAndUserName.setText(model.getUserName()+"."+model.getUserPositionName());

            if(!model.getHaveCanteen()){
                haveCanteen.setVisibility(View.GONE);
            }else{
                haveCanteen.setVisibility(View.VISIBLE);
            }

            if(!model.getHaveClub()){
                haveClub.setVisibility(View.GONE);
            }else{
                haveClub.setVisibility(View.VISIBLE);
            }


            if(!model.getHaveSocialInsurance()){
                haveSocialInsurance.setVisibility(View.GONE);
            }else{
                haveSocialInsurance.setVisibility(View.VISIBLE);
            }


            if(!model.getHaveTraffic()){
                haveTraffic.setVisibility(View.GONE);
            }else{
                haveTraffic.setVisibility(View.VISIBLE);
            }

            if(!model.getNew()){
                isNew.setVisibility(View.GONE);
            }else{
                isNew.setVisibility(View.VISIBLE);
            }


            if(model.getAddress()!=null && !"".equals(model.getAddress())){
                address.setText(model.getAddress());
            }else{
                address.setVisibility(View.GONE);
            }

            if(model.getWorkingExperience()!=null && !"".equals(model.getWorkingExperience()) && !"0".equals(model.getWorkingExperience())){
                workingExperience.setText(model.getWorkingExperience()+"年");
            }else{
                workingExperience.setVisibility(View.GONE);
            }

            if(model.getEducationalBackground()!=null && !"".equals(model.getEducationalBackground())){
                educationalBackground.setText(model.getEducationalBackground());
            }else{
                educationalBackground.setVisibility(View.GONE);
            }

            if(model.getAvatarURL()!=null && !"".equals(model.getAvatarURL()) && model.getAvatarURL().contains("http")){
                mImageLoader.loadAvatarImage(userlogo, model.getAvatarURL(),"CIRCLE");
            }

            if(model.getDateTimeStr()!=null){
                datetTime.setText(model.getDateTimeStr());
            }


        }




        jobInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMsgClickListener != null) {
                    if(message.getJsobInfo()!=null && message.getJsobInfo().getRecruitMessageId()!=null && !message.getJsobInfo().getRecruitMessageId().equals("")){
                        mMsgClickListener.onMessageClick(message);
                    }
                }
            }
        });


    }

    @Override
    public void applyStyle(MessageListStyle style) {

        userlogo.setBorderRadius(style.getAvatarRadius());



    }





}