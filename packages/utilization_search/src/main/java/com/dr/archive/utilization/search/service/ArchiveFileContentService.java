package com.dr.archive.utilization.search.service;

import java.nio.charset.Charset;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/9/15 17:18
 */
public interface ArchiveFileContentService {
    /**
     * 根据业务外键获取附件表中所有附件的文本内容
     *
     * @param refId
     * @return
     */
    String getFileContentsByRefId(String refId);

    /**
     * 获取附件表中一个附件的文本
     *
     * @param fileId
     * @return
     */
    String getFileContent(String fileId);

    /**
     * @return
     */
    Charset getCharset();
}
