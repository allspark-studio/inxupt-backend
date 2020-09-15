package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;

public class DailyStatistics implements Serializable {
    private Integer id;

    private String date;

    private Integer registerUserNum;

    private Integer activeUserNum;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getRegisterUserNum() {
        return registerUserNum;
    }

    public void setRegisterUserNum(Integer registerUserNum) {
        this.registerUserNum = registerUserNum;
    }

    public Integer getActiveUserNum() {
        return activeUserNum;
    }

    public void setActiveUserNum(Integer activeUserNum) {
        this.activeUserNum = activeUserNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", registerUserNum=").append(registerUserNum);
        sb.append(", activeUserNum=").append(activeUserNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}