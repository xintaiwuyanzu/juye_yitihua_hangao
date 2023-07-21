package com.dr.archive.service.impl;

import com.dr.archive.entity.*;
import com.dr.archive.service.RegistrationDetailsService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author lych
 * @date 2023/2/3 上午 9:15
 * @mood happy
 */
@Service
public class RegistrationDetailsServiceImpl extends DefaultBaseService<BorrowingRegistrationDetails> implements RegistrationDetailsService {

    @Autowired
    CommonService commonService;
    /**
     * 申请人查看申请详情按钮功能
     * 带有策略时间判断
     * @param id
     * @return
     */
    @Override
    public ResultEntity check(String id) {

        //根据id查询出申请记录，
        SqlQuery<BorrowingRegistration> equal = SqlQuery.from(BorrowingRegistration.class).equal(BorrowingRegistrationInfo.ID, id);
        BorrowingRegistration registration = commonMapper.selectOneByQuery(equal);

        //根据申请记录 查询出策略
        BorrowingStrategy borrowingStrategy = commonMapper.selectOneByQuery(SqlQuery.from(BorrowingStrategy.class).equal(BorrowingStrategyInfo.ID, registration.getStrategyId()));

        //根据策略  当前时间 - 申请记录批准时间 <  策略时间，大于提示 已经超时，请再次申请
        long eveTiem = System.currentTimeMillis();
        Long borrowingPeriod = borrowingStrategy.getBorrowingPeriod();
        if ((eveTiem - registration.getFinishTime())<86400000*borrowingPeriod){
            //查询详情返回
            Page<BorrowingRegistrationDetails> borrowingRegistrationDetailsPage = commonMapper.selectPageByQuery(SqlQuery.from(BorrowingRegistrationDetails.class).equal(BorrowingRegistrationDetailsInfo.BORROWINGID, id), 0, 15);
            return ResultEntity.success(borrowingRegistrationDetailsPage);
        }else {
            return ResultEntity.error("查阅时间已结束，如还需要请重新提交申请");
        }
    }

    @Override
    public long insert(BorrowingRegistrationDetails entity) {
        entity.setId(null);
        List<BorrowingRegistrationDetails> list = commonMapper.selectByQuery(
                SqlQuery.from(BorrowingRegistrationDetails.class)
                        .equal(BorrowingRegistrationDetailsInfo.BORROWINGID, entity.getBorrowingId())
                        .equal(BorrowingRegistrationDetailsInfo.ARCHIVE_CODE, entity.getARCHIVE_CODE()));
        Assert.isTrue(list.size()<=0,"该档案已添加，请勿重复添加");
        return this.commonService.insert(entity);
    }


}
