package com.dr.archive.service.impl;

import com.dr.archive.entity.task.Task;
import com.dr.archive.service.TaskService;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: yang
 * @create: 2022-06-17 15:32
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends DefaultBaseService<Task> implements TaskService {

}
