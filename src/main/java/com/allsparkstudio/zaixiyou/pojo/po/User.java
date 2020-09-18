package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer id;

    private String phone;

    private String nickname;

    private String password;

    private String description;

    private Integer gender;

    private String grade;

    private String major;

    private String avatarUrl;

    private Integer level;

    private Integer experience;

    private Date registerTime;

    private Integer insertableCoins;

    private Integer exchangeableCoins;

    private Integer state;

    private String userpageBgImgUrl;

    private String customAppColor;

    private String lastActiveTime;

    private Integer likeNum;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major == null ? null : major.trim();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getInsertableCoins() {
        return insertableCoins;
    }

    public void setInsertableCoins(Integer insertableCoins) {
        this.insertableCoins = insertableCoins;
    }

    public Integer getExchangeableCoins() {
        return exchangeableCoins;
    }

    public void setExchangeableCoins(Integer exchangeableCoins) {
        this.exchangeableCoins = exchangeableCoins;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserpageBgImgUrl() {
        return userpageBgImgUrl;
    }

    public void setUserpageBgImgUrl(String userpageBgImgUrl) {
        this.userpageBgImgUrl = userpageBgImgUrl == null ? null : userpageBgImgUrl.trim();
    }

    public String getCustomAppColor() {
        return customAppColor;
    }

    public void setCustomAppColor(String customAppColor) {
        this.customAppColor = customAppColor == null ? null : customAppColor.trim();
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime == null ? null : lastActiveTime.trim();
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", phone=").append(phone);
        sb.append(", nickname=").append(nickname);
        sb.append(", password=").append(password);
        sb.append(", description=").append(description);
        sb.append(", gender=").append(gender);
        sb.append(", grade=").append(grade);
        sb.append(", major=").append(major);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", level=").append(level);
        sb.append(", experience=").append(experience);
        sb.append(", registerTime=").append(registerTime);
        sb.append(", insertableCoins=").append(insertableCoins);
        sb.append(", exchangeableCoins=").append(exchangeableCoins);
        sb.append(", state=").append(state);
        sb.append(", userpageBgImgUrl=").append(userpageBgImgUrl);
        sb.append(", customAppColor=").append(customAppColor);
        sb.append(", lastActiveTime=").append(lastActiveTime);
        sb.append(", likeNum=").append(likeNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}