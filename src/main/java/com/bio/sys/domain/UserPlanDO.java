package com.bio.sys.domain;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("user_plan")
public class UserPlanDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /** 作者ID */
    private Long authorId;

    /** 作者姓名 */
    private String authorName;

    /** 起始时间 */
    private Date pFromDate;

    /** 终止时间 */
    private Date pToDate;

    /** 月度计划 */
    private String monthPlan;

    /** 学期计划 */
    private String termPlan;

    /** 创建时间 */
    private Date pCreateDate;
    /** 修改时间 */

    private Date pChgDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getPFromDate() {
        return pFromDate;
    }

    public void setPFromDate(Date pFromDate) {
        this.pFromDate = pFromDate;
    }

    public Date getPToDate() {
        return pToDate;
    }

    public void setPToDate(Date pToDate) {
        this.pToDate = pToDate;
    }

    public String getMonthPlan() {
        return monthPlan;
    }

    public void setMonthPlan(String monthPlan) {
        this.monthPlan = monthPlan;
    }

    public String getTermPlan() {
        return termPlan;
    }

    public void setTermPlan(String termPlan) {
        this.termPlan = termPlan;
    }

    public Date getPCreateDate() {
        return pCreateDate;
    }

    public void setPCreateDate(Date pCreateDate) {
        this.pCreateDate = pCreateDate;
    }

    public Date getPChgDate() {
        return pChgDate;
    }

    public void setPChgDate(Date pChgDate) {
        this.pChgDate = pChgDate;
    }
}
