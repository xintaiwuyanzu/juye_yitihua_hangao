
package com.dr.archive.kufang.archives.controller;

import com.dr.archive.kufang.archives.entity.Holding;
import com.dr.archive.kufang.archives.entity.HoldingInfo;
import com.dr.archive.kufang.archives.service.HoldingService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 档案馆藏 Controller
 *
 * @author dr
 */

@RestController
@RequestMapping("/api/holding")
public class HoldingController extends BaseController<Holding> {
    @Autowired
    HoldingService holdingService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<Holding> sqlQuery, Holding entity) {
        sqlQuery.like(HoldingInfo.CP005, entity.getCp005())
                .like(HoldingInfo.CN084, entity.getCn084())
                .orderByDesc(HoldingInfo.CREATEDATE);
    }


    /**
     * 根据位置信息查询图书
     *
     * @param bookCaseNo 书架号
     * @param ABsurface  AB面
     * @param layer      层数
     * @param libId      图书馆
     * @param locId      馆藏地
     * @return
     */
    /*@RequestMapping("/getBooksBybookCaseNo")
    public ResultEntity getBooksBybookCaseNo(String bookCaseNo, String ABsurface, int layerNo, int layer, String libId, String locId) {
        return ResultEntity.success(holdingService.getBooksBybookCaseNo(bookCaseNo, ABsurface, layerNo, layer, libId, locId));
    }*/


    /***
     * 修改馆藏的馆藏室
     */
//    @RequestMapping("/updateLibAndLoc")
//    public ResultListEntity updateLibAndLoc(@Current Person person, String barCode, String libId, String locId) {
//        Holding holding = holdingService.getHoldingbyCode(barCode);
//        holdingService.updateLibAndLoc(barCode, libId, locId, person.getId(), person.getUserName());
//        return ResultListEntity.success();
//    }


}
