package com.hr.entity;

import java.util.Date;

public class Performance {
    private Integer perfId;
    private Integer empId;
    private String period;
    private Double score;
    private String performanceGrade;
    private String evaluator;
    private Date evaluateDate;
    private String remark;
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;

    public Performance() {}

    public Integer getPerfId() { return perfId; }
    public void setPerfId(Integer perfId) { this.perfId = perfId; }

    public Integer getEmpId() { return empId; }
    public void setEmpId(Integer empId) { this.empId = empId; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getPerformanceGrade() { return performanceGrade; }
    public void setPerformanceGrade(String performanceGrade) { this.performanceGrade = performanceGrade; }

    public String getEvaluator() { return evaluator; }
    public void setEvaluator(String evaluator) { this.evaluator = evaluator; }

    public Date getEvaluateDate() { return evaluateDate; }
    public void setEvaluateDate(Date evaluateDate) { this.evaluateDate = evaluateDate; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}