package com.dr.archive.enums;

import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.config.model.MetaMap;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FieldType;

import java.util.Collection;

/**
 * @author caor
 * @date 2020/7/30 9:47
 * @deprecated
 */
@Deprecated
public enum FilesField implements FieldModel {
    QUANZONG(ArchiveEntity.COLUMN_FOND_CODE, "全宗", FieldType.STRING, 255, 0, 1),
    NIANDU(ArchiveEntity.COLUMN_YEAR, "年度", FieldType.STRING, 10, 0, 2),
    JIGOU(ArchiveEntity.COLUMN_ORG_CODE, "机构", FieldType.STRING, 255, 0, 4),
    FENLEI(ArchiveEntity.COLUMN_CATEGORY_CODE, "分类", FieldType.STRING, 255, 0, 5),
    DANGHAO(ArchiveEntity.COLUMN_ARCHIVE_CODE, "档号", FieldType.STRING, 255, 0, 6),
    TIMING(ArchiveEntity.COLUMN_TITLE, "题名", FieldType.STRING, 1000, 0, 7),
    BAOGUANQIXIAN(ArchiveEntity.COLUMN_SAVE_TERM, "保管期限", FieldType.STRING, 255, 0, 10),
    ZERENZHE(ArchiveEntity.COLUMN_DUTY_PERSON, "责任者", FieldType.STRING, 255, 0, 11),
    WENHAO(ArchiveEntity.COLUMN_FILECODE, "文号", FieldType.STRING, 255, 0, 12),
    XINGCHENGRIQI(ArchiveEntity.COLUMN_FILETIME, "形成日期", FieldType.STRING, 50, 0, 13),
    YESHU(ArchiveEntity.COLUMN_YS, "页数", FieldType.STRING, 10, 0, 14),
    //"status" TODO 可能有问题
    ZHUANGTAI(ArchiveEntity.STATUS_COLUMN_KEY, "状态", FieldType.STRING, 10, 0, 17);

    private String code, name;
    private FieldType type;
    private int fieldLength, fieldScale, order;

    FilesField(String code, String name, FieldType type, int fieldLength, int fieldScale, int order) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.fieldLength = fieldLength;
        this.fieldScale = fieldScale;
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public void setFieldScale(int fieldScale) {
        this.fieldScale = fieldScale;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getFormDefinitionName() {
        return null;
    }

    @Override
    public String getFormDefinitionId() {
        return null;
    }

    @Override
    public String getFormDefinitionCode() {
        return null;
    }

    @Override
    public String getFieldCode() {
        return this.code;
    }

    @Override
    public Collection<String> getFieldAlias() {
        return null;
    }

    @Override
    public FieldType getFieldType() {
        return this.type;
    }

    @Override
    public int getFieldLength() {
        return this.fieldLength;
    }

    @Override
    public int getFieldScale() {
        return this.fieldScale;
    }

    @Override
    public Integer getFieldOrder() {
        return this.order;
    }

    @Override
    public String getFieldState() {
        return "1";
    }

    @Override
    public Integer getVersion() {
        return null;
    }

    @Override
    public String getLabel() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getRemarks() {
        return null;
    }

    @Override
    public MetaMap getMeta() {
        return null;
    }
}
