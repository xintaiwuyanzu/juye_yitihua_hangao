package com.dr.archive.batch.service;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.vo.BatchCount;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.orm.jdbc.Relation;

import java.util.List;

/**
 * 基础的批量操作service
 *
 * @author caor
 */
public interface BaseArchiveBatchDetailService<D extends AbstractBatchDetailEntity> extends BaseService<D> {
    /**
     * 移交
     */
    String BATCH_TYPE_SEND_CHECK = "SEND_CHECK";
    /**
     * 鉴定
     */
    String BATCH_TYPE_JD = "JD";
    /**
     * 到期鉴定
     */
    String BATCH_TYPE_DQJD = "DQJD";
    /**
     * 开放鉴定
     */
    String BATCH_TYPE_KFJD = "KFJD";
    /**
     * 导入
     */
    String BATCH_TYPE_IMP = "IMP";
    /**
     * 导出
     */
    String BATCH_TYPE_EXP = "EXP";
    /**
     * 业务系统预归档
     */
    String BATCH_TYPE_PRE_ARCHIVE = "PRE_ARCHIVE";
    /**
     * 归档
     */
    String BATCH_TYPE_ARCHIVE = "ARCHIVE";
    /**
     * 纠错
     */
    String BATCH_TYPE_ERROR_RECOVERY = "ERROR_RECOVERY";
    /**
     * 挂接
     */
    String BATCH_TYPE_FILE_HOOK = "FILE_HOOK";

    /**
     * 重建索引
     */
    String BATCH_TYPE_REBUILD_INDEX = "REBUILD_INDEX";

    /**
     * 业务指导
     */
    String BATCH_TYPE_JDZD = "JDZD";

    /**
     * 年度检查
     */

    String BATCH_TYPE_CHECK = "CHECK";

    /**
     * 执法检查
     */

    String BATCH_TYPE_LAW = "LAW";

    /**
     * 根据查询条件创建详情信息
     *
     * @param query
     * @param entity
     */
    void createDetail(BatchCreateQuery query, AbstractArchiveBatch entity);

    /**
     * 根据批次Id查询该批次所有详情
     * TODO，将来需要升级成分页查询功能，数据量特别大的时候会内存爆炸
     *
     * @param batchId
     * @return
     */
    List<D> selectByBatchId(String batchId);

    /**
     * 根据批次Id查询分页数据
     *
     * @param batch
     * @param query
     * @param start
     * @param pageSize
     * @return
     */
    Page<D> selectPage(AbstractArchiveBatch batch, BatchCreateQuery query, Integer start, Integer pageSize);

    /**
     * 根据批次统计总数
     *
     * @param batchId
     * @return
     */
    long countTotal(String batchId);

    /**
     * 根据批次Id统计总数
     *
     * @param batchId
     * @return
     */
    BatchCount count(String batchId);

    /**
     * 更新单条详情状态
     * <p>
     * 可以是通过也可以是不通过
     *
     * @param id
     * @param status
     * @param advice      审核意见
     * @param targetValue
     * @return
     */
    long changeStatus(String id, String status, String advice, String targetValue);

    /**
     * 获取批次类型
     *
     * @return
     */
    String getType();

    /**
     * 获取批次名称
     *
     * @return
     */
    String getName();

    /**
     * 删除批次时调用
     * 关联删除批次详情
     *
     * @param batchId
     * @return
     */
    long deleteByBatchId(String batchId);

    /**
     * 根据detailDd查询详细信息
     *
     * @param id
     * @return
     */
    @Deprecated
    default D detail(String id) {
        return selectById(id);
    }

    /**
     * 更新档案库数据
     *
     * @param archiveBatch
     */
    void updateFormData(AbstractArchiveBatch archiveBatch);

    /**
     * 初始化详情
     *
     * @param detail   详情对象
     * @param batch    批次对象
     * @param formData 表单数据对象
     * @param fond     全宗对象
     * @param category 门类对象
     */
    void initDetail(D detail, AbstractArchiveBatch batch, FormData formData, Fond fond, Category category);

    /**
     * 获取详情表实体类
     *
     * @return
     */
    Class<D> getDetailClass();

    /**
     * 获取详情表表结构定义
     *
     * @return
     */
    Relation getDetailRelation();

}
