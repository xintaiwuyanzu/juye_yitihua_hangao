package com.dr.archive.utilization.consult.service;

import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.framework.common.service.BaseService;

/**
 * 查档用户对应service
 *
 * @author dr
 */
public interface ArchiveConsultOutUserService extends BaseService<ArchiveBatchConsult> {
    /**
     * 根据身份证号查询某一个人的查档历史记录
     *
     * @param idNo
     * @return 不管查询多少次，只返回一条最新的历史查档登记信息
     */
    ArchiveBatchConsult selectByIdNo(String idNo);
}
