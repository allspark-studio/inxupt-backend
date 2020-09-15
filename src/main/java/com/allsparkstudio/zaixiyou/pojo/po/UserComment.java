package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;

public class UserComment implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer commentId;

    private Boolean liked;

    private Boolean coined;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getCoined() {
        return coined;
    }

    public void setCoined(Boolean coined) {
        this.coined = coined;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", commentId=").append(commentId);
        sb.append(", liked=").append(liked);
        sb.append(", coined=").append(coined);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}