package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.FileContentParser;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.ooxml.POIXMLRelation;
import org.apache.poi.ooxml.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XSLFRelation;
import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 借助poi读取office系列文件内容
 *
 * @author dr
 */
@Component
public class OfficeFileContentParser implements FileContentParser, InitializingBean {
    final List<MediaType> mediaTypeList = new ArrayList<>();

    @Override
    public boolean accept(MediaType mediaType) {
        for (MediaType type : mediaTypeList) {
            if (type.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String read(InputStream stream) throws IOException {
        try (POITextExtractor extractor = ExtractorFactory.createExtractor(stream)) {
            return extractor.getText();
        } catch (XmlException | OpenXML4JException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void afterPropertiesSet() {
        addMediaTypes(XSSFExcelExtractor.SUPPORTED_TYPES);
        addMediaTypes(XWPFWordExtractor.SUPPORTED_TYPES);
        addMediaTypes(XSLFPowerPointExtractor.SUPPORTED_TYPES);
        addMediaTypes(XSSFBEventBasedExcelExtractor.SUPPORTED_TYPES);
        addMediaTypes(XSLFRelation.THEME_MANAGER);
    }

    private void addMediaTypes(POIXMLRelation... relations) {
        for (POIXMLRelation relation : relations) {
            mediaTypeList.add(MediaType.parseMediaType(relation.getContentType()));
        }
    }
}
