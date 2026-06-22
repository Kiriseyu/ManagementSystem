package com.hr.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private Integer deptId;
    private String deptName;
    private String deptLocation;
    private String deptPhone;
    private Integer parentId;  // 父部门ID，用于树形结构
    private List<Department> children;  // 子部门列表
    private Timestamp createTime;
    private Timestamp updateTime;

    public Department() {
    }

    public Department(Integer deptId, String deptName, String deptLocation, String deptPhone, Integer parentId, Timestamp createTime, Timestamp updateTime) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptLocation = deptLocation;
        this.deptPhone = deptPhone;
        this.parentId = parentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
