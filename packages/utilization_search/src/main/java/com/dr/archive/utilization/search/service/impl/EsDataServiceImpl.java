package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.task.entity.SortField;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.to.SearchResultTo;
import com.dr.archive.tag.service.TagLibArchiveService;
import com.dr.archive.util.JsonUtils;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.archive.utilization.search.service.SearchHistoryService;
import com.dr.archive.utilization.search.to.Querys;
import com.dr.archive.utilization.search.to.SearchQuerysTo;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/22 10:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EsDataServiceImpl extends AbstractEsHandler implements EsDataService {
    @Autowired
    ArchiveFileContentService fileContentService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    FondService fondService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ResourceManager resourceManager;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    SearchHistoryService searchHistoryService;
    @Autowired
    TagLibArchiveService tagLibArchiveService;

    @Override
    public SearchResultTo searchBykeyWord2(SearchQuerysTo searchQuerysTo, Integer index, Integer size) {
        SearchResultTo searchResultTo = new SearchResultTo();
        Person person = SecurityHolder.get().currentPerson();
        //查询当前登录人所有全宗
        List<Fond> fonds = (List<Fond>) resourceManager.getPersonResources(person.getId(), FondService.RESOURCE_TYPE_FOND);

        List<String> collect = fonds.stream().map(Fond::getCode).collect(Collectors.toList());

        List<String> fondId = fonds.stream().map(Fond::getId).collect(Collectors.toList());
        //查询当前登录人所有门类编码
        List<String> categories = commonMapper.selectByQuery(SqlQuery.from(Category.class, false).column(CategoryInfo.CODE).in(CategoryInfo.FONDID, fondId).setReturnClass(String.class));
        //创建模板查询对象
        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setScriptType(ScriptType.STORED);
        //设置要查询的索引
        request.setRequest(new SearchRequest("archive*"));
        request.setScript("SEARCH_TEMPLATE");
        //准备动态请求参数
        Map<String, Object> params = new HashMap<>();
        //分页参数
        params.put("from", index*size);
        params.put("size", size);

        List<Map<String, Object>> termList = new ArrayList<>();
        List<Querys> querysList = new ArrayList<>();
        try {
            querysList = JsonUtils.jsonToList(searchQuerysTo.getQuerysListJson(), Querys.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //多条件
        List<Map<String, Object>> boolList = getBoolList(querysList);
        params.put("boolList", boolList);

        if (searchQuerysTo.isOpenScope()) {
            Map<String, Object> termMap = new HashMap<>();
            termMap.put("field", "OPEN_SCOPE");
            termMap.put("values", "公开");
            termList.add(termMap);

        }
        //查询结果必须包含这些门类code
        if (StringUtils.hasText(searchQuerysTo.getCategory())) {
            String[] split = searchQuerysTo.getCategory().split(",");
            //根据一级门类编码获取所有子门类
            List<String> childrenCategorys = getChildrenCategorys(person, split);
            Map<String, Object> termMap = new HashMap<>();
            termMap.put("field", "CATE_GORY_CODE");
            termMap.put("values", childrenCategorys);
            termList.add(termMap);
        } else {
            Map<String, Object> termMap = new HashMap<>();
            termMap.put("field", "CATE_GORY_CODE");
            termMap.put("values", categories);
            termList.add(termMap);
        }
        Organise organise = SecurityHolder.get().currentOrganise();

        //判断当前登录人所属机构是档案馆还是档案室
        //TODO 先暂时去掉
/*        if (!StringUtils.isEmpty(organise) && organise.getOrganiseType().equals("dag")) {
            Map<String, Object> termMap = new HashMap<>();
            termMap.put("field", "ORGANISEID");
            termMap.put("values", organise.getId());
            termList.add(termMap);
        }*/
        //首页多选全宗编码
        if (StringUtils.hasText(searchQuerysTo.getFondCode())) {
            Map<String, Object> termMap2 = new HashMap<>();
            termMap2.put("field", "FOND_CODE");
            termMap2.put("values", searchQuerysTo.getFondCode().split(","));
            termList.add(termMap2);
        } else {
            //查询结果必须包含当前登陆人所有的全宗号
            Map<String, Object> termMap2 = new HashMap<>();
            termMap2.put("field", "FOND_CODE");
            termMap2.put("values", collect);
            termList.add(termMap2);
        }

        //判断是否点击分类查询
        if (!StringUtils.isEmpty(searchQuerysTo.getCategoryName())) {
            Map<String, Object> termMap = new HashMap<>();
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add(searchQuerysTo.getCategoryName());
            termMap.put("field", "CATEGORY_NAME");
            termMap.put("values", categoryNames);
            termList.add(termMap);
        }

        //判断是否点击标签查询
        if (!StringUtils.isEmpty(searchQuerysTo.getTagName())) {
            Map<String, Object> termMap = new HashMap<>();
            List<String> tagNames = new ArrayList<>();
            tagNames.add(searchQuerysTo.getTagName());
            termMap.put("field", "tag.name");
            termMap.put("values", tagNames);
            termList.add(termMap);
        }
        termList.get(termList.size() - 1).put("last", "true");
        params.put("termList", termList);

        //当前登录人所属全宗和所有门类为空的话不不进行查询
        if (collect.size() > 0 && categories.size() > 0) {
            request.setScriptParams(params);
            Set<String> highlight = new HashSet<>();
            String[] fields = {"TITLE", "DUTY_PERSON", "KEY_WORDS", "FOND_NAME", "CATEGORY_NAME", "ORG_CODE", "ARCHIVE_CODE", "ORG_NAME"};
            //遍历数组
            highlight.addAll(Arrays.asList(fields));
            try {
                /*MustacheFactory mf = new DefaultMustacheFactory();
                Mustache mustache = mf.compile("es/search_template.mustache");
                StringWriter writer = new StringWriter();
                mustache.execute(writer, params);
                System.out.println(writer.toString());*/
                SearchTemplateResponse response = client.searchTemplate(request, RequestOptions.DEFAULT);

                SearchHits hits = response.getResponse().getHits();

                ParsedStringTerms countTag = response.getResponse().getAggregations().get("tagCount");
                ParsedStringTerms countCateGoryCode = response.getResponse().getAggregations().get("cateGory");
                List<? extends Terms.Bucket> buckets = countCateGoryCode.getBuckets();
                List<? extends Terms.Bucket> buckets2 = countTag.getBuckets();


                List<Map<String, Object>> listData = new ArrayList<>();
                Map map = new HashMap();
                List list1 = new ArrayList();
                List list2 = new ArrayList();
                buckets.forEach(item -> {
                    Map map1 = new HashMap();
                    String key = (String) item.getKey();

                    ParsedValueCount count = item.getAggregations().get("countCateGoryCode");

                    double value1 = count.value();
                    map1.put("key", key);
                    map1.put("value", value1);
                    list1.add(map1);

                });

                buckets2.forEach(item -> {
                            Map map1 = new HashMap();
                            String key = (String) item.getKey();
                            //ParsedValueCount count = item.getAggregations().get("counttag");
                            // double d = count.value();
                            long aa = item.getDocCount();
                            map1.put("key", key);
                            map1.put("value", aa);
                            list2.add(map1);
                        }
                );

                map.put("count", list1);
                map.put("tagCount", list2);

                List<Map<String, Object>> resultData = mapSearchHits(hits.getHits(), highlight);

                map.put("data", resultData);
                listData.add(map);
                searchResultTo.setTotal(hits.getTotalHits().value);
                searchResultTo.setData(listData);
                searchResultTo.setSize(resultData.size());
                searchResultTo.setStart(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        searchHistoryService.addSearchHistory(searchQuerysTo.getQuerysListJson(), searchQuerysTo.isSecondRetrieval(), searchQuerysTo.getCategoryName(), searchQuerysTo.isOpenScope(), searchQuerysTo.getCategory(), person);
        return searchResultTo;
    }

    private List<String> getChildrenCategorys(Person person, String[] split) {
        List<Category> list = new ArrayList<>();
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(person.getId(), "fond");
        if (!CollectionUtils.isEmpty(fondList)) {
            Fond fond = fondList.get(0);
            for (String s : split) {
                Category categoryByCode = categoryService.findCategoryByCode(s, fond.getId());
                List<Category> allChildrenCategoryByParentId = categoryService.getAllChildrenCategoryByParentId(categoryByCode.getId());
                list.addAll(allChildrenCategoryByParentId);
            }
        }
        return !CollectionUtils.isEmpty(list) ? list.stream().map(Category::getCode).collect(Collectors.toList()) : new ArrayList<>();
    }

    private List<Map<String, Object>> getBoolList(List<Querys> seachQuerys) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Querys querys1 : seachQuerys) {
            Map<String, Object> bools = new HashMap<>();
            if (querys1.isMultiSearch()) {
                //高级检索
                List<HashMap> keyWordList = null;
                try {
                    keyWordList = JsonUtils.jsonToList(querys1.getKeyWords(), HashMap.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Map<String, String>> mustList = new ArrayList<>();
                List<Map<String, String>> shouldList = new ArrayList<>();
                for (Map map : keyWordList) {
                    Map<String, String> queryMap = new HashMap<>();
                    //type=must 精确匹配
                    if ("must".equals(map.get("type"))) {
                        queryMap.put("query", map.get("query").toString().toLowerCase()); //查询内容全部转为小写
                        queryMap.put("field", (String) map.get("field"));
                        queryMap.put("isPhrase", "true");
                    } else {
                        queryMap.put("query", map.get("query").toString().toLowerCase()); //查询内容全部转为小写
                        queryMap.put("field", (String) map.get("field"));
                    }
                    if ("OR".equals(querys1.getOperato())) { //或者 操作
                        shouldList.add(queryMap);
                    } else {
                        mustList.add(queryMap);
                    }
                }


                if (!mustList.isEmpty()) {
                    bools.put("isMust", true);
                    mustList.get(mustList.size() - 1).put("mustLast", "true"); //用于模板 列表尾部是否有,
                }
                if (!shouldList.isEmpty()) {
                    bools.put("isShould", true);
                    shouldList.get(shouldList.size() - 1).put("shouldLast", "true"); //用于模板 列表尾部是否有,
                }

                bools.put("shouldList", shouldList);
                bools.put("mustList", mustList);
            } else {
                //全文检索
                List<Map<String, String>> querys = new ArrayList<>();
                String[] kws = querys1.getKeyWords().split(" ");
                for (String kw : kws) {
                    Map<String, String> q = new HashMap<>();
                    q.put("query", kw.trim().toLowerCase()); //查询内容全部转为小写
                    querys.add(q);
                }
                querys.get(querys.size() - 1).put("querysLast", "true");
                bools.put("querys", querys);
                bools.put("query", true);
            }
            list.add(bools);
        }
        if (!list.isEmpty()) {
            list.get(list.size() - 1).put("last", "true");
        }
        return list;
    }

    @Override
    public SearchResultTo searchByKeyword(String keyword, boolean multiSearch, String fond, String category, List<SortField> sortFields, Integer page, Integer size) {
        //设置要查询的索引 archive*代表所有
        SearchRequest searchRequest = new SearchRequest("archive*");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //高亮字段
        Set<String> highlight = new HashSet<>();
        //定义高亮查询
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //判断是否多条件查询
        if (multiSearch) {
            //如果是多条件查询需要将json字符串转换为集合
            List<Map> list = null;
            try {
                list = objectMapper.readValue(keyword, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //should相当于OR must相当于AND
            for (Map map : list) {
                highlight.add(map.get("field").toString());
                if ("should".equals(map.get("type"))) {
                    MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(map.get("field").toString(), map.get("query").toString());
                    if ("AND".equals(map.get("operator"))) {
                        boolQueryBuilder.must(matchQueryBuilder);
                    } else if ("OR".equals(map.get("operator"))) {
                        boolQueryBuilder.should(matchQueryBuilder);
                    }
                } else if ("must".equals(map.get("type"))) {
                    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(map.get("field").toString(), map.get("query").toString());
                    if ("AND".equals(map.get("operator"))) {
                        boolQueryBuilder.must(termQueryBuilder);
                    } else if ("OR".equals(map.get("operator"))) {
                        boolQueryBuilder.should(termQueryBuilder);
                    }
                }
            }
        } else {
            String[] fields = {"TITLE", "DUTY_PERSON", "KEY_WORDS", "FOND_NAME", "CATEGORY_NAME", "ORG_CODE", "ARCHIVE_CODE", "ORG_NAME", "CONTENT"};
            //遍历数组
            for (String field : fields) {
                highlight.add(field);
                //matchQuery为模糊匹配
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, keyword);
                //should相当于OR
                boolQueryBuilder.should(matchQueryBuilder);
            }
        }
        //将需要高亮的字段放进highlightBuilder
        for (String field : highlight) {
            highlightBuilder.field(field);
        }
        //设置分页
        sourceBuilder.from(page);
        sourceBuilder.size(20);
        //创建搜索条件
        sourceBuilder.query(boolQueryBuilder);
        //设置高亮的前缀和后缀  会自动将关键词包住 false为多个字段高亮
        highlightBuilder.requireFieldMatch(false);
        /*highlightBuilder.preTags("<mark>");
        highlightBuilder.postTags("</mark>");*/
        highlightBuilder.preTags("<span style=\"-webkit-text-fill-color: red\">");
        highlightBuilder.postTags("</span>");
        searchRequest.source(sourceBuilder);
        //将高亮放进查询器
        sourceBuilder.highlighter(highlightBuilder);
        if (!StringUtils.isEmpty(sortFields)) {
            for (SortField sortField : sortFields) {
                if ("asc".equals(sortField.getOrder())) {
                    sourceBuilder.sort(sortField.getKey(), SortOrder.ASC);
                } else {
                    sourceBuilder.sort(sortField.getKey(), SortOrder.DESC);
                }
            }
        } else {
            //设置排序 _score是查询出数据的评分 匹配度越高评分越高
            sourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
        }
        //结果过滤，必须包含这些全宗
        if (fond.length() > 1) {
            sourceBuilder.postFilter(QueryBuilders.termsQuery("FOND_CODE", fond.split(",")));
        }
        SearchResultTo searchResultTo = new SearchResultTo();
        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            searchResultTo.setTotal(search.getHits().getTotalHits().value);
            searchResultTo.setData(mapSearchHits(search.getHits().getHits(), highlight));
            searchResultTo.setSize(mapSearchHits(search.getHits().getHits(), highlight).size());
            if (page == 0) {
                searchResultTo.setStart(1);
            } else {
                searchResultTo.setStart(page / size + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResultTo;
    }

    private List<Map<String, Object>> mapSearchHits(SearchHit[] hits, Set<String> highlight) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            for (String field : highlight) {
                HighlightField highlightField = highlightFields.get(field);
                if (highlightField != null) {
                    Text[] fragments = highlightField.getFragments();
                    StringBuilder value = new StringBuilder();
                    for (Text text : fragments) {
                        value.append(text);
                    }
                    sourceAsMap.put(field, value.toString());
                }
            }
            String con = sourceAsMap.get("CONTENT").toString();
            if (con.length() > 200) {
                sourceAsMap.put("CONTENT", con.substring(0, 200));
            }

            list.add(sourceAsMap);
        }
        return list;
    }

    @Override
    public void addIndexData(String categoryId, String formDefinitionId, FormData formData) throws IOException {
        //TODO 状态判断，只有档案库的数据才能添加全文搜索
        //TODO 新增原文索引，添加目录不会执行，在修改目录时会用到
        addFileContent(formData);
        String content = formData.get("CONTENT");
        content = content.replaceAll("\r", "");
        content = content.replaceAll("\n", "");
        formData.put("CONTENT", content);
        Category category = categoryService.selectById(categoryId);
        Fond fond = fondService.selectById(category.getFondId());
        //绑定关联数据
        formData.put("FOND_NAME", fond.getName());
        formData.put("FOND_CODE", fond.getCode());
        formData.put("CATE_GORY_CODE", category.getCode());
        formData.put("CATEGORY_NAME", category.getName());
        formData.put("formDefinitionId", formDefinitionId);
        //TODO 标签信息暂时没加
        IndexRequest request = new IndexRequest(indexName(formDefinitionId)).id(formData.getId()).source(formData);
        client.index(request, getRequestOptions());
    }

    @Override
    public void removeIndexData(String formDefinitionId, String dataId) throws IOException {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        String indexName = nameMaker.makeIndexName(formModel);
        DeleteRequest request = new DeleteRequest(indexName).id(dataId);
        client.delete(request, getRequestOptions());
    }

    /**
     * 从档案附件中提取文本并放到搜索引擎中
     *
     * @param data
     */
    private void addFileContent(FormData data) {
        data.put(FILE_CONTENT_KEY, fileContentService.getFileContentsByRefId(data.getId()));
    }

    @Async
    @Override
    public void updateContentData(String formDefinitionId, String formDataId) throws IOException {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        FormData formData = formDataService.selectOneFormData(formDefinitionId, formDataId);
        String content = fileContentService.getFileContentsByRefId(formData.getId());
        Fond fondTo = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fondTo.getId());
        formData.put("FOND_NAME", fondTo.getName());
        formData.put("CATEGORY_NAME", category.getName());
        formData.put(EsDataService.FILE_CONTENT_KEY, content);
        IndexRequest request = new IndexRequest(indexName(formModel)).id(formData.getId()).source(formData);
        client.index(request, getRequestOptions());
    }
}
