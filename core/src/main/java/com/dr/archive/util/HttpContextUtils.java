package com.dr.archive.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caor
 * @date 2020/8/12 15:17
 */
public class HttpContextUtils {
    public static HttpServletRequest getHttpServletRequest() {
        return null == RequestContextHolder.getRequestAttributes() ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
