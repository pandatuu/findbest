package imui.jiguang.cn.imuisample.models;

import java.util.HashMap;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.model.JobInfoModel;


public class MyMessage implements IMessage {

    private long id;
    private String text;
    private String translateText;
    private String timeString;
    private int type;
    private IUser user;
    private String mediaFilePath;
    private long duration;
    private String progress;
    private MessageStatus mMsgStatus = MessageStatus.CREATED;
    public static Boolean isTrans = false;


    //消息通道中接收到的消息的id
    private String messageChannelMsgId;
    //消息是否已经被处理
    private boolean handled=false;
    //表示文件大小
    private String size;
    //房间号
    private String roomNumber;
    //文件类型
    private  Integer mimeType;


    //交换信息id / 面试状态信息id
    private String interviewId;

    @Override
    public   boolean getEnable(){
        return isTrans;
    }

    private JobInfoModel jsobInfo=null;

    public MyMessage() {

    }

    public MyMessage(String text, int type) {
        this.text = text;
        this.type = type;

        this.id = UUID.randomUUID().getLeastSignificantBits();

        //如果是被处理了
        if( type== MessageType.RECEIVE_EXCHANGE_LINE_HANDLED.ordinal() ||
            type== MessageType.RECEIVE_EXCHANGE_PHONE_HANDLED.ordinal() ||
            type== MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal()||
            type== MessageType.RECEIVE_INTERVIEW_VIDEO_HANDLED.ordinal()){
            setHandled(true);
        }
    }

    public void setInterviewId(String interviewId) {
        this.interviewId = interviewId;
    }

    public void setTransMsg(String msg){
        this.translateText = msg;
    }

    public String getInterviewId() {
        return interviewId;
    }

    @Override
    public JobInfoModel getJsobInfo() {
        return jsobInfo;
    }

    public void setJsobInfo(JobInfoModel jsobInfo) {
        this.jsobInfo = jsobInfo;
    }

    @Override
    public String getMsgId() {
        return String.valueOf(id);
    }

    public long getId() {
        return this.id;
    }

    public String getMessageChannelMsgId(){
        return this.messageChannelMsgId;
    }
    public  void setMessageChannelMsgId(String messageChannelMsgId){
        this.messageChannelMsgId=messageChannelMsgId;
    }



    public String getRoomNumber(){
        return this.roomNumber;
    }

    public void setRoomNumber(String roomNumber){
        this.roomNumber=roomNumber;
    }




    @Override
    public String getSize() {
        return this.size;
    }


    public void setSize(String s){
        this.size=s;
    }
    @Override
    public boolean getHandled(){
        return this.handled;
    }
    public void setHandled(boolean handled){
        this.handled=handled;
    }

    @Override
    public IUser getFromUser() {
        if (user == null) {
            return new DefaultUser("0", "user1", null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public void setMediaFilePath(String path) {
        this.mediaFilePath = path;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String getProgress() {
        return progress;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String getTimeString() {
        return timeString;
    }

    public void setType(int type) {
//        if (type >= 0 && type <= 12) {
//            throw new IllegalArgumentException("Message type should not take the value between 0 and 12");
//        }

        if( type== MessageType.RECEIVE_EXCHANGE_LINE_HANDLED.ordinal() ||
            type== MessageType.RECEIVE_EXCHANGE_PHONE_HANDLED.ordinal() ||
            type== MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal() ||
                type== MessageType.RECEIVE_INTERVIEW_VIDEO_HANDLED.ordinal()
                ){
            setHandled(true);
        }

        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }


    public void  setMimeType(Integer mimeType) {
       this.mimeType=mimeType;
    }
    /**
     * @return
     */
    @Override
    public Integer getMimeType() {
        return this.mimeType;
    }

    /**
     * Set Message status. After sending Message, change the status so that the progress bar will dismiss.
     * @param messageStatus {@link MessageStatus}
     */
    public void setMessageStatus(MessageStatus messageStatus) {
        this.mMsgStatus = messageStatus;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return this.mMsgStatus;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return mediaFilePath;
    }
}