package com.dr.archive.receive.online.service.impl;

import com.dr.framework.common.file.FileResource;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 从zip中提取文件流
 *
 * @author dr
 */
public class ZipFileResource implements FileResource {

    private ZipArchiveEntry zipEntry;
    private String zipPath;
    private String subFilePath;

    public ZipFileResource(ZipArchiveEntry zipEntry, String zipPath) {
        this.zipEntry = zipEntry;
        this.zipPath = zipPath;
        this.subFilePath = zipEntry.getName();
    }

    public ZipFileResource(String zipPath, String subFilePath) throws IOException {
        this.zipPath = zipPath;
        this.subFilePath = subFilePath;
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            this.zipEntry = zipFile.getEntry(subFilePath);
        }
    }

    @Override
    public String getName() {
        String fileName = zipEntry.getName();
        String[] arr = fileName.split("/");
        return arr[arr.length - 1];
    }

    @Override
    public long getCreateDate() {
       // return zipEntry.getCreationTime().toMillis();
        //查不到创建日期
        return 0;
    }

    @Override
    public long getLastModifyDate() {
        return zipEntry.getLastModifiedTime().toMillis();
    }

    @Override
    public long getFileSize() {
        return zipEntry.getSize();
    }

    @Override
    public String getDescription() {
        return subFilePath;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ZipFile zipFile = new ZipFile(zipPath);
        return zipFile.getInputStream(zipEntry);
    }

    public String getZipPath() {
        return zipPath;
    }

    public String getSubFilePath() {
        return subFilePath;
    }
}
