package com.duobi.manager.sys.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyReader {

    @Value("${global.pageSize}")
    private int pageSize;

    @Value("${sysPath}")
    private String sysPath;

    @Value("${sysModuleId}")
    private Long sysModuleId;

    @Value("${khjfModuleId}")
    private Long khjfModuleId;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSysPath() {
        return sysPath;
    }

    public void setSysPath(String sysPath) {
        this.sysPath = sysPath;
    }

    public Long getSysModuleId() {
        return sysModuleId;
    }

    public void setSysModuleId(Long sysModuleId) {
        this.sysModuleId = sysModuleId;
    }

    public Long getKhjfModuleId() {
        return khjfModuleId;
    }

    public void setKhjfModuleId(Long khjfModuleId) {
        this.khjfModuleId = khjfModuleId;
    }
}
