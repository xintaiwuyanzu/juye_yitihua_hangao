package com.dr.archive.receive.offline.service;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.framework.core.organise.entity.Person;

import java.util.Map;

/**
 * 挂接相关操作
 *
 * @Author: caor
 * @Date: 2021-12-17 13:05
 * @Description: 新增挂接方式，原始挂接方式保留，后面需要去掉原始挂接方式
 */
public interface ArchiveBatchOfflineService extends BaseArchiveBatchService<ArchiveBatchReceiveOffline> {
    /**
     * 挂接类型 ：只挂接原文
     */
    String HOOK_TYPE_FILE = "FILE";
    /**
     * 挂接类型 ：挂接目录加原文 文件夹方式
     */
    String HOOK_TYPE_METADATA_AND_FILE = "METADATA_AND_FILE";
    /**
     * 挂接类型 ：数据包
     */
    String HOOK_TYPE_ZIP = "ZIP";

    /**
     * 文件挂接方式 ：清除原有文件，挂接新目录
     */
    String HOOK_MODE_COVER = "COVER";
    /**
     * 文件挂接方式 ：直接追加新文件
     */
    String HOOK_MODE_ADD = "ADD";

    /**
     * 根据查询条件创建批次记录和详情，
     * <p>
     * 此方法适用于从系统中查询表单数据，并且创建详情的场景
     *
     * @param query  批次查询条件
     * @param person 当前登录人
     * @return
     */
    ArchiveBatchReceiveOffline newBatch(BatchCreateQuery query, Person person);

    /**
     * 根据离线目录批次id进行原文挂接
     *
     * @param type          挂接类型 原文：
     * @param fileLocations 来源：客户端上传 服务器位置
     * @param filePath      服务器位置
     * @param impBatchId    批量导入目录id
     * @param clientBatchId 客户端上传原文的批次id
     * @param isDeleteFile  是否删除客户端上传文件
     * @param coverOrAdd    覆盖挂接还是追加挂接
     */
    void hookByBatchId(String type, String fileLocations, String filePath, String impBatchId, String clientBatchId, boolean isDeleteFile, String coverOrAdd);

    /*
     * 重新进行四性检测*/
    long doTestOffline(String batchId);

    Map<String, Long> getReport(String batchId);

    /**
     * 查询离线接收的数量
     *
     * @return
     */
    long getReceiveOfflineCountByFond();

    String getError(String batchId);

    /**
     * 获取配置文件中数据维护客户端地址
     *
     * @return
     */
    String getClientUrl();

    void extracted(ArchiveBatchReceiveOffline entity);
    void deleteFormData(String categoryId, String formId, String id);
}
