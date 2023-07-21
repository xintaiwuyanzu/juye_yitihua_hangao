package com.dr.archive.event;

/**
 * 档案表单状态数据更新事件
 *
 * @author dr
 */
public class ArchiveDataDeleteEvent extends BaseArchiveDataEvent {

    public ArchiveDataDeleteEvent(String formDefinitionId, String dataId) {
        super(null, formDefinitionId, dataId);
    }
}
