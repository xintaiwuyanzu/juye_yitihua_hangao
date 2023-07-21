package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.ZfjcCheck;
import com.dr.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ZfjcCheckService extends BaseService<ZfjcCheck> {
    String JDJC_ZFJC_DICT_JCNR = "zfjc.type";

    void change(ZfjcCheck entity, String admin);

    void updateResult(String result, String pId, String param, String opinion, String reply, String infoId);

    void banjie(String id);

    void exptz(HttpServletRequest request, HttpServletResponse response, String id);
}
