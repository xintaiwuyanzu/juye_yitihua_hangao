package com.dr.archive.util;

import com.dr.framework.common.entity.StatusEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommonTools {


    //**********************************************   UUID    *******************************************************************

    //获取uuid
    public static String getUUID() {
        String str = UUID.randomUUID().toString();
        // 去掉"-"符号
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }

    //*********************************************  手机号校验   ******************************************************

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //**************************************  boolean转String  ************************************************************

    /**
     * boolean转String true->"1",false->"0"
     *
     * @param status
     * @return
     */
    public static String booleanToString(boolean status) {
        return status ? StatusEntity.STATUS_ENABLE_STR : StatusEntity.STATUS_DISABLE_STR;
    }

    /**
     * 保留两位小数,进行四舍五入
     *
     * @param d
     * @return
     */
    public static Double SaveTwoDecimalRound(Double d) {
        BigDecimal bd = new BigDecimal(d);
        Double tem = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return tem;
    }

    /**
     * 图片上传工具类
     *
     * @param imgPath
     * @return
     * @throws Exception
     */
    public static String ImageToBase64Fenduan(String imgPath) throws Exception {
        Assert.isTrue(!StringUtils.isEmpty(imgPath), "图片路径不能为空！");
        Assert.isTrue(Files.exists(Paths.get(imgPath)), "指定的图片文件不能为空！");
        return Base64Utils.encodeToString(
                StreamUtils.copyToByteArray(new FileInputStream(new File(imgPath)))
        );
    }

    public static String getLocalHostMac() {

        InetAddress ip = null;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        NetworkInterface network = null;
        byte[] mac = null;
        try {
            network = NetworkInterface.getByInetAddress(ip);
            mac = network.getHardwareAddress();
        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }


    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        BeanUtils.populate(obj, map);

        return obj;
    }

    /**
     * 将一个list分成多个子list
     * @param source
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<T>> result = new ArrayList<List<T>>();

        int sourceSize = source.size();
        int size = (sourceSize % n) == 0 ? (sourceSize / n) : ((source.size() / n) + 1);
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
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
