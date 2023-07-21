package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.ZfjcSpecialist;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ZfjcSpecialistService extends BaseService<ZfjcSpecialist> {
    List<ZfjcSpecialist> getZfjcSpecialistListByDefaultOrgId(String orgId);
}
