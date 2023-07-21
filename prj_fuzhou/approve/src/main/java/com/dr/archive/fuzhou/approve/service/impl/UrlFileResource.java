package com.dr.archive.fuzhou.approve.service.impl;

import com.dr.framework.common.file.FileResource;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author caor
 * @date 2021-08-28 13:40
 */
public class UrlFileResource implements FileResource {

    private String url;
    private File tempFile;
    private String fileName;

    public UrlFileResource(String url, String fileName) throws IOException {
        this.fileName = fileName;
        this.url = url;
        Path path = Files.createTempFile("", fileName);
        tempFile = path.toFile();
        FileUtils.copyURLToFile(new URL(url), tempFile, 2000, 2000);
        //临时文件使用完就删除掉
        tempFile.deleteOnExit();
    }

    public UrlFileResource(String url) {
        int index = url.lastIndexOf("/");
        if (index > 0) {
            this.fileName = url.substring(index + 1);
        }
        this.url = url;
        tempFile = new File(url);
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public long getCreateDate() {
        return 0;
    }

    @Override
    public long getLastModifyDate() {
        return 0;
    }

    @Override
    public long getFileSize() {
        return tempFile.length();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }
}
