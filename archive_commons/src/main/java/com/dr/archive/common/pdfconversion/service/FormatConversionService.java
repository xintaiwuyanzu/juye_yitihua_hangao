package com.dr.archive.common.pdfconversion.service;

/**
 * @author caor
 * @Date 2020-11-02 16:42
 */
public interface FormatConversionService {
    String fileToPdf(String fileId);

    String batchFileToPdf(String fileIds);
}
