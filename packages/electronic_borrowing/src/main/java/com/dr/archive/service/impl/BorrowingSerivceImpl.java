package com.dr.archive.service.impl;

import com.dr.archive.entity.BorrowingStrategy;
import com.dr.archive.service.BorrowingService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author lych
 * @date 2023/2/1 上午 11:18
 * @mood happy
 */
@Service
public class BorrowingSerivceImpl extends DefaultBaseService<BorrowingStrategy> implements BorrowingService {

    @Autowired
    CommonService commonService;

    @Override
    public long insert(BorrowingStrategy entity) {
        //初始化启用状态。设置为不启用
        entity.setEnableOrNot("0");
        //添加机构信息
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise!=null){
            entity.setOrganiseId(organise.getId());
            entity.setOrganiseName(organise.getOrganiseName());
        }else {
            Assert.isTrue(false,"当前用户没有机构，不能创建策略");
        }
        return commonService.insert(entity);
    }

}
