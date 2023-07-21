package com.dr.archive.manage.impexpscheme.service.impl;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailServiceImpl;
import com.dr.archive.formMap.service.FormKeyMapService;
import com.dr.archive.formMap.service.DataParser;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 带有数据转换的service
 *
 * @author dr
 */
public abstract class AbstractDataParserArchiveBatchDetailService<D extends AbstractBatchDetailEntity> extends AbstractArchiveBatchDetailServiceImpl<D> {
    protected DataParser dataParser;
    @Autowired
    protected CommonFileConfig commonFileConfig;
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected FormKeyMapService formKeyMapService;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        dataParser = new DataParserComposite(applicationContext.getBeansOfType(DataParser.class).values());
    }
}

