package com.dr.archive.util;

import java.util.UUID;

/**
 * uuid工具类
 *
 * @author Songxc
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
