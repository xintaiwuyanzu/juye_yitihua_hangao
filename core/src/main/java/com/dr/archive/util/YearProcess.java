package com.dr.archive.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：年度重复验证
 *
 * @author tuzl
 * @date 2020/6/4 10:25
 */
public class YearProcess {
    /**
     * 功能描述: 验证新添加的编码方案年度是否存在时间段重复
     *
     * @param tList 处理的带有起止年度的集合
     *              属性：startYear 起始年度
     *              endYear 终止年度
     * @param t     要插入的实体类
     * @return : {@link String}
     * @author : tzl
     * @date : 2020/6/3 11:56
     */
    public static <T> String isYesYear(List<T> tList, T t) {
        int startYear1 = getStartYear(t);
        int endYear1 = getEndYear(t);
        //已存在数据不可以新添加全范围年度数据
        if (startYear1 == 0 && endYear1 == 0 && tList.size() > 0) {
            return "年度设置存在重复时间段，请查证后重新输入";
        }
        //判断起始年度不得大于终止年度
        if (endYear1 != 0 && startYear1 > endYear1) {
            return "起始年度必须小于终止年度";
        }
        //判断年度范围不可重复(startYear1>endYear&& endYear1>endYear)||
        for (T t1 : tList) {
            int startYear = getStartYear(t1);
            int endYear = getEndYear(t1);
            //已存在全范围年度不可以新添加数据
            if (startYear == 0 && endYear == 0) {
                return "年度设置存在重复时间段，请查证后重新输入";
            }
            //已存在数据的startYear=0，插入的新数据年度都要大于已存在数据的endYear，且新添加数据的开始年度不为0
            boolean a = startYear == 0 && (startYear1 > endYear && endYear1 > endYear);
            //已存在数据的endYear=0，插入的新数据年度都要小于已存在数据的startYear，且新添加数据的终止年度不为0
            boolean b = endYear == 0 && (startYear1 < startYear && (endYear1 < startYear && endYear1 != 0));
            //已存在数据的起止年度都不为0时，新添加的数据起止年度都不为0则新添加数据或者都大于已有数据的endYear，或者都小于已存在数据的startYear
            //      若新添加数据起始年度为0，则新添加数据的终止年度一定小于已有数据的起始年度
            //      若新添加数据终止年度为0，则新添加数据的其实年度一定大于已有数据的终止年度
            boolean c = endYear != 0 && startYear != 0 && ((startYear1 != 0 && endYear1 != 0 && (startYear1 < startYear && endYear1 < startYear) || (startYear1 > startYear && endYear1 > startYear)) ||
                    (startYear1 == 0 && endYear1 < startYear) || (endYear1 == 0 && startYear1 > endYear));
            if (!(a || b || c)) {
                return "年度设置存在重复时间段，请查证后重新输入";
            }
        }
        return Constants.SUCCESS;

    }

    /**
     * 功能描述: 获取实体类中的起始年度
     *
     * @param t
     * @return : {@link int}
     * @author : tzl
     * @date : 2020/6/4 14:48
     */
    private static <T> int getStartYear(T t) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            long year = (long) t.getClass().getDeclaredMethod("getStartYear").invoke(t);
            if (year>10000) {
                String getStartYear = format.format(new Date(year));
                return Integer.parseInt(getStartYear);
            }else if (year<=1970){
                return  0;
            }else {
                return (int)year;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 功能描述: 获取实体类中的终止年度
     *
     * @param t
     * @return : {@link int}
     * @author : tzl
     * @date : 2020/6/4 14:49
     */
    private static <T> int getEndYear(T t) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            long year = (long) t.getClass().getDeclaredMethod("getEndYear").invoke(t);
            if (year>10000) {
                String getEndYear = format.format(new Date(year));
                return Integer.parseInt(getEndYear);
            }else if (year<=1970){
                return  0;
            }else {
                return (int)year;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
