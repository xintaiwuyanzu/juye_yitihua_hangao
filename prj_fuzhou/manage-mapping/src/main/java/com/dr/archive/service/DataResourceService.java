package com.dr.archive.service;

import com.dr.archive.entity.DataResource;
import com.dr.archive.entity.task.Task;
import com.dr.framework.common.service.BaseService;

/**
 * @author: yang
 * @create: 2022-06-10 17:31
 **/
public interface DataResourceService extends BaseService<DataResource> {
    long transform(String result, Task task);

    long saveRelationResult(String resultStr, Task task);

    long saveRelationResultPre(String result);

    long transformPre(String result);
}
