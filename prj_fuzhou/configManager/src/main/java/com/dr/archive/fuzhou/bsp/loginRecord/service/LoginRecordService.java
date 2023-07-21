package com.dr.archive.fuzhou.bsp.loginRecord.service;

import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginRecord;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import java.util.Map;

/**
 * @author lych
 * @date 2023/1/31 上午 11:19
 * @mood happy
 */
public interface LoginRecordService extends BaseService<LoginRecord> {
    //获取最大人数，当前人数
    Map<String, Long> maxLoginNumber();

    //登录操作,存储在线人数
    void insertPerson(Person person);

    //退出操作
    void offLine();
    //退出操作
    void offLine(Person person);
}
