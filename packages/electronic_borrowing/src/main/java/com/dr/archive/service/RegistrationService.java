package com.dr.archive.service;

import com.dr.archive.entity.BorrowingRegistration;
import com.dr.archive.vo.CountsVo;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * @author lych
 * @date 2023/2/1 上午 11:52
 * @mood happy
 */
public interface RegistrationService extends BaseService<BorrowingRegistration> {

    List<CountsVo> getBorrowData(String startDate, String endDate);
}
