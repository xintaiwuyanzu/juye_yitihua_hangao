package com.dr.archive.common.pdfconversion.service;

import com.dr.framework.common.file.model.FileInfo;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

public interface PdfConversion extends Ordered {

    boolean accept(MediaType mediaType);

    void conversion(String filePath, FileInfo fileInfo, String fileName,String path);
}
