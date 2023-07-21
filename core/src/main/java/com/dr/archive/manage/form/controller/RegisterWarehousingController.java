package com.dr.archive.manage.form.controller;

import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetailsInfo;
import com.dr.archive.manage.form.entity.RegisterWarehousingInfo;
import com.dr.archive.manage.form.service.RegisterWarehousingDetailsService;
import com.dr.archive.manage.form.service.RegisterWarehousingService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("api/registerWarehousing")
public class RegisterWarehousingController extends BaseServiceController<RegisterWarehousingService, RegisterWarehousing> {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    RegisterWarehousingDetailsService registerWarehousingDetailsService;
    @Autowired
    RegisterWarehousingService registerWarehousingService;
    private String url = "";

    @Override
    protected SqlQuery<RegisterWarehousing> buildPageQuery(HttpServletRequest request, RegisterWarehousing entity) {
        SqlQuery<RegisterWarehousing> sqlQuery = SqlQuery.from(RegisterWarehousing.class);
        if (entity.getRecord_name() != null) {
            sqlQuery.like(RegisterWarehousingInfo.RECORD_NAME, entity.getRecord_name());
        }
        Person person = SecurityHolder.get().currentPerson();
        if (person != null) {
            sqlQuery.equal(RegisterWarehousingInfo.CREATEPERSON, person.getId());
        }
        sqlQuery.orderByDesc(RegisterWarehousingInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping("selects")
    public ResultEntity selects() {
        List<RegisterWarehousing> registerWarehousings = commonMapper.selectByQuery(SqlQuery.from(RegisterWarehousing.class));
        return ResultEntity.success(registerWarehousings);
    }

    @RequestMapping("/download")
    public ResultEntity download(String id) throws IOException,DocumentException {

        SqlQuery<RegisterWarehousingDetails> sqlQuerys = SqlQuery.from(RegisterWarehousingDetails.class).equal(RegisterWarehousingDetailsInfo.BID, id);
        SqlQuery<RegisterWarehousing> sqlQuery = SqlQuery.from(RegisterWarehousing.class).equal(RegisterWarehousingInfo.ID, id);
        RegisterWarehousing registerWarehousing = commonMapper.selectOneByQuery(sqlQuery);
        List<RegisterWarehousingDetails> registerWarehousingDetails = registerWarehousingDetailsService.selectList(sqlQuerys);
        url = registerWarehousingService.processInformation(registerWarehousing, registerWarehousingDetails);


        return "".equals(url) ? ResultEntity.error("") : ResultEntity.success();
    }

    @RequestMapping("registerPDF")
    public void registerPDF(HttpServletResponse response) {
        if (url == null) {
            return;
        }
        try (InputStream in = new FileInputStream(url)) {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(UUIDUtils.getUUID() + ".pdf", "UTF-8"));
            StreamUtils.copy(in, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
