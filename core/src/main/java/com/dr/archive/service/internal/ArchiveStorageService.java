package com.dr.archive.service.internal;

import com.dr.archive.model.query.StorageConfigQuery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 档案存储相关操作接口
 *
 * @author
 */
public interface ArchiveStorageService {

    void saveFile(MultipartFile file, String configId, boolean guiDang);

    default void saveFile(MultipartFile file, StorageConfigQuery storageConfigQuery, boolean guidang) throws IOException {
        saveFile(file.getInputStream(), storageConfigQuery, guidang);
    }

    //保存文件
    void saveFile(InputStream inputStream, StorageConfigQuery storageConfigQuery, boolean guidang);

    //根据档号查询文件list,1为归档2为未归档

    //根据档号删除文件list

    //读取文件  op
    //1读取暂存文件
    //读取归档文件

    //暂存文件的转换
    //转换文件
}
