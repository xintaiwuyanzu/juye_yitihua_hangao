package com.dr.archive.fuzhou;

import com.dr.archive.common.Application;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: caor
 * @Date: 2022-06-22 22:13
 * @Description:
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class FileTest {
    @Autowired
    CommonFileService commonFileService;
    Logger logger = LoggerFactory.getLogger(FileTest.class);
    @Autowired
    FileTempMapper fileTempMapper;

    @Test
    public void testAA() {
        //1、复制common_file_relation为common_file_relation_temp1
        //2、调用本方法删除重复数据
        //3、备份common_file_relation为common_file_relation_temp2
        //4、重命名common_file_relation_temp1为common_file_relation
        //5、访问系统检查数据是否还有重复数据
        List<String> refid = fileTempMapper.selectRefIds("");
        for (String s : refid) {
            String id = s.split(":::")[0];
            removeId(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeId(String refId) {
        List<FileInfo> fileInfoList = commonFileService.list(refId, "archive", "default");
        Set<String> fileIdset = new HashSet<>();
        List<String> removeIds = new ArrayList<>();
        for (FileInfo fileInfo : fileInfoList) {
            if (fileIdset.contains(fileInfo.getBaseFileId())) {
                removeIds.add(fileInfo.getId());
            } else {
                fileIdset.add(fileInfo.getBaseFileId());
            }
        }
        for (String removeId : removeIds) {
            commonFileService.removeFile(removeId);
        }
        logger.info("" + fileInfoList.size());
    }

}
