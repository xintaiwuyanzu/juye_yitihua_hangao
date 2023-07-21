package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.ofdrw.reader.OFDReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author: qiuyf
 * 版式文件通过提取电子文件文本内容判断电子文件能否打开。多媒体文件通过提取文件头信息判断文件能否打开。
 */
@Component
public class Usability003Service extends AbstractApproveReceiveDetectService {
    @Autowired
    protected CommonFileService commonFileService;

    @Override
    public String code() {
        return "Usability003";
    }

    @Override
    public String modeCode() {
        return "3-2-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context)) {
            if ("Online".equals(gdType)) {
                //对zip包里的文件进行检测
                File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
                //对包内文件遍历
                try {
                    ZipFile zipFile = new ZipFile(receiveFile);
                    Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
                    while (entries.hasMoreElements()) {
                        ZipArchiveEntry entry = entries.nextElement();
                        InputStream in = zipFile.getInputStream(entry);
                        if (!entry.isDirectory()) {
                            String suffix = entry.getName().substring(entry.getName().lastIndexOf(".") + 1);
                            switch (suffix) {
                                case "pdf":
                                case "PDF":
                                    checkPdfFile(in, entry.getName(), context);
                                    break;
                                case "ofd":
                                case "OFD":
                                    checkOfdFile(in, entry.getName(), context);
                                    break;
                                case "zip":
                                case "ZIP":
                                    checkZipFile(in, entry.getName(), context);
                                    break;
                                case "png":
                                case "jpg":
                                case "jpeg":
                                case "bmp":
                                case "gif":
                                    checkImgFile(in, entry.getName(), context);
                                    break;
                                case "xml":
                                    checkXmlFile(in, entry.getName(), context);
                                    break;
                            }
                        }
                        in.close();
                    }
                } catch (Exception ignored) {

                }

            } else {
                //离线的判断挂接的原文是否可用
                List<FileInfo> fileInfoList = commonFileService.list(context.getFormData().getId(), "archive", "default");
                for (FileInfo fileInfo : fileInfoList) {
                    try {
                        InputStream in = commonFileService.fileStream(fileInfo.getId());
                        switch (fileInfo.getSuffix()) {
                            case "pdf":
                            case "PDF":
                                checkPdfFile(in, fileInfo.getName(), context);
                                break;
                            case "ofd":
                            case "OFD":
                                checkOfdFile(in, fileInfo.getName(), context);
                                break;
                            case "zip":
                            case "ZIP":
                                checkZipFile(in, fileInfo.getName(), context);
                                break;
                            case "png":
                            case "jpg":
                            case "jpeg":
                            case "bmp":
                            case "gif":
                                checkImgFile(in, fileInfo.getName(), context);
                                break;
                            case "xml":
                                checkXmlFile(in, fileInfo.getName(), context);
                                break;
                        }
                        in.close();
                    } catch (Exception e) {

                    }

                }
            }
        }
    }

    public void addFailItem(ItemDetectContext context, String fileName) {
        context.addRecordItem(code(), modeCode(),
                "电子文件不能被打开!",
                TestRecordService.TEST_STATUS_FAIL,
                "",
                "电子文件",
                fileName);
    }

    public void addSuccessItem(ItemDetectContext context, String fileName) {
        context.addRecordItem(code(), modeCode(),
                "检测通过",
                TestRecordService.TEST_STATUS_SUCCESS,
                "",
                "电子文件",
                fileName);
    }

    public void checkPdfFile(InputStream in, String fileName, ItemDetectContext context) {
        try {
            PdfReader pdfReader = new PdfReader(in);
            PdfDocument document = new PdfDocument(pdfReader);
            // 获取页码
            int pages = document.getNumberOfPages();
            pdfReader.close();
            addSuccessItem(context, fileName);
        } catch (Exception e) {
            addFailItem(context, fileName);
        }
    }

    public void checkOfdFile(InputStream in, String fileName, ItemDetectContext context) {
        try {
            OFDReader reader = new OFDReader(in);
            addSuccessItem(context, fileName);
            reader.close();
        } catch (Exception e) {
            addFailItem(context, fileName);
        }
    }

    public void checkZipFile(InputStream in, String fileName, ItemDetectContext context) {
        try {
            ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(in);
            addSuccessItem(context, fileName);
            zipInputStream.close();
        } catch (Exception e) {
            addFailItem(context, fileName);
        }
    }

    public void checkImgFile(InputStream in, String fileName, ItemDetectContext context) {
        try {
            ImageInputStream imgIps = ImageIO.createImageInputStream(in);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imgIps);
            while (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                reader.setInput(imgIps);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width > 0 && height > 0) {
                    addSuccessItem(context, fileName);
                }
            }
            imgIps.close();

        } catch (Exception e) {
            addFailItem(context, fileName);
        }
    }

    /*通过解析xml文件判断是否可用*/
    public void checkXmlFile(InputStream in, String fileName, ItemDetectContext context) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(in);
            XPath xPath = XPathFactory.newInstance().newXPath();
            addSuccessItem(context, fileName);
        } catch (Exception e) {
            addFailItem(context, fileName);
        }
    }
}
