package com.dr.archive.manage.handover.controller;

import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.archive.util.Constants;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 到期移交批次controller
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/manage/handover"})
public class ArchiveBatchHandOverController extends BaseServiceController<ArchiveBatchHandOverService, ArchiveBatchHandOver> {

    /**
     * 档案馆接收或者不接受
     *
     * @param person
     * @param archiveBatchHandOver
     * @return
     */
    @PostMapping("/updateDag")
    public ResultEntity<ArchiveBatchHandOver> updateDag(@Current Person person, ArchiveBatchHandOver archiveBatchHandOver) {
        return ResultEntity.success(service.updateDag(person, archiveBatchHandOver));
    }

    @Override
    protected SqlQuery<ArchiveBatchHandOver> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBatchHandOver archiveBatchHandOver) {
        SqlQuery<ArchiveBatchHandOver> sqlQuery = SqlQuery.from(ArchiveBatchHandOver.class);
        Person person = getUserLogin(httpServletRequest);
        //如果是档案馆类型，只查询接收单位是本单位的数据
        Organise organise = BaseController.getOrganise(httpServletRequest);
        if (Constants.ORG_TYPE_DAG.equals(organise.getOrganiseType())) {
            //如果是档案馆，根据机构id查询数据
            sqlQuery.equal(ArchiveBatchHandOverInfo.TARORGID, organise.getId())
                    .in(ArchiveBatchHandOverInfo.STATUS,
                            ArchiveBatchHandOverService.STATUS_RECEIVE_DAG,
                            ArchiveBatchHandOverService.STATUS_AUDIT_DAG,
                            ArchiveBatchHandOverService.STATUS_DONE);
        } else {
            //如果是档案室，根据创建人查询数据
            sqlQuery.equal(ArchiveBatchHandOverInfo.CREATEPERSON, person.getId());
        }
        return sqlQuery.orderByDesc(ArchiveBatchHandOverInfo.CREATEDATE);
    }

    /**
     * 创建移交批次
     *
     * @param person
     * @param organise
     * @param fondId
     * @param libIds
     * @param type
     * @return
     */
    @PostMapping("/newBatch")
    public ResultEntity<ArchiveBatchHandOver> newBatch(@Current Person person, @Current Organise organise, String fondId, String libIds, String type) {
        return ResultEntity.success(service.newBatch(person, organise, fondId, libIds, type));
    }

    /**
     * 总数
     */

    @RequestMapping("/total")
    public ResultEntity total() {
        return ResultEntity.success(service.total());
    }

    @RequestMapping("/organiseType")
    public ResultEntity organiseType(@Current Organise organise) {
        return ResultEntity.success(organise);
    }
}
