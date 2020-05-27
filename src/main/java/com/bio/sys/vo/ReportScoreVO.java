package com.bio.sys.vo;

import java.io.Serializable;

public class ReportScoreVO implements Serializable,Comparable<ReportScoreVO>  {
    private static final long serialVersionUID = 1L;
    /** 作者Id */
    private Long authorId;
    /** 作者姓名 */
    private String authorName;
    /** 专题Id*/
    private Long deptId;
    /** 专题名*/
    private String deptName;
    /** 分数*/
    private String score;

    private Integer orderNum;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public int compareTo(ReportScoreVO o) {
        if(this.deptId>o.deptId) return 1;
        else if(this.deptId<o.deptId) return  -1;
        else {
            return this.authorName.compareTo(o.authorName);
        }
    }
}
