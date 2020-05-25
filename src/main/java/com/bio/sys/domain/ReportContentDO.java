package com.bio.sys.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;


@TableName("report_content")
public class ReportContentDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId
    private Long id;

    /** 上周计划 */
    private String uuid;

    /** 上周计划 */
    private String lastPlan;

    /** 本周总结*/
    private String summary;

    /** 下周计划 */
    private String nextPlan;

    /** 问题反馈*/
    private String problem;

    /** 其他情况 */
    private String other;

    public ReportContentDO(String uuid, String lastPlan, String summary, String nextPlan, String problem, String other) {
        this.uuid = uuid;
        this.lastPlan = lastPlan;
        this.summary = summary;
        this.nextPlan = nextPlan;
        this.problem = problem;
        this.other = other;
    }

    public ReportContentDO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastPlan() {
        return lastPlan;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLastPlan(String lastPlan) {
        this.lastPlan = lastPlan;
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

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "ReportContentDO{" +
                "lastPlan='" + lastPlan + '\'' +
                ", summary='" + summary + '\'' +
                ", nextPlan='" + nextPlan + '\'' +
                ", problem='" + problem + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
