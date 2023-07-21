package com.dr.archive.manage.dict.controller;

import com.dr.archive.manage.dict.entity.ArchiveDictGroup;
import com.dr.archive.manage.dict.entity.ArchiveDictGroupInfo;
import com.dr.archive.manage.dict.service.DictGroupService;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/2 17:40
 */
@RestController
@ResponseBody
@RequestMapping("api/manage/dictgroup")
public class DictGroupController extends BaseServiceController<DictGroupService, ArchiveDictGroup> {


    @Override
    protected SqlQuery<ArchiveDictGroup> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveDictGroup archiveDictGroup) {
        SqlQuery<ArchiveDictGroup> sqlQuery = SqlQuery.from(ArchiveDictGroup.class);
        if (!StringUtils.isEmpty(archiveDictGroup.getName())) {
            sqlQuery.like(ArchiveDictGroupInfo.NAME, archiveDictGroup.getName());
        }
        return sqlQuery;
    }

    @Override
    @SysLog("删除字典组")
    public ResultEntity<Boolean> delete(HttpServletRequest request, ArchiveDictGroup entity) {
        return ResultEntity.success(service.deleteByIds(request.getParameter("aIds")) > 0);
    }
}
