package com.dr.archive.tag.controller;

import com.dr.archive.tag.entity.StdTag;
import com.dr.archive.tag.entity.StdTagInfo;
import com.dr.archive.tag.service.StdTagService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 国家标准
 *
 * @author dr
 */
@RestController
@RequestMapping("api/stdTag")
public class StdTagController extends BaseServiceController<StdTagService, StdTag> {
    @Override
    protected SqlQuery<StdTag> buildPageQuery(HttpServletRequest request, StdTag entity) {
        return SqlQuery.from(StdTag.class);
    }

    @RequestMapping("/getAllStdTag")
    ResultEntity<List<StdTag>> getAllStdTag() {
        return ResultEntity.success(service.selectList(SqlQuery.from(StdTag.class).orderBy(StdTagInfo.ORDERINFO1ST)));
    }

    @RequestMapping("/updateInfo")
    public ResultEntity updateInfo(HttpServletRequest request, StdTag entity) {
        long n = service.updateBySqlQuery(SqlQuery.from(StdTag.class)
                .equal(StdTagInfo.LABELID1ST, entity.getLabelId1st())
                .equal(StdTagInfo.LABELID2ND, entity.getLabelId2nd())
                .set(StdTagInfo.LABELNAME1ST, entity.getLabelName1st())
                .set(StdTagInfo.LABELNAME2ND, entity.getLabelName2nd()));
        if (n > 0) {
            return ResultEntity.success();
        } else {
            return ResultEntity.error("修改失败!");
        }
    }
}
