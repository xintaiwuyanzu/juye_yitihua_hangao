package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBatch;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchDetail;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatchDetailInfo;
import com.dr.archive.common.dzdacqbc.service.EArchiveBatchDetailService;
import com.dr.archive.common.dzdacqbc.vo.DeliveryDetailVo;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 出入库批次详情
 *
 * @author dr
 */
@Service
public class EArchiveBatchDetailServiceImpl extends DefaultBaseService<EArchiveBatchDetail> implements EArchiveBatchDetailService {

    @Override
    public ResultEntity deleteById(String id, String batchId) {
        EArchiveBatch archiveBatch = commonMapper.selectById(EArchiveBatch.class, batchId);
        if(archiveBatch.getStatus().equals("1")){
            return ResultEntity.error("已出库无法移除");
        }
        EArchiveBatchDetail detail = selectById(id);
        if(detail != null){
            if("1".equals(detail.getModelType())){//案卷类型 同步移除卷内文件
                SqlQuery<EArchiveBatchDetail> sqlC = SqlQuery.from(EArchiveBatchDetail.class)
                        .like(EArchiveBatchDetailInfo.ARCHIVECODE, detail.getArchiveCode()).equal(EArchiveBatchDetailInfo.MODELTYPE, "4")
                        .equal(EArchiveBatchDetailInfo.BATCHID, batchId);
                commonMapper.deleteByQuery(sqlC);
            }
            long l = commonMapper.deleteById(EArchiveBatchDetail.class,id);
            if(l==0){
                return ResultEntity.error("请不要重复移除");
            }
        }
        return ResultEntity.success();
    }
    /**
     * 获得档案出库详情page
     */
    @Override
    public Page<DeliveryDetailVo> getDetailPage(String batchId, String archiveCode, int pageIndex, int pageSize) {
        SqlQuery<EArchiveBatchDetail> sqlQuery = SqlQuery.from(EArchiveBatchDetail.class);
        if(StringUtils.hasText(batchId)){
            sqlQuery.equal(EArchiveBatchDetailInfo.BATCHID, batchId);
        }
        if(StringUtils.hasText(archiveCode)){
            sqlQuery.equal(EArchiveBatchDetailInfo.ARCHIVECODE, archiveCode);
        }
        //查询案卷和文件
        sqlQuery.andNew().equal(EArchiveBatchDetailInfo.MODELTYPE, "1").or().equal(EArchiveBatchDetailInfo.MODELTYPE, "0");

        Page<DeliveryDetailVo> details = commonMapper.selectPageByQuery(sqlQuery.setReturnClass(DeliveryDetailVo.class), pageIndex * pageSize, (pageIndex + 1) * pageSize);
        for (DeliveryDetailVo detail : details.getData()) {
            if("1".equals(detail.getModelType())){//如果类型是案卷，查询名下的卷内文件
                //查询案卷下的卷内文件
                SqlQuery<EArchiveBatchDetail> sqlC = SqlQuery.from(EArchiveBatchDetail.class)
                        .like(EArchiveBatchDetailInfo.ARCHIVECODE, detail.getArchiveCode()).equal(EArchiveBatchDetailInfo.MODELTYPE, "4")
                        .equal(EArchiveBatchDetailInfo.BATCHID, batchId);
                List<DeliveryDetailVo> child = commonMapper.selectByQuery(sqlC.setReturnClass(DeliveryDetailVo.class));
                detail.setVoList(child);
            }
        }
        return details;
    }

}
