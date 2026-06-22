package com.hr.entity;

/**
 * 职称实体类
 */
public class Title {
    private Integer titleId;
    private String titleName;
    private String titleLevel;  // 初级, 中级, 副高级, 高级
    private String titleDesc;
    private Integer status;
    private String createTime;
    private String updateTime;

    // 构造函数
    public Title() {}

    public Title(Integer titleId, String titleName, String titleLevel, Integer status) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.titleLevel = titleLevel;
        this.status = status;
    }

    // Getter和Setter方法
    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(String titleLevel) {
        this.titleLevel = titleLevel;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}