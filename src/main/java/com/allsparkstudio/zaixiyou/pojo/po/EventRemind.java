package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import java.util.Date;

public class EventRemind implements Serializable {
    private Integer id;

    private Integer action;

    private Integer sourceId;

    private String sourceContent;

    private Integer state;

    private Integer senderId;

    private Integer receiveId;

    private Date remindTime;

    private String replyContent;

    private Integer postType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceContent() {
        return sourceContent;
    }

    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent == null ? null : sourceContent.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", action=").append(action);
        sb.append(", sourceId=").append(sourceId);
        sb.append(", sourceContent=").append(sourceContent);
        sb.append(", state=").append(state);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiveId=").append(receiveId);
        sb.append(", remindTime=").append(remindTime);
        sb.append(", replyContent=").append(replyContent);
        sb.append(", postType=").append(postType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}