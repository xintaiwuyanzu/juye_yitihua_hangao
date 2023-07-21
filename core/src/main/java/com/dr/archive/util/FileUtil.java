package com.dr.archive.util;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 根据文件名返回文件list
     * @param fileNames
     * @param files
     * @return
     */
    public static List<File> getFiles(List<String> fileNames, File[] files) {
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (ObjectUtils.isNotEmpty(listFiles)) {
                    fileList.addAll(getFiles(fileNames, listFiles));
                }
            }
            else if (fileNames.contains(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
}
