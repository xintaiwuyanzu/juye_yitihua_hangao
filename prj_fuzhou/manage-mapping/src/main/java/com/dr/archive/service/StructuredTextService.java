package com.dr.archive.service;

import com.dr.archive.entity.StructuredText;
import com.dr.framework.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: yang
 * @create: 2022-06-03 15:25
 **/
public interface StructuredTextService extends BaseService<StructuredText> {
    String upload(MultipartFile file);

    List<String> excelTitle(String id, int type);

    long findExcelText(String result, String id, int type);

    void loadData() throws IOException;

    long saveRelationResult(String result, int type);

    void download(String fileId, HttpServletResponse response);
}
