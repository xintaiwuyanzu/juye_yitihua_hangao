package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 档案移交或者
 *
 * @author dr
 */
public abstract class AbstractArchiveHandOverProcessProvider implements ProcessTypeProvider {
    @Autowired
    protected ArchiveBatchHandOverService handOverService;
    @Autowired
    protected ArchiveBatchHandOverLibService libService;
    @Autowired
    protected TaskInstanceService taskInstanceService;


}
