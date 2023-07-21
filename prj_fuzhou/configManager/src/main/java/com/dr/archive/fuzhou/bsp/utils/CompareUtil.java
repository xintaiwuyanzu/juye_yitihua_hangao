package com.dr.archive.fuzhou.bsp.utils;

import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * 对比工具
 *
 * @author dr
 */
public class CompareUtil {
    /**
     * 对比工具
     *
     * @param source
     * @param target
     * @param valueFunction
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> boolean compare(T source, T target, Function<T, Object>... valueFunction) {
        if (source == null || target == null) {
            return true;
        }
        for (Function<T, Object> tObjectFunction : valueFunction) {
            if (!ObjectUtils.nullSafeEquals(tObjectFunction.apply(source), tObjectFunction.apply(target))) {
                return true;
            }
        }
        return false;
    }
}
