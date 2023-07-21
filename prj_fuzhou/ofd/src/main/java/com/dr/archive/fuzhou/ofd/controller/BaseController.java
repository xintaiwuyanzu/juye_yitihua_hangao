package com.dr.archive.fuzhou.ofd.controller;

import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Base controller class.
 */
public class BaseController {
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final int DEFAULT_CACHE_SECONDS = 1200;
    private static boolean useRedirect = false;

    public static final String DEFAULT_TEMP_DIR_WIN = "D:\\TEMPFILES\\";
    public static final String DEFAULT_TEMP_DIR_LINUX = "/opt/FoxitSoftware/";


    /**
     * Write buffer to the file specified by the file name.
     *
     * @param request
     * @param response
     * @param fileName
     * @param buffer
     * @throws IOException
     */
    public void writeFile(HttpServletRequest request, HttpServletResponse response, String fileName, byte[] buffer) throws IOException {
        if (buffer == null) {
            return;
        }

        response.setContentType("application/octet-stream");

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            userAgent = "";
        }
        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#").replaceAll("%26", "\\&");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", encodeFileName));
        } else {
            // ��IE������Ĵ��?
            String encodeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1); // ����safari����������
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", encodeFileName));
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(buffer.length);
        response.getOutputStream().write(buffer);
    }


    /**
     * Write buffer to the file specified by the file name.
     *
     * @param request
     * @param response
     * @param fileName
     * @throws IOException
     */
    public void writeFile(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream inputstream) throws IOException {
        response.setContentType("application/octet-stream");

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            userAgent = "";
        }
        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#").replaceAll("%26", "\\&");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", encodeFileName));
        } else {
            // ��IE������Ĵ��?
            String encodeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1); // ����safari����������
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", encodeFileName));
        }
        response.setCharacterEncoding("UTF-8");
        FileCopyUtils.copy(inputstream, response.getOutputStream());
//        response.setContentLength(buffer.length);
//        response.getOutputStream().write(buffer);
    }
}
