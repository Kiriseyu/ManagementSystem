package com.hr.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Employee {
    private Integer empId;
    private String empName;
    private String empGender;
    private Date empBirthdate;
    private String empPhone;
    private String empEmail;
    private String empAddress;
    private Integer deptId;
    private Date hireDate;
    private String jobTitle;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Employee() {
    }

    public Employee(Integer empId, String empName, String empGender, Date empBirthdate, String empPhone, String empEmail, String empAddress, Integer deptId, Date hireDate, String jobTitle, Timestamp createTime, Timestamp updateTime) {
        this.empId = empId;
        this.empName = empName;
        this.empGender = empGender;
        this.empBirthdate = empBirthdate;
        this.empPhone = empPhone;
        this.empEmail = empEmail;
        this.empAddress = empAddress;
        this.deptId = deptId;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public Date getEmpBirthdate() {
        return empBirthdate;
    }

    public void setEmpBirthdate(Date empBirthdate) {
        this.empBirthdate = empBirthdate;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
