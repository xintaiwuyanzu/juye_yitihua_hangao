package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;

/**
 * 电子档案长期保存档案信息表实体类
 *
 * @author hyj
 */
@Table(name = Constants.DZDNCQBC + "archives", module = Constants.MODULE_NAME, comment = "电子档案长期保存档案信息表")
public class EArchive extends AbstractArchiveEntity {
    public static final String CQBCARCHIVE_ASTATUS_MANAGE = "MANAGE";

    @Column(comment = "档案id", length = 100)
    private String archiveId;

    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;

    @Column(comment = "数据id", length = 100)
    private String formDataId;

    @Column(comment = "分类信息id", length = 200)
    private String classificationId;
    @Column(comment = "存储方案Id", length = 200)
    private String spaceId;
    @Column(comment = "整理方式", length = 200)
    private String collatedForm;

    @Column(comment = "载体类型", length = 200)
    private String carrierType;

    @Column(comment = "所有文件大小总数")
    private long fileTotalSize;

    @Column(comment = "入库时间", type = ColumnType.DATE)
    private long inDate;
    @Column(comment = "入库人名称")
    private String inPerson;
    @Column(comment = "入库Id")
    private String inBatchId;
    @Column(comment = "入库批次名称")
    private String inBatchNo;
    @Column(comment = "出库次数")
    private long outCount;
    @Column(comment = "上次出库时间", type = ColumnType.DATE)
    private long lastOutDate;
    @Column(comment = "上次出库人名称")
    private String lastOutPerson;
    @Column(comment = "最后检测时间", type = ColumnType.DATE)
    private long lastTestDate;
    @Column(comment = "档案状态",length = 50)
    private String archiveStatus;
    @Column(comment = "检测次数",length = 100)
    private long testingNum;
    @Column(comment = "最后备份时间")
    private long lastBackupsDate;

    @Column(comment = "是否为卷内文件")
    private String isJNWJ;
    @Column(comment = "案卷档号")
    private String ajdh;

    @Column(comment = "档案类型", length = 50)
    private String modelType;

    public String getIsJNWJ() {
        return isJNWJ;
    }

    public void setIsJNWJ(String isJNWJ) {
        this.isJNWJ = isJNWJ;
    }

    public String getAjdh() {
        return ajdh;
    }

    public void setAjdh(String ajdh) {
        this.ajdh = ajdh;
    }

    public EArchive() {
    }

    public EArchive(FormData formData) {
        setId(UUIDUtils.getUUID());
        setStatus("MANAGE");
        setCreateDate(System.currentTimeMillis());
        setUpdateDate(System.currentTimeMillis());
        setCreatePerson(SecurityHolder.get().currentPerson().getId());
        setUpdatePerson(SecurityHolder.get().currentPerson().getId());
        setTitle(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_TITLE)) ? "" : formData.getString(ArchiveEntity.COLUMN_TITLE));
        setCatalogueCode(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_CATALOGUE_CODE)) ? "" : formData.getString(ArchiveEntity.COLUMN_CATALOGUE_CODE));
        setArchiveCode(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE)) ? "" : formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        setOrgCode(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_ORG_CODE)) ? "" : formData.getString(ArchiveEntity.COLUMN_ORG_CODE));
        setSourceType(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SOURCE_TYPE)) ? "" : formData.getString(ArchiveEntity.COLUMN_SOURCE_TYPE));
        setSourceId(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SOURCE_ID)) ? "" : formData.getString(ArchiveEntity.COLUMN_SOURCE_ID));
        setKeyWords(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_KEY_WORDS)) ? "" : formData.getString(ArchiveEntity.COLUMN_KEY_WORDS));
        setYear(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_YEAR)) ? "" : formData.getString(ArchiveEntity.COLUMN_YEAR));
        setFileTime(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_FILETIME)) ? "" : formData.getString(ArchiveEntity.COLUMN_FILETIME));
        setSaveTerm(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SAVE_TERM)) ? "" : formData.getString(ArchiveEntity.COLUMN_SAVE_TERM));
        setSecurityLevel(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SECURITY_LEVEL)) ? "" : formData.getString(ArchiveEntity.COLUMN_SECURITY_LEVEL));
        setDutyPerson(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_DUTY_PERSON)) ? "" : formData.getString(ArchiveEntity.COLUMN_DUTY_PERSON));
        setSubStatus(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_SUB_STATUS)) ? "" : formData.getString(ArchiveEntity.COLUMN_SUB_STATUS));
        setOpenScope(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_OPEN_SCOPE)) ? "" : formData.getString(ArchiveEntity.COLUMN_OPEN_SCOPE));
        setNote(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_NOTE)) ? "" : formData.getString(ArchiveEntity.COLUMN_NOTE));
        setYW_HAVE(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_YW_HAVE)) ? "" : formData.getString(ArchiveEntity.COLUMN_YW_HAVE));
        setWJLX(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_WJLX)) ? "" : formData.getString(ArchiveEntity.COLUMN_WJLX));
        //setMLDM(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_MLDM)) ? "" : formData.getString(ArchiveEntity.COLUMN_MLDM));
        setORGANISEID(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_ORGANISEID)) ? "" : formData.getString(ArchiveEntity.COLUMN_ORGANISEID));
        setFILE_COUNTS(StringUtils.isEmpty(formData.getString(ArchiveEntity.COLUMN_FILE_COUNTS)) ? "" : formData.getString(ArchiveEntity.COLUMN_FILE_COUNTS));
    }

    public long getLastBackupsDate() {
        return lastBackupsDate;
    }

    public void setLastBackupsDate(long lastBackupsDate) {
        this.lastBackupsDate = lastBackupsDate;
    }

    public long getTestingNum() {
        return testingNum;
    }

    public void setTestingNum(long testingNum) {
        this.testingNum = testingNum;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(String archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getCollatedForm() {
        return collatedForm;
    }

    public void setCollatedForm(String collatedForm) {
        this.collatedForm = collatedForm;
    }

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }

    public long getFileTotalSize() {
        return fileTotalSize;
    }

    public void setFileTotalSize(long fileTotalSize) {
        this.fileTotalSize = fileTotalSize;
    }

    public long getInDate() {
        return inDate;
    }

    public void setInDate(long inDate) {
        this.inDate = inDate;
    }

    public String getInPerson() {
        return inPerson;
    }

    public void setInPerson(String inPerson) {
        this.inPerson = inPerson;
    }

    public long getOutCount() {
        return outCount;
    }

    public void setOutCount(long outCount) {
        this.outCount = outCount;
    }

    public long getLastOutDate() {
        return lastOutDate;
    }

    public void setLastOutDate(long lastOutDate) {
        this.lastOutDate = lastOutDate;
    }

    public String getLastOutPerson() {
        return lastOutPerson;
    }

    public void setLastOutPerson(String lastOutPerson) {
        this.lastOutPerson = lastOutPerson;
    }

    public String getInBatchId() {
        return inBatchId;
    }

    public void setInBatchId(String inBatchId) {
        this.inBatchId = inBatchId;
    }

    public String getInBatchNo() {
        return inBatchNo;
    }

    public void setInBatchNo(String inBatchNo) {
        this.inBatchNo = inBatchNo;
    }

    public long getLastTestDate() {
        return lastTestDate;
    }

    public void setLastTestDate(long lastTestDate) {
        this.lastTestDate = lastTestDate;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
