package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dataClient.entity.DataBatch;
import com.dr.archive.common.dataClient.entity.DataBatchFileDetail;
import com.dr.archive.common.dataClient.service.DataBatchService;
import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.DeliveryHistoryService;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class EArchiveBatchServiceImpl extends DefaultBaseService<EArchiveBatch> implements EArchiveBatchService, ProcessTypeProvider {

    @Autowired
    EArchiveService EArchiveService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    DeliveryHistoryService deliveryHistoryService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondService fondService;
    @Autowired
    DataBatchService dataBatchService;

    @Override
    public String getType() {
        return "PROCESS_TYPE_Delivery";
    }

    @Override
    public String getName() {
        return "档案出库";
    }

    /**
     * 启动流程前回调
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String deliveryId = (String) context.getBusinessParams().get("businessId");
        EArchiveBatch DeliveryBatch = selectById(deliveryId);
        if (DeliveryBatch != null) {
            context.setBusinessId(deliveryId);
            context.setProcessInstanceTitle(context.getPerson().getUserName() + "发起的" + getName() + "审核 -" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            //设置流程查看详情跳转页面
            context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/dzdacqbc/archive/delivery");
            context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, context.getBusinessId());
        }
    }

    /**
     * 启动流程后回调
     *
     * @param context
     */
    @Override
    public void onAfterStartProcess(ProcessContext context) {
        EArchiveBatch DeliveryBatch = selectById(context.getBusinessId());
        DeliveryBatch.setStatus("1");//0:初始创建，1:审核中，2:已完成
        DeliveryBatch.setStartDate(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(DeliveryBatch);
        //添加流转记录就及流转的意见
        deliveryHistoryService.insetDeliveryHistory(DeliveryBatch, context);
    }

    /**
     * 环节执行前回调
     *
     * @param context
     */
    @Override
    public void onBeforeCompleteTask(TaskContext context) {
        String deliveryId = context.getTaskInstance().getProcessVariables().get("$businessId").toString();
        EArchiveBatch DeliveryBatch = selectById(deliveryId);
        DeliveryBatch.setCreatePerson(SecurityHolder.get().currentPerson().getId());
        commonMapper.updateIgnoreNullById(DeliveryBatch);
    }

    /**
     * 环节执行完回调
     *
     * @param context
     */
    @Override
    public void onAfterCompleteTask(TaskContext context) {
        String deliveryId = context.getTaskInstance().getProcessVariables().get("$businessId").toString();
        EArchiveBatch eArchiveBatch = selectById(deliveryId);
        //添加流转记录就及流转的意见
        deliveryHistoryService.insetDeliveryHistory(eArchiveBatch, context);
    }

    /**
     * 结束流程后回调
     *
     * @param context
     */
    @Override
    public void onAfterEndProcess(TaskContext context) {
        String deliveryId = context.getTaskInstance().getProcessVariables().get("$businessId").toString();
        EArchiveBatch DeliveryBatch = selectById(deliveryId);
        DeliveryBatch.setStatus("2");//0:初始状态，1:审核中，2:已完成
        commonMapper.updateIgnoreNullById(DeliveryBatch);
        //添加流转记录就及流转的意见
        deliveryHistoryService.insetDeliveryHistory(DeliveryBatch, context);
        //把这条批次复制到 维护客户端


    }

    @Override
    public EArchiveBatch addToBatch(EArchive eArchive, Person person) {
        person = SecurityHolder.get().currentPerson();
        //创建批次
        SqlQuery<EArchiveBatch> equal = SqlQuery.from(EArchiveBatch.class)
                .equal(EArchiveBatchInfo.DELIVERYSTATUS, "0")
                .equal(EArchiveBatchInfo.CREATEPERSON, person.getId());
        EArchiveBatch archiveBatch = commonMapper.selectOneByQuery(equal);
        EArchiveBatch DeliveryBatch = null;
        //不存在未提交的批次，新建一个批次
        if (StringUtils.isEmpty(archiveBatch)) {
            if (!StringUtils.isEmpty(eArchive)) {
                //创建批次详情
                DeliveryBatch = insetDeliveryBatch(1, person);
                //加人到批次详情中
                insetDeliveryDetailItem(DeliveryBatch, eArchive);
            }
            //存在一个未提交的批次
        } else {
            SqlQuery<EArchiveBatchDetail> eq = SqlQuery.from(EArchiveBatchDetail.class)
                    .equal(EArchiveBatchDetailInfo.BATCHID, archiveBatch.getId())
                    .equal(EArchiveBatchDetailInfo.ARCHIVECODE, eArchive.getArchiveCode());
            List<EArchiveBatchDetail> eArchiveBatchDetails = commonMapper.selectByQuery(eq);
            if (eArchiveBatchDetails.size() == 0) {
                DeliveryBatch = archiveBatch;
                if (!StringUtils.isEmpty(eArchive)) {
                    //创建批次
                    //DeliveryBatch = insetDeliveryBatch(eArchives.size(), person);
                    if (!"4".equals(eArchive.getModelType())) {
                        DeliveryBatch.setDetailNum(DeliveryBatch.getDetailNum() + 1);
                    }
                    DeliveryBatch.setUpdateDate(System.currentTimeMillis());
                    DeliveryBatch.setUpdatePerson(person.getId());
                    commonMapper.updateIgnoreNullById(DeliveryBatch);
                    //加人到 出库批次 和批次详情中
                    insetDeliveryDetailItem(DeliveryBatch, eArchive);
                }
            }
        }
        return DeliveryBatch;
    }


    @Override
    public long checkSubmit(String deliveryId) {
        SqlQuery sqlQuery = SqlQuery.from(EArchiveBatchDetail.class)
                .equal(EArchiveBatchDetailInfo.DELIVERYSTATUS, "0")
                .equal(EArchiveBatchDetailInfo.BATCHID, deliveryId);
        return count(sqlQuery);
    }

    @Override
    public long fastDelivery(String deliveryId) {
        SqlQuery sqlQuery = SqlQuery.from(EArchiveBatchDetail.class)
                .equal(EArchiveBatchDetailInfo.BATCHID, deliveryId)
                .equal(EArchiveBatchDetailInfo.DELIVERYSTATUS, 0)
                .set(EArchiveBatchDetailInfo.DELIVERYSTATUS, 1);
        return commonMapper.updateByQuery(sqlQuery);
    }

    @Override
    public long updateNotNull(EArchiveBatch eArchiveBatch, String type) {
        if (eArchiveBatch.getStatus().equals("1") && !"submit".equals(type)) {
            return 0;
        }
        return commonMapper.updateIgnoreNullById(eArchiveBatch);
    }

    /**
     * 新建出库批次
     *
     * @param detailNum
     * @param person
     * @return
     */
    public EArchiveBatch insetDeliveryBatch(long detailNum, Person person) {
        EArchiveBatch DeliveryBatch = new EArchiveBatch();
        DeliveryBatch.setBatchName(person.getUserName() + System.currentTimeMillis() + "的出库申请");
        DeliveryBatch.setBatchType(EArchiveBatchService.BATCH_TYPE_OUT);
        DeliveryBatch.setDetailNum(detailNum);
        DeliveryBatch.setBeizhu("出库申请");
        DeliveryBatch.setCreatePerson(person.getId());
        DeliveryBatch.setDeliveryPerson(person.getUserName());
        DeliveryBatch.setCreateDate(System.currentTimeMillis());
        //这里写死开始时间
        DeliveryBatch.setStartDate(System.currentTimeMillis());
        String formatDate = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
        DeliveryBatch.setDeliveryTime(formatDate);
        DeliveryBatch.setDeliveryStatus("0");
        DeliveryBatch.setStatus("0");//0:初始创建，2:已完成
        DeliveryBatch.setId(UUIDUtils.getUUID());
        commonMapper.insert(DeliveryBatch);
        return DeliveryBatch;
    }

    /**
     * 新建批次 详情
     *
     * @param DeliveryBatch
     * @param eArchive
     * @return
     */
    public EArchiveBatchDetail insetDeliveryDetailItem(EArchiveBatch DeliveryBatch, EArchive eArchive) {
        EArchiveBatchDetail detail = new EArchiveBatchDetail();
        detail.setBatchId(DeliveryBatch.getId());
        detail.setBatchName(DeliveryBatch.getBatchName());
        detail.setDeliveryPerson(DeliveryBatch.getDeliveryPerson());
        detail.setDeliveryTime(DeliveryBatch.getDeliveryTime());

        detail.bindArchiveInfo(eArchive);
        detail.setFormDefinitionId(eArchive.getFormDefinitionId());
        detail.setFormDataId(eArchive.getArchiveId());
        detail.setDeliveryStatus("0");
        detail.setId(UUIDUtils.getUUID());
        detail.setYear(eArchive.getYear());
        detail.setCategoryCode(eArchive.getCategoryCode());
        detail.setFondCode(eArchive.getFondCode());
        detail.setArchiveId(eArchive.getId());
        detail.setModelType(eArchive.getModelType());
        Fond fond = fondService.findFondByCode(eArchive.getFondCode());
        if (fond != null) {
            Category category = categoryService.findCategoryByCode(eArchive.getCategoryCode(), fond.getId());
            detail.setFondName(fond.getName());
            detail.setCategoryName(category.getName());
        }
        //eArchive.setOutCount(eArchive.getOutCount() + 1);
        //eArchive.setLastOutDate(System.currentTimeMillis());
        //eArchive.setLastOutPerson(DeliveryBatch.getCreatePerson());
        DeliveryBatch.setUpdateDate(System.currentTimeMillis());
        //修改人就是创建人呗
        DeliveryBatch.setUpdatePerson(DeliveryBatch.getCreatePerson());
        if (StringUtils.isEmpty(eArchive.getIsJNWJ())) {
            eArchive.setIsJNWJ(null);
        }
        commonMapper.updateIgnoreNullById(eArchive);
        commonMapper.insert(detail);
        return detail;
    }

    //提交到数据维护客户端
    @Override
    public long addDownLoad(String batchId) {
        EArchiveBatch eArchiveBatch = selectById(batchId);
        DataBatch dataBatch = new DataBatch();
        dataBatch.setId(UUIDUtils.getUUID());
        dataBatch.setBatchName(eArchiveBatch.getBatchName());
        dataBatch.setBatchDescription("电子档案长期保存出库");
        dataBatch.setBatchType("1");
        dataBatchService.insert(dataBatch);
        //找长期保存的原文文件
        SqlQuery<EFileInfo> sqlQuery = SqlQuery.from(EFileInfo.class)
                .join(EFileInfoInfo.ARCHIVEID, EArchiveBatchDetailInfo.ARCHIVEID)
                .equal(EArchiveBatchDetailInfo.BATCHID, batchId);
        List<EFileInfo> eFileInfos = commonMapper.selectByQuery(sqlQuery);
        for (EFileInfo eFileInfo : eFileInfos) {
            DataBatchFileDetail fileDetail = new DataBatchFileDetail();
            CommonService.bindCreateInfo(fileDetail);
            fileDetail.setBatchId(dataBatch.getId());
            fileDetail.setFileSize(eFileInfo.getFileSize());
            fileDetail.setFilePath(eFileInfo.getFileName());
            int index = eFileInfo.getFilePath().lastIndexOf(File.separator);
            String batchDir = eFileInfo.getFilePath().substring(0, index);
            fileDetail.setBatchDir(batchDir);
            fileDetail.setFileName(eFileInfo.getFileName());
            File file = new File(eFileInfo.getFilePath());
            //求文件的md5hash值
            try {
                String fileHash = md5HashCode(new FileInputStream(file));
                fileDetail.setFileHash(fileHash);
            } catch (Exception e) {
                e.printStackTrace();
            }
            commonMapper.insert(fileDetail);
        }

        eArchiveBatch.setDeliveryStatus(StatusEntity.STATUS_ENABLE_STR);//设置为已出库
        commonMapper.updateIgnoreNullById(eArchiveBatch);

        //提交后再增加次数
        List<EArchiveBatchDetail> eArchiveBatchDetails = commonMapper.selectByQuery(SqlQuery.from(EArchiveBatchDetail.class).equal(EArchiveBatchDetailInfo.BATCHID, eArchiveBatch.getId()));
        for (EArchiveBatchDetail eArchiveBatchDetail : eArchiveBatchDetails) {
            EArchive eArchive = commonMapper.selectById(EArchive.class, eArchiveBatchDetail.getArchiveId());
            eArchive.setOutCount(eArchive.getOutCount() + 1);
            eArchive.setLastOutDate(System.currentTimeMillis());
            eArchive.setLastOutPerson(eArchiveBatch.getCreatePerson());
            commonMapper.updateById(eArchive);
        }
        return 1;
    }


    public String md5HashCode(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            String fileHash = Hex.encodeHexString(md.digest());
            return fileHash;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public EArchiveBatch insertEArchiveBatch(long num) {
        EArchiveBatch batch = new EArchiveBatch();
        batch.setBatchName("自动入库");
        batch.setBatchType(EArchiveBatchService.BATCH_TYPE_IN);
        batch.setStartDate(System.currentTimeMillis());
        batch.setDetailNum(num);
        batch.setBeizhu("归档入库");
//        batch.setOrgId();
        insert(batch);
        return batch;
    }

}