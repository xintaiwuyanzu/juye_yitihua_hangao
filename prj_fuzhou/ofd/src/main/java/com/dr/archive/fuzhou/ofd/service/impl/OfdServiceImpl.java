package com.dr.archive.fuzhou.ofd.service.impl;

import com.dr.archive.fuzhou.ofd.bo.FileByteInfo;
import com.dr.archive.fuzhou.ofd.bo.FileStreamResult;
import com.dr.archive.fuzhou.ofd.service.OfdClient;
import com.dr.archive.fuzhou.ofd.service.OfdService;
import com.dr.framework.common.file.FileResource;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.ByteArrayFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author caor
 * @date 2021-10-25 14:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OfdServiceImpl implements OfdService {
    @Autowired
    OfdClient client;
    @Autowired
    CommonFileService commonFileService;

    @Override
    public void fileToOfd(String fileId) {
        Assert.isTrue(StringUtils.hasText(fileId), "id不能为空");
        FileInfo fileInfo = commonFileService.fileInfo(fileId);

        try {
            FileStreamResult result = client.convertStream(FileByteInfo.fromInputStream(commonFileService.fileStream(fileId), fileInfo.getSuffix()));
            byte[] fileBytes = Base64Utils.decodeFromString(result.getBytes());
            FileResource fileResource = new ByteArrayFileResource(fileBytes, fileInfo.getName());
            commonFileService.addFile(fileResource, fileInfo.getRefId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
