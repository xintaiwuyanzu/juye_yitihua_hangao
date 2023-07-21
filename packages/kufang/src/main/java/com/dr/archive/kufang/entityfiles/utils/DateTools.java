package com.dr.archive.kufang.entityfiles.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关工具类
 *
 * @author cuiyj
 */
public class DateTools {

    /**
     * 获取今天开始时间
     */
    public static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取今天结束时间
     */
    public static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 0);
        return todayEnd.getTime().getTime();
    }

    /**
     * 获取当前时间的下num个月
     *
     * @param num
     * @return
     */
    public static long getNextMonth(int num) {
        //获取当前时间的下一个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime().getTime();
    }

    /**
     * 时间转换(String to long)
     *
     * @param dateTime
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

    public static long getTime(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date dtime = sdf.parse(dateTime);
            return dtime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 字符串转时间戳
     */
    public static Long stringToMillis(String dateStr, String formatStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
            return simpleDateFormat.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 时间转换(long to String)
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    public static String getDates(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
    }

    public static String getDate2(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 现在年月日
     *
     * @return
     */
    public static String getYMD() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    /**
     * 时间流水号
     *
     * @return
     */
    public static String getYMDHms() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmss.ssss").format(date);
    }

    /**
     * long转星期
     *
     * @param dateTime
     * @return
     */
    public static String getWeek(long dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sdf.applyPattern("E");
            Date date = new Date(dateTime);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
