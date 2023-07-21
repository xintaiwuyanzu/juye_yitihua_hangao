package com.dr.archive.fuzhou.approve.service.impl;

import com.dr.archive.fuzhou.approve.ApproveCenterConfig;
import com.dr.archive.fuzhou.approve.bo.TransferInfo;
import com.dr.archive.fuzhou.configManager.bo.FieldConfig;
import com.dr.archive.fuzhou.configManager.bo.FieldMetaData;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.service.ArchiveOnlineReceiveService;
import com.dr.archive.receive.online.service.OnLineReceiveProvider;
import com.dr.archive.receive.online.service.OnlineReceiveBatchContext;
import com.dr.archive.receive.online.service.impl.AbstractOnlineReceiveProvider;
import com.dr.archive.receive.online.service.impl.ZipFileResource;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.file.FileResource;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: qiuyf
 * 数字化加工工具在线接收实现类
 */
@Component
public class DigitalOnlineReceiveProvider extends AbstractOnlineReceiveProvider<String> implements OnLineReceiveProvider.ReceiveTypeProvider {
    static final Logger logger = LoggerFactory.getLogger(ApprovalOnlineReceiveProvider.class);

    static final String TRANSFER_KEY = "$transfer_key";
    static final String INDEX_KEY = "$index_key";

    /**
     * 在线接收目录key
     */
    public static String DIRECTORY_KEY = "$directory";
    /**
     * 在线接收文件key
     */
    public static String DETAIL_FILE_KEY = "$DETAIL_FILE";

    @Autowired
    ApproveCenterConfig centerConfig;

    @Lazy
    @Autowired
    ArchiveOnlineReceiveService onlineReceiveService;

    @Autowired
    ConfigManagerClient configManagerClient;

    @Override
    public boolean canHandle(OnlineReceiveBatchContext context) {
        //只处理数字化类型的系统
        return code().equalsIgnoreCase(context.getSysManage().getSysType());
    }

    /**
     * 同步接收数据
     *
     * @param context
     * @return
     */
    @Override
    public String syncReceiveResult(OnlineReceiveBatchContext context) {

        try {
            String xmlPath = context.getArchiveReceiveBo().getXmlPath();

            ArchiveBatchReceiveOnline online = context.getBatchReceiveOnline();
            //设置文件名称
            online.setFileName(online.getId() + ".xml");
            //拼写本地缓存路径
            File localFile = onlineReceiveService.buildBatchFile(online);
            //复制到本地
            downLoadFile(xmlPath, localFile);
            TransferInfo transferInfo = xmlObjectMapper.readValue(localFile, TransferInfo.class);
            //将移交信息追加到上下文中
            context.addAttribute(TRANSFER_KEY, transferInfo);
            context.addAttribute(INDEX_KEY, 0);
            //批次信息加到批次表中
            online.setBatchName(transferInfo.getBatchName());
            online.setTransferUnitPerson(transferInfo.getTransactor());
            Long startTime = DateTimeUtils.stringToMillis(transferInfo.getExchangeTime(), "yyyy-MM-dd HH:mm:ss");
            online.setStartDate(startTime);
            online.setDetailNum(transferInfo.getDirectories().size());
        } catch (Exception e) {
            logger.error("数字化加工工具接收数据异常", e);
        }
        //转换xml文件为java对象
        return "请求成功";
    }

    @Override
    public boolean hasNext(OnlineReceiveBatchContext context) {
        TransferInfo transferInfo = context.getAttribute(TRANSFER_KEY);
        int index = context.getAttribute(INDEX_KEY);
        return transferInfo.getDirectories().size() > index;
    }

    /**
     * 接收单个电子文件
     *
     * @param context
     * @return
     */

    @Override
    public OnlineReceiveBatchContext.ReceiveDataContext receiveNext(OnlineReceiveBatchContext context) {
        TransferInfo transferInfo = context.getAttribute(TRANSFER_KEY);
        int index = context.getAttribute(INDEX_KEY);

        TransferInfo.TransferDirectory directory = transferInfo.getDirectories().get(index);
        context.addAttribute(INDEX_KEY, index + 1);

        OnlineReceiveBatchContext.ReceiveDataContext dataContext = newDataContext(context, directory.getFondsIdentifier(), directory.getCategoryCode(), directory.getYear());
        dataContext.addAttribute(DIRECTORY_KEY, directory);

        //构建详情数据，主要是为了下载文件，其他信息可以后面补充
        dataContext.getDetailReceiveOnline().setRegionCode(directory.getRegionCode());
        dataContext.getDetailReceiveOnline().setSocialCode(directory.getSocialCode());
        dataContext.getDetailReceiveOnline().setTitle(directory.getTitle());
        dataContext.getDetailReceiveOnline().setDigitaldigest(directory.getDigitalSummary());
        dataContext.getDetailReceiveOnline().setArchiveCode(directory.getArchivalCode());

        //本地文件路径
        File dir = onlineReceiveService.buildDetailDir(dataContext.getDetailReceiveOnline());
        //根据数字化 中没有电子文件号,用档号保存文件
        String fileName = directory.getArchivalCode() + ".zip";
        File targetFile = new File(dir, fileName);
        //todo 文件已经下载过
        if (downLoadFile(directory.getSoftwareEnvironment(), targetFile)) {
            //如果文件下载成功
            // 追加表单信息
            parseFormData(context, dataContext, targetFile);
            //解析文件数据
            parseFileSource(context, dataContext, targetFile);
        }
        return dataContext;
    }

    /**
     * 追加原文数据
     *
     * @param context
     * @param dataContext
     * @param targetFile
     */
    private void parseFileSource(OnlineReceiveBatchContext context, OnlineReceiveBatchContext.ReceiveDataContext dataContext, File targetFile) {
        try {
            List<FileResource> fileResources = new ArrayList<>();
            ZipFile zipFile = new ZipFile(targetFile);
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    fileResources.add(new ZipFileResource(entry, targetFile.toString()));
                }
            }
            dataContext.setFileInfos(fileResources);
            context.addAttribute(DETAIL_FILE_KEY, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析表单数据
     *
     * @param context
     * @param dataContext
     * @param targetFile
     */
    private void parseFormData(OnlineReceiveBatchContext context, OnlineReceiveBatchContext.ReceiveDataContext dataContext, File targetFile) {
        List<FieldMetaData> fieldMetaData = getArchiveFieldMetaData(context, dataContext);
        try {
            Map<String, String> xmlMapData = newXPathParserXml(new ZipFileResource(targetFile.getPath(), "基本信息元数据.xml"), fieldMetaData);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            FormDefinition formDefinition = dataContext.getFormDefinition();
            //塞入数据的具体 表单
            FormData formData = new FormData(formDefinition.getId(), UUID.randomUUID().toString());
            //根据解析出来的数据,比对咱系统上的表字段有,则添加.
            for (Map.Entry<String, String> entry : xmlMapData.entrySet()) {
                FieldModel fieldModel = Optional.ofNullable(formDefinition.getFieldByAlias(entry.getKey()))
                        .orElse(formDefinition.getFieldByCode(entry.getKey()));
                if (ObjectUtils.isNotEmpty(fieldModel)) {
                    //处理时间 转换为 long类型
                    long aLong;
                    try {
                        aLong = simpleDateFormat.parse(entry.getValue()).getTime();
                        formData.put(fieldModel.getFieldCode(), aLong);
                    } catch (Exception e) {
                        formData.put(fieldModel.getFieldCode(), entry.getValue());
                    }
                }
            }
            dataContext.setFormData(formData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据上下文信息读取档案元数据定义
     *
     * @param dataContext
     * @return
     */
    protected List<FieldMetaData> getArchiveFieldMetaData(OnlineReceiveBatchContext context, OnlineReceiveBatchContext.ReceiveDataContext dataContext) {

        String gdTime = "";
        String classify = "2"; //默认为2
        TransferInfo transferInfo = context.getAttribute(TRANSFER_KEY);
        if (!StringUtils.isEmpty(transferInfo.getExchangeTime())) {
            gdTime = transferInfo.getExchangeTime().substring(0, 10);
        } else {
            gdTime = DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd");
        }
        if (!StringUtils.isEmpty(transferInfo.getStandard())) {
            classify = transferInfo.getStandard();
        }
        List<FieldConfig> metadata = configManagerClient.getCategoryMetadata(dataContext.getCategory().getCode(), classify, "1", gdTime);
        Assert.isTrue(metadata.size() > 0, "未找到元数据配置!!!");
        return metadata.get(0).getMetadata();
    }

    private Map<String, String> newXPathParserXml(ZipFileResource fis, List<FieldMetaData> metadata) {
        Map<String, String> map = new HashMap();
        try (InputStream ips = fis.getInputStream()) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(ips);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String root = "";
            for (FieldMetaData fieldMetaData : metadata) {
                if ("0".equals(fieldMetaData.getParentID())) {
                    root = fieldMetaData.geteName();
                }
            }
            //TODO  暂时没处理fileset下面的
            for (FieldMetaData fieldMetaData : metadata) {
                // String titleXpath = "/" + root + "/" + fieldMetaData.geteName();
                //暂时写死
                String titleXpath = "/base_info/" + fieldMetaData.geteName();
                NodeList nodeList = (NodeList) xPath.evaluate(titleXpath, document, XPathConstants.NODESET);
                if (nodeList.getLength() == 1) {
                    Node item = nodeList.item(0);
                    NodeList childNodes = item.getChildNodes();
                    if (childNodes.getLength() <= 1) {
                        String titleValue = (String) xPath.evaluate(titleXpath, document, XPathConstants.STRING);
                        map.put(fieldMetaData.geteName(), titleValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String code() {
        return "lc-sz";
    }

    @Override
    public String name() {
        return "数字化加工工具";
    }

}
