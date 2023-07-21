package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.bo.BaseInfoFiles;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.transfer.file.ZipFileResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;

/**
 * 1-3-2 检测归档信息包内归档文件集元数据是否与包内实际文件一致
 * 在线归档时才判断
 *
 * @author: qiuyf
 */
@Component
public class Authenticity005Service extends AbstractApproveReceiveDetectService {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String code() {
        return "Authenticity005";
    }

    @Override
    public String modeCode() {
        return "1-3-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        if (detectEnable(context) && "Online".equals(gdType)) { //
            detectionFile(context, code(), modeCode());
            //context.addRecordItem(code());
        }
    }

    //对比文件
    public void detectionFile(ItemDetectContext context, String code, String modeCode) {
        //获取基础元数据中的文件集元数据
        File receiveFile = context.getAttribute(ApprovalOnlineReceiveProvider.DETAIL_FILE_KEY);
        Assert.notNull(receiveFile, "文件包为空！");
        BaseInfoFiles baseInfoFiles = new BaseInfoFiles();
        try {
            boolean result = true;
            StringBuilder meg = new StringBuilder();
            ZipFileResource zipFileResource = new ZipFileResource(receiveFile.getPath(), "基本信息元数据.xml");
            ZipFile zipFile = new ZipFile(receiveFile);
            InputStream inputStream = zipFileResource.getInputStream();
            //baseInfoFiles = objectMapper.readValue(inputStream, BaseInfoFiles.class);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String titleXpath = "/base_info/fileset/folder";
            NodeList nodeList = (NodeList) xPath.evaluate(titleXpath, document, XPathConstants.NODESET);
            int foldNum = nodeList.getLength();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                Node foldNameNode = item.getAttributes().getNamedItem("title"); //文件名
                String foldName = foldNameNode.getNodeValue();
                if (zipFile.getEntries(foldName) == null) {
                    result = false;
                    meg.append("文件夹(").append(foldName).append(")不存在;");
                }
                NodeList childNodes = item.getChildNodes();
                int cl = childNodes.getLength();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childItem = childNodes.item(j);
                    //判断是否是节点
                    if (childItem.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList childNodes2 = childItem.getChildNodes();
                        String fileName = "";
                        long fileSize = 0;
                        for (int k = 0; k < childNodes2.getLength(); k++) {
                            Node childItem2 = childNodes2.item(k);
                            if ("file_actual_name".equals(childItem2.getNodeName())) {
                                fileName = foldName + "/" + childItem2.getTextContent();
                            }
                            if ("computer_file_size".equals(childItem2.getNodeName())) {
                                fileSize = Long.parseLong(childItem2.getTextContent());
                            }
                        }
                        ZipArchiveEntry zfile = zipFile.getEntry(fileName);
                        if (zfile == null) {
                            result = false;
                            meg.append("文件(").append(fileName).append(")不存在;");
                        } else {
                            if (zfile.getSize() != fileSize) {
                                result = false;
                                meg.append("文件(").append(fileName).append(")大小不一致;");
                            }
                        }
                    }
                }
            }
            if (result) {
                context.addRecordItem(code, modeCode);
            } else {
                context.addRecordItem(code, modeCode, meg.toString(), TestRecordService.TEST_STATUS_FAIL);
            }
        } catch (Exception ignored) {

        }

    }
}
