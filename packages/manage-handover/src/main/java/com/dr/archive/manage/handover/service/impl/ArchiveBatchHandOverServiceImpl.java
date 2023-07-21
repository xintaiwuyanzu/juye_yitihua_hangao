package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchServiceImpl;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverInfo;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLib;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLibInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverDetailService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.archive.util.Constants;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 到期移交批次service实现类
 *
 * @author dr
 */
@Service
public class ArchiveBatchHandOverServiceImpl extends AbstractArchiveBatchServiceImpl<ArchiveBatchHandOver> implements ArchiveBatchHandOverService {
    @Autowired
    ArchiveBatchHandOverLibService libService;
    @Autowired
    ArchiveBatchHandOverDetailService detailService;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveBatchHandOver newBatch(Person person, Organise organise, String fondId, String libIds, String type) {
        Assert.isTrue(BATCH_TYPE_DELAY.equals(type) || BATCH_TYPE_HAND_OVER.equals(type), "不支持的批次类型");
        ArchiveBatchHandOver handOver = new ArchiveBatchHandOver();
        handOver.setBatchType(type);
        handOver.setStatus(STATUS_EDIT);
        handOver.setFondId(fondId);

        String typeName = "移交";
        String libStatus = ArchiveBatchHandOverLibService.STATUS_HAND_AUDIT;
        if (BATCH_TYPE_DELAY.equalsIgnoreCase(type)) {
            typeName = "延期";
            libStatus = ArchiveBatchHandOverLibService.STATUS_DELAY_AUDIT;
        }
        //更新待移交库状态
        libService.updateStatus(libIds, libStatus);

        handOver.setLibIds(libIds);
        //载体起止顺序号
        List<String> strings = new ArrayList<>();
        for (String s : libIds.split(",")) {
            strings.add(libService.computeSumInfo(s));
        }
        handOver.setSequence(String.join("\n", strings));

        handOver.setCreatePerson(person.getId());
        handOver.setCreateDate(System.currentTimeMillis());
        //移交信息
        handOver.setTransferPersonName(person.getUserName());
        handOver.setSourceOrgId(organise.getId());
        handOver.setSourceOrgName(organise.getOrganiseName());

        //接收单位
        List<Organise> targetList = organisePersonService.getParentOrganiseList(organise.getId(), Constants.ORG_TYPE_DAG);
        //todo 找到所属档案馆，现在没这没多数据
        //Assert.isTrue(targetList.size() > 0, "未找到" + organise.getOrganiseName() + "上级档案馆数据");
        Organise target = targetList.isEmpty() ? organise : targetList.get(0);
        handOver.setTarOrgId(target.getId());
        handOver.setTarOrgName(target.getOrganiseName());

        //移交总数
        long sum = commonMapper.selectOneByQuery(
                SqlQuery.from(ArchiveBatchHandOverLib.class, false)
                        .column(ArchiveBatchHandOverLibInfo.DETAILNUM.sum())
                        .in(ArchiveBatchHandOverLibInfo.ID, libIds.split(","))
                        .setReturnClass(Long.class)
        );
        handOver.setBatchName(person.getUserName() + "于" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd") + "提交的" + typeName + "申请");
        handOver.setDetailNum(sum);
        insert(handOver);
        if (BATCH_TYPE_HAND_OVER.equalsIgnoreCase(type)) {
            detailService.addManageDetail(handOver);
        }
        return handOver;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveBatchHandOver updateDag(Person person, ArchiveBatchHandOver archiveBatchHandOver) {
        ArchiveBatchHandOver saved = selectById(archiveBatchHandOver.getId());
        if (STATUS_NO_PASS_DAG.equals(archiveBatchHandOver.getStatus())) {
            //档案馆不接受，更新状态退回数据
            saved.setStatus(STATUS_NO_PASS_DAG);
            //更新档案批次为待移交状态
            libService.updateStatus(saved.getLibIds(), ArchiveBatchHandOverLibService.STATUS_WAITING);
        }
        saved.setTarPerId(person.getId());
        saved.setTarPerName(person.getUserName());
        updateById(saved);
        return saved;
    }

    @Override
    public List<ArchiveBatchHandOver> total() {
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery<ArchiveBatchHandOver> sqlQuery = SqlQuery.from(ArchiveBatchHandOver.class, false)
                .equal(ArchiveBatchHandOverInfo.SOURCEORGID, organise.getId())
                .count(ArchiveBatchHandOverInfo.ID, "id", false, false);
        List<ArchiveBatchHandOver> totals = commonMapper.selectByQuery(sqlQuery);
        return totals;
    }

}
