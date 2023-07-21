package com.dr.archive.tag.service.impl;

import com.dr.archive.tag.entity.TagLib;
import com.dr.archive.tag.entity.TagLibInfo;
import com.dr.archive.tag.service.TagLibService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: qiuyf
 * @date: 2022/3/15 11:28
 */
@Service
public class TagLibServiceImpl extends DefaultBaseService<TagLib> implements TagLibService {
    /**
     * 根据提供的标签数据判断 标签库中是否有该标签数据,无则创建该标签
     *
     * @param tag 提供的标签数据
     * @return 标签库中的标签数据
     */
    @Override
    public TagLib getTagLib(TagLib tag) {
        SqlQuery<TagLib> tagsql = SqlQuery.from(TagLib.class)
                .equal(TagLibInfo.TAGNAME, tag.getTagName())
                .equal(TagLibInfo.CTYPE, tag.getCtype())
                .equal(TagLibInfo.STTYPE, tag.getStType());
        TagLib tagLib = selectOne(tagsql);
        if (tagLib == null) {
            tag.setId(UUID.randomUUID().toString());
            tagLib = tag;
            insert(tag);
        }
        return tagLib;
    }

    @Override
    public List<Map> selectParents() {
        List<Map> mapList = commonMapper.selectByQuery(SqlQuery.from(TagLib.class, false)
                .column(TagLibInfo.ID, TagLibInfo.FULLLABLE)
                .equal(TagLibInfo.CTYPE, "gt")
                .equal(TagLibInfo.LEAF, "0")
                .orderByDesc(TagLibInfo.CREATEDATE)
                .setReturnClass(Map.class));
        return mapList;
    }

    public List<TagLib> selectTagList(TagLib tagLib) {
        List<TagLib> tagLibs = commonMapper.selectByQuery(SqlQuery.from(TagLib.class));
        return tagLibs;
    }

    @Override
    public String updateTagState(TagLib tagLib) {
        tagLib.setStatus("1");//1 代表通过启用
        long l = commonMapper.updateIgnoreNullById(tagLib);
        return "完成";
    }
}
