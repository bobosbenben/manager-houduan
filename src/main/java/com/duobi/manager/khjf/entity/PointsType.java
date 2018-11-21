package com.duobi.manager.khjf.entity;


import com.duobi.manager.sys.utils.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PointsType extends DataEntity<PointsType,Long> {

    private String name;
    private String shortName;
    private String remarks;
    private Boolean allOrgValid;
    private String type;            //类型：1-自动，2-手动
    private String availableStatus;    //启用停用标志：0-正常，1-停用

    //修改积分值时用到的参数，该属性在PointsTypeChange也含有，此处只是为了方便从前端接受值
    private Double value;
    private Date startDate;
    private Date endDate;
    private Boolean validFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getAllOrgValid() {
        return allOrgValid;
    }

    public void setAllOrgValid(Boolean allOrgValid) {
        this.allOrgValid = allOrgValid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public Boolean getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Boolean validFlag) {
        this.validFlag = validFlag;
    }

    public String getTypeString(){
        if (this.type.equals(TYPE_AUTO)) return "自动";
        else if (this.type.equals(TYPE_MANUAL)) return "手动";
        else return "";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    final public static String TYPE_AUTO = "1";     //自动
    final public static String TYPE_MANUAL = "2";   //手动
    final public static String AVAILABLE_STATUS_NORMAL = "0";   //产品正常
    final public static String AVAILABLE_STATUS_STOP = "1";     //产品停用
}
