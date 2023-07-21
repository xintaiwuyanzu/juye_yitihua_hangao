package com.dr.archive.receive.online.service.impl;

import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.archive.receive.online.service.ArchiveReceiveOnlineSysManageService;
import com.dr.archive.receive.online.service.OnlineReceiveBatchContext;
import com.dr.archive.receive.online.service.OnlineReceiveContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认在线接收上下文构造器
 *
 * @author dr
 */
@Component
public class DefaultOnlineReceiveContextBuilder implements OnlineReceiveContextBuilder {
    @Autowired
    ArchiveReceiveOnlineSysManageService archiveReceiveOnlineSysManageService;

    @Override
    public OnlineReceiveBatchContext buildReceiveContext(HttpServletRequest request, HttpServletResponse response, ArchiveReceiveBo receiveBo) {
        //在线接收上下文
        OnlineReceiveBatchContext context = new OnlineReceiveBatchContext();
        context.setRequest(request);
        context.setResponse(response);
        context.setArchiveReceiveBo(receiveBo);
        //在线接收的系统编码
        //String sysCode = request.getParameter("sysCode");
        String sysCode = receiveBo.getFiles().get(0).getSystemNum();

        ArchiveReceiveOnlineSysManage sysManage = archiveReceiveOnlineSysManageService.selectBySysCode(sysCode);
        context.setSysManage(sysManage);
        return context;
    }
}
