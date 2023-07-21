package com.dr.archive.utilization.consult.controller;

import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveConsultOutUserService;
import com.dr.archive.utilization.consult.vo.TempUser;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.exception.NeedLoginException;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 查档外部登录用户对应接口
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/utilization/consult/user")
public class ConsultOutUserController extends BaseServiceController<ArchiveConsultOutUserService, ArchiveBatchConsult> {
    final static String TEMP_USER_KEY = "tempUser";


    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public ResultEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResultEntity.success("退出成功");
    }

    /**
     * 外部查档登录用户登录方法  返回某一次查档批次
     *
     * @param idNo
     * @return
     */
    @PostMapping("/validate")
    public ResultEntity<ArchiveBatchConsult> validate(String idNo, HttpSession session) {
        TempUser tempUser = new TempUser();
        session.setAttribute(TEMP_USER_KEY, tempUser);
        ArchiveBatchConsult batchConsult = service.selectByIdNo(idNo);
        if (batchConsult != null) {
            tempUser.setIdNo(batchConsult.getIdNo());
            session.setAttribute(TEMP_USER_KEY, tempUser);
        }
        return ResultEntity.success(service.selectByIdNo(idNo));
    }

    @Override
    protected SqlQuery<ArchiveBatchConsult> buildPageQuery(HttpServletRequest request, ArchiveBatchConsult entity) {
        TempUser tempUser = getTempUser(request);
        SqlQuery<ArchiveBatchConsult> sideSqlQuery = SqlQuery.from(ArchiveBatchConsult.class)
                .equal(ArchiveBatchConsultInfo.IDNO, tempUser.getIdNo())
                .orderByDesc(ArchiveBatchConsultInfo.CREATEDATE);
        return sideSqlQuery;
    }

    /**
     * 获取临时账户
     *
     * @param request
     * @return
     */
    protected TempUser getTempUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new NeedLoginException("用户未登录");
        }
        TempUser tempUser = (TempUser) session.getAttribute(TEMP_USER_KEY);
        if (tempUser == null) {
            throw new NeedLoginException("用户未登录");
        }
        return tempUser;
    }


}
