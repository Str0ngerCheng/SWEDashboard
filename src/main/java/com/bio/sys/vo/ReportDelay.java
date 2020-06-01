package com.bio.sys.vo;

import java.io.Serializable;

public class ReportDelay implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 作者Id */
    private Long authorId;
    /** 作者姓名 */
    private String authorName;
    /** 专题Id*/
    private Long deptId;
    /** 专题名*/
    private String deptName;

    private Integer count;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}
