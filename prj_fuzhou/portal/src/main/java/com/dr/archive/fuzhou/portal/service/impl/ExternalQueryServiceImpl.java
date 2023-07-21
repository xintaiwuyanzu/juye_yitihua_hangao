package com.dr.archive.fuzhou.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dr.archive.fuzhou.portal.service.ExternalQueryService;
import com.dr.archive.model.to.SearchResultTo;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service
public class ExternalQueryServiceImpl implements ExternalQueryService {

    @Autowired
    RestHighLevelClient client;

    @Override
    public SearchResultTo advancedRetrieval(String keyWord, String type, String orgId, String operato,
                                            boolean multiSearch, Integer index, Integer size) {
        //设置要查询的索引
        SearchRequest searchRequest = new SearchRequest("archive*");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //高亮字段
        Set<String> highlight = new HashSet<>();
        //定义高亮查询
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        if (multiSearch) {
            List<Map> keyWordList = JSONObject.parseArray(keyWord, Map.class);
            if(keyWordList.size() == 1){
                for (Map map : keyWordList) {
                    highlight.add(map.get("field").toString());
                    if ("must".equals(map.get("type"))) {
                        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(map.get("field").toString(), map.get("query").toString());
                        boolQueryBuilder.must(matchPhraseQueryBuilder);
                    } else {
                        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(map.get("field").toString(), map.get("query").toString());
                        boolQueryBuilder.must(matchQueryBuilder);
                    }
                }
            }else{
                if ("OR".equals(operato)) {
                    for (Map map : keyWordList) {
                        highlight.add(map.get("field").toString());
                        if ("must".equals(map.get("type"))) {
                            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(map.get("field").toString(), map.get("query").toString());
                            boolQueryBuilder.should(matchPhraseQueryBuilder);
                        } else {
                            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(map.get("field").toString(), map.get("query").toString());
                            boolQueryBuilder.should(matchQueryBuilder);
                        }
                    }
                } else {
                    for (Map map : keyWordList) {
                        if ("must".equals(map.get("type"))) {
                            highlight.add(map.get("field").toString());
                            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(map.get("field").toString(), map.get("query").toString());
                            boolQueryBuilder.must(matchPhraseQueryBuilder);
                        } else {
                            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(map.get("field").toString(), map.get("query").toString());
                            boolQueryBuilder.must(matchQueryBuilder);
                        }
                    }
                }
            }
        } else {
            if (!StringUtils.isEmpty(keyWord)) {
                highlight.add("TITLE");
                //不分词查询
                MatchPhraseQueryBuilder title = QueryBuilders.matchPhraseQuery("TITLE", keyWord);
                //MatchQueryBuilder title = QueryBuilders.matchQuery("TITLE", keyWord);
                boolQueryBuilder.must(title);
            }
        }

        for (String fields : highlight) {
            highlightBuilder.field(fields);
        }
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color: red\">");
        highlightBuilder.postTags("</span>");
        //将高亮放进查询器
        sourceBuilder.highlighter(highlightBuilder);
        TermQueryBuilder openScope = QueryBuilders.termQuery("OPEN_SCOPE.keyword", "开放");
        boolQueryBuilder.must(openScope);
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(index);
        sourceBuilder.size(size);
        searchRequest.source(sourceBuilder);
        SearchResultTo searchResultTo = new SearchResultTo();
        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            List<Map<String, Object>> maps = mapSearchHits(search.getHits().getHits(), highlight);
            searchResultTo.setData(maps);
            searchResultTo.setTotal(search.getHits().getTotalHits().value);
            searchResultTo.setSize(maps.size());
            searchResultTo.setStart(index);
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
            list.add(sourceAsMap);
        }
        return list;
    }

    private List<Map<String, Object>> mapSearchHits(SearchHit[] hits) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            if (sourceAsMap.containsKey("CONTENT")) {
                if (sourceAsMap.get("CONTENT").toString().length() > 150) {
                    sourceAsMap.put("CONTENT", sourceAsMap.get("CONTENT").toString().substring(0, 150));
                }
            }
            list.add(sourceAsMap);
        }
        return list;
    }
}