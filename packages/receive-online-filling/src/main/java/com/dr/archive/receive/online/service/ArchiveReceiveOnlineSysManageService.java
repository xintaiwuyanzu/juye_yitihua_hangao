package com.dr.archive.receive.online.service;

import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.framework.common.service.BaseService;

/**
 * @author: qiuyf
 * @date: 2022/3/7 14:20
 */
public interface ArchiveReceiveOnlineSysManageService extends BaseService<ArchiveReceiveOnlineSysManage> {
    /**
     * 根据Id获取公钥
     *
     * @param id
     * @return
     */
    String getPublicKey(String id);

    /**
     * 根据系统编码查询子系统配置
     *
     * @param sysCode
     * @return
     */
    ArchiveReceiveOnlineSysManage selectBySysCode(String sysCode);
}
