package com.dr.archive.model.entity;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.orm.annotations.Column;

import static com.dr.archive.model.entity.ArchiveEntity.*;

/**
 * 档案关联表
 * 从这张表能够关联查询到具体的一条档案信息
 *
 * @author dr
 */
public class AbstractArchiveRelateEntity extends AbstractFondCategoryRelateEntity {
    /**
     * 表单
     */
    @Column(comment = "表单定义Id", length = 100)
    private String formDefinitionId;
    @Column(comment = "表单数据Id", length = 100)
    private String formDataId;

    /**
     * 档案基本信息
     */
    @Column(name = COLUMN_ORG_CODE, comment = "机构或问题")
    private String orgCode;
    @Column(name = COLUMN_ARCHIVE_CODE, comment = "档号")
    private String archiveCode;
    @Column(name = COLUMN_TITLE, comment = "题名", length = 800)
    private String title;
    @Column(name = COLUMN_KEY_WORDS, comment = "关键词", length = 800)
    private String keyWords;
    @Column(name = COLUMN_NOTE, comment = "备注", length = 2000)
    private String note;
    @Column(name = COLUMN_YEAR, comment = "年度", length = 50)
    private String year;
    @Column(name = COLUMN_SAVE_TERM, comment = "保管期限", length = 50)
    private String saveTerm;


    /**
     * 从一个关联表复制到另外一个关联表中
     * <p>
     * 注意，子类有特殊情况要记得继承
     *
     * @param target
     */
    public void cloneArchive(AbstractArchiveRelateEntity target) {
        target.setFondName(getFondName());
        target.setFondCode(getFondCode());
        target.setCategoryCode(getCategoryCode());
        target.setCategoryId(getCategoryId());
        target.setCategoryName(getCategoryName());
        target.setFormDefinitionId(getFormDefinitionId());
        target.setFormDataId(getFormDataId());
        target.setOrgCode(getOrgCode());
        target.setArchiveCode(getArchiveCode());
        target.setTitle(getTitle());
        target.setKeyWords(getKeyWords());
        target.setNote(getNote());
        target.setYear(getYear());
        target.setSaveTerm(getSaveTerm());
    }

    /**
     * 根据档案实际数据表复制数据
     *
     * @param archiveEntity
     */
    public void bindArchiveInfo(AbstractArchiveEntity archiveEntity) {
        setFondCode(archiveEntity.getFondCode());
        setCategoryCode(archiveEntity.getCategoryCode());

        setFormDataId(archiveEntity.getId());

        setOrgCode(archiveEntity.getOrgCode());
        setArchiveCode(archiveEntity.getArchiveCode());
        setTitle(archiveEntity.getTitle());
        setKeyWords(archiveEntity.getKeyWords());
        setNote(archiveEntity.getNote());
        setYear(archiveEntity.getYear());
        setSaveTerm(archiveEntity.getSaveTerm());
    }

    /**
     * 绑定表单信息
     *
     * @param formData
     */
    public void bindFormData(FormData formData) {
        if (formData != null) {
            setFormDefinitionId(formData.getFormDefinitionId());
            setFormDataId(formData.getId());

            setOrgCode(formData.getString(ArchiveEntity.COLUMN_ORG_CODE));
            setArchiveCode(formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            setTitle(formData.getString(ArchiveEntity.COLUMN_TITLE));
            setKeyWords(formData.getString(ArchiveEntity.COLUMN_KEY_WORDS));
            setNote(formData.getString(ArchiveEntity.COLUMN_NOTE));
            setYear(formData.getString(ArchiveEntity.COLUMN_YEAR));
            setSaveTerm(formData.getString(COLUMN_SAVE_TERM));
            setFondCode(formData.getString(COLUMN_FOND_CODE));
            setCategoryCode(formData.getString(COLUMN_CATEGORY_CODE));
        }
    }

    public void bindAll(FormData formData, Fond fond, Category category) {
        bindAll(formData, fond, category, false);
    }

    public void bindAll(FormData formData, Fond fond, Category category, boolean bindCreateInfo) {
        //绑定表单信息
        bindFormData(formData);
        //绑定全宗信息
        bindFondInfo(fond);
        //绑定门类信息
        bindCategoryInfo(category);
        if (bindCreateInfo) {
            CommonService.bindCreateInfo(this);
        }
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }


}
