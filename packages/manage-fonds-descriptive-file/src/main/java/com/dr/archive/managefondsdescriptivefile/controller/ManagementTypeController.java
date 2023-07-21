package com.dr.archive.managefondsdescriptivefile.controller;

import com.dr.archive.managefondsdescriptivefile.entity.ManagementType;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementTypeInfo;
import com.dr.archive.managefondsdescriptivefile.service.ManagementTypeService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/managementType")
public class ManagementTypeController extends BaseServiceController<ManagementTypeService, ManagementType> {

    @Autowired
    ManagementTypeService managementTypeService;

    @Override
    protected SqlQuery<ManagementType> buildPageQuery(HttpServletRequest request, ManagementType managementType) {
        SqlQuery<ManagementType> sqlQuery = SqlQuery.from(ManagementType.class).equal(ManagementTypeInfo.ENABLED, true).like(ManagementTypeInfo.NAME, managementType.getName()).orderBy(ManagementTypeInfo.ORDERBY);
        return sqlQuery;
    }

    /**
     * 查询所有材料分类
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResultEntity findAll() {
        List<ManagementType> all = managementTypeService.findAll();
        return ResultEntity.success(all);
    }

    @RequestMapping("/updateOrder")
    public ResultEntity updateOrder(String id, Integer priority) {
        return ResultEntity.success(service.updateBySqlQuery(SqlQuery.from(ManagementType.class).set(ManagementTypeInfo.ORDERBY, priority).equal(ManagementTypeInfo.ID, id)));
    }

    /**
     * 重写删除方法，可以批量删除
     *
     * @param request
     * @param management
     * @return
     */
    public ResultEntity delete(HttpServletRequest request, ManagementType management) {
        String aIds = request.getParameter("ids");
        if (!StringUtils.isEmpty(aIds)) {
            service.deleteByIds(aIds);
        } else {
            service.deleteById(management.getId());
        }
        return ResultEntity.success();
    }

    /**
     * 获取全宗卷大类树
     *
     * @return
     */
    @RequestMapping("/getManagementTypeTree")
    public ResultEntity getManagementTypeTree() {
        return ResultEntity.success(service.getManagementTypeTree());
    }

    /**
     * 获取全宗卷大类和分类树
     *
     * @return
     */
    @RequestMapping("/getManagementTypeAndDossierTree")
    public ResultEntity getManagementTypeAndDossierTree() {
        return ResultEntity.success(service.getManagementTypeAndDossierTree());
    }
}
