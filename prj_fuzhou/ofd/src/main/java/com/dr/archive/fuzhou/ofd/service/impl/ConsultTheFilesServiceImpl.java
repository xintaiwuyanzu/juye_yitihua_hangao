package com.dr.archive.fuzhou.ofd.service.impl;

import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.service.ConsultTheFilesService;
import com.dr.archive.fuzhou.ofd.service.OfdOnlineService;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author: caor
 * @Date: 2021-12-24 19:38
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConsultTheFilesServiceImpl implements ConsultTheFilesService {
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    OfdOnlineService ofdOnlineService;

    /**
     * 原文预览，调用ofd云阅读，根据formDataId，查询第一条原文，多的过滤，无原文的返回原文不存在
     *
     * @param refId
     * @param person
     * @return
     */
    @Override
    public String filePreview(String refId, Person person) {
        String ofdOnlineUrl = "";
        List<FileInfo> fileInfoList = commonFileService.list(refId, "archive");
        if (fileInfoList.size() > 0) {
            FileInfo fileInfo = fileInfoList.get(0);
            WaterMarkBo waterMarkBo = new WaterMarkBo();
            waterMarkBo.setUserId(Optional.ofNullable(person.getId()).orElse(""));
            waterMarkBo.setFileId(fileInfo.getId());
            ofdOnlineUrl = ofdOnlineService.getOfdOnlineUrl(waterMarkBo, null, null, false,"none");
        }
        return ofdOnlineUrl;
    }
}
