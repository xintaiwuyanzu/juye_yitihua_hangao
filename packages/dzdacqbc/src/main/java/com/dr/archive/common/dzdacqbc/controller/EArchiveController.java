package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子档案长期保存档案管理
 *
 * @author hyj
 */
@RestController
@RequestMapping("/api/earchive")
public class EArchiveController extends BaseServiceController<EArchiveService, EArchive> {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveDataManager dataManager;

    @RequestMapping(value = "/updateStatusByQuery")
    @SysLog("档案状态信息修改")
    public ResultEntity updateStatusByQuery(@RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                            ArchiveDataQuery query,
                                            String status) {
        query.parseQuery(queryContent);
        dataManager.updateStatusByQuery(query, status);
        service.addEArchive(query);
        return ResultEntity.success();
    }

    @Override
    protected SqlQuery<EArchive> buildPageQuery(HttpServletRequest request, EArchive entity) {
        SqlQuery sqlQuery = SqlQuery.from(EArchive.class);
        if(StringUtils.isEmpty(entity.getFondCode()) && StringUtils.isEmpty(entity.getCategoryCode())){
            return null;
        }
        sqlQuery.like(EArchiveInfo.TITLE, entity.getTitle())
                .like(EArchiveInfo.YEAR, entity.getYear())
                .like(EArchiveInfo.ARCHIVECODE, entity.getArchiveCode())
                .equal(EArchiveInfo.STATUS, entity.getStatus())
                .equal(EArchiveInfo.CATEGORYCODE, entity.getCategoryCode())
                .equal(EArchiveInfo.FONDCODE, entity.getFondCode())
                .equal(EArchiveInfo.SPACEID,entity.getSpaceId());
        if(StringUtils.hasText(entity.getAjdh()) && "all".equals(entity.getAjdh())){
            sqlQuery.isNotNull(EArchiveInfo.ISJNWJ);
        }else if(StringUtils.hasText(entity.getAjdh())){
            sqlQuery.equal(EArchiveInfo.AJDH, entity.getAjdh());
        }else{
            sqlQuery.isNull(EArchiveInfo.ISJNWJ);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        if (null != organise) {
            if ("dag".equals(organise.getOrganiseType())) {
                sqlQuery.equal(EArchiveInfo.ORGANISEID, organise.getId());
            }
        }
        sqlQuery.orderBy(EArchiveInfo.ARCHIVECODE)
                .orderByDesc(EArchiveInfo.CREATEDATE);

        return sqlQuery;
    }

    @RequestMapping("pageForCar")
    public ResultEntity pageForCar(@RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, EArchive entity) {
        SqlQuery sqlQuery = SqlQuery.from(EArchive.class)
                .like(EArchiveInfo.TITLE, entity.getTitle())
                .like(EArchiveInfo.YEAR, entity.getYear())
                .equal(EArchiveInfo.STATUS, "CAR")
                .orderBy(EArchiveInfo.ARCHIVECODE);
        return ResultEntity.success(commonMapper.selectPageByQuery(sqlQuery, pageIndex, pageSize));
    }

    @RequestMapping("/countArchive")
    public ResultEntity countArchive() {
        return ResultEntity.success(service.countArchive());
    }

    @RequestMapping("/countArchiveByClassificationId")
    public ResultEntity countArchiveByClassificationId(){
        SqlQuery<Map> problemNum = SqlQuery.from(EArchive.class, false)
                .count(EArchiveInfo.ID, "problemNum")
                .column(EArchiveInfo.CLASSIFICATIONID)
                //档案状态为1就是有问题
                .equal(EArchiveInfo.ARCHIVESTATUS,"1")
                .groupBy(EArchiveInfo.CLASSIFICATIONID).setReturnClass(Map.class);
        List<Map> maps = commonMapper.selectByQuery(problemNum);
        List<Map<String,String>> archives = new ArrayList<>();
        maps.forEach(i->{
            Map<String, String> map = new HashMap<>();
            map.put("problemNumMap", i.get("problemNum").toString());
            if(i.get("classificationId")!=null){
                map.put("id",i.get("classificationId").toString());
            }
            archives.add(map);
        });
        return ResultEntity.success(archives);
    }


}
