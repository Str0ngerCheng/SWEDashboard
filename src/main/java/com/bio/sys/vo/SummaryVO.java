package com.bio.sys.vo;

import com.bio.sys.domain.SummaryDO;

import java.io.Serializable;
import java.util.Date;

public class SummaryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long deptId;
    /** 部门名称 */
    private String deptName;
    /** 周报题目 */
    private String title;
    /** 浏览次数 */
    private Long count;
    /** 创建时间 */
    private Date rCreateDate;
    /** 创建时间 */
    private Integer orderNum;

    public SummaryVO() {
    }

    public SummaryVO(SummaryDO summaryDO, Integer orderNum) {
        this.deptId = summaryDO.getDeptId();
        this.deptName = summaryDO.getDeptName();
        this.title = summaryDO.getTitle();
        this.count = summaryDO.getCount();
        this.rCreateDate = summaryDO.getRCreateDate();
        this.orderNum = orderNum;
    }

    public SummaryVO(Long deptId, String deptName, String name, Long count, Date rCreateDate, Integer orderNum) {
        this.deptId=deptId;
        this.deptName=deptName;
        this.title=name;
        this.count=count;
        this.rCreateDate=rCreateDate;
        this.orderNum=orderNum;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getrCreateDate() {
        return rCreateDate;
    }

    public void setrCreateDate(Date rCreateDate) {
        this.rCreateDate = rCreateDate;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
