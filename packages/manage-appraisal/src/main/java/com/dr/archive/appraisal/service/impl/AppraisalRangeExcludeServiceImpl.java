package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.AppraisalRangeExclude;
import com.dr.archive.appraisal.entity.AppraisalRangeExcludeInfo;
import com.dr.archive.appraisal.service.AppraisalRangeExcludeService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppraisalRangeExcludeServiceImpl extends DefaultBaseService<AppraisalRangeExclude> implements AppraisalRangeExcludeService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    FondService fondService;


    @Override
    public List<AppraisalRangeExclude> getAppraisalRangeExcludeByOrgId(String orgId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalRangeExclude.class)
                                    .equal(AppraisalRangeExcludeInfo.ORGID,orgId);
        return selectList(sqlQuery);
    }

    @Override
    public Map<String, List> getAppraisalRangeExcludeMapByOrgId(String orgId) {
        Map<String, List> excludeMap = new HashMap();
        List<AppraisalRangeExclude> appraisalRangeExcludeList = getAppraisalRangeExcludeByOrgId(orgId);
        for (AppraisalRangeExclude appraisalRangeExclude:appraisalRangeExcludeList){
            Fond fond = fondService.selectById(appraisalRangeExclude.getFondId());
            List<Category> categoryList = categoryService.getAllChildrenCategoryByParentId(appraisalRangeExclude.getCategoryId());
            for(Category category:categoryList){
                excludeMap.put(fond.getCode()+"_"+category.getCode()+"_"+orgId,null);
            }
        }
        return excludeMap;
    }
}
