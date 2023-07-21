package com.dr.archive.fuzhou;

import com.dr.archive.fuzhou.ofd.bo.FileByteInfo;
import com.dr.archive.fuzhou.ofd.bo.FileStreamResult;
import com.dr.archive.fuzhou.ofd.service.OfdClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class OfdTest {
    Logger logger = LoggerFactory.getLogger(OfdTest.class);
    @Autowired
    OfdClient client;

    @Test
    public void convertTest() throws IOException {
        ApplicationHome home = new ApplicationHome();
        ClassPathResource resource = new ClassPathResource("test.doc");

        long start = System.currentTimeMillis();
        FileStreamResult result = client.convertStream(FileByteInfo.fromInputStream(resource.getInputStream(), "doc"));
        byte[] fileBytes = Base64Utils.decodeFromString(result.getBytes());
        long end = System.currentTimeMillis();

        File ofdFile = new File(new File(home.getDir(), "target"), UUID.randomUUID() + ".ofd");
        FileCopyUtils.copy(fileBytes, ofdFile);
        logger.info("转换成功，耗时{}毫秒", end - start);
        logger.info("原始文件大小：{}kb，生成ofd文件大小:{}kb", resource.contentLength() / 1000, ofdFile.length() / 1000);
        logger.info("生成ofd文件位置为：{}", ofdFile.getPath());
    }
}
