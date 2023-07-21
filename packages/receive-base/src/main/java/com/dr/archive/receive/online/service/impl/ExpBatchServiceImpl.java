package com.dr.archive.receive.online.service.impl;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.receive.online.entity.ExpBatch;
import com.dr.archive.receive.online.service.ExpBatchService;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yang
 * @create: 2022-08-04 10:33
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ExpBatchServiceImpl extends DefaultBaseService<ExpBatch> implements ExpBatchService {

    @Autowired
    ArchiveBatchService archiveBatchService;
    Map<String, BaseArchiveBatchDetailService> baseBatchServiceMap;

    @Override
    public void newBatch(BatchCreateQuery query, Person person) {
        afterPropertiesSet();
        BaseArchiveBatchDetailService baseArchiveBatchDetailService = baseBatchServiceMap.get(query.getType());
        Assert.notNull(baseArchiveBatchDetailService, "不能处理：" + query.getType() + "类型的批量操作");
        //创建导出批次
        ExpBatch batch = new ExpBatch();
        //创建批次信息
        batch.setStartDate(System.currentTimeMillis());
        batch.setStatus(StatusEntity.STATUS_DISABLE_STR);
        batch.setBatchType(query.getType());
        String batchName = String.format("%s提交的%s", person.getUserName(), baseArchiveBatchDetailService.getName());
        if (!StringUtils.isEmpty(query.getBatchName())) {
            batch.setBatchName(query.getBatchName());
        } else {
            batch.setBatchName(batchName);
        }
        batch.setBeizhu(query.getBeizhu());
        batch.setOrgId(SecurityHolder.get().currentOrganise().getId());
        CommonService.bindCreateInfo(batch);
        commonMapper.insert(batch);
        baseArchiveBatchDetailService.createDetail(query, batch);
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Map<String, BaseArchiveBatchDetailService> beans = getApplicationContext().getBeansOfType(BaseArchiveBatchDetailService.class);
        baseBatchServiceMap = Collections.synchronizedMap(new HashMap<>(beans.size()));
        beans.forEach((k, v) -> baseBatchServiceMap.put(v.getType(), v));
    }
}
