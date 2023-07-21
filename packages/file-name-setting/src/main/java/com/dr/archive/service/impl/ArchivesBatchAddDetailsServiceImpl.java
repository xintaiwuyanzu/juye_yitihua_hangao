package com.dr.archive.service.impl;

import com.dr.archive.entity.ArchivesBatchAddDetaile;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItem;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItemInfo;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.service.ArchivesBatchAddDetailsService;
import com.dr.archive.service.FileNameSettingService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.dr.archive.model.entity.ArchiveEntity.*;
import static jdk.nashorn.internal.objects.NativeString.substring;

@Service
public class ArchivesBatchAddDetailsServiceImpl extends DefaultBaseService<ArchivesBatchAddDetaile> implements ArchivesBatchAddDetailsService {
    @Autowired
    CommonService commonService;
    @Autowired
    protected ArchiveDataManager dataManager;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected FileNameSettingService fileNameSettingService;
    @Autowired
    FormDataService formDataService;

    @Override
    public void newBatchDetails(FormData formData, ArchiveBatchReceiveOffline batch, Organise organise) {
        //将开始和结束的档案拿出来
        String startNumber = formData.get("startNumber");
        String lastNumber = formData.get("endNumber");
        //截取序列号前面的档案，只截取一个即可
        String codeSetting = startNumber.substring(0, startNumber.length() - 4);
        //截取后面的序列号，这里使用上面的字段，。没有从新赋值
        startNumber = startNumber.substring(startNumber.length() - 4, startNumber.length());
        lastNumber = lastNumber.substring(lastNumber.length() - 4, lastNumber.length());
        //获取生成个数
        int statr = Integer.parseInt(startNumber);
        int last = Integer.parseInt(lastNumber);
        if (last - statr > 0) {
            //防止前端乱传全宗号
            formData.put(ArchiveEntity.COLUMN_FOND_CODE, formData.get("fondCode"));
            for (; statr <= last; statr++) {
                //清除id，不然无法生成多个数据
                String index = statr + "";
                formData.put(ArchiveEntity.ID_COLUMN_NAME, "");
                //生成form对象
                FormData data = new FormData(formData.getFormDefinitionId());
                data.setId(UUIDUtils.getUUID());
                data.put(ArchiveEntity.STATUS_COLUMN_KEY, "RECEIVE");
                data.put(ArchiveEntity.COLUMN_FOND_CODE, formData.get("fondCode"));
                data.put(ArchiveEntity.COLUMN_CATEGORY_CODE, formData.get("categoryCode"));
                //设置数据来源
                data.put(AbstractArchiveEntity.COLUMN_SOURCE_TYPE, "batchAdd");
                //档号组成
                String archiveCodeOrder = fileNameSettingService.archiveCodeOrder(index);
                data.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, codeSetting + archiveCodeOrder);
                if (organise != null) {
                    if ("dag".equals(organise.getOrganiseType())) {
                        data.put(ArchiveEntity.COLUMN_ORGANISEID, organise.getId());
                    }
                }
                //根据档号组成规则生成其他档案信息。
                String code = codeSetting;
                //查询档号配置规则
                List<CodingSchemeItem> codingSchemeItems = commonService.selectList(SqlQuery.from(CodingSchemeItem.class)
                        .equal(CodingSchemeItemInfo.BUSINESSID, formData.getFormDefinitionId())
                        .orderBy(CodingSchemeItemInfo.ORDERBY)
                );
                Assert.isTrue(!codingSchemeItems.isEmpty(), "未配置档号生成规则，请到【元数据管理-档号配置规则】配置后再重试。");
                //截取档案信息填充数据
                for (int i = 0; i < codingSchemeItems.size(); i++) {
                    //获取间隔字段index,截取字符串填充data
                    if (StringUtils.hasText(code)) {
                        int commector = code.indexOf(codingSchemeItems.get(i).getConnector());
                        if (commector != -1) {
                            String substring = code.substring(0, commector);
                            data.put(codingSchemeItems.get(i).getCode(), substring);
                            code = code.substring(commector + 1);
                        }
                    }
                }

                //自动设置全宗号
                dataManager.insertFormData(data, null, formData.get("categoryId"));
                //创建批次信息，绑定批次id 帮你formdataid
                ArchiveBatchReceiveOfflineDetail detail = new ArchiveBatchReceiveOfflineDetail();
                Fond fond = fondService.selectById(formData.get("fondId"));
                Category category = categoryService.selectById(formData.get("categoryId"));
                //绑定表单信息
                detail.setBatchId(batch.getId());
                detail.setFormDefinitionId(formData.getFormDefinitionId());
                detail.setFormDataId(data.getId());
                detail.setOrgCode(data.get(ArchiveEntity.COLUMN_ORG_CODE));
                detail.setArchiveCode(data.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
                detail.setTitle(data.getString(ArchiveEntity.COLUMN_TITLE));
                detail.setKeyWords(data.getString(ArchiveEntity.COLUMN_KEY_WORDS));
                detail.setNote(data.getString(ArchiveEntity.COLUMN_NOTE));
                detail.setYear(data.getString(ArchiveEntity.COLUMN_YEAR));
                detail.setSaveTerm(data.getString(COLUMN_SAVE_TERM));
                detail.setFondCode(data.getString(COLUMN_FOND_CODE));
                detail.setCategoryCode(data.getString(COLUMN_CATEGORY_CODE));
                //绑定全宗信息
                detail.setFondCode(fond.getCode());
                detail.setFondName(fond.getName());
                //绑定门类信息
                detail.setCategoryCode(category.getCode());
                detail.setCategoryName(category.getName());
                detail.setCategoryId(category.getId());
                detail.setTestStatus("-1");
                detail.setHookStatus("0");
                commonService.insert(detail);
            }
            List<ArchiveBatchReceiveOfflineDetail> details = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class).equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, batch.getId()));
            //保存结果
            batch.setDetailNum(details.size());
            batch.setStatus(StatusEntity.STATUS_ENABLE_STR);
            batch.setEndDate(System.currentTimeMillis());
            commonMapper.updateById(batch);
        } else {
            Assert.isTrue(false, "档号错误");
        }
    }
}
