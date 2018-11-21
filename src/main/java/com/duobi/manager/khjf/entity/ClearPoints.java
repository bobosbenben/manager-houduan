package com.duobi.manager.khjf.entity;

import com.duobi.manager.sys.utils.persistence.DataEntity;

public class ClearPoints extends DataEntity<ClearPoints,Long> {

    private Long customerPointsRecordId;
    private String orgCode;
    private String identityNo;
    private String type;

    public Long getCustomerPointsRecordId() {
        return customerPointsRecordId;
    }

    public void setCustomerPointsRecordId(Long customerPointsRecordId) {
        this.customerPointsRecordId = customerPointsRecordId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public static final String CLEAR_POINTS_TYPE_AUTO = "1";
    public static final String CLEAR_POINTS_TYPE_MANUAL_SINGLE_RECORD = "2";
    public static final String CLEAR_POINTS_TYPE_MANUAL_SINGLE_CUSTOMER = "3";
    public static final String CLEAR_POINTS_TYPE_MANUAL_BATCH = "4";
}
