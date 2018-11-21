package com.duobi.manager.khjf.entity;

import com.duobi.manager.sys.utils.persistence.DataEntity;

import java.util.Date;

public class PointsTypeChange extends DataEntity<PointsTypeChange,Long> {

    private Long pointsTypeId;
    private Double value;
    private Date startDate;
    private Date endDate;
    private Boolean validFlag;
    private String availableStatus;

    public Long getPointsTypeId() {
        return pointsTypeId;
    }

    public void setPointsTypeId(Long pointsTypeId) {
        this.pointsTypeId = pointsTypeId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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

    public Boolean getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Boolean validFlag) {
        this.validFlag = validFlag;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
