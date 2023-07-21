package com.dr.archive.detection.service;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import java.util.Map;

/**
 * 四性检测核心操作类
 *
 * @author caor
 * @date 2020-11-15 11:51
 */
public interface TestRecordService extends BaseService<TestRecord> {
    /**
     * 检测状态通过
     */
    String TEST_STATUS_SUCCESS = "1";
    /**
     * 检测状态不通过
     */
    String TEST_STATUS_FAIL = "0";

    /**
     * 四性检测业务外键主键
     * 结合四性检测场景
     * 1、手动点击四性检测
     * 2、各个环节自动四性检测
     * 不管哪种情况，四性检测都是针对档案的，
     * 一份档案执行过几次四性检测，所以没必要创建四性检测的批次表。
     * <p>
     * 但是接收或者入库场景，档案可能是批量操作的，所以可以在四性检测记录中关联一个业务批次Id，可以方便的查询出四性检测结果
     */
    String CONTEXT_BUSINESS_ID_KEY = "$businessId";

    /**
     * 执行指定档案数据四性检测
     *
     * @param formData       表单数据
     * @param formDefinition 表单定义
     * @param person         检测人员，系统自动检测时可以为空
     * @param fond           全宗
     * @param category       门类
     * @param linkType       检测环节
     * @param attr           额外属性
     */
    TestRecord testData(FormData formData, FormDefinition formDefinition, Person person, Fond fond, Category category, LinkType linkType, Map<String, Object> attr);

}
