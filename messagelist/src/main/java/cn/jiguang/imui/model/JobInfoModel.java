package cn.jiguang.imui.model;



public class JobInfoModel  {

    //工作经验 可选
    private String workingExperience;
    //货币类型
    private String currencyType;
    //薪水类型
    private String salaryType;
    //展示出来的薪水范围
    private String showSalaryMinToMax;
    //教育背景 可选
    private String educationalBackground;
    //地点 可选
    private String  address;
    //
    private String  content;
    //是最新的吗
    private Boolean isNew;
    //职位名称
    private String name;
    //公司名称
    private String  companyName;
    //有食堂吗
    private Boolean haveCanteen;
    //有俱乐部吗
    private Boolean  haveClub;
    //有社保吗
    private Boolean  haveSocialInsurance;
    //有交通补助吗
    private Boolean  haveTraffic;
    //发布人职位名称
    private String  userPositionName ;
    //用户头像
    private String avatarURL  ;
    //用户ID
    private String userId  ;
    //用户名字
    private String userName  ;
    //是否搜藏
    private Boolean isCollection  ;
    //本条招聘信息的id
    private String recruitMessageId ;
    //技能要求
    private String skill;
    //所属的公司
    private String organizationId;
    //搜藏记录Id
    private String  collectionId;
    //底部的时间
    private String  dateTimeStr;


    public JobInfoModel(String workingExperience, String currencyType, String salaryType, String showSalaryMinToMax, String educationalBackground, String address, String content, Boolean isNew, String name, String companyName, Boolean haveCanteen, Boolean haveClub, Boolean haveSocialInsurance, Boolean haveTraffic, String userPositionName, String avatarURL, String userId, String userName, Boolean isCollection, String recruitMessageId, String skill, String organizationId, String collectionId) {
        this.workingExperience = workingExperience;
        this.currencyType = currencyType;
        this.salaryType = salaryType;
        this.showSalaryMinToMax = showSalaryMinToMax;
        this.educationalBackground = educationalBackground;
        this.address = address;
        this.content = content;
        this.isNew = isNew;
        this.name = name;
        this.companyName = companyName;
        this.haveCanteen = haveCanteen;
        this.haveClub = haveClub;
        this.haveSocialInsurance = haveSocialInsurance;
        this.haveTraffic = haveTraffic;
        this.userPositionName = userPositionName;
        this.avatarURL = avatarURL;
        this.userId = userId;
        this.userName = userName;
        this.isCollection = isCollection;
        this.recruitMessageId = recruitMessageId;
        this.skill = skill;
        this.organizationId = organizationId;
        this.collectionId = collectionId;
    }
    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public String getWorkingExperience() {
        return workingExperience;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public String getShowSalaryMinToMax() {
        return showSalaryMinToMax;
    }

    public String getEducationalBackground() {
        return educationalBackground;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return content;
    }

    public Boolean getNew() {
        return isNew;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Boolean getHaveCanteen() {
        return haveCanteen;
    }

    public Boolean getHaveClub() {
        return haveClub;
    }

    public Boolean getHaveSocialInsurance() {
        return haveSocialInsurance;
    }

    public Boolean getHaveTraffic() {
        return haveTraffic;
    }

    public String getUserPositionName() {
        return userPositionName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public String getRecruitMessageId() {
        return recruitMessageId;
    }

    public String getSkill() {
        return skill;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getCollectionId() {
        return collectionId;
    }
}
