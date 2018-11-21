package com.duobi.manager.khjf.entity;


import com.duobi.manager.sys.utils.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CustomerPointsRecord extends DataEntity<CustomerPointsRecord,Long> {

    private String customerNo;
    private String accountNo;
    private String childAccountNo;
    private String cardNo;
    private Double balance;
    private String subjectNo;
    private String orgCode;
    private Date date;
    private String month;
    private String year;
    private Date startDate;
    private Date endDate;
    private Long pointsTypeId;
    private String validFlag;
    private String tellerCode;
    private Double value;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getChildAccountNo() {
        return childAccountNo;
    }

    public void setChildAccountNo(String childAccountNo) {
        this.childAccountNo = childAccountNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getPointsTypeId() {
        return pointsTypeId;
    }

    public void setPointsTypeId(Long pointsTypeId) {
        this.pointsTypeId = pointsTypeId;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getTellerCode() {
        return tellerCode;
    }

    public void setTellerCode(String tellerCode) {
        this.tellerCode = tellerCode;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    private String pointsTypeName;
    private String customerName;
    private String customerIdentityNo;

    public String getPointsTypeName() {
        return pointsTypeName;
    }

    public void setPointsTypeName(String pointsTypeName) {
        this.pointsTypeName = pointsTypeName;
    }

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
}
