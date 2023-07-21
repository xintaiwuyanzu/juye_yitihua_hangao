/*
package com.dr.archive.opinion.management.controller;

import com.dr.archive.opinion.management.entity.OpinionManagement;
import com.dr.archive.opinion.management.entity.OpinionManagementInfo;
import com.dr.archive.opinion.management.service.OpinionManagementService;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

*/
/**
 * wx
 *  意见管理
 *//*

//@RestController
//@RequestMapping("/api/opinion")
public class OpinionManagementController extends BaseServiceController<OpinionManagementService, OpinionManagement> {

    @Autowired
    OpinionManagementService opinionManagementService;

    @Override
    protected SqlQuery<OpinionManagement> buildPageQuery(HttpServletRequest request, OpinionManagement entity) {
        String id = SecurityHolder.get().currentPerson().getId();
        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.BUSINESSID, id)
                .or()
                .equal(OpinionManagementInfo.DEFENABLE,"0")
                .andNew()
                .orderBy(OpinionManagementInfo.CREATEDATE);

        return equal;


    }

    @RequestMapping("/queryAllOpinion")
    public ResultEntity queryAllOpinion (){
        String id = SecurityHolder.get().currentPerson().getId();
        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.BUSINESSID, id)
                .or()
                .equal(OpinionManagementInfo.DEFENABLE,"0")
                .andNew()
                .orderBy(OpinionManagementInfo.CREATEDATE);
        return ResultEntity.success(opinionManagementService.selectList(equal));

    }

    */
/**
     * 普通删除
     * @param opinionManagement
     * @return
     *//*

    @RequestMapping("/deleteOpinion")
    public ResultEntity deleteOpinion(OpinionManagement opinionManagement){
        String id = SecurityHolder.get().currentPerson().getId();
        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.DEFENABLE, "1")
                .equal(OpinionManagementInfo.ID, opinionManagement.getId())
                .equal(OpinionManagementInfo.BUSINESSID,id);
        long delete = opinionManagementService.delete(equal);
        if(delete == 0){
            return ResultEntity.error("删除失败");
        }
         return ResultEntity.success();
    }

    */
/**
     *  普通添加数据
     * @param opinionManagement
     * @return
     *//*

    @RequestMapping("/addOpinion")
    public ResultEntity addOpinion(OpinionManagement opinionManagement){
        String id = SecurityHolder.get().currentPerson().getId();
        String s = opinionManagementService.addOpinion(opinionManagement.getOpinion(), id);
        if(!"添加完成".equals(s)){
            return ResultEntity.error(s);
        }
        return ResultEntity.success(s);
    }

    */
/**
     *  添加默认数据
     *//*

    @RequestMapping("/addDefOpinion")
    public ResultEntity addDefOpinion(OpinionManagement opinionManagement){
        String id = SecurityHolder.get().currentPerson().getId();
        String s = opinionManagementService.addDefOpinion(opinionManagement.getOpinion(), id);
        if(!"添加完成".equals(s)){
            return ResultEntity.error(s);
        }
        return ResultEntity.success(s);
    }
}
*/
