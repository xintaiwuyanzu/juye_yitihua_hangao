package com.dr.archive.detection.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailUtils;
import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.organise.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 抽象四性检测父类，用来提供工具方法，
 * <p>
 * 主要是都在子类实现了代码太多了，看不清楚逻辑
 *
 * @author dr
 */
public class AbstractTestRecordService extends AbstractArchiveBatchDetailUtils<TestRecord> {
    static protected Logger logger = LoggerFactory.getLogger(TestRecordService.class);
    @Autowired
    protected CommonFileService commonFileService;

    /**
     * 构造四性检测上下文
     *
     * @param formData
     * @param formDefinition
     * @param person
     * @param fond
     * @param category
     * @param linkType
     * @param attr
     * @return
     */
    protected ItemDetectContext buildContext(FormData formData, FormDefinition formDefinition, Person person, Fond fond, Category category, LinkType linkType, Map<String, Object> attr) {
        //构造上下文
        ItemDetectContext context = new ItemDetectContext();
        context.setCategory(category);
        context.setFond(fond);
        context.setFormData(formData);
        context.setFormDefinition(formDefinition);
        context.setPerson(person);
        context.setLinkType(linkType);
        if (attr != null) {
            context.getSessionMap().putAll(attr);
        }

        List<FileInfo> fileInfos = commonFileService.list(formData.getId());
        context.setFileInfos(fileInfos);
        return context;
    }


    /**
     * 根据上下文构造检测批次
     *
     * @param context
     * @return
     */
    protected TestRecord newRecord(ItemDetectContext context) {
        TestRecord record = new TestRecord();
        //用于后面小项绑定
        record.setId(UUID.randomUUID().toString());
        //检测环节
        record.setLinkCode(context.getLinkType().getCode());
        //绑定档案表单数据
        initDetail(record, null, context.getFormData(), context.getFond(), context.getCategory());
        //绑定业务外键
        if (context.containsKey(TestRecordService.CONTEXT_BUSINESS_ID_KEY)) {
            record.setBusinessId(context.getAttribute(TestRecordService.CONTEXT_BUSINESS_ID_KEY));
        }
        return record;
    }
}
