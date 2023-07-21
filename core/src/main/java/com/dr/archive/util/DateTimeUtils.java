package com.dr.archive.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {

    /**
     * 获取当前时间戳
     */
    public static Long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Date转时间戳
     */
    public static Long dateToMillis(Date date) {
        return date.getTime();
    }

    /**
     * Date转字符串
     */
    public static String dateToString(Date date, String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat.format(date);
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
     * 字符串转Date
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间戳转日期字符串
     */
    public static String longToDate(Long millSec, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(millSec);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(millSec);
        }
    }

    /**
     * 时间戳转Date
     */
    public static Date longToDate(Long millSec) {
        return new Date(millSec);
    }

    /**
     * 获取两个时间之间的日期列表（年 / 月 / 日）
     *
     * @param startDate
     * @param endxDate
     * @return
     */
    public static List<String> getTimeList(String startDate, String endxDate) {
        SimpleDateFormat sdf;
        int calendarType;

        switch (startDate.length()) {
            case 10:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendarType = Calendar.DATE;
                break;
            case 7:
                sdf = new SimpleDateFormat("yyyy-MM");
                calendarType = Calendar.MONTH;
                break;
            case 4:
                sdf = new SimpleDateFormat("yyyy");
                calendarType = Calendar.YEAR;
                break;
            default:
                return null;
        }

        List<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(startDate));
            min.add(calendarType, 0);
            max.setTime(sdf.parse(endxDate));
            max.add(calendarType, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(calendarType, 1);
        }
        return result;
    }

    public static String getDates(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
    }
    public static int getH24(){
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY);
    }
}

