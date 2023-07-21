package com.dr.archive.utilization.search.eventListener;

import com.dr.archive.event.ArchiveDataAddEvent;
import com.dr.archive.event.ArchiveDataDeleteEvent;
import com.dr.archive.event.ArchiveDataEditEvent;

/**
 * spring代理需要声明接口
 */
interface ArchiveDataListener {
    void ArchiveDataEdit(ArchiveDataEditEvent event);

    void removeFormData(ArchiveDataDeleteEvent event);

    void addFormData(ArchiveDataAddEvent event);
}
