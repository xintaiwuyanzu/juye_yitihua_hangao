package com.dr.archive.utilization.search.service;

import com.dr.archive.manage.task.entity.SortField;
import com.dr.archive.model.to.SearchResultTo;
import com.dr.archive.utilization.search.to.SearchQuerysTo;
import com.dr.framework.common.form.core.model.FormData;

import java.io.IOException;
import java.util.List;

/**
 * 描述： 对es索引库操作接口
 * 操作索引数据
 *
 * @author tuzl
 * @author dr
 * @date 2020/7/23 10:09
 */
public interface EsDataService {
    /**
     * 原文内容key
     */
    String FILE_CONTENT_KEY = "CONTENT";
    /**
     * 搜索模板
     */
    String SEARCH_TEMPLATE_ID = "SEARCH_TEMPLATE";

    /**
     * 用模板查询
     *
     * @param searchQuerysTo
     * @param index
     * @param size
     * @return
     */
    SearchResultTo searchBykeyWord2(SearchQuerysTo searchQuerysTo, Integer index, Integer size);

    /**
     * 关键字查询，分词查询
     *
     * @param keyword
     * @param multiSearch 是否综合多条件查询
     * @param fond        全宗
     * @param category    分类
     * @param sortFields
     * @param page
     * @param size
     * @return
     */
    SearchResultTo searchByKeyword(String keyword, boolean multiSearch, String fond, String category, List<SortField> sortFields, Integer page, Integer size);

    /**
     * 添加数据
     *
     * @param categoryId
     * @param formDefinitionId
     * @param formData
     */
    void addIndexData(String categoryId, String formDefinitionId, FormData formData) throws IOException;

    /**
     * 删除数据
     *
     * @param formDefinitionId
     * @param dataId
     */
    void removeIndexData(String formDefinitionId, String dataId) throws IOException;

    /**
     * 上传时添加原文索引
     *
     * @param formDefinitionId
     * @param formDataId
     * @throws IOException
     */
    void updateContentData(String formDefinitionId, String formDataId) throws IOException;
}
