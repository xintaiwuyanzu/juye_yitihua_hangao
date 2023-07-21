package com.dr.archive.tag.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.tag.TagConfig;
import com.dr.archive.tag.entity.Tag;
import com.dr.archive.tag.service.TagService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    RestHighLevelClient client;
    @Autowired
    TagConfig tagConfig;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void insertTag(String text, Integer count) {

        List<Fond> fonds = commonMapper.selectByQuery(SqlQuery.from(Fond.class).equal(FondInfo.ENABLED, StatusEntity.STATUS_ENABLE));

        for (Fond fond : fonds) {
            List<Map> list = commonMapper.selectByQuery(SqlQuery.from(Category.class).join(CategoryInfo.ID, CategoryConfigInfo.BUSINESSID).column(CategoryConfigInfo.ARCFORMID, CategoryConfigInfo.FILEFORMID).equal(CategoryInfo.FONDID, fond.getId()).setReturnClass(Map.class));

            for (Map map : list) {
                if (!StringUtils.isEmpty(map.get("arcFormId"))) {
                    getContent(map.get("arcFormId").toString(), fond.getCode(), map.get("code").toString(), fond.getId(), map.get("id").toString());

                }
                if (!StringUtils.isEmpty(map.get("fileFormId"))) {
                    getContent(map.get("fileFormId").toString(), fond.getCode(), map.get("code").toString(), fond.getId(), map.get("id").toString());
                }
            }
        }
    }

    /**
     * 查询档案原文数据
     *
     * @param formDefinitionId
     * @param fondCode
     * @param cateGroyCode
     * @param fondId
     * @param cateGroyId
     */
    public void getContent(String formDefinitionId, String fondCode, String cateGroyCode, String fondId, String cateGroyId) {

        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);

        Relation tableInfo = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        if (null != tableInfo) {
            SqlQuery<String> from = SqlQuery.from(tableInfo, false).column(tableInfo.getColumn("id")).equal(tableInfo.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE), cateGroyCode).equal(tableInfo.getColumn(ArchiveEntity.COLUMN_FOND_CODE), fondCode).setReturnClass(String.class);

            List<String> list1 = commonMapper.selectByQuery(from);

            for (String formDataId : list1) {

                String content = searchByFormDataId(formDataId, formModel.getFormCode());

                if (!StringUtils.isEmpty(content)) {

                    addTag(content, formDataId, formDefinitionId, fondCode, cateGroyCode, fondId, cateGroyId);
                }
            }
        }
    }

    /**
     * 从全文检索查询数据
     *
     * @param formDataId
     * @param index
     * @return
     */
    public String searchByFormDataId(String formDataId, String index) {

        SearchRequest searchRequest = new SearchRequest("archive*");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        TermQueryBuilder id = QueryBuilders.termQuery("id", formDataId);

        boolQueryBuilder.should(id);
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1);
        searchRequest.source(sourceBuilder);
        sourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
        List<Map<String, Object>> list = new ArrayList();
        try {

            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] hits = search.getHits().getHits();

            for (SearchHit searchHit : hits) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                list.add(sourceAsMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0 && list.get(0).containsKey("CONTENT")) {
            return list.get(0).get("CONTENT").toString();
        } else {
            return "";
        }
    }

    /**
     * 添加标签进数据库
     *
     * @param text
     * @param formDataId
     * @param formDefinitionId
     * @param fondCode
     * @param cateGroyCode
     * @param fondId
     * @param cateGroyId
     */
    public void addTag(String text, String formDataId, String formDefinitionId, String fondCode, String cateGroyCode, String fondId, String cateGroyId) {

        List<String> types = new ArrayList<>();

        types.add("PRE");
        types.add("LOC");
        types.add("ORG");
        types.add("TIME");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("text", text);

        params.add("remove_stop_words", "a");

        try {
            JsonNode tagNode = objectMapper.readTree(sendPostRequest(tagConfig.getTagword(), params));
            List<Map<String, String>> list = objectMapper.readValue(tagNode.get("data").traverse(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
            List<Map<String, String>> type1 = list.stream().filter(entry -> !entry.get("type").isEmpty()).collect(Collectors.toList());

            for (String type : types) {

                List<Map<String, String>> contains = contains(type1, type);

                for (Map<String, String> map : contains) {
                    Tag tag = new Tag();
                    tag.setTag(map.get("name").trim().replaceAll("\r\n", ""));
                    tag.setType(map.get("type"));
                    tag.setFondId(fondId);
                    tag.setFondCode(fondCode);
                    tag.setCateGoryId(cateGroyId);
                    tag.setCateGoryCode(cateGroyCode);
                    tag.setFormDefinitionId(formDefinitionId);
                    tag.setFormDataId(formDataId);
                    CommonService.bindCreateInfo(tag);
                    commonMapper.insert(tag);

                }
            }
            JsonNode jsonNode = objectMapper.readTree(sendPostRequest(tagConfig.getTagkwords(), params));
            Map<String, List<String>> data = objectMapper.readValue(
                    jsonNode.get("data").traverse(),
                    objectMapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            objectMapper.getTypeFactory().constructType(String.class),
                            objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
                    )
            );
            if (data.size() > 0) {
                for (String keywords : data.get("keywords")) {
                    Tag tag = new Tag();
                    tag.setTag(keywords);
                    tag.setFondId(fondId);
                    tag.setFondCode(fondCode);
                    tag.setCateGoryId(cateGroyId);
                    tag.setCateGoryCode(cateGroyCode);
                    tag.setFormDefinitionId(formDefinitionId);
                    tag.setFormDataId(formDataId);
                    CommonService.bindCreateInfo(tag);
                    commonMapper.insert(tag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用实体识别词性分析，关键词提取接口
     *
     * @param url
     * @param params
     * @return
     */
    public String sendPostRequest(String url, MultiValueMap<String, String> params) {

        return restTemplate.postForEntity(url, params, String.class).getBody();

    }

    /**
     * 根据词性类型对带有词性的关键词进行处理
     *
     * @param list
     * @param type
     * @return
     */
    public List<Map<String, String>> contains(List<Map<String, String>> list, String type) {

        //筛选出某一种词性的关键词并去重
        List<Map<String, String>> type1 = list.stream()
                .filter(item -> type.contains(item.get("type")))
                .distinct()
                .sorted((map1, map2) -> {
                    Integer value1 = map1.get("name").length();
                    Integer value2 = map2.get("name").length();
                    return value2.compareTo(value1);
                })
                .collect(Collectors.toList());

        //对关键词进行排序

        List<Integer> list1 = new ArrayList<>();

        //筛选关键词
        for (int i = 0; i < type1.size() - 1; i++) {

            for (int j = 1; j < type1.size(); j++) {

                if (type1.get(i).get("name").startsWith(type1.get(j).get("name"))) {

                    list1.add(j);

                }
            }
        }
        if (list1.size() > 0) {
            List<Integer> collect = list1.stream()
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            for (Integer index : collect) {
                type1.remove(type1.get(index));
            }
        }
        return type1;
    }

    /*
     * 实体识别和词性分析*/
    @Override
    public List getWord(String text) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("text", text);

        params.add("remove_stop_words", "a");
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JsonNode tagNode = objectMapper.readTree(sendPostRequest(tagConfig.getTagword(), params));
            list = objectMapper.readValue(tagNode.get("data").traverse(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
            //去掉重复数据
            list = list.stream().distinct().collect(Collectors.toList());
        } catch (Exception ignored) {

        }
        return list;
    }

    @Override
    public List getKeywords(String text) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("text", text);

        params.add("count", "10");
        List<String> list = new ArrayList<>();
        try {
            JsonNode tagNode = objectMapper.readTree(sendPostRequest(tagConfig.getTagkwords(), params));
            Map<String, List<String>> data = objectMapper.readValue(
                    tagNode.get("data").traverse(),
                    objectMapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            objectMapper.getTypeFactory().constructType(String.class),
                            objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
                    )
            );
            list = data.get("keywords");

        } catch (Exception ignored) {

        }
        return list;
    }

    @Override
    public List getKeysentences(String text) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("text", text);

        params.add("count", "10");
        List<String> list = new ArrayList<>();
        try {
            JsonNode tagNode = objectMapper.readTree(sendPostRequest(tagConfig.getKeysentences(), params));
            Map<String, List<String>> data = objectMapper.readValue(
                    tagNode.get("data").traverse(),
                    objectMapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            objectMapper.getTypeFactory().constructType(String.class),
                            objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
                    )
            );
            list = data.get("keySentences");
        } catch (Exception ignored) {

        }
        return list;
    }

}
