package com.dr.archive.receive.online.service.impl;

import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManageInfo;
import com.dr.archive.receive.online.service.ArchiveReceiveOnlineSysManageService;
import com.dr.archive.receive.online.utils.KeyUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author: qiuyf
 * @date: 2022/3/7 14:19
 */
@Service
public class ArchiveReceiveOnlineSysManageServiceImpl extends DefaultBaseService<ArchiveReceiveOnlineSysManage> implements ArchiveReceiveOnlineSysManageService {
    @Override
    public String getPublicKey(String id) {
        String key = "";
        try {
            ArchiveReceiveOnlineSysManage archiveReceiveOnlineSysManage = selectById(id);
            if (StringUtils.isEmpty(archiveReceiveOnlineSysManage.getPublicKey())) {
                String[] keys = KeyUtils.generateSmKey();
                key = keys[0];
                archiveReceiveOnlineSysManage.setPublicKey(keys[0]);
                archiveReceiveOnlineSysManage.setPrivateKey(keys[1]);
                updateById(archiveReceiveOnlineSysManage);
            } else {
                key = archiveReceiveOnlineSysManage.getPublicKey();
            }


        } catch (Exception ignored) {

        }
        return key;
    }

    @Override
    public ArchiveReceiveOnlineSysManage selectBySysCode(String sysCode) {
        Assert.isTrue(StringUtils.hasText(sysCode), "系统编码不能为空！");
        return selectOne(SqlQuery.from(ArchiveReceiveOnlineSysManage.class).equal(ArchiveReceiveOnlineSysManageInfo.SYSCODE, sysCode));
    }
}
