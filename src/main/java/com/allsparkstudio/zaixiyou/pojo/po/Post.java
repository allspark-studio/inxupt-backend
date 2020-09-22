package com.allsparkstudio.zaixiyou.pojo.po;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private Integer id;

    private String body;

    private Integer authorId;

    private Integer state;

    private Date createTime;

    private Date editTime;

    private Integer type;

    private String postMediaUrls;

    private String articleTitle;

    private String articleText;

    private String atIds;

    private Boolean visibleOutsideCircle;

    private String articleCover;

    private String tags;

    private Integer heat;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPostMediaUrls() {
        return postMediaUrls;
    }

    public void setPostMediaUrls(String postMediaUrls) {
        this.postMediaUrls = postMediaUrls == null ? null : postMediaUrls.trim();
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle == null ? null : articleTitle.trim();
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText == null ? null : articleText.trim();
    }

    public String getAtIds() {
        return atIds;
    }

    public void setAtIds(String atIds) {
        this.atIds = atIds == null ? null : atIds.trim();
    }

    public Boolean getVisibleOutsideCircle() {
        return visibleOutsideCircle;
    }

    public void setVisibleOutsideCircle(Boolean visibleOutsideCircle) {
        this.visibleOutsideCircle = visibleOutsideCircle;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover == null ? null : articleCover.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", body=").append(body);
        sb.append(", authorId=").append(authorId);
        sb.append(", state=").append(state);
        sb.append(", createTime=").append(createTime);
        sb.append(", editTime=").append(editTime);
        sb.append(", type=").append(type);
        sb.append(", postMediaUrls=").append(postMediaUrls);
        sb.append(", articleTitle=").append(articleTitle);
        sb.append(", articleText=").append(articleText);
        sb.append(", atIds=").append(atIds);
        sb.append(", visibleOutsideCircle=").append(visibleOutsideCircle);
        sb.append(", articleCover=").append(articleCover);
        sb.append(", tags=").append(tags);
        sb.append(", heat=").append(heat);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}