package com.dr.archive.controller;

import com.dr.archive.entity.BorrowingRegistrationDetails;
import com.dr.archive.entity.BorrowingRegistrationDetailsInfo;
import com.dr.archive.service.RegistrationDetailsService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lych
 * @date 2023/2/3 上午 9:18
 * @mood happy
 */
@RestController
@RequestMapping("api/RegistrationDetails")
public class RegistrationDetailsController extends BaseServiceController<RegistrationDetailsService, BorrowingRegistrationDetails> {

    /**
     * 查看详情
     * @param request
     * @param entity
     * @return
     */
    @Override
    protected SqlQuery<BorrowingRegistrationDetails> buildPageQuery(HttpServletRequest request, BorrowingRegistrationDetails entity) {
        SqlQuery<BorrowingRegistrationDetails> from = SqlQuery.from(BorrowingRegistrationDetails.class);
        //根据申请借阅id查询详情信息
        if(StringUtils.hasText(entity.getBorrowingId())){
            from.equal(BorrowingRegistrationDetailsInfo.BORROWINGID,entity.getBorrowingId());
        }
        from.orderByDesc(BorrowingRegistrationDetailsInfo.CREATEDATE);
        return from;
    }


    /*
     申请人查看借阅详情，返回分页，过期返回错误提示信息
     */
    @RequestMapping("check")
    public ResultEntity check(String borrowingId){
        return service.check(borrowingId);
    }


}
