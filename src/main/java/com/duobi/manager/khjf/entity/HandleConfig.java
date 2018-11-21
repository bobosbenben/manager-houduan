package com.duobi.manager.khjf.entity;

import java.util.Date;

public class HandleConfig extends BaseModel{

    private Long id;
    private PointsType pointsType;
    private Date startDate;
    private Date endDate;
    private Date nextStartDate;
    private Date nextEndDate;
    private String executeType;
    private Long createBy;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PointsType getPointsType() {
        return pointsType;
    }

    public void setPointsType(PointsType pointsType) {
        this.pointsType = pointsType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getNextStartDate() {
        return nextStartDate;
    }

    public void setNextStartDate(Date nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

    public Date getNextEndDate() {
        return nextEndDate;
    }

    public void setNextEndDate(Date nextEndDate) {
        this.nextEndDate = nextEndDate;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String EXECUTE_TYPE_DAILY = "1";
    public static final String EXECUTE_TYPE_MONTHLY = "2";
    public static final String EXECUTE_TYPE_YEARLY = "3";

}
