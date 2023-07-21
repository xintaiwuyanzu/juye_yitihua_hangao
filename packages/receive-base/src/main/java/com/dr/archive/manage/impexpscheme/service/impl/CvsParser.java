package com.dr.archive.manage.impexpscheme.service.impl;

import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * 借助fastxml实现数据解析，对应json和xml
 *
 * @author dr
 */
@Component
public class CvsParser extends AbstractObjectMapperDataParser {
    final MediaType mediaType = MediaType.parseMediaType("text/csv");

    @Override
    public String getFileSuffix(String mine) {
        return "csv";
    }

    @Override
    public boolean canHandle(String mine) {
        return mediaType.includes(MediaType.parseMediaType(mine));
    }

    @Override
    protected Jackson2ObjectMapperBuilder createJacksonBuilder() {
        return new Jackson2ObjectMapperBuilder().factory(new CsvFactory());
    }
}
