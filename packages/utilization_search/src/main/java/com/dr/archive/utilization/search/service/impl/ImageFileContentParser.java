package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.FileContentParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读取text文本
 *
 * @author dr
 */
@Component
public class ImageFileContentParser implements FileContentParser {

    final MediaType mediaType = new MediaType("image");

    @Override
    public boolean accept(MediaType mediaType) {
        return this.mediaType.includes(mediaType);
    }

    @Override
    public String read(InputStream stream) throws IOException {
        //todo 这里可能有内存泄露
        /*BufferedImage textImage = ImageIO.read(stream);
        ITesseract instance = new Tesseract();
        //instance.setDatapath("C:\\Program Files (x86)\\Tesseract-OCR\\tessdata");//设置训练库
        instance.setLanguage("chi_sim");//中文识别
        try {
            return instance.doOCR(textImage);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "";
        }*/
        return "";
    }

}
