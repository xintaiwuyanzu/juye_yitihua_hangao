package com.dr.archive.manage.form.service;

import com.dr.archive.manage.form.entity.MoveGoRecording;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

public interface MoveGoRecordingService extends BaseService<MoveGoRecording> {
    void updateCategoryByQuery(ArchiveDataQuery query, String newCategoryId, String oldCategoryId, Person person);
}
