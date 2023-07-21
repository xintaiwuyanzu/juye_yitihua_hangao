package com.dr.archive.service.internal;

import com.dr.archive.model.query.CategoryFormQuery;

import java.util.Map;

/**
 * 描述：档案管理方面接口
 *
 * @author tuzl
 * @date 2020/6/19 10:07
 */
public interface ArchiveManageConfigService {

    /**
     * 功能描述: 组件
     * 1.根据分类查询信息查询组件的档案分类表单信息
     * 2.根据分类获取表单id
     * 3.根据表单id获取操作对象表
     * 4.在操作对象表中添加一条新数据 newArchive
     * 5.件原来数据对应的附件与新数据关联
     *
     * @param categoryFormQuery 分类查询信息
     * @param ids               组件的档案id集合
     * @param newArchive        新档案
     * @return :
     * @author : tzl
     * @date : 2020/6/19 10:12
     */
    void composedFile(CategoryFormQuery categoryFormQuery, String ids, Map<String, String> newArchive);
}
