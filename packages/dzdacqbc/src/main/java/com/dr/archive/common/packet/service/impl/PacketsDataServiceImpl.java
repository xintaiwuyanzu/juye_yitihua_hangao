package com.dr.archive.common.packet.service.impl;

import com.dr.archive.common.packet.service.PacketsDataService;
import com.dr.archive.common.packet.util.PacketDataParserUtil;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecordInfo;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.util.ZipUtil;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.file.service.impl.DefaultFileHandler;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * @Author: caor
 * @Date: 2021-11-23 15:47
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PacketsDataServiceImpl implements PacketsDataService {

    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    ArchiveFormDefinitionService archiveFormDefinitionService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    DefaultFileHandler defaultFileHandler;
    @Autowired
    FormDataService formDataService;
    @Autowired
    ArchiveMetadataRecordService archiveMetadataRecordService;

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void packet(String formDefinitionId, String formDataId, String targetPath) {
        //获取字段列表
//        List<FormField> formFieldList = archiveFormDefinitionService.findFieldList(formDefinitionId);
        //获取数据信息
        FormData formData = archiveDataManager.selectOneFormData(formDefinitionId, formDataId);
        List<FormData> formDataList = new ArrayList<>();
        formDataList.add(formData);
        //获取档号用来生成文件夹名称
        String archiveCode = formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
        //TODO 获取存储路径
        targetPath = Optional.ofNullable(targetPath).orElse(commonFileConfig.getUploadDir("temp") + File.separator + archiveCode);
//        String targetPath = commonFileConfig.getUploadDir("temp") + File.separator + archiveCode;
        //获取原文
        List<String> fileList = buildFile(formDataId, "archive", null, targetPath);
        //TODO 原文专版ofd格式文件

        //生成xml文件，把原文列表也放到基本信息元数据xml中去
        buildXML(formDataList, targetPath, fileList);
        //生成管理过程元数据
        List<ArchiveMetadataRecord> archiveMetadataRecordList = archiveMetadataRecordService.selectList(SqlQuery.from(ArchiveMetadataRecord.class).equal(ArchiveMetadataRecordInfo.FORMDATAID, formData.getId()));
        buildXMLMetadata(archiveMetadataRecordList, targetPath);
        //生成zip包
        try {
            ZipUtil.toZip(targetPath, targetPath + ".zip", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void parser(InputStream inputStream) {
        //TODO 获取zip文件
        String filePath = "D:\\caor\\project\\prj_archive_fuzhou\\files\\upload\\temp\\F10001.zip";
        File file = new File(filePath);
        //获取字段列表 TODO 根据全宗分类获取表单id
        String formDefinitionId = "3501bd48-ea04-4cf1-ba50-1bd6674d82f1";
//        String formDefinitionId = "98ed0624-ea50-4d95-8e39-7d77057565a3";
//        List<FormField> formFieldList = archiveFormDefinitionService.findFieldList(formDefinitionId);
        //往表单插入数据
        FormData formData = new FormData(formDefinitionId);
        Map<String, Map<String, String>> zipMap = new HashMap<>();
        //解析xml文件,TODO 这种解析方式有问题
        try {
            InputStream is = Files.newInputStream(file.toPath());
            zipMap = PacketDataParserUtil.parseZipXml(is, PacketDataParserUtil.objectMapper, PacketsDataService.BASIC_INFORMATION_METADATA, Map.class);
            //TODO 这里跟打包生成的标签一致
            Map<String, String> dataMap = zipMap.get("data");
            formData.putAll(dataMap);
            //获取文件列表 TODO 这里跟打包生成的标签一致
            Map<String, String> fileMap = zipMap.get("filelist");
            List<String> fileList = new ArrayList<>();
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                fileList.add(entry.getValue());
            }
            //TODO 四性检测放到业务中实现，不再这里实现
            // 插入数据,TODO 分类id传值，添加数据方式后面要改掉,插入数据放到业务中实现，不再这里实现
            formData.setId(UUIDUtils.getUUID());
            //archiveDataManager.insertFormData(formData, null, null);
            formDataService.addFormData(formData);
            // 挂接原文
//            fileHook(filePath, formData.getId(), fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建xml文件
     *
     * @param formDataList
     * @param targetPath   元数据存储路径
     */
    void buildXML(List<FormData> formDataList, String targetPath, List<String> fileList) {
        if (StringUtils.isEmpty(targetPath)) {
            targetPath = commonFileConfig.getUploadDir("temp");
        }
        targetPath += File.separator + PacketsDataService.BASIC_INFORMATION_METADATA;
        ifFileExit(targetPath, true);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(targetPath))) {
            PacketDataParserUtil.writeData(fileList.toArray(new String[0]), converseList(formDataList).iterator(), "application/zip", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据目录数据将原文复制到指定位置
     *
     * @param refId
     * @param refType
     * @param groupCode
     * @param targetPath
     */
    List<String> buildFile(String refId, String refType, String groupCode, String targetPath) {
        List<String> filePathList = new ArrayList<>();
        if (StringUtils.isEmpty(groupCode)) {
            groupCode = "default";
        }
        List<FileInfo> fileInfoList = commonFileService.list(refId, refType, groupCode);
        for (FileInfo fileInfo : fileInfoList) {
            String realTargetPath = targetPath;
            String relative = "";
            try {
                if (!StringUtils.isEmpty(fileInfo.getDescription())) {
                    relative = File.separator + fileInfo.getDescription();
                } else {
                    relative = File.separator + "结果文件";
                }
                realTargetPath += relative;
                ifFileExit(realTargetPath, false);
                defaultFileHandler.copyTo(fileInfo, realTargetPath + File.separator + fileInfo.getName());
                filePathList.add(relative + File.separator + fileInfo.getName());
                //TODO 转ofd，现在转换服务授权过期了，需要部署到正式库进行调试,把上面两行注释掉
//                FileStreamResult result = client.convertStream(FileByteInfo.fromInputStream(commonFileService.fileStream(fileInfo.getId()), fileInfo.getSuffix()));
//                byte[] fileBytes = Base64Utils.decodeFromString(result.getBytes());
//                String ofdName = fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf(".")) + ".ofd";
//                File ofdFile = new File(new File(realTargetPath), ofdName);
//                FileCopyUtils.copy(fileBytes, ofdFile);
//                filePathList.add(relative + File.separator + ofdName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePathList;
    }

    /**
     * 将List<FormData>转为相应的List<Map<String, Object>>
     *
     * @param formDataList
     * @return
     */
    private List<Map<String, Object>> converseList(List<FormData> formDataList) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FormData formData : formDataList) {
            Map<String, Object> map = new HashMap<>();
            Set<Map.Entry<String, Serializable>> entrySet = formData.entrySet();
            for (Map.Entry<String, Serializable> item : entrySet) {
                String key = item.getKey();
                map.put(key, item.getValue());
                map.put(item.getKey(), item.getValue());
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 根据路径创建文件夹，或文件
     *
     * @param path            文件路径
     * @param ifCreateNewFile 是否创建文件
     * @return
     */
    void ifFileExit(String path, boolean ifCreateNewFile) {
        File file = new File(path);

        if (file.isDirectory() && !file.exists()) {
            file.getParentFile().mkdirs();
        }
        if (ifCreateNewFile) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 挂接原文
     *
     * @param filePath zip地址
     * @param dataId   数据id
     * @param fileList xml中解析的文件列表
     */
    void fileHook(String filePath, String dataId, List<String> fileList) {
        try {
            ZipFile zipFile = new ZipFile(filePath);
            Enumeration<ZipArchiveEntry> entryEnumeration = zipFile.getEntries();
            while (entryEnumeration.hasMoreElements()) {
                ZipArchiveEntry entry = entryEnumeration.nextElement();
                for (String fileName : fileList) {
                    if (!entry.isDirectory() && entry.getName().substring(entry.getName().lastIndexOf("\\") + 1).equals(fileName)) {
                        commonFileService.addFile(new ZipEntryFileResouce(entry, zipFile), dataId, "archive");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成管理过程元数据.xml
     *
     * @param archiveMetadataRecordList
     * @param targetPath
     */
    void buildXMLMetadata(List<ArchiveMetadataRecord> archiveMetadataRecordList, String targetPath) {
        targetPath += File.separator + PacketsDataService.MANAGEMENT_PROCESS_METADATA;
        ifFileExit(targetPath, true);
        try (OutputStream fileOutputStream = Files.newOutputStream(Paths.get(targetPath))) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            JavaType javaType = PacketDataParserUtil.objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ArchiveMetadataRecord.class);
            PacketDataParserUtil.objectMapper.writerWithDefaultPrettyPrinter().forType(javaType).writeValue(fileOutputStream, archiveMetadataRecordList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
