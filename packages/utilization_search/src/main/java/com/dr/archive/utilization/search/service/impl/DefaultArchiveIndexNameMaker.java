package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.ArchiveIndexNameMaker;
import com.dr.framework.common.form.engine.model.core.FormModel;
import org.springframework.stereotype.Component;

/**
 * 默认的表单索引名称生成器
 *
 * @author dr
 */
@Component
public class DefaultArchiveIndexNameMaker implements ArchiveIndexNameMaker {
    @Override
    public String makeIndexName(FormModel formModel) {
        return INDEX_PREFIX + formModel.getFormCode();
    }
}
