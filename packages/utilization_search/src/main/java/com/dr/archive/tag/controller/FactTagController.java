package com.dr.archive.tag.controller;

import com.dr.archive.tag.entity.FactTag;
import com.dr.archive.tag.entity.FactTagInfo;
import com.dr.archive.tag.service.FactTagService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 基础事实
 *
 * @author: yang
 * @create: 2022-07-14 18:30
 */
@RestController
@RequestMapping("api/factTag")
public class FactTagController extends BaseServiceController<FactTagService, FactTag> {
    @Override
    protected SqlQuery<FactTag> buildPageQuery(HttpServletRequest httpServletRequest, FactTag factTag) {
        return SqlQuery.from(FactTag.class);
    }

    @RequestMapping("/getAllFactTag")
    ResultEntity getAllFactTag() {
        ArrayList<FactTag> factTags = (ArrayList<FactTag>) service.selectList(SqlQuery.from(FactTag.class)
                .orderBy(FactTagInfo.LABELNAME1ST, FactTagInfo.LABELNAME2ND, FactTagInfo.LABELNAME3RD)
                .isNotNull(FactTagInfo.LABELNAME2ND, FactTagInfo.LABELNAME3RD));
        ArrayList<Object> list = new ArrayList<>();
        for (FactTag tag : factTags) {
            if (StringUtils.hasText(tag.getLabelName3rd()) && StringUtils.hasText(tag.getLabelName2nd())) {
                list.add(tag);
            }
        }
        return ResultEntity.success(list);
    }

    @RequestMapping("/addFactTag")
    ResultEntity addFactTag(FactTag factTag, int type) {
        if (type == 1) {
            factTag.setLabelId1st(UUIDUtils.getUUID());
        } else if (type == 2) {
            factTag.setLabelId1st(factTag.getLabelName1st());
            factTag.setLabelName1st(getFactTagByLabelId(factTag.getLabelName1st(), type).getLabelName1st());
            factTag.setLabelId2nd(UUIDUtils.getUUID());
        } else if (type == 3) {
            FactTag tag = getFactTagByLabelId(factTag.getLabelName2nd(), type);
            factTag.setLabelId1st(tag.getLabelId1st());
            factTag.setLabelName1st(tag.getLabelName1st());
            factTag.setLabelId2nd(tag.getLabelId2nd());
            factTag.setLabelName2nd(tag.getLabelName2nd());
            factTag.setLabelId3rd(UUIDUtils.getUUID());
        }
        return ResultEntity.success(service.insert(factTag));
    }

    public FactTag getFactTagByLabelId(String labelId, int type) {
        SqlQuery<FactTag> query = SqlQuery.from(FactTag.class);
        if (type == 2) {
            query.equal(FactTagInfo.LABELID1ST, labelId);
        } else if (type == 3) {
            query.equal(FactTagInfo.LABELID2ND, labelId);
        }
        return service.selectList(query).get(0);
    }
}
