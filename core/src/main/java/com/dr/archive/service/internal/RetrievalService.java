package com.dr.archive.service.internal;

import com.dr.framework.common.entity.IdEntity;

import java.util.List;

/**
 * 描述：es索引库准备数据接口
 *
 * @author tuzl
 * @date 2020/6/22 10:13
 */
public interface RetrievalService {

    /**
     * 功能描述: 将要向索引库中添加的数据的获取方法
     *
     * @param
     * @return : {@link List<? extends   IdEntity >}
     * @author : tzl
     * @date : 2020/7/22 16:44
     */
    List<IdEntity> findAddEsData();
}
