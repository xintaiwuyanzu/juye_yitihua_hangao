package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.FileContentParser;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 读取text文本
 *
 * @author dr
 */
@Component
public class TextFileContentParser implements FileContentParser {
    final MediaType mediaType = new MediaType("text");

    @Override
    public boolean accept(MediaType mediaType) {
        return this.mediaType.includes(mediaType);
    }

    @Override
    public String read(InputStream stream) throws IOException {
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }
}
