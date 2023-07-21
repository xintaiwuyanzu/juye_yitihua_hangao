package com.dr.archive.kufang.entityfiles.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 全局工具类
 *
 * @author cuiyj
 */
public class CommonTools {

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUID() {
        String str = UUID.randomUUID().toString();
        // 去掉"-"符号
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }

    /**
     * 将一个list分成多个子list
     *
     * @param source
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();

        int sourceSize = source.size();
        int size = (sourceSize % n) == 0 ? (sourceSize / n) : ((source.size() / n) + 1);
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }

}
