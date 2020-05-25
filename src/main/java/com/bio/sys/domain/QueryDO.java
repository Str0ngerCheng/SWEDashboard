package com.bio.sys.domain;

public class QueryDO {
    private Integer id;
    private String title;
    private String summary;
    private String plan;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public QueryDO(String title,String summary,String plan){
        this.plan=plan;
        this.summary=summary;
        this.title=title;
    }
    public QueryDO(Integer id,String title,String summary,String plan){
        this.id=id;
        this.plan=plan;
        this.summary=summary;
        this.title=title;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
