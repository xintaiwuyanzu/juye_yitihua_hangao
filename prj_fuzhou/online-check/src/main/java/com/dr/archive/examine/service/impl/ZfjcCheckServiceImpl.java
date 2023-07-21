package com.dr.archive.examine.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dr.archive.examine.entity.*;
import com.dr.archive.examine.service.*;
import com.dr.archive.kufang.entityfiles.utils.CommonTools;
import com.dr.archive.kufang.entityfiles.utils.ExportWordUtil;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.sys.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 执法检查
 */
@Service
public class ZfjcCheckServiceImpl extends DefaultBaseService<ZfjcCheck> implements ZfjcCheckService {
    @Autowired
    ZfjcResultService zfjcResultService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    JdzdCategoryService jdzdCategoryService;
    @Autowired
    JdzdWeightService jdzdWeightService;
    @Autowired
    ZfjcSpecialistService zfjcSpecialistService;
    @Autowired
    PublicInformationService publicInformationService;
    @Autowired
    protected SysDictService sysDictService;

    @Override
    public long insert(ZfjcCheck zfjcCheck) {
        long count = 0;
        if (StringUtils.isEmpty(zfjcCheck.getPici())) {
            zfjcCheck.setPici("FZ" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        }
        long timeMillis = System.currentTimeMillis();
        /*if (zfjcCheck.getJihuaTime() == 0) {
            zfjcCheck.setJihuaTime(timeMillis);
        }
        if (zfjcCheck.getShijiTime() == 0) {
            zfjcCheck.setShijiTime(timeMillis);
        }
        if (zfjcCheck.getJieguoTime() == 0) {
            zfjcCheck.setJieguoTime(timeMillis);
        }
        if (zfjcCheck.getTongzhiTime() == 0) {
            zfjcCheck.setTongzhiTime(timeMillis);
        }

        if (zfjcCheck.getZhenggaiTime() == 0) {
            zfjcCheck.setZhenggaiTime(timeMillis);
        }
        if (zfjcCheck.getWanchengTime() == 0) {
            zfjcCheck.setWanchengTime(timeMillis);
        }*/
        //默认不公示
        zfjcCheck.setPublish("0");
        zfjcCheck.setCheckResult(ZfjcCheckResult.ZFJCCHECKRESULT_WAIT);

        //orangiseName
        String oId = zfjcCheck.getOrganiseId();

        Organise organise = SecurityHolder.get().currentOrganise();
        String[] organiseIds = oId.split(",");
        for (String organiseId : organiseIds) {
            zfjcCheck.setId(CommonTools.getUUID());
            if (StringUtils.isNotBlank(organiseId)) {
                JdzdWeight jdzdWeight = jdzdWeightService.getOneByOId(organiseId);
                zfjcCheck.setOrganiseId(organiseId);
                zfjcCheck.setOrganiseName(jdzdWeight.getOrganiseName());
                setName(zfjcCheck, true);
            }
            zfjcCheck.setStatus(ZfjcCheck.status_add);
            //添加默认机构id 名称
            zfjcCheck.setCreateOrgId(organise.getId());
            zfjcCheck.setCreateOrgName(organise.getOrganiseName());
            count = super.insert(zfjcCheck);
        }
        return count;
    }

    @Override
    public void change(ZfjcCheck zfjcCheck, String personId) {
        setName(zfjcCheck, false);
        zfjcCheck.setUpdatePerson(personId);
        zfjcCheck.setUpdateDate(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(zfjcCheck);
    }

    /**
     * 更新结果表
     *
     * @param result
     * @param pId
     * @param param
     * @param opinion
     */
    @Override
    public void updateResult(String result, String pId, String param, String opinion, String reply, String infoId) {


        //更新检查主表
        ZfjcCheck zfjcCheck = selectById(pId);
        zfjcCheck.setOpinion(opinion);
        zfjcCheck.setReply(reply);


        //更新结果表
        List<Map> maps = JSON.parseArray(result, Map.class);
        deleteresult(pId);
        for (Map map : maps) {
            Integer categoryResult = (Integer) map.get("categoryResult");
            String categoryId = null;
            try {
                categoryId = (String) map.get("categoryId");
            } catch (Exception e) {
                JSONArray categoryJSon = (JSONArray) map.get("categoryId");
                categoryId = (String) categoryJSon.get(1);
            }
            String samllopinion = (String) map.get("opinion");
            String samllreply = (String) map.get("reply");
            String situation = (String) map.get("situation");
            String id = (String) map.get("id");
//            String categoryName = (String) map.get("categoryName");
            if (!"".equals(categoryId)) {

                if ("".equals(id)) {
                    //表示这是新增

                    JdzdCategory jdzdCategory = jdzdCategoryService.getOneById(categoryId);
                    ZfjcCheckResult result1 = new ZfjcCheckResult();
                    result1.setCategoryId(jdzdCategory.getId());
                    result1.setCategoryName(jdzdCategory.getSmallCategory());
                    result1.setCategoryResult(ZfjcCheckResult.ZFJCCHECKRESULT_WAIT);
                    result1.setpId(zfjcCheck.getId());
                    result1.setOrganiseId(zfjcCheck.getOrganiseId());
                    result1.setOrganiseName(zfjcCheck.getOrganiseName());
                    result1.setPersonId(zfjcCheck.getPersonId());
                    result1.setPersonName(zfjcCheck.getPersonName());
                    result1.setStatus("0");//新增的时候默认0
                    result1.setCategoryNum(zfjcCheck.getCategoryId());
                    result1.setCategoryResult(categoryResult);
                    result1.setOpinion(samllopinion);
                    result1.setReply(samllreply);
                    result1.setSituation(situation);
                    zfjcResultService.insert(result1);


                } else {
                    //这是更新
                    SqlQuery<ZfjcCheckResult> equal = SqlQuery.from(ZfjcCheckResult.class).equal(ZfjcCheckResultInfo.PID, pId).equal(ZfjcCheckResultInfo.CATEGORYID, categoryId);
                    //ZfjcCheckResult zfjcCheckResult = zfjcResultService.selectOne(equal);
                    ZfjcCheckResult zfjcCheckResult = new ZfjcCheckResult();
                    //存在更新
                    zfjcCheckResult.setId(id);
                    zfjcCheckResult.setCreatePerson(map.get("createPerson").toString());
                    zfjcCheckResult.setCreateDate((Long) map.get("createDate"));
                    zfjcCheckResult.setPersonName(map.get("personName").toString());
                    zfjcCheckResult.setPersonId(map.get("personId").toString());
                    zfjcCheckResult.setOrganiseId(map.get("organiseId").toString());
                    zfjcCheckResult.setOrganiseName(map.get("organiseName").toString());
                    zfjcCheckResult.setpId(map.get("pId").toString());
                    zfjcCheckResult.setCategoryNum(map.get("categoryNum").toString());
                    zfjcCheckResult.setCategoryId(map.get("categoryId").toString());
                    zfjcCheckResult.setCategoryName(map.get("categoryName").toString());
                    zfjcCheckResult.setCategoryResult(categoryResult);
                    zfjcCheckResult.setOpinion(samllopinion);
                    zfjcCheckResult.setReply(samllreply);
                    zfjcCheckResult.setSituation(situation);
                    zfjcResultService.insert(zfjcCheckResult);
                }

            }


        }

        Integer res = ZfjcCheckResult.ZFJCCHECKRESULT_PASS;
        List<ZfjcCheckResult> resultList = zfjcResultService.getCheckResultListByPId(pId);
        for (ZfjcCheckResult zfjcCheckResult : resultList) {
            if (zfjcCheckResult.getCategoryResult().equals(ZfjcCheckResult.ZFJCCHECKRESULT_NO_PASS)) {
                res = ZfjcCheckResult.ZFJCCHECKRESULT_NO_PASS;
            }
        }
        zfjcCheck.setCheckResult(res);
        updateById(zfjcCheck);

        //通知被检查单位
        if (param.equals("1")) {
            //发个通知调整
            publicInformationService.add(pId, SecurityHolder.get().currentPerson().getId(), zfjcCheck.getOrganiseId(), opinion, PublicInformation.INFOTYPE_ADJUST, PublicInformation.checkType_zfjc);
        } else if (param.equals("2")) {
            //发个通知反馈
            publicInformationService.reply(infoId, reply, PublicInformation.checkType_zfjc);
        }
    }

    /**
     * 办结   更改状态  并且做出权重调整
     *
     * @param id
     */
    @Override
    public void banjie(String id) {
        ZfjcCheck zfjcCheck = selectById(id);

        JdzdWeight jdzdWeight = jdzdWeightService.selectById(zfjcCheck.getOrganiseId());
        List<ZfjcCheckResult> resultList = zfjcResultService.getCheckResultListByPId(id);
        //检查是否检查项全部通过
        boolean eval = eval(resultList, n -> n != 1);
        if (eval) {
            //全部通过 减权重
            if (jdzdWeight.getWeight() > 1) {
                //大于1  还能减少  到了1 就不能再减少了
                jdzdWeight.setWeight(jdzdWeight.getWeight() - 1);
            }
        } else {
            //存在问题  加权重
            jdzdWeight.setWeight(jdzdWeight.getWeight() + 1);
        }
        jdzdWeightService.updateById(jdzdWeight);
        //办结
        zfjcCheck.setStatus(ZfjcCheck.status_banjie);
        updateById(zfjcCheck);
    }

    @Override
    public void exptz(HttpServletRequest request, HttpServletResponse response, String id) {
        Organise organise = SecurityHolder.get().currentOrganise();
        ZfjcCheck zfjcCheck = selectById(id);
        Map<String, Object> map = new HashMap<>();
        //TODO 结构设计的有点问题
//        List<ZfjcCheck> zfjcCheckList = getZfjcCheckListByBatchCode(zfjcCheck.getPici());
//        StringBuilder organiseName = new StringBuilder();
//        zfjcCheckList.forEach(zfjcCheck1 -> {
//            organiseName.append(zfjcCheck1.getOrganiseName() + "、");
//        });
//        if (organiseName.length() > 0) {
//            organiseName.deleteCharAt(organiseName.length() - 1);
//        }

        map.put("organiseName", zfjcCheck.getOrganiseName());//被检查单位
//        map.put("zfjcCheckList", zfjcCheckList);//被检查单位
        map.put("organiseNum", "1");//被检查单位数量
        map.put("personName", zfjcCheck.getPersonName());//检查人员
        map.put("personNum", zfjcCheck.getPersonName().length() > 0 ? zfjcCheck.getPersonName().split(",").length : 0);//检查人员数量
        map.put("currentOrganiseName", organise.getOrganiseName());//当前单位名称
        map.put("jihuaTime", new SimpleDateFormat("yyyy年MM月dd日").format(zfjcCheck.getJihuaTime()));// 通知时间计划检查时间
        map.put("jihuaNianYue", new SimpleDateFormat("yyyy年MM月").format(zfjcCheck.getJihuaTime()));//计划年月
        map.put("shijiYue", new SimpleDateFormat("MM月").format(zfjcCheck.getShijiTime()));//实际检查月
        map.put("shijiTime", new SimpleDateFormat("yyyy年MM月dd日").format(zfjcCheck.getShijiTime()));//检查时间
        //TODO 其他参数
        try (OutputStream out = response.getOutputStream()) {
            ExportWordUtil word = new ExportWordUtil("/word", "tongzhi.ftl");
            word.setHeader(request, response, "通知单.doc");
            word.doExport(out, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据批次号查询检查单位
     *
     * @param batchCode
     * @return
     */
    List<ZfjcCheck> getZfjcCheckListByBatchCode(String batchCode) {
        return commonMapper.selectByQuery(SqlQuery.from(ZfjcCheck.class).equal(ZfjcCheckInfo.PICI, batchCode));
    }

    /**
     * 设置name
     *
     * @param zfjcCheck
     * @return
     */
    private ZfjcCheck setName(ZfjcCheck zfjcCheck, boolean addRsult) {

        //旧检查单
        ZfjcCheck oldZfjcCheck = selectById(zfjcCheck.getId());

        //personName
        String pId = zfjcCheck.getPersonId();
        zfjcCheck.setPersonName(pIdToPName(pId));

        //将CategoryId 字符串去重 之后 再转回字符串加逗号的格式
        List<String> cNums = Arrays.asList(zfjcCheck.getCategoryId().split(",")).stream().distinct().collect(Collectors.toList());
//        String catename = cNumsToCatename(cNums);
        String catename = setDaLeiMingCheng(zfjcCheck.getCategorySmallId());
        //从检查项管理获取大类
        List<TreeNode> treeNodeList = sysDictService.dict(JDJC_ZFJC_DICT_JCNR);

        //set  大类 id  大类名称
        zfjcCheck.setCategoryName(catename);
        String join = String.join(",", cNums);
        zfjcCheck.setCategoryId(join);


        //下面代码特别复杂  建议不动
        StringBuilder smallTypeName = new StringBuilder();
        List<String> categorySmallIds = Arrays.asList(zfjcCheck.getCategorySmallId().split(","));
        for (String cId : categorySmallIds) {
            JdzdCategory jdzdCategory = jdzdCategoryService.getOneById(cId);
            smallTypeName.append(jdzdCategory.getSmallCategory()).append(",");
            //新增检查单 同时  新增结果单
            if (addRsult) {
                addresult(zfjcCheck, jdzdCategory);
            } else {
                String categoryIdnew = zfjcCheck.getCategorySmallId();//xin
                String categoryIdold = oldZfjcCheck.getCategorySmallId();//jiu
                //编辑
                if (categoryIdnew.equals(categoryIdold)) {
                    //相等 表示  这个不用更新
                } else {
                    //不相等 就得 重新改了
                    List<String> newIds = Arrays.asList(categoryIdnew.split(","));
                    List<String> oldIds = Arrays.asList(categoryIdold.split(","));
                    //这里有点复杂  为了节约时间 采用最笨的方法

                    for (String oldId : oldIds) {
                        if (newIds.contains(oldId)) {
                            //同时存在  不作处理
                            continue;
                        } else {
                            //新的没有  旧的有  删除
                            SqlQuery<ZfjcCheckResult> oldQuery = SqlQuery.from(ZfjcCheckResult.class).equal(ZfjcCheckResultInfo.PID, zfjcCheck.getId()).equal(ZfjcCheckResultInfo.CATEGORYID, oldId);
                            zfjcResultService.delete(oldQuery);
                        }
                    }
                    for (String newId : newIds) {
                        if (oldIds.contains(newId)) {
                            //同时存在  不作处理
                            continue;
                        } else {
                            //新的有 旧的没有  新增
                            JdzdCategory newCategory = jdzdCategoryService.getOneById(newId);
                            addresult(zfjcCheck, newCategory);
                        }
                    }
                }
            }
        }
        //set 小类  名称
        zfjcCheck.setCategorySmallName(smallTypeName.toString());
        return zfjcCheck;
    }

    /**
     * @param categorySmallId 检查内容id，也就是小类id
     */
    private String setDaLeiMingCheng(String categorySmallId) {
        StringBuilder daLeiMingCheng = new StringBuilder();
        String[] categorySmallIdArray = categorySmallId.split(",");
        //1、先查找jdzdCategory
        if (categorySmallIdArray.length > 0) {
            for (String id : categorySmallIdArray) {
                daLeiMingCheng.append(jdzdCategoryService.selectById(id).getSmallCategory() + ",");
            }
        }
        return daLeiMingCheng.toString();
        //2、从字典查询大类名称
    }

    private void deleteresult(String pid) {
        SqlQuery<ZfjcCheckResult> deleteq = SqlQuery.from(ZfjcCheckResult.class).equal(ZfjcCheckResultInfo.PID, pid);
        zfjcResultService.delete(deleteq);
    }

    /**
     * 增加结果信息
     *
     * @param zfjcCheck
     * @param jdzdCategory
     */
    private void addresult(ZfjcCheck zfjcCheck, JdzdCategory jdzdCategory) {
        ZfjcCheckResult result1 = new ZfjcCheckResult();
        result1.setCategoryId(jdzdCategory.getId());
        result1.setCategoryName(jdzdCategory.getSmallCategory());
        result1.setCategoryResult(ZfjcCheckResult.ZFJCCHECKRESULT_WAIT);
        result1.setpId(zfjcCheck.getId());
        result1.setOrganiseId(zfjcCheck.getOrganiseId());
        result1.setOrganiseName(zfjcCheck.getOrganiseName());
        result1.setPersonId(zfjcCheck.getPersonId());
        result1.setPersonName(zfjcCheck.getPersonName());
        result1.setStatus("0");//新增的时候默认0
        result1.setCategoryNum(zfjcCheck.getCategoryId());
        zfjcResultService.insert(result1);
    }

    /**
     * 大类名称 拼接
     *
     * @param cNums
     * @return
     */
    private String cNumsToCatename(List<String> cNums) {
        StringBuilder catename = new StringBuilder();
        for (String cNum : cNums) {
            if (ZfjcCheck.categoryId_type_one.equals(cNum)) {
                //贯彻实施档案法规监督检查
                catename.append(JdzdCategory.largeCategory_one + ",");
            } else if (ZfjcCheck.categoryId_type_two.equals(cNum)) {
                catename.append(JdzdCategory.largeCategory_two + ",");
            } else if (ZfjcCheck.categoryId_type_three.equals(cNum)) {
                catename.append(JdzdCategory.largeCategory_three + ",");
            }
        }
        return catename.toString();
    }

    /**
     * perosnName 拼接
     *
     * @param pId
     * @return
     */
    private String pIdToPName(String pId) {
        String[] split = pId.split(",");
        StringBuilder name = new StringBuilder();
        for (String perId : split) {
            if (!StringUtils.isBlank(perId)) {
                ZfjcSpecialist zfjcSpecialist = zfjcSpecialistService.selectById(perId);
                name.append(zfjcSpecialist.getUserName()).append(",");
            }

        }
        return name.substring(0, name.length() - 1);//把最后一个逗号切走
    }

    /**
     * oName 拼接
     *
     * @param oId
     * @return
     */
    private String oIdToOName(String oId) {
        String[] splito = oId.split(",");
        StringBuilder nameo = new StringBuilder();
        for (String perId : splito) {
            if (!StringUtils.isBlank(perId)) {
                JdzdWeight oneById = jdzdWeightService.getOneById(perId);
                nameo.append(oneById.getOrganiseName()).append(",");
            }

        }
        return nameo.substring(0, nameo.length() - 1);//把最后一个逗号切走
    }

    public boolean eval(List<ZfjcCheckResult> resultList, Predicate<Integer> predicate) {
        boolean a = true;
        for (ZfjcCheckResult n : resultList) {

            if (predicate.test(n.getCategoryResult())) {
                a = false;
                break;
            }
        }
        return a;
    }
}
