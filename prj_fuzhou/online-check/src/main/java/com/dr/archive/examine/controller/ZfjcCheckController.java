package com.dr.archive.examine.controller;

import com.dr.archive.examine.entity.ZfjcCheck;
import com.dr.archive.examine.entity.ZfjcCheckInfo;
import com.dr.archive.examine.entity.ZfjcCheckResult;
import com.dr.archive.examine.service.ZfjcCheckService;
import com.dr.archive.examine.service.ZfjcResultService;
import com.dr.archive.kufang.entityfiles.utils.ExportWordUtil;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执法检查
 */
@RestController
@RequestMapping("api/zfjcCheck/")
public class ZfjcCheckController extends BaseServiceController<ZfjcCheckService, ZfjcCheck> {
    @Autowired
    ZfjcResultService zfjcResultService;

    @Override
    protected SqlQuery<ZfjcCheck> buildPageQuery(HttpServletRequest httpServletRequest, ZfjcCheck zfjcCheck) {
        return SqlQuery.from(ZfjcCheck.class).like(ZfjcCheckInfo.CATEGORYNAME, zfjcCheck.getCategoryName()).like(ZfjcCheckInfo.ORGANISENAME, zfjcCheck.getOrganiseName()).like(ZfjcCheckInfo.PERSONNAME, zfjcCheck.getPersonName()).equal(ZfjcCheckInfo.CHECKTYPE, zfjcCheck.getCheckType()).like(ZfjcCheckInfo.PICI, zfjcCheck.getPici()).orderByDesc(ZfjcCheckInfo.UPDATEDATE);
    }

    @Override
    protected SqlQuery<ZfjcCheck> buildDeleteQuery(HttpServletRequest request, ZfjcCheck entity) {
        Assert.isTrue(StringUtils.hasText(entity.getId()), "要删除的数据不能为空！");
        return SqlQuery.from(ZfjcCheck.class).in(ZfjcCheckInfo.ID, entity.getId().split(","));
    }

    /**
     * 我的公示单
     *
     * @param request
     * @param entity
     * @param pageIndex
     * @param pageSize
     * @param page
     * @return
     */
    @RequestMapping({"/myZfjcCheck"})
    public ResultEntity myZfjcCheck(HttpServletRequest request, ZfjcCheck entity, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        SqlQuery<ZfjcCheck> sqlQuery = SqlQuery.from(ZfjcCheck.class, true);
        sqlQuery.equal(ZfjcCheckInfo.PUBLISH, "1").or().equal(ZfjcCheckInfo.CREATEPERSON, SecurityHolder.get().currentPerson().getId()).or().equal(ZfjcCheckInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId());  //执法检查机构等于当前登录人默认机构
        sqlQuery.andNew().equal(ZfjcCheckInfo.CHECKTYPE, entity.getCheckType());
//        this.buildPageQuery(request, sqlQuery, entity);
        Object result;
        if (page) {
            result = service.selectPage(sqlQuery, pageIndex, pageSize);
        } else {
            result = service.selectList(sqlQuery);
        }
        return ResultEntity.success(result);
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @RequestMapping("change")
    public ResultEntity change(ZfjcCheck entity) {
        String id = SecurityHolder.get().currentPerson().getId();
        service.change(entity, id);
        return ResultEntity.success();
    }

    /**
     * 新增执法单
     *
     * @param
     * @param id
     * @return
     */
    @RequestMapping("banjie")
    public ResultEntity banjie(String id) {
        service.banjie(id);
        return ResultEntity.success();
    }

    /**
     * 新增检查结果单
     *
     * @param result json格式的list<map>
     * @param param  0 只保存结果  1 保存结果并通知被检查单位调整
     * @return
     */
    @RequestMapping("updateResult")
    public ResultEntity updateResult(String result, String pId, String param, String opinion, String reply, String infoId) {
        service.updateResult(result, pId, param, opinion, reply, infoId);
        return ResultEntity.success();
    }


    /**
     * 获取档案数量 todo 假数据
     *
     * @return
     */
    @RequestMapping("loadArriveCount")
    public ResultEntity loadArriveCount() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ws", "123056");
        map.put("sx", "23056");
        map.put("kj", "53056");
        map.put("zy", "13056");
        map.put("dz", "283056");
        map.put("qt", "183156");
        return ResultEntity.success(map);
    }

    /**
     * 导出通知
     *
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping("/exptz")
    public void exptz(HttpServletRequest request, HttpServletResponse response, String id) {
        service.exptz(request, response, id);
    }


    /**
     * 导出检查公式
     *
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping("/expgs")
    public void expgs(HttpServletRequest request, HttpServletResponse response, String id) {
        OutputStream out = null;

        ZfjcCheck zfjcCheck = service.selectById(id);
        List<ZfjcCheckResult> resultList = zfjcResultService.getCheckResultListByPId(id);
        //todo 检查项 这里搞成动态的 就可以适配多种 检查类别

        try {
            out = response.getOutputStream();

            //
            Map<String, Object> map = new HashMap<String, Object>();
            //

            map.put("title", zfjcCheck.getCategoryName());//检查项
            map.put("org", zfjcCheck.getOrganiseName());//被检查机构
            map.put("persons", zfjcCheck.getPersonName());//检查人
            map.put("opinion", zfjcCheck.getOpinion());//整改意见

            map.put("itemList", resultList);//整改意见

            ExportWordUtil word = new ExportWordUtil("/word", "gongshi.ftl");
            word.setHeader(request, response, "检查报告.doc");
            word.doExport(out, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出 通报（县区）
     *
     * @param request
     * @param response
     * @param ids
     */
    @RequestMapping("/exptb")
    public void exptb(HttpServletRequest request, HttpServletResponse response, String ids) {
        OutputStream out = null;


        String[] idArr = ids.split(",");
        //完成时间
        long wanchengTime = 0;
        //执行检查时间
        long shijiTime = 7953496417000L;
        //批次
        String pici = "";
        //描述
        StringBuilder des = new StringBuilder();

        //检查单位数
        long orgCount = idArr.length;

        //公共的情况
        ZfjcCheck zfjcCheck2 = service.selectById(idArr[0]);
        pici = zfjcCheck2.getPici();
        //personId人员数量
        long personCount = zfjcCheck2.getPersonId().split(",").length;
        String[] cateArr = zfjcCheck2.getCategoryId().split(",");
        for (String cateNum : cateArr) {
            List<ZfjcCheckResult> results = zfjcResultService.getCheckResultListByPIdAndCateNum(idArr[0], cateNum);
            for (ZfjcCheckResult result : results) {
                des.append(getDes(cateNum)).append("中的").append(result.getCategoryName()).append("、");
            }
        }
        des.toString().replace(ZfjcCheck.categoryId_type_one, ZfjcCheck.categoryId_type_one_des).replace(ZfjcCheck.categoryId_type_two, ZfjcCheck.categoryId_type_two_des).replace(ZfjcCheck.categoryId_type_three, ZfjcCheck.categoryId_type_three_des);

        //被检查机构
        String orgName = "";
        for (String id : idArr) {
            ZfjcCheck zfjcCheck = service.selectById(id);
            orgName += zfjcCheck.getOrganiseName();
            if (zfjcCheck.getWanchengTime() > wanchengTime) {
                wanchengTime = zfjcCheck.getWanchengTime();
            }
            if (zfjcCheck.getShijiTime() < shijiTime) {
                shijiTime = zfjcCheck.getShijiTime();
            }

        }
        //完成时间
        String dateYYYYMM = DateFormatUtils.format(wanchengTime, "YYYYMM");
        //实际检查时间
        String startTime = DateFormatUtils.format(shijiTime, "YYYYMM");


        try {
            out = response.getOutputStream();

            //
            Map<String, Object> map = new HashMap<String, Object>();
            //

            map.put("startTime", startTime);//检查开始时间
            map.put("dateYYYYMM", dateYYYYMM);//检查完成时间
            map.put("orgName", orgName);//检查机构
            map.put("personCount", "" + personCount);//检查人数
            map.put("pici", pici);//检查批次
            map.put("orgCount", "" + orgCount);//检查机构数
            map.put("des", "" + des);//检查机构数


            ExportWordUtil word = new ExportWordUtil("/word", "tongbao_xianqu.ftl");
            word.setHeader(request, response, "双随机检查通报.doc");
            word.doExport(out, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDes(String cateNum) {

        if (cateNum.equals(ZfjcCheck.categoryId_type_one)) {
            return ZfjcCheck.categoryId_type_one_des;
        } else if (cateNum.equals(ZfjcCheck.categoryId_type_two)) {
            return ZfjcCheck.categoryId_type_two_des;
        } else if (cateNum.equals(ZfjcCheck.categoryId_type_three)) {
            return ZfjcCheck.categoryId_type_three_des;
        }
        return cateNum;


    }
}
