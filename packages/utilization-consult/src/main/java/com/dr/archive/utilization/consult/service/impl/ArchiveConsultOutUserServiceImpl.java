package com.dr.archive.utilization.consult.service.impl;

import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveConsultOutUserService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 默认实现类
 *
 * @author dr
 */
@Service
public class ArchiveConsultOutUserServiceImpl extends DefaultBaseService<ArchiveBatchConsult> implements ArchiveConsultOutUserService {
    @Override
    @Transactional(readOnly = true)
    public ArchiveBatchConsult selectByIdNo(String idNo) {
        Assert.isTrue(StringUtils.hasText(idNo), "证件号不能为空");
        SqlQuery<ArchiveBatchConsult> sideSqlQuery = SqlQuery.from(ArchiveBatchConsult.class)
                .equal(ArchiveBatchConsultInfo.IDNO, idNo)
                .orderByDesc(ArchiveBatchConsultInfo.CREATEDATE);
        List<ArchiveBatchConsult> consultList = commonMapper.selectLimitByQuery(sideSqlQuery, 0, 1);
        return consultList.size() == 1 ? consultList.get(0) : null;
    }
}
