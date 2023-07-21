package com.dr.archive.archivecar.service.impl;

import com.dr.archive.archivecar.bo.ArchiveCarDetailTag;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认档案车类型
 *
 * @author dr
 */
@Component
public class DefaultArchiveCarTypeProvider implements ArchiveCarTypeProvider {
    @Autowired
    ArchiveCarDetailService archiveCarDetailService;

    @Override
    public String getType() {
        return "default";
    }

    @Override
    public String getName() {
        return "默认档案车";
    }

    @Override
    public boolean isTagStatic() {
        return false;
    }

    @Override
    public List<ArchiveCarDetailTag> getDetailTags(String personId) {
        SqlQuery<ArchiveCarDetail> sqlQuery = SqlQuery.from(ArchiveCarDetail.class, false)
                //类型相同
                .equal(ArchiveCarDetailInfo.BATCHTYPE, getType())
                //创建人相同
                .equal(ArchiveCarDetailInfo.CREATEPERSON, personId)
                .column(ArchiveCarDetailInfo.ARCHIVETAG)
                .groupBy(ArchiveCarDetailInfo.ARCHIVETAG);

        return archiveCarDetailService.selectList(sqlQuery)
                .stream()
                .map(d -> {
                    ArchiveCarDetailTag tag = new ArchiveCarDetailTag();
                    tag.setCarType(getType());
                    tag.setCode(d.getArchiveTag());
                    tag.setName(d.getArchiveTag());
                    tag.setPersonId(personId);
                    return tag;
                })
                .collect(Collectors.toList());
    }
}
