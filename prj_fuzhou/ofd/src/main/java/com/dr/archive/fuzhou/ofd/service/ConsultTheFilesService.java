package com.dr.archive.fuzhou.ofd.service;

import com.dr.framework.core.organise.entity.Person;

/**
 * @Author: caor
 * @Date: 2021-12-24 19:38
 * @Description:
 */
public interface ConsultTheFilesService {
    /**
     * 原文预览，调用ofd云阅读，根据formDataId，查询第一条原文，多的过滤，无原文的返回原文不存在
     *
     * @param refId
     * @param person
     * @return
     */
    String filePreview(String refId, Person person);
}
