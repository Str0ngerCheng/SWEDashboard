package com.bio.sys.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
 * <pre>
 * 周报
 * </pre>
 * <small> 2019-12-15 10:18:15 | chenx</small>
 */
 @TableName("report")
public class ReportDO implements Serializable {
	 
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /** ID */
    @TableId
    private Long id;
    /** 作者ID */
    private Long authorId;
    /** 作者姓名 */
    private String authorName;
    
    /** 所属部门ID */
    private Long deptId;
    /** 所属部门名称 */
    private String deptName;
    
    /** 部门所属上级部门ID */
    private Long parentDeptId;
    
    /** 起始时间 */
    private Date rFromDate;
    /** 终止时间 */
    private Date rToDate;
    /** 周报题目 */
    private String title;
    /** 周报内容 */
    private String contentId;

    /** 评价 */
    private String comment;

    /** 分数*/
    private String score;

    /** 意见*/
    private String suggest;

    /** 创建时间 */
    private Date rCreateDate;
    /** 修改时间 */

    private Date rChgDate;
    /** 状态0: 系统生成, 1:人工修改 */
    private Integer statusMod;
    /** 状态0: 本人未提交, 1:本人已提交 */
    private Integer statusMSub;
    /** 状态0: 组长未提交, 1:组长已提交 */
    private Integer statusLSub;
    /**
     * 设置：ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 获取：ID
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：作者ID
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    /**
     * 获取：作者ID
     */
    public Long getAuthorId() {
        return authorId;
    }
    /**
     * 设置：作者姓名
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    /**
     * 获取：作者姓名
     */
    public String getAuthorName() {
        return authorName;
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
	
    public Long getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(Long parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	/**
     * 设置：起始时间
     */
    public void setRFromDate(Date rFromDate) {
        this.rFromDate = rFromDate;
    }
    /**
     * 获取：起始时间
     */
    public Date getRFromDate() {
        return rFromDate;
    }
    /**
     * 设置：终止时间
     */
    public void setRToDate(Date rToDate) {
        this.rToDate = rToDate;
    }
    /**
     * 获取：终止时间
     */
    public Date getRToDate() {
        return rToDate;
    }
    /**
     * 设置：周报题目
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * 获取：周报题目
     */
    public String getTitle() {
        return title;
    }
    /**
     * 获取：周报内容ID
     */
    public String getContentId() {
        return contentId;
    }
    /**
     * 设置：周报内容ID
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    /**
     * 设置：创建时间
     */
    public void setRCreateDate(Date rCreateDate) {
        this.rCreateDate = rCreateDate;
    }
    /**
     * 获取：创建时间
     */
    public Date getRCreateDate() {
        return rCreateDate;
    }
    /**
     * 设置：修改时间
     */
    public void setRChgDate(Date rChgDate) {
        this.rChgDate = rChgDate;
    }
    /**
     * 获取：修改时间
     */
    public Date getRChgDate() {
        return rChgDate;
    }

    public Integer getStatusMod() {
        return statusMod;
    }

    public void setStatusMod(Integer statusMod) {
        this.statusMod = statusMod;
    }

    public Integer getStatusMSub() {
        return statusMSub;
    }

    public void setStatusMSub(Integer statusMSub) {
        this.statusMSub = statusMSub;
    }

    public Integer getStatusLSub() {
        return statusLSub;
    }

    public void setStatusLSub(Integer statusLSub) {
        this.statusLSub = statusLSub;
    }

    @Override
    public String toString() {
        return "ReportDO{" +
                "ide='" + id + '\'' +
                "authorName='" + authorName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", title='" + title + '\'' +
                ", contentId='" + contentId + '\'' +
                ", score='" + score + '\'' +
                ", statusMod=" + statusMod +
                ", statusMSub=" + statusMSub +
                ", statusLSub=" + statusLSub +
                '}';
    }
}
