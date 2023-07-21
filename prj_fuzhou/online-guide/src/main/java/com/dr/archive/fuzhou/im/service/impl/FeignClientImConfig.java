package com.dr.archive.fuzhou.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.dr.archive.fuzhou.im.ImConfig;
import com.dr.archive.fuzhou.im.service.WordProClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author caor
 * @date 2021-09-18 16:43
 */
@Configuration
public class FeignClientImConfig implements RequestInterceptor, InitializingBean {
    @Autowired
    ImConfig imConfig;
    Map<Class, String> classStringMap;

    @Override
    public void apply(RequestTemplate template) {
        Class clientType = template.feignTarget().type();
        if (classStringMap.containsKey(clientType)) {
            String nonce = UUID.randomUUID().toString();
            String timestamp = getTimestamp();
            //String authen = getAuthen(timestamp);
            template.header("Content-Type","application/json; charset=UTF-8");
            template.header("authen",getAuthen(timestamp));
            template.header("appid","1638838645");
            template.header("timestamp",timestamp);
            template.header("uid","imadmin");//
            template.header("lang", "zh-cn");
        }
    }

    /**
     * 获取秒的时间戳
     *
     * @return
     */
    static String getTimestamp() {
        return (System.currentTimeMillis() + "").substring(0, (System.currentTimeMillis() + "").length() - 3);
    }

    /*
    * 获取认证码
    * 认证码生成规则：SHA256(应用ID + 应用密钥 + 时间戳)
    * */
    static String getAuthen(String timestamp){
        String str = "1638838645"+"8d1dd4fac6bf4ef"+timestamp;

        return shaEncode(str);
    }

    public static void main(String[] args) {
        String timestamp = getTimestamp();
        System.out.println(getAuthen(timestamp));
        System.out.println(timestamp);
    }
    /**
     * @param inStr
     * @return
     */
    public static String shaEncode(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        classStringMap = new HashMap<>();
        classStringMap.put(WordProClient.class, imConfig.getWordPro_pubkey());
    }
}
