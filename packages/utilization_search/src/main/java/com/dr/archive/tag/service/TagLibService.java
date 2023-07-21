package com.dr.archive.tag.service;

import com.dr.archive.tag.entity.TagLib;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author: qiuyf
 * @date: 2022/3/15 11:30
 */
public interface TagLibService extends BaseService<TagLib> {
    /**
     * 根据提供的标签数据判断 标签库中是否有该标签数据,无则创建该标签
     * @param tag 提供的标签数据
     * @return 标签库中的标签数据
     */
    TagLib getTagLib(TagLib tag);

    List<Map> selectParents();

    List<TagLib> selectTagList(TagLib tagLib);

    String updateTagState(TagLib tagLib);
}
