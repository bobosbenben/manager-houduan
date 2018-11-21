package com.duobi.manager.khjf.entity;

import com.duobi.manager.sys.utils.persistence.DataEntity;

public class Statistics extends DataEntity<Statistics,Long> {

    private Double pointsValue;
    private Double consumedPointsValue;
    private Integer accountNoCount;
    private Integer cardNoCount;
    private Integer customerNoCount;
    private String orgCode;
    private String orgName;

    public Double getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(Double pointsValue) {
        this.pointsValue = pointsValue;
    }

    public Double getConsumedPointsValue() {
        return consumedPointsValue;
    }

    public void setConsumedPointsValue(Double consumedPointsValue) {
        this.consumedPointsValue = consumedPointsValue;
    }

    public Integer getAccountNoCount() {
        return accountNoCount;
    }

    public void setAccountNoCount(Integer accountNoCount) {
        this.accountNoCount = accountNoCount;
    }

    public Integer getCardNoCount() {
        return cardNoCount;
    }

    public void setCardNoCount(Integer cardNoCount) {
        this.cardNoCount = cardNoCount;
    }

    public Integer getCustomerNoCount() {
        return customerNoCount;
    }

    public void setCustomerNoCount(Integer customerNoCount) {
        this.customerNoCount = customerNoCount;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Double getValidPointsValue() {
        if (consumedPointsValue == null) consumedPointsValue = 0D;
        if (pointsValue == null) pointsValue = 0D;
        return pointsValue - consumedPointsValue;
    }


}
