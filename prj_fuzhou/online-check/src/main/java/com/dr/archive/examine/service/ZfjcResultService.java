package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.ZfjcCheckResult;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ZfjcResultService extends BaseService<ZfjcCheckResult> {


    List<ZfjcCheckResult> getCheckResultListByPId(String pId);

    List<ZfjcCheckResult> getCheckResultListByPIdAndCateNum(String pId, String num);
}
