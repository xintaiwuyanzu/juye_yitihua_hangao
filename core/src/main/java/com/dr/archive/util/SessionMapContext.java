package com.dr.archive.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 档案中好多需要批量线性处理数据的业务逻辑
 * <p>
 * 这里的功能模仿{@link  HttpServletRequest}设计，用一个hashmap缓存上下文变量
 * <p>
 * 实际上是设计模式中的【上下文】设计模式
 *
 * @author dr
 */
public class SessionMapContext {
    private Map<String, Object> sessionMap = new HashMap<>();

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void addAttribute(String key, Object value) {
        sessionMap.put(key, value);
    }

    public <T> T getAttribute(String key) {
        return (T) sessionMap.get(key);
    }

    public boolean containsKey(String key) {
        return sessionMap.containsKey(key);
    }

}
