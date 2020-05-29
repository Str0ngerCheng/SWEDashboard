package com.bio.sys.vo;


import java.io.Serializable;

public class TopicReportDetailsVO implements Serializable,Comparable<TopicReportDetailsVO> {

    private static final long serialVersionUID = 1L;

    private Long reportId;

    private String deptName;

    private String authorName;

    private String monthPlan;

    private String summary;

    private String nextPlan;

    private String problem;

    private String comment;

    private String suggest;

    private Integer deptOrder;

    private Integer userOrder;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getMonthPlan() {
        return monthPlan;
    }

    public void setMonthPlan(String monthPlan) {
        this.monthPlan = monthPlan;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNextPlan() {
        return nextPlan;
    }

    public void setNextPlan(String nextPlan) {
        this.nextPlan = nextPlan;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Integer getDeptOrder() {
        return deptOrder;
    }

    public void setDeptOrder(Integer deptOrder) {
        this.deptOrder = deptOrder;
    }

    public Integer getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(Integer userOrder) {
        this.userOrder = userOrder;
    }

    @Override
    public int compareTo(TopicReportDetailsVO o) {
        if(this.deptOrder>o.deptOrder) return 1;
        else if(this.deptOrder<o.deptOrder) return  -1;
        else {
            if (this.userOrder>o.userOrder) return  1;
            else if(this.userOrder<o.userOrder) return  -1;
            else return  0;
        }
    }

}
