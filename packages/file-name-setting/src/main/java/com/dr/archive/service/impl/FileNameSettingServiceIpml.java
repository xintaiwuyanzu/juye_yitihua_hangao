package com.dr.archive.service.impl;


import com.dr.archive.entity.FileNameSetting;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItem;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItemInfo;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.service.FileNameSettingService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class FileNameSettingServiceIpml extends DefaultBaseService<FileNameSetting> implements FileNameSettingService {
    @Autowired
    FormDataService formDataService;
    @Autowired
    @Qualifier("camundaTaskExecutor")
    protected Executor executor;
    @Autowired
    CommonService commonService;
    @Autowired
    ArchiveDataManager dataManager;

    @Override
    public ResultEntity startUp(FormData formData, String state) {
        //判断没有档号的数据
        List<FormData> formDataList = formDataService.selectFormData(formData.getFormDefinitionId(), ((sqlQuery, formRelationWrapper) -> {
            sqlQuery.isNotNull(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE))
                    .in((formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE)), "")
                    .orNew()
                    .isNull(formRelationWrapper.getColumn(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE))
                    .equal(formRelationWrapper.getColumn(ArchiveEntity.STATUS_COLUMN_KEY),"RECEIVE");
        }));
        //有数据说明有为空
        if (!formDataList.isEmpty()) {

            //查询档号配置规则
            List<CodingSchemeItem> codingSchemeItems = commonService.selectList(SqlQuery.from(CodingSchemeItem.class)
                    .equal(CodingSchemeItemInfo.BUSINESSID, formData.getFormDefinitionId())
                    .orderBy(CodingSchemeItemInfo.ORDERBY)
            );

            Assert.isTrue(!codingSchemeItems.isEmpty(), "未配置档号生成规则，请到【元数据管理-档号配置规则】配置后再重试。");

            for (FormData data : formDataList) {
                String endCode = CodeSetting(data, codingSchemeItems);
                //拼好后修改form。
                data.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, endCode);
                //查询出批次中相对应的档案，然后一起修改掉档号。
                ArchiveBatchReceiveOfflineDetail archiveBatchReceiveOfflineDetail = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                        .equal(ArchiveBatchReceiveOfflineDetailInfo.FORMDATAID, data.getId()));
                if (archiveBatchReceiveOfflineDetail!=null){
                    archiveBatchReceiveOfflineDetail.setArchiveCode(endCode);
                    commonMapper.updateById(archiveBatchReceiveOfflineDetail);
                }
                formDataService.updateFormDataById(data);
            }
        } else {
            return ResultEntity.error("未查询到存在空的档号");
        }
        return ResultEntity.success("正在生成中请稍后刷新查看");
    }

    @Override
    public void insertFormBatchData(FormData formData, String fondId, String categoryId, int number_code) {
        Assert.isTrue(number_code>0, "请输入批量添加个数。");

        //查询是否添加了档号匹配规则，根据匹配规则生成档号。
        List<CodingSchemeItem> codingSchemeItems = commonService.selectList(SqlQuery.from(CodingSchemeItem.class)
                .equal(CodingSchemeItemInfo.BUSINESSID, formData.getFormDefinitionId())
                .orderBy(CodingSchemeItemInfo.ORDERBY));

        Assert.isTrue(!codingSchemeItems.isEmpty(), "未配置档号生成规则，请到【元数据管理-档号配置规则】配置后再重试。");
        for (int i = 0;i<number_code;i++){
            //根据传来的数据，生成每一项formDate数据,然后获取最大流水号，生成档号，最后添加档案。
            FormData data = new FormData(formData.getFormDefinitionId());
            for (CodingSchemeItem item : codingSchemeItems) {
                data.put(item.getCode(), formData.get(item.getCode()));
            }
            //档号生成
            String endCode = CodeSetting(formData, codingSchemeItems);
            data.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, endCode);
            dataManager.insertFormData(data, fondId, categoryId);
        }
    }

    @Override
    public String CodeSetting(FormData formData, List<CodingSchemeItem> codingSchemeItems) {

        StringBuffer sb = new StringBuffer();
        String endCode = "";
        for (CodingSchemeItem item : codingSchemeItems) {
            String code = (String) formData.get(item.getCode());
            //判断档号数据  与 档号生成规则字段 对应的数据是否存在，不存在则提示给用户。
            Assert.isTrue(StringUtils.hasText(code), "题名为【" + formData.get(ArchiveEntity.COLUMN_TITLE) + "】的档号，字段【" + item.getName() + "】数据不存在，请补全后再试");
            if ("SAVE_TERM".equals(item.getCode())) {
                if (code.equals("永久")){
                    code = "Y";
                }else if (code.matches("\\d+?")){
                    code = "D"+code;
                }
            }
            sb.append(code);//档号字段
            sb.append(item.getConnector());//分隔符
        }
        //拼好的档号--还差流水号
        String sCode = sb.toString();
        List<FormData> maxDates = formDataService.selectFormData(formData.getFormDefinitionId(), ((sqlQuery, formRelationWrapper) -> {
            sqlQuery.like(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE), sCode);
        }));

        if (!maxDates.isEmpty()) {
            List<Integer> integers = new ArrayList<>();
            for (FormData maxDate : maxDates) {
                String maxCode = maxDate.get(ArchiveEntity.COLUMN_ARCHIVE_CODE).toString();
                String substring = maxCode.substring(maxCode.length() - 4, maxCode.length());
                integers.add(Integer.parseInt(substring));
            }
            Integer max = Collections.max(integers);
            String archiveCodeOrder = archiveCodeOrder(max + 1 + "");
            endCode = sCode + archiveCodeOrder;
        } else {
            //没有该档号，从0001开始。
            endCode = sCode + "0001";
        }
        return endCode;
    }

    @Override
    public String archiveCodeOrder(String index) {
        String archiveCode = "";
        if (index.length() == 3) {
            archiveCode = "0" + index;
        } else if (index.length() == 2) {
            archiveCode = "00" + index;
        } else if (index.length() == 1) {
            archiveCode = "000" + index;
        } else {
            archiveCode = index;
        }
        return archiveCode;
    }

}
