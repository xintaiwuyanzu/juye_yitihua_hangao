package com.dr.archive.fuzhou.ofd.bo;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

/**
 * @Title: CommonExtensionUtils
 * @Description 公共工具类
 * @Author zhaoJing
 * @Date Create on 2020/11/17 14:35
 */
public class CommonExtensionUtils {

    /**
     * 获取当前项目WebApp目录路径
     * create by zhaojing on 2018.4
     */
    public static String webAppTempFilePath() {
        ClassPathResource resource = new ClassPathResource("/word");
//        String path = CommonExtensionUtils.class.getClassLoader().getResource(File.separator).getPath();
//        String substring = path.substring(0, path.lastIndexOf("/")) + File.separator + "webpdf-extension-sample" + File.separator + "files" + File.separator;
        return makeSureDirAvailable(resource.getPath());
    }

    /**
     * 确保给出的文件夹可用-不存在即创建
     * create by zhaojing on 2018.4
     */
    public static String makeSureDirAvailable(String expectedPath) {
        if (isEmpty(expectedPath)) {
            return null;
        }
        File targetDir = new File(expectedPath);
        if (!targetDir.exists()) {
            boolean isSucc = targetDir.mkdirs();
            if (isSucc) {
                System.out.println("成功创建目标文件夹. --> " + expectedPath);
            } else {
                System.out.println("目标文件夹 --> " + expectedPath + " <-- 不存在且未能创建.");
            }
        }
        return expectedPath;
    }

    /* 根据指定format pattern获取指定的时间戳 */
    public static String getTime(String pattern) {
        Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(calendar.getTime());
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    //读取json文件
    public static String readJsonFile(String filePath) {
        String jsonStr = "";
        try {
            File jsonFile = new File(filePath);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getBase64FromInputStream(InputStream in) {
        byte[] data = null;
        // 读取文件字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();

        // 返回 Base64 编码过的字节数组字符串
        return encoder.encodeToString(data);
    }


    /**
     * 文件转化成base64字符串
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     */
    public static String getFileStr(InputStream io) {
        InputStream in = null;
        byte[] data = null;
        // 读取文件字节数组
        try {
            in = io;
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();

        // 返回 Base64 编码过的字节数组字符串
        return encoder.encodeToString(data);
    }


    /**
     * base64字符串转化成文件，可以是jpg、png、txt、pdf、ofd等等
     *
     * @param base64FileStr
     * @param filePath
     * @return
     * @throws Exception
     */
    public static boolean generateFile(String base64FileStr, String filePath) throws Exception {
        // 数据为空
        if (base64FileStr == null) {
            System.out.println(" 数据为空！ ");
            return false;
        }
        Base64.Decoder decoder = Base64.getDecoder();


        // Base64解码,对字节数组字符串进行Base64解码并生成文件
        byte[] byt = decoder.decode(base64FileStr);
        for (int i = 0, len = byt.length; i < len; ++i) {
            // 调整异常数据
            if (byt[i] < 0) {
                byt[i] += 256;
            }
        }
        OutputStream out = null;
        InputStream input = new ByteArrayInputStream(byt);
        try {
            // 生成指定格式的文件
            out = Files.newOutputStream(Paths.get(filePath));
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = input.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
        return true;
    }


    public static byte[] base64tobyte(String base64FileStr) {
        // 数据为空
        if (base64FileStr == null) {
            System.out.println(" 数据为空！ ");
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();

        // Base64解码,对字节数组字符串进行Base64解码并生成文件
        byte[] byt = decoder.decode(base64FileStr);

        return byt;
    }


    /* 获取 */

}
