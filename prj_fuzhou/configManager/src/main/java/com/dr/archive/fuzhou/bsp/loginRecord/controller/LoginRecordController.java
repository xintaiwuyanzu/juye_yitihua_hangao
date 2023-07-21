package com.dr.archive.fuzhou.bsp.loginRecord.controller;

import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginRecord;
import com.dr.archive.fuzhou.bsp.loginRecord.service.LoginRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lych
 * @date 2023/1/31 上午 11:42
 * @mood happy
 */
@RestController
@RequestMapping("api/LoginRecord")
public class LoginRecordController extends BaseServiceController<LoginRecordService, LoginRecord> {
    @Override
    protected SqlQuery<LoginRecord> buildPageQuery(HttpServletRequest request, LoginRecord entity) {
        return SqlQuery.from(LoginRecord.class);
    }
    @RequestMapping("maxLoginNumber")
    public ResultEntity maxLoginNumber(){
        Map<String,Long> map = service.maxLoginNumber();
        return ResultEntity.success(map);
    }
    @RequestMapping("offLine")
    public ResultEntity offLine(){
        service.offLine();
        return ResultEntity.success();
    }
}
