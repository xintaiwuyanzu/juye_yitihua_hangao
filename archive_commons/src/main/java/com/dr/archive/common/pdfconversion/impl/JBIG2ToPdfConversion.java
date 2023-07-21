package com.dr.archive.common.pdfconversion.impl;

import com.dr.framework.common.file.model.FileInfo;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.image.Jbig2ImageData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * jbig2
 * TODO 这里应该有更简便的方法 pdfbox
 *
 * @author dr
 */
@Component
public class JBIG2ToPdfConversion extends AbstractImageToPdfConversion {
    protected final MediaType mediaType = MediaType.parseMediaType("image/jbig2");

    @Override
    protected ImageData[] doReadImageData(InputStream inputStream) throws IOException {
        //TODO 这里待验证！！
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileCopyUtils.copy(inputStream, byteArrayOutputStream);
        byte[] imgData = byteArrayOutputStream.toByteArray();
        int pages = Jbig2ImageData.getNumberOfPages(imgData);
        ImageData[] imageDatas = new ImageData[pages];
        for (int i = 0; i < pages; i++) {
            imageDatas[i] = ImageDataFactory.createJbig2(imgData, i);
        }
        return new ImageData[]{ImageDataFactory.create(byteArrayOutputStream.toByteArray())};
    }

    @Override
    public boolean accept(MediaType mediaType) {
        return this.mediaType.includes(mediaType);
    }


    @Override
    public String fileToPdf(String fileId) {
        return null;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 9;
    }

}
