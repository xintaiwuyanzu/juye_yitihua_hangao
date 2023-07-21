package com.dr.archive.common.dataBase.service;


import com.dr.archive.common.dataBase.entity.DataBaseBackUp;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lirs
 * @date 2022/7/25 14:58
 */
public interface DataBaseBackUpService extends BaseService<DataBaseBackUp> {
    /**
     * 备份
     *
     * @param request
     * @param person
     * @param dataBaseBackUp
     * @return
     */
    void dbBackUp(HttpServletRequest request, Person person, DataBaseBackUp dataBaseBackUp);

    /**
     * 恢复
     * @param person
     * @param dataBaseBackUp
     */
    void dbRecover(Person person, DataBaseBackUp dataBaseBackUp);
}
