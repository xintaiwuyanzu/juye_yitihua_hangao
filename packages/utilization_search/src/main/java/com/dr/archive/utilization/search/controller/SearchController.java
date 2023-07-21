package com.dr.archive.utilization.search.controller;

import com.dr.archive.enums.FilesField;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.task.entity.SortField;
import com.dr.archive.model.to.SearchResultTo;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.archive.utilization.search.to.SearchQuerysTo;
import com.dr.archive.utils.PdfHelper;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.service.ResourceManager;
import com.dr.framework.core.web.annotations.Current;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/8/11 14:55
 */
@RestController
@RequestMapping("api/search")
public class SearchController {
    @Autowired
    EsDataService esHandleService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CommonFileService fileService;
    @Autowired
    ArchiveFileContentService fileContentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ResourceManager resourceManager;

    @RequestMapping("/searchBykeyWords")
    public ResultEntity searchBykeyWord(SearchQuerysTo searchQuerysTo,
                                        @RequestParam(value = "index", defaultValue = "0") Integer index,
                                        @RequestParam(value = "size", defaultValue = "25") Integer size) {

        return ResultEntity.success(esHandleService.searchBykeyWord2(searchQuerysTo, index, size));
    }

    /**
     * 功能描述: 关键字全文搜索档案及附件
     *
     * @param keyword 搜索关键字
     * @param index
     * @param size
     * @return : {@link ResultEntity}
     * @author : tzl
     * @date : 2020/7/23 14:47
     */
    @RequestMapping("/searchByKeyword")
    public ResultEntity searchByKeyword(String keyword,
                                        //是否综合多条件查询
                                        boolean multiSearch,
                                        String operato,
                                        String sort,
                                        String fond,
                                        String cateGory,
                                        @RequestParam(defaultValue = "1") Integer index,
                                        @RequestParam(defaultValue = "50") Integer size) {
        List<SortField> sortFields = Collections.emptyList();
        try {
            sortFields = objectMapper.readValue(sort, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SortField.class));
            if (multiSearch) {
                List<Map> list = objectMapper.readValue(keyword, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
                for (Map map : list) {
                    map.put("operator", operato);
                }
            }
        } catch (JsonProcessingException ignored) {
        }
        SearchResultTo searchResultTo = esHandleService.searchByKeyword(keyword, multiSearch, fond, cateGory, sortFields, (index - 1) * 20, size);
        return ResultEntity.success(searchResultTo);
    }

    @RequestMapping("/getFieldCheck")
    public ResultEntity getFieldCheck(@RequestParam(defaultValue = "false") Boolean fond) {
        FilesField[] values = FilesField.values();
        List<Map> list = new ArrayList<>();
        for (FilesField value : values) {
            Map<String, String> map = new HashMap<>();
            if (!fond && "QUANZONG".equals(value.getCode())) {

            } else {
                map.put("code", value.getCode());
                map.put("name", value.getName());
                list.add(map);
            }

        }
        return ResultEntity.success(list);
    }

    @RequestMapping(value = "/highLightKeyWord")
    public void highLightKeyWord(String id, String keyword, HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(keyword)) {
            FileInfo fileInfo = fileService.fileInfo(id);
            try (InputStream inputStream = fileService.fileStream(fileInfo.getId())) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PdfHelper.highLightKeyWord(inputStream, keyword, byteArrayOutputStream);

                StreamUtils.copy(byteArrayOutputStream.toByteArray(), response.getOutputStream());
            } catch (Exception e) {
                StreamUtils.copy(fileService.fileStream(fileInfo.getId()), response.getOutputStream());
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/getFileContent")
    public ResultEntity getFileContent(String refId) {
        return ResultEntity.success(fileContentService.getFileContentsByRefId(refId));
    }

    /**
     * 根据当前登录人全宗权限，查询一级门类
     *
     * @param person
     * @return
     */
    @RequestMapping("/getCategorys")
    public ResultEntity getCategorys(@Current Person person) {
        SqlQuery<Category> sqlQuery = SqlQuery.from(Category.class, false)
                .column(CategoryInfo.NAME, CategoryInfo.CODE, CategoryInfo.ORDERBY)
                .equal(CategoryInfo.PARENTID, CategoryInfo.FONDID);
        //查询当前登录人全宗权限
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(person.getId(), "fond");
        List<Category> categories = new ArrayList<>();
        //不为空
        if (!CollectionUtils.isEmpty(fondList)) {
            List<String> collect = fondList.stream().map(Fond::getId).collect(Collectors.toList());
            sqlQuery.in(CategoryInfo.FONDID, collect);
            //查询所有全宗的一级门类
            categories = commonMapper.selectByQuery(sqlQuery);
        }
        //全宗为空，返回空
        return ResultEntity.success(categories);
    }

    @RequestMapping("/getFonds")
    public ResultEntity getFonds() {
        List<Fond> fondList = commonMapper.selectByQuery(SqlQuery.from(Fond.class));
        return ResultEntity.success(fondList);
    }
}
