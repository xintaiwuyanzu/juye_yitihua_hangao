package com.dr.archive.common.dzdacqbc.eventListener;

import com.dr.archive.event.ArchiveDataAddEvent;
import com.dr.archive.event.ArchiveDataDeleteEvent;
import com.dr.archive.event.ArchiveDataEditEvent;


interface ArchiveChangeDataListener {
    void ArchiveDataEdit(ArchiveDataEditEvent event);

    void removeFormData(ArchiveDataDeleteEvent event);

    void addFormData(ArchiveDataAddEvent event);
}
