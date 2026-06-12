package com.hr.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Salary {
    private Integer salId;
    private Integer empId;
    private String salMonth;
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal allowance;
    private BigDecimal deduction;
    private BigDecimal totalSalary;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Salary() {
    }

    public Salary(Integer salId, Integer empId, String salMonth, BigDecimal baseSalary, BigDecimal bonus, BigDecimal allowance, BigDecimal deduction, BigDecimal totalSalary, Timestamp createTime, Timestamp updateTime) {
        this.salId = salId;
        this.empId = empId;
        this.salMonth = salMonth;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.allowance = allowance;
        this.deduction = deduction;
        this.totalSalary = totalSalary;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getSalId() {
        return salId;
    }

    public void setSalId(Integer salId) {
        this.salId = salId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getSalMonth() {
        return salMonth;
    }

    public void setSalMonth(String salMonth) {
        this.salMonth = salMonth;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
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
