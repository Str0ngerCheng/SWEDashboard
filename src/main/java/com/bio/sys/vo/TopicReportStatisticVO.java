package com.bio.sys.vo;


import java.io.Serializable;

public class TopicReportStatisticVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deptName;

    private Integer totalCount;

    private Integer unMSubmitCount;

    private String unMSubmitUsers;

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
}
