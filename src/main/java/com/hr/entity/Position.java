package com.hr.entity;

/**
 * 职位实体类
 */
public class Position {
    private Integer posId;
    private String posName;
    private Integer posLevel;
    private String posDesc;
    private Integer status;
    private String createTime;
    private String updateTime;

    // 构造函数
    public Position() {}

    public Position(Integer posId, String posName, Integer posLevel, Integer status) {
        this.posId = posId;
        this.posName = posName;
        this.posLevel = posLevel;
        this.status = status;
    }

    // Getter和Setter方法
    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Integer getPosLevel() {
        return posLevel;
    }

    public void setPosLevel(Integer posLevel) {
        this.posLevel = posLevel;
    }

    public String getPosDesc() {
        return posDesc;
    }

    public void setPosDesc(String posDesc) {
        this.posDesc = posDesc;
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