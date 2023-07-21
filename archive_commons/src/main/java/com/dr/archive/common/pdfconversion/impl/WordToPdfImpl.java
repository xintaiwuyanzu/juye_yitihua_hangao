package com.dr.archive.common.pdfconversion.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.dr.archive.common.pdfconversion.service.PdfConversion;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class WordToPdfImpl implements PdfConversion {

    protected final MediaType[] mediaTypes = new MediaType[]{
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
            MediaType.parseMediaType("application/msword"),
    };

    @Autowired
    protected CommonFileService commonFileService;

    @Autowired
    protected CommonFileConfig commonFileConfig;

    @Override
    public boolean accept(MediaType mediaType) {
        for (MediaType type : mediaTypes) {
            if (type.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void conversion(String filePath, FileInfo fileInfo, String fileName,String path) {

        String tmpDir = commonFileConfig.getFullDirPath("tmp", "pdf", null);
        File inputWord = new File(filePath);
        //File outputFile = new File(tmpDir, fileName + ".pdf");
        File outputFile = new File(path);
        try {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter  converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            docxInputStream.close();
            outputStream.close();
           // FileResource fileResource = new FileSystemFileResource(outputFile);
            //commonFileService.addFileLast(fileResource, fileInfo.getRefId(), "archive", "default");
            //outputFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            while (true) {
                System.gc();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //inputWord.delete();
                //outputFile.delete();
               /* if (!inputWord.exists() && !outputFile.exists()) {
                    break;
                }*/
            }
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
