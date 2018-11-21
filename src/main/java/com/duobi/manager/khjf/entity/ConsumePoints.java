package com.duobi.manager.khjf.entity;

import com.duobi.manager.sys.utils.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class ConsumePoints extends DataEntity<ConsumePoints,Long> {

    private String customerNo;          //客户号
    private String identityNo;          //证件号码
    private String orgCode;             //网点
    private Double allOrgPoints;        //该客户在所有网点合计可用的积分
    private Double currentOrgPoints;    //该客户在本网点合计可用的积分
    private Boolean consumeAllOrgPoints;//此时是否允许跨网点使用积分
    private Double pointsValue;              //该客户本次用使用的积分
    private String validFlag;           //有效标志：0-正常 1-自动清理,不计算在已用积分里  2-手动清理,不计算在已用积分里
    private Date date;                  //使用积分的日期

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Double getAllOrgPoints() {
        return allOrgPoints;
    }

    public void setAllOrgPoints(Double allOrgPoints) {
        this.allOrgPoints = allOrgPoints;
    }

    public Double getCurrentOrgPoints() {
        return currentOrgPoints;
    }

    public void setCurrentOrgPoints(Double currentOrgPoints) {
        this.currentOrgPoints = currentOrgPoints;
    }

    public Boolean getConsumeAllOrgPoints() {
        return consumeAllOrgPoints;
    }

    public void setConsumeAllOrgPoints(Boolean consumeAllOrgPoints) {
        this.consumeAllOrgPoints = consumeAllOrgPoints;
    }

    public Double getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(Double pointsValue) {
        this.pointsValue = pointsValue;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    private String customerName;
    private String customerIdentityNo;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdentityNo() {
        return customerIdentityNo;
    }

    public void setCustomerIdentityNo(String customerIdentityNo) {
        this.customerIdentityNo = customerIdentityNo;
    }

    public String getValidFlagString(){
        if (VALID_FLAG_NORMAL.equals(validFlag)) return "正常";
        if (VALID_FLAG_AUTO_CLEAR.equals(validFlag)) return "自动清理";
        if (VALID_FLAG_MANUAL_SINGLE_RECORD_CLEAR.equals(validFlag)) return "单条清理";
        if (VALID_FLAG_MANUAL_SINGLE_CUSTOMER_CLEAR.equals(validFlag)) return "单户清理";
        if (VALID_FLAG_MANUAL_BATCH_CLEAR.equals(validFlag)) return "手工批量清理";
        return "";
    }

    public static final String VALID_FLAG_NORMAL = "0";
    public static final String VALID_FLAG_AUTO_CLEAR = "1";
    public static final String VALID_FLAG_MANUAL_SINGLE_RECORD_CLEAR = "2";
    public static final String VALID_FLAG_MANUAL_SINGLE_CUSTOMER_CLEAR = "3";
    public static final String VALID_FLAG_MANUAL_BATCH_CLEAR = "4";

    @JsonIgnore
    private Boolean clearAllOrgPoints; //这个属性本来不应该出现在这个类中。因为：修改通兑积分标志的功能放在该类中，为了统一，就同时在该类中做了修改跨网点清理积分标志的功能。这个属性就表示跨网点清理积分标志

    public Boolean getClearAllOrgPoints() {
        return clearAllOrgPoints;
    }

    public void setClearAllOrgPoints(Boolean clearAllOrgPoints) {
        this.clearAllOrgPoints = clearAllOrgPoints;
    }
}
