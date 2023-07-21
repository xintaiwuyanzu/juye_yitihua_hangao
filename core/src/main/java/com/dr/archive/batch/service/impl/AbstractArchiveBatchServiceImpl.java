package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 批次抽象父类，实现基础业务逻辑
 *
 * @param <E>
 * @author dr
 */
public abstract class AbstractArchiveBatchServiceImpl<E extends AbstractArchiveBatch> extends DefaultBaseService<E> implements BaseArchiveBatchService<E> {
    @Autowired
    protected CommonFileConfig commonFileConfig;
    @Autowired
    protected CommonFileService commonFileService;
    @Autowired
    protected ArchiveDataManager archiveDataManager;
    @Autowired
    protected ArchiveDataManager dataManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void computeTotal(String id) {
        //TODO
        Assert.isTrue(StringUtils.hasText(id), "主键不能为空！");
    }

    public E newBatch(BatchCreateQuery query, Person person) {
        E instance = newBatchInstance();
        //绑定创建人相关信息
        bindInitInfo(instance, person);
        //绑定查询条件相关信息
        bindQueryInfo(instance, query);
        //创建标题
        String batchName = buildBatchName(instance, person, query);
        instance.setBatchName(batchName);
        return instance;
    }

    /**
     * 创建批次名称
     *
     * @param instance
     * @param person
     * @param query
     * @return
     */
    protected String buildBatchName(E instance, Person person, BatchCreateQuery query) {
        return instance.getBatchName();
    }


    /**
     * 绑定查询条件相关信息
     *
     * @param instance
     * @param query
     */
    protected void bindQueryInfo(E instance, BatchCreateQuery query) {
        //批次启动时间
        instance.setStartDate(System.currentTimeMillis());
        //批次类型
        instance.setBatchType(query.getType());
        //批次备注
        instance.setBeizhu(query.getBeizhu());
    }

    /**
     * 绑定创建人相关信息
     *
     * @param instance
     * @param person
     */
    protected void bindInitInfo(E instance, Person person) {
        if (person != null) {
            //绑定创建人信息
            instance.setCreatePerson(person.getId());
            instance.setUpdatePerson(person.getId());
        }
        instance.setId(UUID.randomUUID().toString());
        instance.setCreateDate(System.currentTimeMillis());
        instance.setUpdateDate(System.currentTimeMillis());
    }

    public E newBatchInstance() {
        return (E) ReflectUtils.newInstance(getEntityClass());
    }

    public CommonFileConfig getCommonFileConfig() {
        return commonFileConfig;
    }

    public ArchiveDataManager getArchiveDataManager() {
        return archiveDataManager;
    }

    public CommonFileService getCommonFileService() {
        return commonFileService;
    }
}
