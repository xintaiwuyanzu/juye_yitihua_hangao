package com.dr.archive.receive.online.service;

import com.dr.archive.receive.online.bo.ArchiveReceiveBo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在线接收上下文构造器
 *
 * @author dr
 */
public interface OnlineReceiveContextBuilder {
    /**
     * 根据request构造在线接收上下文
     *
     * @param request
     * @param response
     * @return
     */
    OnlineReceiveBatchContext buildReceiveContext(HttpServletRequest request, HttpServletResponse response, ArchiveReceiveBo receiveBo);
}
