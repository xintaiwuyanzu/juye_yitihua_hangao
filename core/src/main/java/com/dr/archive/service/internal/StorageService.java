package com.dr.archive.service.internal;

import com.dr.archive.model.to.ArchiveFileTo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 存储相关的操作
 *
 * @author dr
 */
public interface StorageService {
    String DEFAULT_CREATE_GROUP_CODE = "default";
    /*
     * =======================================
     * 添加附件
     * =======================================
     */

    /**
     * 保存附件
     *
     * @param stream
     * @return
     */
    ArchiveFileTo saveFile(InputStream stream);

    /**
     * 长期保存指定的附件
     *
     * @param archiveId
     * @param storageSchemaId
     * @return
     */
    default long longSaveFile(String archiveId, String storageSchemaId) {
        return longSaveFile(archiveId, storageSchemaId, DEFAULT_CREATE_GROUP_CODE);
    }

    /**
     * 按照存储方案长期保存指定的附件
     *
     * @param archiveId
     * @param storageSchemaId
     * @param groupCode
     * @return
     */
    long longSaveFile(String archiveId, String storageSchemaId, String groupCode);

    /*
     * =======================================
     * 查询附件
     * =======================================
     */

    /**
     * 查询一条档案默认的附件列表
     *
     * @param archiveId
     * @return
     */
    default List<ArchiveFileTo> getArchiveFiles(String archiveId) {
        return getArchiveFiles(archiveId, DEFAULT_CREATE_GROUP_CODE);
    }

    /**
     * 根据档案id查询指定类型的附件列表
     *
     * @param archiveId
     * @param groupCode
     * @return
     */
    List<ArchiveFileTo> getArchiveFiles(String archiveId, String groupCode);

    /**
     * 查询一条档案所有的附件分组
     *
     * @param archiveId
     * @return
     */
    List<String> getArchiveGroupCodes(String archiveId);

    /**
     * 根据附件Id读取附件流
     *
     * @param fileId
     * @param outputStream
     */
    void readFile(String fileId, OutputStream outputStream);
    //TODO 获取附件缩略图
    //TODO 删除附件
}
