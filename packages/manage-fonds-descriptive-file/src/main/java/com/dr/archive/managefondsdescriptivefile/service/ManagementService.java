package com.dr.archive.managefondsdescriptivefile.service;

import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.vo.FondsDescriptiveFileCountVo;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ManagementService extends BaseService<Management> {
    //全宗介绍类 - 全宗指南
    String SUB_TYPE_CODE_QZJSL_QZZN = "qzzn";
    //全宗介绍类 - 大事记
    String SUB_TYPE_CODE_QZJSL_DSJ = "dsj";
    //档案收集类 - 档案接收和征集工作的办法、标准
    String SUB_TYPE_CODE_DASJL_DAJSHZJGZDBF = "dajshzjgzdbf";
    //档案收集类 - 档案来源和档案历史转移过程说明材料
    String SUB_TYPE_CODE_DASJL_DALYHDALSZYGCSMCL = "dalyhdalszygcsmcl";
    //档案利用类 - 档案编硏与岀版情况
    String SUB_TYPE_CODE_DALYL_DABYYCBQK = "dabyycbqk";
    //档案利用类 - 保管期限规定
    String SUB_TYPE_CODE_DALYL_BGQXGD = "bgqxgd";
    //档案鉴定类 - 鉴定结果与报告
    String SUB_TYPE_CODE_DAJDL_JDGZXCDBG = "jdgzxcdbg";

    /**
     * 根据全宗id统计数量
     *
     * @param fondId
     * @return
     */
    List<FondsDescriptiveFileCountVo> getFondsCountByYear(String fondId);

    /**
     * 审核时修改
     *
     * @param processInstanceId 流程实例id
     * @param taskId            环节实例Id
     * @param optType           操作类型
     * @param management        全宗卷
     * @return
     */
    long updateByExamin(String processInstanceId, String taskId, String optType, Management management);

    /**
     * 根据年度范围，全宗id查询全宗卷
     *
     * @param fondId
     * @param startYear
     * @param endYear
     * @return
     */
    List<Management> getManagementList(String fondId, String startYear, String endYear);
}
