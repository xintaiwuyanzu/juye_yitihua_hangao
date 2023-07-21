package com.dr.archive.common.file.service.impl;

import com.dr.archive.common.file.service.FileViewService;
import com.dr.archive.common.pdfconversion.service.PdfConversion;
import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.service.OfdOnlineService;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.file.FileSaveHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: caor
 * @Date: 2022-01-09 15:44
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileViewViewServiceImpl implements FileViewService {
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    OfdOnlineService ofdOnlineService;
    @Autowired
    FileSaveHandler fileSaveHandler;
    @Autowired
    PdfConversion pdfConversionWord;
    @Autowired
    protected CommonFileConfig fileConfig;
    @Override
    public List<TreeNode> getFileTree(String refId, String refType, String groupCode) {
        List<FileInfo> fileInfoList = commonFileService.list(refId, refType, groupCode);
        List<TreeNode> fileTree = new ArrayList<>();

        for (FileInfo fileInfo : fileInfoList) {
            List<String> filePathList = new ArrayList<>();
            //读取zip数据包中的数据
            if ("zip".equalsIgnoreCase(fileInfo.getSuffix())) {
                parseZip(fileInfo, filePathList);
            }
            if (getSetTreeNode(filePathList, fileInfo).size() > 0) {
                TreeNode treeNode = new TreeNode(fileInfo.getId(), fileInfo.getName(), fileInfo, getSetTreeNode(filePathList, fileInfo));
                treeNode.setParentId(refId);
                fileTree.add(treeNode);
            } else {
                TreeNode treeNode = new TreeNode(fileInfo.getId(), fileInfo.getName(), refId, fileInfo.getId());
                treeNode.setParentId(refId);
                fileTree.add(treeNode);
            }
        }
        return fileTree;
    }

    @Override
    public String getFile(HttpServletResponse response, String fileId, String filePath, String keyWord, String systemModel, boolean watermark, String tools) {
        WaterMarkBo waterMarkBo = new WaterMarkBo();
        waterMarkBo.setFileUrl("");
        FileInfo fileInfo = commonFileService.fileInfo(fileId);
        //处理zip数据包格式
        if ("zip".equalsIgnoreCase(fileInfo.getSuffix())) {
            waterMarkBo.setFileUrl(filePath);
        }
        waterMarkBo.setUserId(SecurityHolder.get().currentPerson().getId());
        waterMarkBo.setFileId(fileId);


        //数据包中的ofd、pdf 或者直接上传的ofd、pdf格式，通过福昕云阅读方式,其他项目可以整合到一块
        if (("zip".equalsIgnoreCase(fileInfo.getSuffix())
                && ("ofd".equalsIgnoreCase(filePath.substring(filePath.lastIndexOf(".") + 1))
                /*|| filePath.substring(filePath.lastIndexOf(".") + 1).equalsIgnoreCase("pdf")*/
        ))
                || ("ofd".equalsIgnoreCase(fileInfo.getSuffix())
                /* || fileInfo.getSuffix().equalsIgnoreCase("pdf")*/

        )) {
            return ofdOnlineService.getOfdOnlineUrl(waterMarkBo, keyWord, systemModel, watermark, tools);
        } else if ("docx".equals(fileInfo.getSuffix()) || "doc".equals(fileInfo.getSuffix())) {
            String path = getPath(fileInfo);
            //String path1 = path.replaceAll("(?:.docx|.doc)", ".pdf");
            String path1= path.substring(0, path.lastIndexOf("."))+".pdf";
            File file = new File(path1);
            if (!file.exists()) {
                //转pdf
                getWord(fileInfo,path1);
            }
            this.getImg(response, fileInfo, filePath, keyWord, systemModel, watermark, tools,path1);
            return "返回流";
        } else {//图片和数据包中的图片用此方法
            this.getImg(response, fileInfo, filePath, keyWord, systemModel, watermark, tools,"");
            return "返回流";
        }
    }
    public void getWord(FileInfo fileInfo,String path1) {
        pdfConversionWord.conversion(getPath(fileInfo), fileInfo, fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf(".")),path1);

    }

    public String getPath(FileInfo fileInfo) {
        Date date = new Date(fileInfo.getSaveDate());
        String mineType = StringUtils.isEmpty(fileInfo.getSuffix()) ? "default" : fileInfo.getSuffix();
        return String.join(File.separator, this.fileConfig.getFullDirPath((String) null, mineType, date), fileInfo.getBaseFileId() + "." + fileInfo.getSuffix());
    }
    @Override
    public String getXml(String fileId, String filePath) {
        FileInfo fileInfo = commonFileService.fileInfo(fileId);
        //数据包中的xml跟直接上传的xml处理方式不同  单独处理
        try {
            if ("zip".equalsIgnoreCase(fileInfo.getSuffix())) {
                InputStream inputStream = getInputStream(fileInfo, filePath);
                byte[] content = StreamUtils.copyToByteArray(inputStream);
                inputStream.close();
                return Base64Utils.encodeToString(content);
            } else {
                return Base64Utils.encodeToString(StreamUtils.copyToByteArray(fileSaveHandler.readFile(fileInfo)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取zip数据包中Inputstream
     *
     * @param fileInfo
     * @param filePath
     * @return
     */
    private InputStream getInputStream(FileInfo fileInfo, String filePath) {
        try {
            ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(fileSaveHandler.readFile(fileInfo));
            ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
            while (zipEntry != null) {
                if (!zipEntry.isDirectory() && zipEntry.getName().equalsIgnoreCase(filePath)) {
                    break;
                }
                zipEntry = zipInputStream.getNextZipEntry();
            }
            return zipInputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getImg(HttpServletResponse response, FileInfo fileInfo, String filePath, String keyWord, String systemModel, boolean watermark, String tools,String path) {
        //统一的response配置
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        //目前可以处理的图片格式
        //String[] imgType = {"JPG", "JPEG", "PNG"};
        //ArrayUtils.contains(imgType, filePath.substring(filePath.lastIndexOf(".") + 1).toUpperCase())
        //数据包中的图片跟直接上传的图片处理方式不同  单独处理
        if ("zip".equalsIgnoreCase(fileInfo.getSuffix())) {
            try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(fileSaveHandler.readFile(fileInfo))) {
                ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
                while (zipEntry != null) {
                    if (!zipEntry.isDirectory() && zipEntry.getName().equalsIgnoreCase(filePath)) {
                        break;
                    }
                    zipEntry = zipInputStream.getNextZipEntry();
                }
                //设置文件名称
                setFileName(response, filePath.substring(filePath.lastIndexOf("/") + 1));
                //设置媒体类型
                setMimeType(response, filePath.substring(filePath.lastIndexOf(".") + 1));
                //设置文件长度
                setFileLength(response, zipEntry.getSize());
                StreamUtils.copy(zipInputStream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if("docx".equals(fileInfo.getSuffix()) || "doc".equals(fileInfo.getSuffix())){
            try {
                //设置文件名称
                setFileName(response, fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf("."))+".pdf");
                //设置媒体类型
                setMimeType(response, "application/pdf");
                //设置文件长度
                File sysFile = new File(path);
                if (!sysFile.exists()) {
                    throw new IOException("指定的文件不存在:" + sysFile.getPath());
                }
                setFileLength(response, sysFile.length());
                StreamUtils.copy( new FileInputStream(sysFile), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //设置文件名称
                setFileName(response, fileInfo.getName());
                //设置媒体类型
                setMimeType(response, fileInfo.getMimeType());
                //设置文件长度
                setFileLength(response, fileInfo.getFileSize());
                StreamUtils.copy(fileSaveHandler.readFile(fileInfo), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置文件长度
     *
     * @param response
     * @param size
     */
    private void setFileLength(HttpServletResponse response, long size) {
        try {
            //设置文件长度
            response.setContentLength(((Long) size).intValue());
        } catch (Exception ignored) {
        }
    }

    /**
     * 设置文件名称
     *
     * @param response
     * @param fileName
     */
    private void setFileName(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }

    /**
     * 设置媒体类型
     *
     * @param response
     * @param suffix
     */
    private void setMimeType(HttpServletResponse response, String suffix) {
        String fileMine = "";
        switch (suffix) {
            case "jpg":
            case "jpeg":
                fileMine = "image/jpeg";
                break;
            case "png":
                fileMine = "image/png";
                break;
            case "gif":
                fileMine = "image/gif";
                break;
            case "xml":
                fileMine = "text/xml";
                break;
            case "html":
                fileMine = "text/html";
                break;
            default:
                break;
        }
        response.setContentType(fileMine);
    }

    private List<String> parseZip(FileInfo fileInfo, List<String> filePathList) {
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(commonFileService.fileStreamByHash(fileInfo.getFileHash()), "gbk")) {
            ZipArchiveEntry archiveEntry = zais.getNextZipEntry();
            while (archiveEntry != null) {
                filePathList.add(archiveEntry.getName());
                archiveEntry = zais.getNextZipEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePathList;
    }

    /**
     * 生成treeNode
     *
     * @param filePathList
     * @return
     */
    private List<TreeNode> getSetTreeNode(List<String> filePathList, FileInfo fileInfo) {
        Set<String> files = new HashSet<>();
        for (String filePath : filePathList) {
            files.add(filePath);
            while (filePath.contains("/")) {
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
                files.add(filePath);
            }
        }
        Map<String, List<TreeNode>> pidMaps = new HashMap<>();

        files.stream().map(s -> {
            TreeNode treeNode = new TreeNode<>(s, getName(s), s, fileInfo.getId());
            if (s.contains("/")) {
                treeNode.setParentId(s.substring(0, s.lastIndexOf("/")));
            } else {
                treeNode.setParentId(fileInfo.getId());
            }
            return treeNode;
        }).forEach(t -> pidMaps.computeIfAbsent(t.getParentId(), (k) -> new ArrayList<>()).add(t));
        return CommonService.mapToTree(fileInfo.getId(), 0, pidMaps);
    }

    /**
     * 获取文件名称
     *
     * @param filePath 文件路径
     * @return
     */
    public String getName(String filePath) {
        int index = filePath.lastIndexOf("/");
        if (index > 0) {
            filePath = filePath.substring(index + 1);
        }
        return filePath;
    }
}
