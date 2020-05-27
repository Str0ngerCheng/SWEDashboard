package com.bio.sys.domain;

import java.io.Serializable;


public class TopicDao implements Serializable {
    private String deptName;
    private String authorName;
    private String title;
    private String summary;
    private String problam;
    private String nextPlan;
    private String comment;
    private String suggest;


    public TopicDao(String deptName,String authorName,String title,String summary,String problam,String nextPlan,String comment,String suggest) {
        this.deptName=deptName;
        this.authorName=authorName;
        this.title=title;
        this.summary=summary;
        this.problam=problam;
        this.nextPlan=nextPlan;
        this.comment=comment;
        this.suggest=suggest;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProblam() {
        return problam;
    }

    public void setProblam(String problam) {
        this.problam = problam;
    }

    public String getNextPlan() {
        return nextPlan;
    }

    public void setNextPlan(String nextPlan) {
        this.nextPlan = nextPlan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
