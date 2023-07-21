package com.dr.archive.event;

import com.dr.framework.common.form.core.model.FormData;

/**
 * 档案表单数据添加事件
 *
 * @author dr
 */
public class ArchiveDataAddEvent extends BaseArchiveDataEvent {
    /**
     * 表单数据
     */
    private final FormData data;

    public ArchiveDataAddEvent(String categoryId, FormData data) {
        super(categoryId, data.getFormDefinitionId(), data.getId());
        this.data = data;
    }

    public FormData getData() {
        return data;
    }
}
