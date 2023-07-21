package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.archive.utilization.search.service.FileContentParser;
import com.dr.framework.common.file.FileSaveHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/8/11 13:55
 */
@Service
public class ArchiveFileContentServiceImpl implements ArchiveFileContentService, InitializingBean {
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    ApplicationContext applicationContext;

    List<FileContentParser> fileContentParsers;
    @Autowired
    protected FileSaveHandler fileSaveHandler;
    final Logger logger = LoggerFactory.getLogger(ArchiveFileContentService.class);

    @Override
    public String getFileContentsByRefId(String refId) {
        List<FileInfo> fileInfos = commonFileService.list(refId, "archive");
        return fileInfos.stream().map(this::doGetFileContent).collect(Collectors.joining());
    }

    @Override
    public String getFileContent(String fileId) {
        return doGetFileContent(commonFileService.fileInfo(fileId));
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    private String doGetFileContent(FileInfo fileInfo) {
        if (fileInfo != null) {
            //todo 将来可以换成minio进一步实现性能
            Path ocrFilePath = buildOcrFilePath(fileInfo);
            if (Files.exists(ocrFilePath)) {
                try (InputStream stream = Files.newInputStream(ocrFilePath)) {
                    return StreamUtils.copyToString(stream, getCharset());
                } catch (IOException ignore) {
                }
            }
            try (InputStream inputStream = fileSaveHandler.readFile(fileInfo)) {
                MediaType mediaType = MediaType.parseMediaType(fileInfo.getMimeType());
                for (FileContentParser fileContentParser : fileContentParsers) {
                    if (fileContentParser.accept(mediaType, fileInfo.getSuffix())) {
                        String content = fileContentParser.read(inputStream);
                        if (StringUtils.hasText(content)) {
                            try (OutputStream ops = Files.newOutputStream(ocrFilePath)) {
                                StreamUtils.copy(content, getCharset(), ops);
                            } catch (Exception ignore) {
                            }
                        }
                        return content;
                    }
                }
            } catch (Exception e) {
                logger.warn("读取原文内容失败:{}", e.getMessage());
            }
        }
        return "";
    }

    /**
     * 根据文件基本信息创建txt文件路径
     *
     * @param fileInfo
     * @return
     */
    private Path buildOcrFilePath(FileInfo fileInfo) {
        String dir = commonFileConfig.getFullDirPath("ocr", "txt", null);
        return Paths.get(String.join(File.separator, dir, fileInfo.getBaseFileId() + ".txt"));
    }

    @Override
    public void afterPropertiesSet() {
        fileContentParsers = applicationContext.getBeansOfType(FileContentParser.class)
                .values()
                .stream()
                .sorted(OrderComparator.INSTANCE)
                .collect(Collectors.toList());
    }
}
