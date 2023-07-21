package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.FileContentParser;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.MediaType.APPLICATION_PDF;

/**
 * 读取text文本
 *
 * @author dr
 */
@Component
public class PdfFileContentParser implements FileContentParser {
    @Override
    public boolean accept(MediaType mediaType) {
        return APPLICATION_PDF.includes(mediaType);
    }

    @Override
    public String read(InputStream stream) throws IOException {
        return doReadText(stream);
    }

    private String doReadText(InputStream stream) throws IOException {
        try (PdfReader pdfReader = new PdfReader(stream)) {
            PdfDocument document = new PdfDocument(pdfReader);
            // 获取页码
            int pages = document.getNumberOfPages();
            String[] contents = new String[pages];
            for (int i = 1; i <= pages; i++) {
                contents[i - 1] = PdfTextExtractor.getTextFromPage(document.getPage(i));
            }
            return String.join("", contents);
        }
    }


}
