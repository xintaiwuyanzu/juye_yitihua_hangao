package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.transfer.file.ZipFileResource;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author: qiuyf
 * 通过读取元数据的方式检测电子文件元数据文件是否可以被正常访问。
 */
@Component
public class Usability001Service extends AbstractApproveReceiveDetectService {
    @Autowired
    protected CommonFileService commonFileService;

    @Override
    public String code() {
        return "Usability001";
    }

    @Override
    public String modeCode() {
        return "3-1-1";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        //只对在线接收进行该项检测
        if (detectEnable(context) && "Online".equals(gdType)) {
            File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
            Assert.notNull(receiveFile, "文件包为空！");
            try {
                ZipFileResource zipFileResource = new ZipFileResource(receiveFile.getPath(), "基本信息元数据.xml");
                InputStream ips = zipFileResource.getInputStream();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                Document document = builder.parse(ips);
                XPath xPath = XPathFactory.newInstance().newXPath();
                addSuccessItem(context);
            } catch (Exception e) {
                addFailItem(context);
            }
        } else {
            //如果离线中也有基本信息元数据也要进行判断
            List<FileInfo> fileInfoList = commonFileService.list(context.getFormData().getId(), "archive", "default");
            for (FileInfo fileInfo : fileInfoList) {
                if ("基本信息元数据.xml".equals(fileInfo.getName())) {
                    try {
                        InputStream ips = commonFileService.fileStream(fileInfo.getId());
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                        Document document = builder.parse(ips);
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        addSuccessItem(context);
                    } catch (Exception e) {
                        addFailItem(context);
                    }
                }
            }
        }
    }

    public void addFailItem(ItemDetectContext context) {
        context.addRecordItem(code(), modeCode(),
                "电子文件元数据文件不能被正常访问!",
                TestRecordService.TEST_STATUS_FAIL,
                "",
                "电子文件元数据文件",
                "基本信息元数据.xml");
    }

    public void addSuccessItem(ItemDetectContext context) {
        context.addRecordItem(code(), modeCode(),
                "检测通过",
                TestRecordService.TEST_STATUS_SUCCESS,
                "",
                "电子文件元数据文件",
                "基本信息元数据.xml");
    }
}
