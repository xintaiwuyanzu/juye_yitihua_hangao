package com.dr.archive.common.dataBase.controller;

import com.dr.archive.common.dataBase.entity.DataBaseBackUp;
import com.dr.archive.common.dataBase.entity.DataBaseBackUpInfo;
import com.dr.archive.common.dataBase.service.DataBaseBackUpService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lirs
 * @date 2022/7/22 14:25
 */
@RestController
@RequestMapping("${common.api-path:/api}/dataBase")
public class DataBaseBackUpController extends BaseServiceController<DataBaseBackUpService, DataBaseBackUp> {

    /**
     * 数据库备份
     *
     * @param request
     * @param person
     * @param dataBaseBackUp
     * @return
     */
    @RequestMapping("/backup")
    public ResultEntity dataBaseBackup(HttpServletRequest request, @Current Person person, DataBaseBackUp dataBaseBackUp) {
        service.dbBackUp(request, person, dataBaseBackUp);
        return ResultEntity.success();
    }

    /**
     * 数据库恢复
     *
     * @param person
     * @param dataBaseBackUp
     * @return
     */
    @RequestMapping("/recover")
    public ResultEntity dataBaseRecover(@Current Person person, DataBaseBackUp dataBaseBackUp) {
        service.dbRecover(person, dataBaseBackUp);
        return ResultEntity.success();
    }

    @Override
    protected SqlQuery<DataBaseBackUp> buildPageQuery(HttpServletRequest request, DataBaseBackUp entity) {
        return SqlQuery.from(DataBaseBackUp.class)
                .like(DataBaseBackUpInfo.BACKUPNAME, entity.getBackUpName())
                .equal(DataBaseBackUpInfo.PERSONNAME, entity.getPersonName())
                .equal(DataBaseBackUpInfo.RECOVERPERSONNAME, entity.getRecoverPersonName())
                .like(DataBaseBackUpInfo.FILENAME, entity.getFileName())
                .orderByDesc(DataBaseBackUpInfo.CREATEDATE);
    }

}
