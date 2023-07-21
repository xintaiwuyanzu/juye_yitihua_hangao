package com.dr.archive.controller;

import com.dr.archive.entity.BorrowingStrategy;
import com.dr.archive.entity.BorrowingStrategyInfo;
import com.dr.archive.service.BorrowingService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lych
 * @date 2023/2/1 上午 11:17
 * @mood happy
 *
 * 申请表
 */
@RestController
@RequestMapping("/api/Borrowing")
public class BorrowingController extends BaseServiceController<BorrowingService, BorrowingStrategy> {
    /**
     * page 可以选择查看已启用策略，根据机构筛选策略
     * @param request
     * @param entity
     * @return
     */
    @Override
    protected SqlQuery<BorrowingStrategy> buildPageQuery(HttpServletRequest request, BorrowingStrategy entity) {
        SqlQuery<BorrowingStrategy> from = SqlQuery.from(BorrowingStrategy.class);

        if (StringUtils.hasText(entity.getStrategyName())){
            from.like(BorrowingStrategyInfo.STRATEGYNAME,entity.getStrategyName());
        }
        //查询启用
        if(StringUtils.hasText(entity.getEnableOrNot())&&entity.getEnableOrNot().equals("1")){
            from.equal(BorrowingStrategyInfo.ENABLEORNOT,entity.getEnableOrNot());
        }
        //获取当前登录人机构  根据机构筛选。
        //机构信息
        Organise organise = SecurityHolder.get().currentOrganise();
        Person person = SecurityHolder.get().currentPerson();
        if(organise!=null){
            from.equal(BorrowingStrategyInfo.ORGANISEID,organise.getId());
        }else if(!"admin".equals(person.getId())){
            //如果没有机构,又不是admin的话则不允许查看信息
            from.equal(BorrowingStrategyInfo.ORGANISEID,"111");
        }
        from.orderByDesc(BorrowingStrategyInfo.CREATEDATE);
        return from;
    }
}
