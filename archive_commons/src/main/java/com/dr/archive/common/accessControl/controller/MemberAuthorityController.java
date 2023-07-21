package com.dr.archive.common.accessControl.controller;

import com.dr.archive.common.accessControl.entity.MemberAuthority;
import com.dr.archive.common.accessControl.entity.MemberAuthorityInfo;
import com.dr.archive.common.accessControl.service.MemberAuthorityService;
import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveInfo;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.statistics.entity.ResourceStatistics;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:32
 */
@RestController
@RequestMapping("/api/member_authority")
public class MemberAuthorityController extends BaseServiceController<MemberAuthorityService, MemberAuthority> {
    @Autowired
    MemberAuthorityService memberAuthorityService;

    @Autowired
    ResourceManager resourceManager;

    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    FondService fondService;

    @Autowired
    CategoryService categoryService;
    @Override
    protected SqlQuery<MemberAuthority> buildPageQuery(HttpServletRequest httpServletRequest, MemberAuthority memberAuthority) {
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
        List<String> fondCodeList = fondList.stream().map(Fond::getCode).collect(Collectors.toList());
        SqlQuery query = SqlQuery.from(MemberAuthority.class);
//        query.in(MemberAuthorityInfo.FONDCODE, fondCodeList);
        return query;
    }

    @RequestMapping("/insertAuthority")
    public ResultEntity<MemberAuthority> insertAuthority(HttpServletRequest request, MemberAuthority entity) {
        memberAuthorityService.setEntity(entity);
        return ResultEntity.success();
    }
    @RequestMapping("/getCategoryByFondId")
    public ResultEntity getCategoryByFondId(String fondId) {
        if (StringUtils.hasText(fondId)) {
            List<Category> categoryByFondId = categoryService.getCategoryByFondId(fondId);
            return ResultEntity.success(categoryByFondId);
        }
        return ResultEntity.error("全宗不能为空");
    }


}
