package com.duobi.manager.sys.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 返回纯日期的当前date，如返回今天'2018-10-11 00:00:00'
     * @return 当前日期
     */
    public static Date getCurrentPureDate(){
        Date date = null;
        try {
            date = sdf.parse(sdf.format(new Date()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取指定日期的前一天日期（纯日期，时间为 00:00:00）
     * @param date 指定日期
     * @return 指定日期的前一天日期
     */
    public static Date getYesterdayDate(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        Date yesterday = null;
        try {
            yesterday = sdf.parse(sdf.format(calendar.getTime()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return yesterday;
    }

    /**
     * 获取字符串类型的日期
     * @param date 日期
     * @return 字符串类型日期
     */
    public static String getPureDateString(Date date){
        if (date == null) return null;
        String dateString = null;
        try {
            dateString = sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dateString;
    }


}
