package imui.jiguang.cn.imuisample.models;

import cn.jiguang.imui.commons.models.IUser;


public class DefaultUser implements IUser {

    private String id;
    private String displayName;
    private String avatar;

    public DefaultUser(String id, String displayName, String avatar) {
        this.id = id;
        this.displayName = displayName;

        if(avatar==null || "".equals(avatar)){
            this.avatar="R.mipmap.default_avatar";
        }else{
            this.avatar= avatar;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }
}
