package com.dr.archive.common.pdfconversion.service;

import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.io.OutputStream;

public interface PdfConversionNew extends Ordered {

    boolean accept(MediaType mediaType);

    String fileToPdf(String fileId);

    /**
     * 将指定的输入流转换成pdf输出流
     *
     * @param inputStream
     * @param outputStream
     */
    void conversion(InputStream inputStream, OutputStream outputStream);
}
