package com.dr.archive.kufang.entityfiles.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_31;

public class ExportWordUtil {
    private Template template;

    /**
     * 指定模板的路径和名称
     *
     * @param basePackage  模板路径
     * @param templateName 模板名称
     */
    public ExportWordUtil(String basePackage, String templateName) throws Exception {
        init(basePackage, templateName);
    }

    /**
     * 初始化操作，创建 freemarker 模板
     */
    private void init(String basePackage, String templateName) throws Exception {
        Configuration config = new Configuration(VERSION_2_3_31);//需要指定版本
        config.setClassLoaderForTemplateLoading(ExportWordUtil.class.getClassLoader(), basePackage);//去那个文件夹下寻找模板文件
        config.setOutputEncoding("utf-8");
        template = config.getTemplate(templateName);//模板名称获取模板
    }

    /**
     * 实际文件导出
     *
     * @param out             输出流
     * @param results,构造的输出结果
     */
    public <K, V> void doExport(OutputStream out, Map<K, V> results) throws TemplateException, IOException {
        template.process(results, new OutputStreamWriter(out, StandardCharsets.UTF_8));//对模板进行数据填充
    }

    /**
     * 设置文件下载的文件名，防止中文乱码
     */
    public void setHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident") ||
                StringUtils.contains(userAgent, "Edge")) { //解决IE中文名称乱码
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Firefox")) {//火狐和其他浏览器中文名乱码
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        } else {//其他浏览器中文名乱码
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        }
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;" + " filename=\"" + fileName + "\"");
    }
}
