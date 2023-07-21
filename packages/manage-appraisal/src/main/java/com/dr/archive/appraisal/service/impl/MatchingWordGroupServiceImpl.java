package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.*;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MatchingWordGroupServiceImpl extends DefaultBaseService<MatchingWordGroup> implements MatchingWordGroupService {

    @Override
    public List<Map> getAllMatchingWordGroup(String toBeAppraisalId) {
        List<Map> mapList = new ArrayList();
        List<MatchingWordGroup> matchingWordGroupList = getMatchingWordGroup(toBeAppraisalId);
        for(MatchingWordGroup matchingWordGroup:matchingWordGroupList){
            Map map = new HashMap();
            map.put("id",matchingWordGroup.getId());
            map.put("wordGroupName",matchingWordGroup.getWordGroupName());
            map.put("wordGroupResult",matchingWordGroup.getWordGroupResult());
            map.put("order",matchingWordGroup.getOrder());
            map.put("children",getMatchingKeyWord(matchingWordGroup.getId()));
            mapList.add(map);
        }
        return mapList;
    }

    public List<Map> getMatchingKeyWord(String matchingWordGroupId) {
        SqlQuery sqlQuery = SqlQuery.from(MatchingKeyWord.class)
                .equal(MatchingKeyWordInfo.MATCHINGWORDGROUPID,matchingWordGroupId);
        List<MatchingKeyWord> matchingKeyWordList = commonMapper.selectByQuery(sqlQuery);
        List<Map> mapList = new ArrayList();
        for(MatchingKeyWord matchingKeyWord:matchingKeyWordList){
            Map map = new HashMap();
            map.put("id",matchingKeyWord.getId());
            map.put("wordGroupName",matchingKeyWord.getKeywords());
            map.put("wordGroupResult","");
            map.put("order","");
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public List<MatchingWordGroup> getMatchingWordGroup(String toBeAppraisalId) {
        SqlQuery sqlQuery = SqlQuery.from(MatchingWordGroup.class)
                .equal(MatchingWordGroupInfo.TOBEAPPRAISALID,toBeAppraisalId);
        return commonMapper.selectByQuery(sqlQuery);
    }
}
