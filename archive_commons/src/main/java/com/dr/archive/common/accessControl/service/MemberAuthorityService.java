package com.dr.archive.common.accessControl.service;

import com.dr.archive.common.accessControl.entity.MemberAuthority;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:35
 */
public interface MemberAuthorityService extends BaseService<MemberAuthority> {
    void setEntity(MemberAuthority entity);
//    List<Fond> getFond();
//    List<Category> getAllCategory();
//    List<MemberAuthority> getMemberAuthority(String fondCode);
}
