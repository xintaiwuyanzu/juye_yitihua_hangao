package com.dr.archive.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * 全局工具类
 *
 * @author cuiyj
 */
public class CommonTools {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 时间转换(String  to long)
     *
     * @param dateTime (yyyy/MM/dd HH:mm:ss)
     * @return
     */
    public static long getTimes(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date dtime = sdf.parse(dateTime);
            return dtime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 时间格式转换  long - String
     *
     * @param time
     * @return
     */
    public static String getDates(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
    }

    /**
     * 判断当前系统是否是linux
     *
     * @return
     */
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        return os != null && os.toLowerCase().contains("linux");
    }
}
