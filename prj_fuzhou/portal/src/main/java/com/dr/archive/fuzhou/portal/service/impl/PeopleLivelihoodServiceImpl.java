package com.dr.archive.fuzhou.portal.service.impl;

import com.dr.archive.fuzhou.portal.service.PeopleLivelihoodService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

@Service
public class PeopleLivelihoodServiceImpl implements PeopleLivelihoodService {

    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    DataBaseService dataBaseService;

    @Autowired
    FormNameGenerator formNameGenerator;

    @Autowired
    CommonMapper commonMapper;


    @Override
    public Page<Map> searchPeopleLivelihood(String idNo, String type, Integer index, Integer size) {

        Assert.isTrue(!idNo.isEmpty(), "身份证号不能为空！");
        //TODO 先暂时把表单ID写死，后面再改
        FormModel formModel = formDefinitionService.selectFormDefinitionById("6f140f19-b00a-42a9-bd78-20df9db4b019");

        Relation relation = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);

        SqlQuery<Object> form = SqlQuery.from(relation).equal(relation.getColumn("idNo"), idNo);

        if (!StringUtils.isEmpty(type)) {

            form.equal(relation.getColumn("type"), type);
        }

        return commonMapper.selectPageByQuery(form.setReturnClass(Map.class), index * size, (index + 1) * size);
    }
}
