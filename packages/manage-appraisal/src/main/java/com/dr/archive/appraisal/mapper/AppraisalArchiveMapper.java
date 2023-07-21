package com.dr.archive.appraisal.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AppraisalArchiveMapper {

    /**
     * 批量将待鉴定数据插入至鉴定批次档案表中
     * @param batchId
     * @return
     */
    @Insert("INSERT into archive_manage_arvhie_appraisal_task(" +
            "id,createDate,status_info,fondId,organiseId,formDataId,filetime,title,vintages,appraisalType," +
            "categoryCode,categoryName,fondCode,categoryId,archiveCode,batchId,formDefinitionId,auxiliaryResult,isSeeHisTory,matchingWordGroup,sign) " +
            "select id,createDate,'0' as status_info,fondId,organiseId,formDataId,filetime,title,vintages,appraisalType," +
            "categoryCode,categoryName,fondCode,categoryId,archiveCode,batchId,formDefinitionId,auxiliaryResult, " +
            " '1' as isSeeHisTory,matchingWordGroup,'0' as sign from  archive_manage_arvhie_4tobe_appraisal t where batchId = #{batchId, jdbcType=VARCHAR} ")
    long insertArchiveTaskOfToBeAppraisal(@Param("batchId") String batchId);


    /**
     * 鉴定任务领取，按照条件更新一定的鉴定档案，此方法需配合synchronized使用
     * @param taskId
     * @param limtNumber
     * @param batchId
     * @param auxiliaryResult
     * @return
     */
    @Update("update archive_manage_arvhie_appraisal_task set taskId = #{taskId, jdbcType=VARCHAR},status_info = 0 where taskId is null " +
            "and batchId = #{batchId, jdbcType=VARCHAR} and auxiliaryResult = #{auxiliaryResult, jdbcType=VARCHAR} LIMIT " +
            "#{limtNumber, jdbcType=BIGINT}")
    long updateAppraisalTaskIdOFOpen(@Param("taskId") String taskId,
                               @Param("limtNumber") long limtNumber,
                               @Param("batchId") String batchId,
                               @Param("auxiliaryResult") String auxiliaryResult);


    /**
     *
     * @param taskId
     * @param limtNumber
     * @param batchId
     * @return
     */
    @Update("update archive_manage_arvhie_appraisal_task set taskId = #{taskId, jdbcType=VARCHAR},status_info = 0 where taskId is null " +
            "and batchId = #{batchId, jdbcType=VARCHAR} LIMIT " +
            "#{limtNumber, jdbcType=BIGINT}")
    long updateAppraisalTaskIdOFExpire(@Param("taskId") String taskId,
                                     @Param("limtNumber") long limtNumber,
                                     @Param("batchId") String batchId);
}
