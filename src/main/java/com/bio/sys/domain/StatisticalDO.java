package com.bio.sys.domain;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("attendance")
public class StatisticalDO implements Serializable {
    private Integer id;
    private String userName;
    private Double absenceRate;
    private Date rFromDate;
    private Date rToDate;

    public StatisticalDO(String useName, Double aRate, Date startDate, Date endDate) {
        this.userName=useName;
        this.absenceRate=aRate;
        this.rFromDate=startDate;
        this.rToDate=endDate;
    }

/*    public StatisticalDO(Integer id, String useName, Double aRate, Date startDate, Date endDate) {
        this.id=id;
        this.userName=useName;
        this.absenceRate=aRate;
        this.rFromDate=startDate;
        this.rToDate=endDate;
    }*/

    public StatisticalDO(String name, Double absenceRate) {
        this.userName=name;
        this.absenceRate=absenceRate;
    }

    public StatisticalDO(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getAbsenceRate() {
        return absenceRate;
    }

    public void setAbsenceRate(Double absenceRate) {
        this.absenceRate = absenceRate;
    }

    public Date getrFromDate() {
        return rFromDate;
    }

    public void setrFromDate(Date rFromDate) {
        this.rFromDate = rFromDate;
    }

    public Date getrToDate() {
        return rToDate;
    }

    public void setrToDate(Date rToDate) {
        this.rToDate = rToDate;
    }
}
