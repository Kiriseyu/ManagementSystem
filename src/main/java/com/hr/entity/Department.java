package com.hr.entity;

import java.sql.Timestamp;

public class Department {
    private Integer deptId;
    private String deptName;
    private String deptLocation;
    private String deptPhone;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Department() {
    }

    public Department(Integer deptId, String deptName, String deptLocation, String deptPhone, Timestamp createTime, Timestamp updateTime) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptLocation = deptLocation;
        this.deptPhone = deptPhone;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptLocation() {
        return deptLocation;
    }

    public void setDeptLocation(String deptLocation) {
        this.deptLocation = deptLocation;
    }

    public String getDeptPhone() {
        return deptPhone;
    }

    public void setDeptPhone(String deptPhone) {
        this.deptPhone = deptPhone;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
