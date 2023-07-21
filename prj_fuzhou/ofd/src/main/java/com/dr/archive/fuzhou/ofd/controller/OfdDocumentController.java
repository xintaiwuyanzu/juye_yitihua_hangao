package com.dr.archive.fuzhou.ofd.controller;


import com.dr.archive.fuzhou.ofd.bo.CommonExtensionUtils;
import com.dr.framework.common.file.FileSaveHandler;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个文件是云阅读文档说明必须存在的文件 功能是实现文件流的处理
 */
@RestController
@RequestMapping("/api/ofd")
public class OfdDocumentController extends BaseController {


    //    private static final String FILE_FIX_PATH = "D:\\extension_dir";
    public static final String FILE_FIX_PATH = "";
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    FileSaveHandler fileSaveHandler;

    /**
     * TODO Tip:
     * TODO 导出方法-该方法根据请求参携带的id(这个Id需要根据系统自身去定义，只要能保证根据此ID可以查询出对应的文件即可)
     * TODO 此处的逻辑仅为根据ID导出了在本服务器 webapp/files/目录中的文件。(为了演示)
     */
    @RequestMapping(value = "/document", method = RequestMethod.GET)
    public void getDocument(HttpServletResponse response, @RequestParam("fileId") String fileId, @RequestParam("userId") String userId, @RequestParam("fileUrl") String fileUrl) {

        FileInfo fileInfo = commonFileService.fileInfo(fileId);
        /* TODO 以下Header属性是必须要封装在Response中的! */
        response.setHeader("Last-Modified", DateFormatUtils.format(new Date(fileInfo.getLastModifyDate()), "yyyy-MM-dd hh:mm:ss"));
        response.setHeader("File-Id", fileId);
        response.setHeader("User-Id", userId);

        if (!StringUtils.isEmpty(fileUrl)) {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            //设置文件名称
            setFileName(response, fileName);
            try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(fileSaveHandler.readFile(fileInfo)); OutputStream ous = response.getOutputStream()) {
                ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
                while (zipEntry != null) {
                    if (!zipEntry.isDirectory() && zipEntry.getName().equalsIgnoreCase(fileUrl)) {
                        break;
                    }
                    zipEntry = zipInputStream.getNextZipEntry();
                }
                //设置文件长度
                setFileLength(response, zipEntry.getSize());
                setMimeType(response, MediaType.APPLICATION_PDF_VALUE);
                StreamUtils.copy(zipInputStream, ous);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //设置文件名称
            setFileName(response, fileInfo.getName());
            //设置文件长度
            setFileLength(response, fileInfo.getFileSize());
            //设置媒体类型
            setMimeType(response, fileInfo.getMimeType());
            try (InputStream ins = fileSaveHandler.readFile(fileInfo); OutputStream ous = response.getOutputStream()) {
                StreamUtils.copy(ins, ous);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Logger logger = LoggerFactory.getLogger(OfdDocumentController.class);

    /**
     * 获取多关键字搜索信息接口
     *
     * @param fileId
     * @param userId
     * @return
     */
    @GetMapping(value = "/searchKeyWords")
    public HashMap<String, Object> getSearchKeyWords(@RequestParam String fileId, @RequestParam String userId) {
        logger.info("获取多关键字搜索信息接口接收请求: 文件标识:" + fileId + "  用户标识:  " + userId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("ret", 0);
        map.put("message", "success");
        Map<String, String> mapData = new HashMap<>();
        /* {"云阅读":"red","优势":"green","功能":"","方案":"yellow"}; */
        mapData.put("云阅读", "red");
        mapData.put("优势", "#00FFFF");
        mapData.put("功能", null);
        mapData.put("方案", "#FFB6C1");
        map.put("data", mapData);
        return map;
    }

    private void setMimeType(HttpServletResponse response, String mimeType) {
        response.setContentType(mimeType);
    }

    private void setFileLength(HttpServletResponse response, long size) {
        try {
            //设置文件长度
            response.setContentLength(((Long) size).intValue());
        } catch (Exception ignored) {
        }
    }

    protected void setFileName(HttpServletResponse response, String fileName) {
        //设置文件名称
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive PDF/OFD document stream
     * TODO Tip:处理文件上传。
     *
     * @return The document handle status
     * @throws IOException hjo
     */
    @RequestMapping(value = "/document/export", method = RequestMethod.POST)
    public Map receiveRemoteFile(HttpServletRequest request, @RequestParam MultipartFile file, @RequestParam String userId, @RequestParam String fileId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
            }

            if (file.isEmpty()) {
                resultMap.put("ret", 10001);
                resultMap.put("message", "FILE_IS_NOT_EXIST");
                return resultMap;
            } else {
                String webAppFilesPath = CommonExtensionUtils.webAppTempFilePath();
                String fileNamePrefix = "NetworkFile_" + CommonExtensionUtils.getTime("yyyy-MM-dd-HH-mm-ss") + "_";
                String filePath = webAppFilesPath + File.separator + fileNamePrefix + file.getOriginalFilename();
                //指定固定文件夹目录
                if (!"".equals(FILE_FIX_PATH)) {
                    filePath = FILE_FIX_PATH + File.separator + "test.ofd";
                }
                File dest = new File(filePath);

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                // 保存文件
                file.transferTo(dest);
            }
            resultMap.put("ret", 0);
            resultMap.put("message", "SUCCESS");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("ret", 10002);
            resultMap.put("message", "SERVER ERROR!");
            return resultMap;
        }
    }

}
