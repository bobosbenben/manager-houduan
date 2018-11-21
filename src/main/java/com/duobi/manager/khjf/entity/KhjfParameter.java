package com.duobi.manager.khjf.entity;

import com.duobi.manager.sys.utils.persistence.DataEntity;

//配置类，新增属性后需要同步更新数据库
public class KhjfParameter extends DataEntity<KhjfParameter,Long>{

    private String name;                    //属性名称
    private String value;                   //属性值
    private Boolean validFlag;              //有效标志

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Boolean validFlag) {
        this.validFlag = validFlag;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public static final Boolean VALID_FLAG_NORMAL = true;
    public static final Boolean VALID_FLAG_INVALID = false;
}
