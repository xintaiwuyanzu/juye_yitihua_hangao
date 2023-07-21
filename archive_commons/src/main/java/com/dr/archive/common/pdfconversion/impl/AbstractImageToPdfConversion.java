package com.dr.archive.common.pdfconversion.impl;

import com.dr.archive.common.pdfconversion.service.PdfConversion;
import com.dr.archive.common.pdfconversion.service.PdfConversionNew;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片转pdf转换器
 *
 * @author dr
 */
public abstract class AbstractImageToPdfConversion implements PdfConversionNew {

    @Override
    public void conversion(InputStream inputStream, OutputStream outputStream) {
        //创建pdf写对象
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        //创建pdf对象
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        //创建pdf文本对象
        Document document = new Document(pdfDocument);
        //读取图片数据，可能有多个
        try {
            //TODO 这里可能有内存溢出情况，数据全读取到内存里面了
            //TODO 理想的状态是数据在流里面，读取的时候动态的方式转换到硬盘上
            ImageData[] imageDates = doReadImageData(inputStream);
            for (ImageData imageData : imageDates) {
                pdfDocument.addNewPage();
                //创建pdf能够识别的图片对象
                Image image = new Image(imageData);
                //设置图片宽度
                image.setWidth(pdfDocument.getDefaultPageSize().getWidth() - 50);
                //设置高度自适应
                image.setAutoScaleHeight(true);
                //设置居中
                image.setHorizontalAlignment(HorizontalAlignment.CENTER);
                //文本对象添加图片
                document.add(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    /**
     * 执行读取图片数据
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    protected abstract ImageData[] doReadImageData(InputStream inputStream) throws IOException;
}
