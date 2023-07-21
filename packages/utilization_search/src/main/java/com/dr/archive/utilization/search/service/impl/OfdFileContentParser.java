package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.FileContentParser;
import org.ofdrw.reader.ContentExtractor;
import org.ofdrw.reader.OFDReader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 读取text文本
 * TODO ofd
 *
 * @author dr
 */
@Component
public class OfdFileContentParser implements FileContentParser {
    final MediaType mediaType = new MediaType("application", "zip");
    final String suffix = "ofd";

    static {
        //设置ofd文件最大大小，TODO，这里应该处理zip炸弹的情况，防止解压文件后文件过大的情况
        OFDReader.setZipFileMaxSize(800 * 1024 * 1024);
    }

    @Override
    public boolean accept(MediaType mediaType) {
        return this.mediaType.includes(mediaType);
    }

    @Override
    public boolean accept(MediaType mediaType, String suffix) {
//        return this.accept(mediaType) && mediaType.getSubtype().equalsIgnoreCase(suffix);
        return this.accept(mediaType) && this.suffix.equalsIgnoreCase(suffix);
    }

    @Override
    public String read(InputStream stream) throws IOException {
        try (OFDReader reader = new OFDReader(stream)) {
            ContentExtractor extractor = new ContentExtractor(reader);
            List<String> context = extractor.extractAll();
            return String.join("", context);
        }
    }

    /**
     * 这个排序稍微考前一点
     *
     * @return
     */
    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
