package com.dr.archive.tag.controller;

import com.dr.archive.tag.entity.TagLib;
import com.dr.archive.tag.entity.TagLibInfo;
import com.dr.archive.tag.service.TagLibService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/3/15 11:32
 */
@RestController
@RequestMapping("api/tagLib")
public class TagLibController extends BaseServiceController<TagLibService, TagLib> {
    @Override
    protected SqlQuery<TagLib> buildPageQuery(HttpServletRequest request, TagLib entity) {
        SqlQuery<TagLib> sqlQuery = SqlQuery.from(TagLib.class);
        if (!StringUtils.isEmpty(entity.getTagName())) {
            sqlQuery.like(TagLibInfo.TAGNAME, entity.getTagName());
        }
        if (!StringUtils.isEmpty(entity.getCtype())) {
            sqlQuery.equal(TagLibInfo.CTYPE, entity.getCtype());
        }
        sqlQuery.orderByDesc(TagLibInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping({"/insert"})
    public ResultEntity<TagLib> insert(HttpServletRequest request, TagLib entity) {
        if ("gt".equals(entity.getCtype())) {
            String fullLable = "";
            if (!StringUtils.isEmpty(entity.getParentId())) {
                TagLib pTagLib = service.selectById(entity.getParentId());
                fullLable = pTagLib.getFullLable() + ",";
            }
            fullLable += entity.getTagName();
            entity.setFullLable(fullLable);
            if (!StringUtils.isEmpty(entity.getTagDescribe())) {
                entity.setLeaf(1);
            } else {
                entity.setLeaf(0);
            }
        }
        entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
        this.service.insert(entity);
        return ResultEntity.success(entity);
    }

    /*
     * 查固体标签的所以非叶数据
     */
    @RequestMapping({"/selectParents"})
    public ResultEntity selectTags(HttpServletRequest request) {
        return ResultEntity.success(service.selectParents());
    }

    @RequestMapping("/selectTagList")
    public ResultEntity selectTagList(HttpServletRequest request, TagLib entity) {
        return ResultEntity.success(service.selectTagList(entity));
    }

    /**
     * 更改标签状态
     *
     * @param request
     * @return
     */
    @RequestMapping("/updateTagState")
    public ResultEntity updateTagState(HttpServletRequest request, TagLib entity) {
        return ResultEntity.success(service.updateTagState(entity));
    }

}
