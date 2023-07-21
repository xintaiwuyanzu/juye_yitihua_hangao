package com.dr.archive.controller;

import com.dr.archive.model.entity.ArchiveOrder;
import com.dr.archive.model.entity.ArchiveOrderInfo;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class ArchiveOrderController extends BaseController<ArchiveOrder> {
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<ArchiveOrder> sqlQuery, ArchiveOrder entity) {
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise != null) {
            sqlQuery.equal(ArchiveOrderInfo.ORGANISEID, organise.getId());
        }
        sqlQuery.equal(ArchiveOrderInfo.CATEGORYID, entity.getCateGoryId())
                .equal(ArchiveOrderInfo.FIELDORDER, entity.getFieldOrder());
    }

    @Override
    protected void onBeforeInsert(HttpServletRequest request, ArchiveOrder entity) {
        if (StringUtils.isEmpty(entity.getCateGoryId())) {
            Assert.isTrue(false, "请选择门类");
        }
        List<ArchiveOrder> archiveOrders = commonMapper.selectByQuery(SqlQuery.from(ArchiveOrder.class)
                .equal(ArchiveOrderInfo.CATEGORYID, entity.getCateGoryId())
                .equal(ArchiveOrderInfo.CODE, entity.getCode())
                .equal(ArchiveOrderInfo.ORDERTYPE, entity.getOrderType())
                .equal(ArchiveOrderInfo.ARCHIVETYPE, entity.getArchiveType())
                .equal(ArchiveOrderInfo.FIELDORDER, entity.getFieldOrder()));

        if (archiveOrders.size() > 0) {
            Assert.isTrue(false, "当前门类已存在相同配置");
        }
        entity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
    }

}
