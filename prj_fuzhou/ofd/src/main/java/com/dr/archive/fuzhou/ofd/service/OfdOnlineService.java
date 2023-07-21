package com.dr.archive.fuzhou.ofd.service;

import com.dr.archive.fuzhou.ofd.OfdConfig;
import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMark;
import com.dr.archive.fuzhou.ofd.watermark.service.WaterMarkService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

/**
 * ofd接口客户端
 * TODO 这里的所有代码都可以在common-file做抽象实现
 *
 * @author dr
 */
@Component
public class OfdOnlineService {

    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    WaterMarkService waterMarkService;
    /**
     * 2进制转16进制
     */
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 单个请求参数
     */
    private static final String PARAM_TYPE_SINGLE = "restfulsingleparam";
    /**
     * 多个请求参数
     */
    private static final String PARAM_TYPE_MULTI = "restfulmultiparam";
    /**
     * ofd相关配置
     */
    @Autowired
    OfdConfig ofdConfig;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 转换统一接口
     *
     * @param waterMarkBo 水印参数
     */
    public String getOfdOnlineUrl(WaterMarkBo waterMarkBo) {
        return getOfdOnlineUrl(waterMarkBo, null);
    }

    /**
     * 转换统一接口
     *
     * @param waterMarkBo 水印参数
     * @param keyWord     高亮关键字参数，多个参数以逗号隔开
     * @return
     */
    public String getOfdOnlineUrl(WaterMarkBo waterMarkBo, String keyWord) {
        return getOfdOnlineUrl(waterMarkBo, keyWord, null, false, "none");
    }

    @Autowired
    ResourceManager resourceManager;

    /**
     * 转换统一接口
     *
     * @param waterMarkBo      水印参数
     * @param keyWord          高亮关键字参数，多个参数以逗号隔开
     * @param extensionApiMode 接口服务分组
     *                         如果ofd服务配置了extensionApi-das.conf表示档案室的回调配置，则extensionApiMode的值设置为das
     * @return
     */
    public String getOfdOnlineUrl(WaterMarkBo waterMarkBo, String keyWord, String extensionApiMode, boolean addwatermark, String tools) {

        //如果设置fileId，没有设置fileUrl，自动拼写访问路径
        String fileUrl = "";
        if (addwatermark) {
            //获取当前登陆人的默认机构  然后使用这个机构的默认水印模板  todo 可能不是这么玩的
            String organiseId = SecurityHolder.get().currentOrganise().getId();
            List<WaterMark> waterMarkList = waterMarkService.getWaterMarkByOrganiseId(organiseId);
            if (waterMarkList.size() > 0) {
                //大于0 表示有自己的水印模板
                //todo 这里写死了 应该是和系统的一致
                String returnPath = waterMarkService.showViewForMob(waterMarkList.get(0).getId(), waterMarkBo.getFileId(), keyWord, tools, waterMarkBo.getFileUrl());
                return returnPath;
            } else {
                String returnPath = waterMarkService.showViewForMob("", waterMarkBo.getFileId(), keyWord, tools, waterMarkBo.getFileUrl());
                return returnPath;
            }
        } else {
            //现在的处理是    走  网络文件集成方式  或许可以切换为 下方格式
//            fileUrl = UriComponentsBuilder.newInstance()
//                    .scheme("http")
//                    .host(ofdConfig.getLocalIp())
//                    .port(localPort)
//                    .path("/api/files/downLoad/" + waterMarkBo.getFileId())
//                    .toUriString();
//            return buildOfdViewUrl("?docuri=" + fileUrl);
            //todo 第二种方式  这种方式的好处是  可以高亮 可以加水印
            String returnPath = waterMarkService.showViewForMob("", waterMarkBo.getFileId(), keyWord, tools);
            return returnPath;
        }


    }

    public String buildOfdViewUrl(String docuri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ofdConfig.getViewerUrl());
        return builder.toUriString() + docuri;
    }

    public String buildOfdViewUrl(String docuri, Object extraParams) {
        return buildOfdViewUrl(docuri, extraParams, null, null, null);
    }

    public String buildOfdViewUrl(String docuri, Object extraParams, String bizType) {
        return buildOfdViewUrl(docuri, extraParams, bizType, null, null);
    }

    public String buildOfdViewUrl(String docuri, Object extraParams, String bizType, String extensionApiMode) {
        return buildOfdViewUrl(docuri, extraParams, bizType, extensionApiMode, null);
    }

    /**
     * 构建ofd在线预览url
     *
     * @param bizType
     * @param extensionApiMode
     * @param keyWord
     * @param extraParams
     * @return
     */
    public String buildOfdViewUrl(String docuri, Object extraParams, String bizType, String extensionApiMode, String keyWord) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ofdConfig.getViewerUrl()).queryParam("docuri=", docuri);
        //请求参数类型
        if (StringUtils.hasText(bizType)) {
            builder.queryParam("paramType", bizType);
        } else {
            builder.queryParam("paramType", PARAM_TYPE_SINGLE);
        }
        //ofd在线阅读回调配置
        if (StringUtils.hasText(extensionApiMode)) {
            builder.queryParam("extensionApiMode", extensionApiMode);
        }
        //高亮关键字参数
        if (StringUtils.hasText(keyWord)) {
            builder.queryParam("keyWord", keyWord);
        }
        //如果有额外的参数，则强制参数类型为多个
        if (extraParams != null) {
            builder.replaceQueryParam("paramType", PARAM_TYPE_MULTI);
            try {
                //先将请求参数转换为json字符串
                String requestParams = objectMapper.writeValueAsString(extraParams);
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                // 防止linux下 随机生成key
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(ofdConfig.getAesPassword().getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = requestParams.getBytes(StandardCharsets.UTF_8);
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                builder.queryParam("requestParams", encodeHex(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toUriString();
    }

    /**
     * 二进制转16进制字符串
     *
     * @param bytes
     * @return
     */
    private static String encodeHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return new String(chars);
    }
}
