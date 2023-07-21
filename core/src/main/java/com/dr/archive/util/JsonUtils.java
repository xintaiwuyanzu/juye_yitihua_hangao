package com.dr.archive.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * json工具类
 *
 * @author dr
 */
public class JsonUtils {
    static final ObjectMapper objectMapper = new JsonMapper();

    /**
     * json转数组
     *
     * @param json  json字符串
     * @param clazz 目标类
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) throws Exception {
        return stringToList(objectMapper, json, clazz);
    }

    /**
     * json 转对象
     *
     * @param string
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T jsonToObject(String string, Class<T> clazz) throws Exception {
        return stringToObject(objectMapper, string, clazz);
    }

    /**
     * json 路径转list
     *
     * @param url
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonURLToList(URL url, Class<T> clazz) throws Exception {
        return URLToList(objectMapper, url, clazz);
    }

    /**
     * json 路径转对象
     *
     * @param url
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T jsonURLToObject(URL url, Class<T> clazz) throws Exception {
        return URLToObject(objectMapper, url, clazz);
    }

    /**
     * json文件转list
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonFileToList(File file, Class<T> clazz) throws Exception {
        return fileToList(objectMapper, file, clazz);
    }

    /**
     * json 文件转对象
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T jsonFileToObject(File file, Class<T> clazz) throws Exception {
        return fileToObject(objectMapper, file, clazz);
    }

    /**
     * json数据流list
     *
     * @param is
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonStreamToList(InputStream is, Class<T> clazz) throws Exception {
        return streamToList(objectMapper, is, clazz);
    }

    /**
     * json数据流转对象
     *
     * @param is
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T jsonStreamToObject(InputStream is, Class<T> clazz) throws Exception {
        return streamToObject(objectMapper, is, clazz);
    }

    public static <T> List<T> URLToList(ObjectMapper objectMapper, URL url, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(objectMapper, clazz);
        return objectMapper.readValue(url, javaType);
    }

    public static <T> T URLToObject(ObjectMapper objectMapper, URL url, Class<T> clazz) throws Exception {
        return objectMapper.readValue(url, clazz);
    }

    public static <T> List<T> fileToList(ObjectMapper objectMapper, File file, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(objectMapper, clazz);
        return objectMapper.readValue(file, javaType);
    }

    public static <T> T fileToObject(ObjectMapper objectMapper, File file, Class<T> clazz) throws Exception {
        return objectMapper.readValue(file, clazz);
    }

    public static <T> List<T> streamToList(ObjectMapper objectMapper, InputStream inputStream, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(objectMapper, clazz);
        return objectMapper.readValue(inputStream, javaType);
    }

    public static <T> T streamToObject(ObjectMapper objectMapper, InputStream inputStream, Class<T> clazz) throws Exception {
        return objectMapper.readValue(inputStream, clazz);
    }

    public static <T> List<T> stringToList(ObjectMapper objectMapper, String string, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(objectMapper, clazz);
        return objectMapper.readValue(string, javaType);
    }

    public static <T> T stringToObject(ObjectMapper objectMapper, String string, Class<T> clazz) throws Exception {
        return objectMapper.readValue(string, clazz);
    }


    static JavaType getCollectionType(ObjectMapper objectMapper, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(ArrayList.class, elementClasses);
    }
}
