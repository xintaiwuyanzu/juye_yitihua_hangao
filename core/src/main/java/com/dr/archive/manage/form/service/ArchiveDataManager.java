package com.dr.archive.manage.form.service;

import com.dr.archive.manage.form.entity.ArchiveRepeat;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.page.Page;

import java.util.List;

/**
 * 档案数据管理对象
 * <p>
 * 汇总类，能够处理各种类型的数据
 *
 * @author dr
 */
public interface ArchiveDataManager {
    /**
     * 五个大状态表示五个库
     */
    /**
     * 接收库
     */
    String STATUS_RECEIVE = "RECEIVE";
    /**
     * 预归档
     */
    String STATUS_PRE = "PRE";
    /**
     * 管理库
     */
    String STATUS_MANAGE = "MANAGE";
    /**
     * 专题库
     */
    String STATUS_SPECIAL = "SPECIAL";
    /**
     * 回收站
     */
    String STATUS_TRASH = "TRASH";
    /**
     * 保存库
     */
    String STATUS_SAVE = "SAVE";
    /**
     * 搜索库
     */
    String STATUS_SEARCH = "SEARCH";
    /**
     * 垃圾箱
     */
    String STATUS_DELETE = "DELETE";

    //锁状态
    /**
     * 字段 COLUMN_SUB_STATUS
     * 锁定
     */
    String SUBSTATUS_LOCKED = "1";
    /**
     * 字段 COLUMN_SUB_STATUS
     * 未锁定
     */
    String SUBSTATUS_UNLOCKED = "0";

    /**
     * 添加表单数据
     *
     * @param formData   表单数据
     * @param fondId
     * @param categoryId
     * @return
     */
    FormData insertFormData(FormData formData, String fondId, String categoryId);

    /**
     * 更新表单数据
     *
     * @param formData
     * @param fondId
     * @param categoryId
     * @return
     */
    FormData updateFormData(FormData formData, String fondId, String categoryId);

    /**
     * 查询单条表单数据
     *
     * @param formDefinitionId
     * @param formDataId
     * @return
     */
    FormData selectOneFormData(String formDefinitionId, String formDataId);

    /**
     * 查询表单数据
     *
     * @param formDefinitionId
     * @param sqlBuilder
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<FormData> formDataPage(String formDefinitionId, SqlBuilder sqlBuilder, int pageIndex, int pageSize);

    /**
     * 给前端用的接口
     *
     * @param query
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<FormData> formDataPage(ArchiveDataQuery query, int pageIndex, int pageSize);


    /**
     * 删除表单数据
     * TODO 这里应该也传全宗和门类Id
     * 犹豫要不要放到每一张表数据中
     *
     * @param formDefinitionId
     * @param aId
     * @return
     */
    Long deleteFormData(String categoryId, String formDefinitionId, String aId);

    /**
     * 根据表单数据Id查询表单数据Id
     *
     * @param formId
     * @return
     */
    default List<FormData> findDataByQuery(String formId) {
        ArchiveDataQuery query = new ArchiveDataQuery();
        query.setFormDefinitionId(formId);
        return findDataByQuery(query);
    }

    /**
     * 根据查询条件查询列表
     *
     * @param query
     * @return
     */
    List<FormData> findDataByQuery(ArchiveDataQuery query);

    /**
     * 直接暴漏出来表单查询数据的方法
     *
     * @param formDefinitionId
     * @param builder
     * @return
     */
    List<FormData> findDataByBuilder(String formDefinitionId, SqlBuilder builder);

    /**
     * 更新状态
     *
     * @param ids
     * @param status
     * @param formDefinitionId
     */
    void updateStatus(String ids, String status, String formDefinitionId);

    /**
     * 更新锁状态
     *
     * @param ids
     * @param subStatus
     * @param formDefinitionId
     */
    void updateSubStatus(String ids, String subStatus, String formDefinitionId);

    /**
     * 数据检查（查重方法，若不传archiveCode则查询该表单下所有重复数据）
     *
     * @param fond        全宗号
     * @param category    分类号
     * @param formId      表单id
     * @param status      库状态（移交库、档案库）
     * @param archiveCode 档号
     * @return
     */
    List<ArchiveRepeat> repeat(String fond, String category, String formId, String status, String archiveCode);

    List<ArchiveRepeat> continuity(String fond, String category, String formId, String status, String archiveCode);

    /**
     * 调整档案到其他分类
     *
     * @param ids        档案id集合
     * @param fondId     全宗id
     * @param categoryId 分类id
     */
    void adjustFormData(String ids, String formId, String fondId, String categoryId);

    void updateStatusByQuery(ArchiveDataQuery query, String status);

    void updateHaveYuanwen(String categoryId, String id, String formId, String haveYuanwen);

    void updateHaveYuanwenByFormData(String categoryId, FormData formData, String haveYuanwen);

    void groupDocument(String ajFormDefinitionId, String wjFormDefinitionId, String ajId, String wjIds, String fondId, String categoryId);

    void disassemble(String ajFormDefinitionId, String wjFormDefinitionId, String ids, String fondId, String categoryId);

    boolean insertFile(String ajFormDefinitionId, String wjFormDefinitionId, String ajArchiveCode, String ids, String fondId, String categoryId);

    void removeFile(String formDefinitionId, String ids, String fondId, String categoryId);

    void updateCategoryByQuery(ArchiveDataQuery query, String newCategoryId, String oldCategoryId);
    /**
     * 处理进入正式库前的数据
     */
    void updateStatusByFormData(FormData formData, String status);

    /**
     * 获得案卷中的卷内文件的query
     */
    void getJNQuery(FormData formData, String status);

    void updateStatusByQueryPlus(ArchiveDataQuery query, String status);

    /**
     * 获得案卷内的卷内文件
     */
    List<FormData> getJNFormData(FormData formData);


}
