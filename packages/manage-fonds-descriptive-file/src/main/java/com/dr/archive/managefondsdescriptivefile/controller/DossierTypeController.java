package com.dr.archive.managefondsdescriptivefile.controller;

import com.dr.archive.managefondsdescriptivefile.entity.DossierType;
import com.dr.archive.managefondsdescriptivefile.entity.DossierTypeInfo;
import com.dr.archive.managefondsdescriptivefile.service.DossierTypeService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/dossierType")
public class DossierTypeController extends BaseServiceController<DossierTypeService, DossierType> {
    @Override
    protected SqlQuery<DossierType> buildPageQuery(HttpServletRequest request, DossierType entity) {
        return SqlQuery.from(DossierType.class).equal(DossierTypeInfo.BUSINESSID, entity.getBusinessId()).orderBy(DossierTypeInfo.ORDERBY);
    }

    /**
     * 根据父类id查询该父类下所有分类
     *
     * @param dossierType1
     * @return
     */
    @RequestMapping("/dossierById")
    public ResultEntity dossierById(DossierType dossierType1) {
        SqlQuery<DossierType> dossierTypeSqlQuery = SqlQuery.from(DossierType.class).equal(DossierTypeInfo.BUSINESSID, dossierType1.getBusinessId()).orderBy(DossierTypeInfo.ORDERBY);
        List<DossierType> dossierTypes = service.selectList(dossierTypeSqlQuery);
        return ResultEntity.success(dossierTypes);
    }

    /**
     * 根据id查询分类
     *
     * @param id
     * @return
     */
    @RequestMapping("/byId")
    public ResultEntity ById(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultEntity.error("Id不能为空");
        }
        DossierType dossierType1 = service.selectById(id);
        return ResultEntity.success(dossierType1);
    }

    /**
     * 根据分类号获取信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getDossierTypeByCode")
    public ResultEntity getDossierTypeByCode(HttpServletRequest request) {
        String managementTypeCode = request.getParameter("managementTypeCode");
        Assert.isTrue(!StringUtils.isEmpty(managementTypeCode), "分类号不能为空");
        return ResultEntity.success(service.getDossierTypeByCode(managementTypeCode));
    }
}
