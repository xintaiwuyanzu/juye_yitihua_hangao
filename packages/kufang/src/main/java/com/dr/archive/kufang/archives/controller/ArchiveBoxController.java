
package com.dr.archive.kufang.archives.controller;

import com.dr.archive.kufang.archives.entity.ArchiveBox;
import com.dr.archive.kufang.archives.entity.ArchiveBoxInfo;
import com.dr.archive.kufang.archives.entity.Position;
import com.dr.archive.kufang.archives.entity.PositionInfo;
import com.dr.archive.kufang.archives.service.ArchiveBoxService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 档案馆藏 Controller
 *
 * @author dr
 */

@RestController
@RequestMapping("/api/archiveBox")
public class ArchiveBoxController extends BaseServiceController<ArchiveBoxService, ArchiveBox> {
    @Autowired
    PositionService positionService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    DataBaseService dataBaseService;

    @Override
    protected SqlQuery<ArchiveBox> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBox archiveBox) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveBox.class);
        sqlQuery.like(ArchiveBoxInfo.BOXCODE, archiveBox.getBoxCode())
                .like(ArchiveBoxInfo.FONDNUM, archiveBox.getFondNum())
                .orderByDesc(ArchiveBoxInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping("/findLocById")
    public ResultEntity findLocById(String locId) {
        SqlQuery<Position> sqlQuery = SqlQuery.from(Position.class);
        sqlQuery.equal(PositionInfo.LOCID, locId);
        return ResultEntity.success(commonMapper.selectByQuery(sqlQuery));
    }

    @RequestMapping("/getOldData")
    public ResultEntity getOldData() {
        Relation tableInfo = dataBaseService.getTableInfo(String.valueOf("ARCHIVES_CLASS".getClass()), "kufang");
        return ResultEntity.success("2");
    }

}
