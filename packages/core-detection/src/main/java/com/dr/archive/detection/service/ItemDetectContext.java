package com.dr.archive.detection.service;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.entity.TestRecordItem;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItem;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.util.SessionMapContext;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.organise.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 所有检测项都检测过之后在保存数据
 * <p>
 * 四性检测上下文
 *
 * @author dr
 */
public class ItemDetectContext extends SessionMapContext {
    /**
     * 全宗
     */
    private Fond fond;
    /**
     * 门类
     */
    private Category category;
    /**
     * 表单定义
     */
    private FormDefinition formDefinition;
    /**
     * 表单数据
     */
    private FormData formData;
    /**
     * 附件数据
     */
    private List<FileInfo> fileInfos;
    /**
     * 当前登录人
     */
    private Person person;
    /**
     * 检测环节
     */
    private LinkType linkType;
    /**
     * 检测批次
     */
    private TestRecord testRecord;
    /**
     * 四性检测结果
     */
    private List<TestRecordItem> itemList = new ArrayList<>();
    /**
     * 配置的检测编码
     */
    private Set<String> configCodes;

    /**
     * 四性检测方案项，具体检测时需要查询出配置的字段检测信息
     */
    private FourNatureSchemeItem fourNatureSchemeItem;

    /**
     * 添加四性检测小项结果
     *
     * @param itemCode   检测小项编码
     * @param modeCode   实现小项编码
     * @param testResult 检测结果描述
     * @param status     检测结果状态
     * @return 其他参数可以在返回结果中继续处理
     */
    public TestRecordItem addRecordItem(String itemCode, String modeCode, String testResult, String status) {
        TestRecordItem recordItem = new TestRecordItem();
        recordItem.setItemCode(itemCode);
        recordItem.setModeCode(modeCode);
        recordItem.setTestResult(testResult);
        recordItem.setStatus(status);
        recordItem.setCreateDate(System.currentTimeMillis());
        itemList.add(recordItem);
        return recordItem;
    }

    /**
     * 添加四性检测小项结果
     *
     * @param itemCode    检测小项编码
     * @param modeCode    实现小项编码
     * @param testResult  检测结果描述
     * @param status      检测结果状态
     * @param targetCode  检测对象编码
     * @param targetName  检测对象名称
     * @param targetValue 检测对象值
     */
    public TestRecordItem addRecordItem(String itemCode, String modeCode, String testResult, String status, String targetCode, String targetName, String targetValue) {
        TestRecordItem recordItem = new TestRecordItem();
        recordItem.setItemCode(itemCode);
        recordItem.setModeCode(modeCode);
        recordItem.setTestResult(testResult);
        recordItem.setStatus(status);
        recordItem.setTargetCode(targetCode);
        recordItem.setTargetName(targetName);
        recordItem.setTargetValue(targetValue);
        recordItem.setCreateDate(System.currentTimeMillis());
        itemList.add(recordItem);
        return recordItem;
    }

    public TestRecordItem addRecordItem(String itemCode, String modeCode, String testResult) {
        return addRecordItem(itemCode, modeCode, testResult, TestRecordService.TEST_STATUS_SUCCESS);
    }

    public FourNatureSchemeItem getFourNatureSchemeItem() {
        return fourNatureSchemeItem;
    }

    public void setFourNatureSchemeItem(FourNatureSchemeItem fourNatureSchemeItem) {
        this.fourNatureSchemeItem = fourNatureSchemeItem;
    }

    /**
     * 追加指定类型的检测编码
     *
     * @param itemCode
     */
    public TestRecordItem addRecordItem(String itemCode, String modeCode) {
        return addRecordItem(itemCode, modeCode, "检测通过", TestRecordService.TEST_STATUS_SUCCESS);
    }

    /**
     * 判断是否检测过指定的编码
     *
     * @param itemCode
     * @return
     */
    public boolean isCodeTest(String itemCode) {
        for (TestRecordItem recordItem : itemList) {
            if (itemCode.equals(recordItem.getItemCode())) {
                return true;
            }
        }
        return false;
    }


    public Fond getFond() {
        return fond;
    }

    public void setFond(Fond fond) {
        this.fond = fond;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FormDefinition getFormDefinition() {
        return formDefinition;
    }

    public void setFormDefinition(FormDefinition formDefinition) {
        this.formDefinition = formDefinition;
    }

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public List<TestRecordItem> getItemList() {
        return itemList;
    }

    public int getItemSize() {
        return itemList.size();
    }

    public void setItemList(List<TestRecordItem> itemList) {
        this.itemList = itemList;
    }

    public TestRecord getTestRecord() {
        return testRecord;
    }

    public void setTestRecord(TestRecord testRecord) {
        this.testRecord = testRecord;
    }

    public Set<String> getConfigCodes() {
        return configCodes;
    }

    public void setConfigCodes(Set<String> configCodes) {
        this.configCodes = configCodes;
    }
}
