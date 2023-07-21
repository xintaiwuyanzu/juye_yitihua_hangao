package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntity;
import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntityInfo;
import com.dr.archive.common.dzdacqbc.service.CqbcLogService;
import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.util.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author hyj
 */
@RequestMapping(value = "/api/cqbclog")
@RestController
public class CqbcLogController extends BaseController<CqbcLogEntity> {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    CqbcLogService cqbcLogService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<CqbcLogEntity> sqlQuery, CqbcLogEntity entity) {
        if (!StringUtils.isEmpty(entity.getCreatePerson()) && !StringUtils.isEmpty(entity.getUpdatePerson())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date start = simpleDateFormat.parse(entity.getCreatePerson());
                Date end = simpleDateFormat.parse(entity.getUpdatePerson());
                sqlQuery.between(CqbcLogEntityInfo.CREATEDATE, start.getTime(), end.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        sqlQuery.like(CqbcLogEntityInfo.OPERATEPERSON, entity.getOperatePerson()).orderByDesc(CqbcLogEntityInfo.CREATEDATE);
        super.onBeforePageQuery(request, sqlQuery, entity);
    }

    @RequestMapping("expLog")
    public void expLog(HttpServletResponse response, String createPerson, String updatePerson, String operatePerson) {
        SqlQuery<CqbcLogEntity> sqlQuery = SqlQuery.from(CqbcLogEntity.class);
        if (!StringUtils.isEmpty(createPerson) && !StringUtils.isEmpty(updatePerson)) {
            sqlQuery.between(CqbcLogEntityInfo.CREATEDATE,
                    DateTimeUtils.stringToMillis(createPerson, "yyyy-MM-dd HH:mm:ss"),  DateTimeUtils.stringToMillis(updatePerson, "yyyy-MM-dd HH:mm:ss"));
        }
        if(!StringUtils.isEmpty(operatePerson)){
            sqlQuery.like(CqbcLogEntityInfo.OPERATEPERSON, operatePerson);
        }
        sqlQuery.orderByDesc(new Column(Constants.DZDNCQBC + "log", "createDate", ""));
        List<CqbcLogEntity> list = commonMapper.selectByQuery(sqlQuery);
        String[] columnNames = {"操作", "操作人", "操作时间", "ip地址"};
        String[] columnCodes = {"content", "operatePerson", "createDate", "ip"};
        String[] convertFields = {};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "日志", columnNames, list, columnCodes, convertFields);
    }

    @RequestMapping("operation")
    public ResultEntity operation(){
        Map<String,Long> map= cqbcLogService.operation();
        return ResultEntity.success(map);
    }
}
