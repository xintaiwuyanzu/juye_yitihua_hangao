package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.ArchiveBatchDetailComment;
import com.dr.archive.batch.entity.ArchiveBatchDetailCommentInfo;
import com.dr.archive.batch.service.ArchiveBatchDetailCommentService;
import com.dr.framework.common.exception.NeedLoginException;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 档案详情意见表实现类
 *
 * @author dr
 */
@Service
public class ArchiveBatchDetailCommentServiceImpl extends DefaultBaseService<ArchiveBatchDetailComment> implements ArchiveBatchDetailCommentService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(ArchiveBatchDetailComment entity) {
        Person person = SecurityHolder.get().currentPerson();
        if (person == null) {
            throw new NeedLoginException("用户未登录");
        }
        entity.setCreatePersonName(person.getUserName());
        return super.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long deleteByDetailId(String detailId) {
        Assert.isTrue(StringUtils.hasText(detailId), "详情Id不能为空");
        return commonMapper.deleteByQuery(SqlQuery.from(ArchiveBatchDetailComment.class).equal(ArchiveBatchDetailCommentInfo.DETAILID, detailId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long deleteByBatchId(String batchId) {
        Assert.isTrue(StringUtils.hasText(batchId), "批次Id不能为空");
        return commonMapper.deleteByQuery(SqlQuery.from(ArchiveBatchDetailComment.class).equal(ArchiveBatchDetailCommentInfo.BATCHID, batchId));
    }
}
