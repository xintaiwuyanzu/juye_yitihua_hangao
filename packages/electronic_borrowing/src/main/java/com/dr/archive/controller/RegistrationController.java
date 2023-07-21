package com.dr.archive.controller;

import com.dr.archive.entity.BorrowingRegistration;
import com.dr.archive.entity.BorrowingRegistrationInfo;
import com.dr.archive.service.RegistrationService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.entity.Role;
import com.dr.framework.core.security.service.SecurityManager;
import com.dr.framework.util.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * @author lych
 * @date 2023/2/1 上午 11:52
 * @mood happy
 */
@RestController
@RequestMapping("api/Registration")
public class RegistrationController extends BaseServiceController<RegistrationService, BorrowingRegistration> {
    @Autowired
    SecurityManager securityManager;

    /**
     * 筛选机构查询，申请人查询方法
     *
     * @param request
     * @param entity
     * @return
     */
    @Override
    protected SqlQuery<BorrowingRegistration> buildPageQuery(HttpServletRequest request, BorrowingRegistration entity) {
        SqlQuery<BorrowingRegistration> from = SqlQuery.from(BorrowingRegistration.class);

        //根据机构，根据用户筛选数据
        //获取当前登录人机构  根据机构筛选。
        //机构信息
        Organise organise = SecurityHolder.get().currentOrganise();
        Person person = SecurityHolder.get().currentPerson();

        //admin 的机构是root 目前超级管理员不能查看，如果查看需要改这个即可
        if (organise != null) {
            from.equal(BorrowingRegistrationInfo.ORGANIZEID, organise.getId());
        } else if (!"admin".equals(person.getId())) {
            //如果没有机构,又不是admin的话则不允许查看信息
            from.equal(BorrowingRegistrationInfo.ORGANIZEID, "111");
        }

        //根据申请人筛选
        if (StringUtils.hasText(entity.getApplicant())){
            from.like(BorrowingRegistrationInfo.APPLICANT,entity.getApplicant());
        }

        //根据关键字筛选
        if (StringUtils.hasText(entity.getKeywordString())){
            from.like(BorrowingRegistrationInfo.KEYWORDSTRING,entity.getKeywordString());
        }
        //根据策略筛选
        if (StringUtils.hasText(entity.getStrategyId())){
            from.equal(BorrowingRegistrationInfo.STRATEGYID,entity.getStrategyId());
        }

        //根据编号筛选
        if (StringUtils.hasText(entity.getBorrowingCode())){
            from.equal(BorrowingRegistrationInfo.BORROWINGCODE,entity.getBorrowingCode());
        }

        if (person != null) {
            //是否是查档账号
            from.equal(BorrowingRegistrationInfo.APPLICANTID, person.getId());

        }
        from.orderByDesc(BorrowingRegistrationInfo.CREATEDATE);

        return from;
    }

    /**
     * 档案查阅人员查询的详情信息。
     * @return
     */
    @RequestMapping("processPage")
    public ResultEntity selectPage(BorrowingRegistration entity,
                                   @RequestParam(defaultValue = "0") int pageIndex,
                                   @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize){
        SqlQuery<BorrowingRegistration> from = SqlQuery.from(BorrowingRegistration.class);
        //根据机构，根据用户筛选数据
        //获取当前登录人机构  根据机构筛选。
        //机构信息
        Organise organise = SecurityHolder.get().currentOrganise();
        Person person = SecurityHolder.get().currentPerson();
        //根据申请人筛选
        if (StringUtils.hasText(entity.getApplicant())){
            from.like(BorrowingRegistrationInfo.APPLICANT,entity.getApplicant());
        }
        //根据关键字筛选
        if (StringUtils.hasText(entity.getKeywordString())){
            from.like(BorrowingRegistrationInfo.KEYWORDSTRING,entity.getKeywordString());
        }
        //根据策略筛选
        if (StringUtils.hasText(entity.getStrategyId())){
            from.equal(BorrowingRegistrationInfo.STRATEGYID,entity.getStrategyId());
        }
        from.equal(BorrowingRegistrationInfo.ID,entity.getId());
        from.notEqual(BorrowingRegistrationInfo.STATUS,"0");

        if (organise != null) {
            from.equal(BorrowingRegistrationInfo.ORGANIZEID, organise.getId());
        } else if (!"admin".equals(person.getId())) {
            //如果没有机构,又不是admin的话则不允许查看信息
            from.equal(BorrowingRegistrationInfo.ORGANIZEID, "111");
        }
        //获取用户所有权限
        List<Role> roles = securityManager.userRoles(person.getId());
        if (roles.size() > 0) {
            Optional<Role> any = roles.stream().filter(a -> "danganjieyue".equals(a.getCode())).findAny();
            if (!any.isPresent()) {
                from.equal(BorrowingRegistrationInfo.APPLICANTID, person.getId());
            }
        }
        from.orderByDesc(BorrowingRegistrationInfo.CREATEDATE);
        Page<BorrowingRegistration> borrowingRegistrationPage = service.selectPage(from, pageIndex, pageSize);
        return ResultEntity.success(borrowingRegistrationPage);
    }

    @RequestMapping("personData")
    public ResultEntity personData(){
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        person.setDefaultOrganiseName(organise.getOrganiseName());
        person.setDefaultOrganiseId(organise.getId());
        return ResultEntity.success(person);
    }
    /**
     * 借阅统计
     */
    @RequestMapping("/getBorrowData")
    public ResultEntity getBorrowData(String startDate, String endDate){
        return ResultEntity.success(service.getBorrowData(startDate, endDate));
    }
    /**
     * 档案借阅统计 报表导出
     */
    @RequestMapping("/expBorrowData")
    public void expBorrowData(HttpServletResponse response, String startDate, String endDate) {
        List list = service.getBorrowData(startDate, endDate);
        String[] columnNames = {"状态", "数量"};
        String[] columnCodes = {"name", "count"};
        String[] convertFields = {};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "档案借阅统计导出", columnNames, list, columnCodes, convertFields);
    }
}
