package imui.jiguang.cn.imuisample.models;

import cn.jiguang.imui.commons.models.IUser;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ResumeListItem  {



    private String title;
    private String time;
    private String size;
    private int type;
    private String attachmentType;
    private String url;
    private String id;
    private String mediaId;
    //是主动 还是 对方请求
    private int chooseType;

    public int getChooseType() {
        return chooseType;
    }

    public String getMediaId() {
        return mediaId;
    }

    public ResumeListItem(String id, String mediaId, String title, Long time, String size, int type, String attachmentType, String url,int chooseType) {

        this.id = id;
        this.title = title;

        if(time!=null){
            Date updateTime=new Date(time);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
            this.time = sdf.format(updateTime)+"更新";
        }
        this.mediaId=mediaId;
        this.size = size;
        this.type = type;
        this.attachmentType=attachmentType;
        this.url=url;
        this.chooseType=chooseType;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
