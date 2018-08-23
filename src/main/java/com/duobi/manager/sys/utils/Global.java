package com.duobi.manager.sys.utils;

public class Global {

    private static PropertyReader propertyReader = (PropertyReader)SpringUtil.getBean("propertyReader");

    public static int pageSize = propertyReader.getPageSize();

    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 获取系统管理模块基础路径
     */
    public static String getSysPath() {
        return propertyReader.getSysPath();
    }

    public static Long getSysModuleId() {
        return propertyReader.getSysModuleId();
    }

}
