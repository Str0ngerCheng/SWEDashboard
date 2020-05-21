package com.bio.sys.vo;


import java.io.Serializable;

public class TopicReportStatisticVO implements Serializable,Comparable<TopicReportStatisticVO> {

    private static final long serialVersionUID = 1L;

    private String deptName;

    private Integer totalCount;

    private Integer unMSubmitCount;

    private String unMSubmitUsers;

    private Integer orderNum;

    private Integer isLSubmit;
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUnMSubmitCount() {
        return unMSubmitCount;
    }

    public void setUnMSubmitCount(Integer unMSubmitCount) {
        this.unMSubmitCount = unMSubmitCount;
    }

    public String getUnMSubmitUsers() {
        return unMSubmitUsers;
    }

    public void setUnMSubmitUsers(String unMSubmitUsers) {
        this.unMSubmitUsers = unMSubmitUsers;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIsLSubmit() {
        return isLSubmit;
    }

    public void setIsLSubmit(Integer isSubmit) {
        this.isLSubmit = isSubmit;
    }

    @Override
    public int compareTo(TopicReportStatisticVO o) {
        if(this.orderNum>o.orderNum) return  1;
        else if(this.orderNum<o.orderNum) return -1;
        else return 0;
    }
}
