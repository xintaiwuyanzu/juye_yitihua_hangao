package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;

import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItem;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItemMethod;
import com.dr.archive.fournaturescheck.entity.FourNatureSchemeItemMethodInfo;


import com.dr.archive.fuzhou.configManager.bo.FieldConfig;
import com.dr.archive.fuzhou.configManager.bo.FieldMetaData;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.MetadataRuleTest;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.TestRule;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dr.archive.fuzhou.configManager.bo.itemconfig.MetaRule.*;

/**
 * 1-2-1 检测归档信息包元数据是否符合《福州市政务服务事项电子文件归档技术规范》中的元数据方案要求，包括数据长度、类型、格式、值域以及元数据项赋值是否合理等。
 *
 * @author: qiuyf
 * @date: 2022/4/20 10:15
 */
@Component
public class Authenticity002Service extends AbstractApproveReceiveDetectService {
    @Autowired
    ConfigManagerClient configManagerClient;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Autowired
    CommonMapper commonMapper;

    @Override
    public String code() {
        return "Authenticity002";
    }

    @Override
    public String modeCode() {
        return "1-2-1";
    }

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy年MM月dd日", "yyyyMMdd", "yyyy/MM/dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};


    @Override
    public void detection(ItemDetectContext context) {

        FormData formData = context.getFormData();

        FourNatureSchemeItem fourNatureSchemeItem = context.getFourNatureSchemeItem();

        List<FourNatureSchemeItemMethod> fourNatureSchemeItemMethods = new ArrayList<>();

        //先查询表单上配置的元数据检测信息，如果没有查到再去查检测项上配置的元数据检测信息
        fourNatureSchemeItemMethods.addAll(commonMapper.selectByQuery(SqlQuery.from(FourNatureSchemeItemMethod.class)
                .equal(FourNatureSchemeItemMethodInfo.FORMDEFINEID, formData.getFormDefinitionId())));

        if (fourNatureSchemeItemMethods.size() == 0) {
            fourNatureSchemeItemMethods.addAll(commonMapper.selectByQuery(SqlQuery.from(FourNatureSchemeItemMethod.class)
                    .equal(FourNatureSchemeItemMethodInfo.FOURNATURESCHEMEITEMID, fourNatureSchemeItem.getId())));
        }

        for (FourNatureSchemeItemMethod schemeItemMethod : fourNatureSchemeItemMethods) {

            if (schemeItemMethod.getIfRequired().equals("是")) {
                if (StringUtils.hasText(formData.get(schemeItemMethod.getFormFieldCode()))) {
                    context.addRecordItem(code(), modeCode(), "【不为空】校验", TestRecordService.TEST_STATUS_SUCCESS, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                } else {
                    context.addRecordItem(code(), modeCode(), "【不为空】校验", TestRecordService.TEST_STATUS_FAIL, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                }
            }

            //判断是否需要进行字段长度检测,如果字段最大值和最小值还有元数据不为空则进行检测
            if (StringUtils.hasText(schemeItemMethod.getFormFieldShort()) &&
                    StringUtils.hasText(schemeItemMethod.getFormFieldLong()) &&
                    StringUtils.hasText(formData.get(schemeItemMethod.getFormFieldCode()))) {
                int length = formData.getString(schemeItemMethod.getFormFieldCode()).length();
                if (Integer.parseInt(schemeItemMethod.getFormFieldShort()) <= length && length >= Integer.parseInt(schemeItemMethod.getFormFieldShort())) {
                    context.addRecordItem(code(), modeCode(), "【长度】校验", TestRecordService.TEST_STATUS_SUCCESS, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                } else {
                    context.addRecordItem(code(), modeCode(), "【长度】校验", TestRecordService.TEST_STATUS_FAIL, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                }
            }
            //类型校验
            typeValidation(context, schemeItemMethod, formData);
        }

        //if (detectEnable(context)) { //判断是否进行检测
        //1,检测元数据,无约束条件
        //获取元数据检测规则 直接从元数据方案获取
        /*String gdType = context.getAttribute("gdType");
        FormData formData = context.getFormData();
        ApproveItemConfig approveItemConfig = new ApproveItemConfig();
        List<FieldConfig> metadata = new ArrayList<>();
        if ("Online".equals(gdType)) {
            TransferInfo.TransferDirectory directory = context.getAttribute(ApprovalOnlineReceiveProvider.DIRECTORY_KEY);
            String classify = context.getAttribute("classify");
            String arrange = context.getAttribute("arrange");
            //  metadata = configManagerClient.getCategoryMetadata(directory.getCategoryCode(), classify, arrange);

        } else {
            String formCode = context.getAttribute("formCode");
            //离线接收 查元数据方案
               *//* for (CategoryYear categoryYear : configManagerClient.getArchiveTypeYear(context.getCategory().getCode())) {
                    String[] fileTypes = categoryYear.getType().split(",");
                    String code = String.join("_", categoryYear.getSysCode(), fileTypes[0]).toLowerCase();
                    if (formCode.equals(code)) {
                        //   metadata = configManagerClient.getCategoryMetadata(context.getCategory().getCode(), categoryYear.getClassify(), categoryYear.getArrange());
                    }
                }*//*
        }
        metadata.add(newMetaConfig());
        if (metadata.size() == 0) {
            context.addRecordItem(code(), modeCode(), "无元数据检测规则!", TestRecordService.TEST_STATUS_FAIL);
        } else {
            List<FieldMetaData> fieldMetaDataList = metadata.get(0).getMetadata();
            //获取元数据信息


            String testResult = "";
            boolean result = true;
            for (FieldMetaData fieldMetaData : fieldMetaDataList) {
                //只校验简单类型元数据 基本信息元数据
                if (FieldMetaData.META_TYPE_SIMPLE.equals(fieldMetaData.getMateType()) && "JBXXYSJ".equals(fieldMetaData.getAscription())) {
                    detectionMeta(fieldMetaData, formData, context);
                }
            }
        }*/
        //2,检测元数据,有约束条件
            /*List<MetadataRuleTest> metadataRuleTests = configManagerClient.getMetadataRuleTest(context.getCategory().getCode(), "2", "1");
            if (metadataRuleTests.size() > 0) {
                for (MetadataRuleTest metadataRuleTest : metadataRuleTests) {
                    detectionMetadata(metadataRuleTest, formData);
                }
            }*/

        // }
    }


    private void typeValidation(ItemDetectContext context, FourNatureSchemeItemMethod schemeItemMethod, FormData formData) {

        switch (schemeItemMethod.getFormFieldType()) {
            case "input":
                if (!isNumber(formData.getString(schemeItemMethod.getFormFieldCode())) && !isDate(formData.getString(schemeItemMethod.getFormFieldCode()))) {
                    context.addRecordItem(code(), modeCode(), "【字符串】数据类型校验", TestRecordService.TEST_STATUS_SUCCESS, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                } else {
                    context.addRecordItem(code(), modeCode(), "【字符串】数据类型校验", TestRecordService.TEST_STATUS_FAIL, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                }
                break;
            case "number":
                if (isNumber(formData.getString(schemeItemMethod.getFormFieldCode()))) {
                    context.addRecordItem(code(), modeCode(), "【数字】数据类型校验", TestRecordService.TEST_STATUS_SUCCESS, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                } else {
                    context.addRecordItem(code(), modeCode(), "【数字】数据类型校验", TestRecordService.TEST_STATUS_FAIL, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                }
                break;
            case "date":
                if (isDate(formData.getString(schemeItemMethod.getFormFieldCode()))) {
                    context.addRecordItem(code(), modeCode(), "【日期时间】数据类型校验", TestRecordService.TEST_STATUS_SUCCESS, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                } else {
                    context.addRecordItem(code(), modeCode(), "【日期时间】数据类型校验", TestRecordService.TEST_STATUS_FAIL, schemeItemMethod.getFormFieldCode(), schemeItemMethod.getFormFieldName(), formData.getString(schemeItemMethod.getFormFieldCode()));
                }
                break;

        }

    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDate(String value) {
        if (StringUtils.hasText(value)) {
            try {
                DateUtils.parseDate(value, parsePatterns);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    private FieldConfig newMetaConfig() {
        FieldConfig config = new FieldConfig();
        List<FieldMetaData> fieldMetaData = new ArrayList<>();
        List<FourNatureSchemeItemMethod> fourNatureSchemeItemList = commonMapper.selectAll(FourNatureSchemeItemMethod.class);
        for (FourNatureSchemeItemMethod method : fourNatureSchemeItemList) {
            FieldMetaData metaData = new FieldMetaData();
            metaData.setMateType(FieldMetaData.META_TYPE_SIMPLE);
            metaData.setAscription("JBXXYSJ");
            metaData.seteName(method.getFormFieldCode());
            metaData.setName(method.getFormFieldName());
            metaData.setConstraints("是".equals(method.getIfRequired()) ? "1" : "0");
            metaData.setType(TYPE_CHARACTER);
            fieldMetaData.add(metaData);
        }
        config.setMetadata(fieldMetaData);
        return config;
    }

    public void addFailItem(ItemDetectContext context, String result, String code, String Name, String value) {
        context.addRecordItem(code(), modeCode(),
                result,
                TestRecordService.TEST_STATUS_FAIL,
                code,
                Name,
                value);
    }

    public void addSuccessItem(ItemDetectContext context, String result, String code, String Name, String value) {
        context.addRecordItem(code(), modeCode(),
                result,
                TestRecordService.TEST_STATUS_SUCCESS,
                code,
                Name,
                value);
    }

    private void detectionMeta(FieldMetaData rule, FormData formData, ItemDetectContext context) {
        Map<String, Object> resultmap = new HashMap<>();
        FormDefinition definition = context.getFormDefinition();
        String metaName = rule.geteName();
        //判断是否是别名
        FormField formField = definition.getFieldByAlias(metaName);
        if (formField != null) {
            metaName = formField.getFieldCode();
        }
        Object metaValue = formData.get(metaName);
        boolean result = true;
        String mgs = "";

        //判断是否必填
        if (metaValue == null || StringUtils.isEmpty(metaValue.toString())) {
            if ("1".equals(rule.getConstraints())) {
                result = false;
                addFailItem(context, "元数据【" + rule.geteName() + "】不能为空;", rule.geteName(), rule.getName(), null);
            }
        } else {
            if ("1".equals(rule.getConstraints())) {
                addSuccessItem(context, "【不为空】校验;", rule.geteName(), rule.getName(), metaValue.toString());
            }
            //判断数据类型
            Class valueType = metaValue.getClass();
            switch (rule.getType()) {
                case TYPE_INTEGER:
                    if (!isNumeric2(metaValue.toString())) {
                        result = false;
                        addFailItem(context, "数据类型不对，应该为整型;", metaName, rule.getName(), metaValue.toString());
                        //mgs += "数据类型" + metaValue + "不对，应该为" + TYPE_INTEGER + ";";
                    } else {
                        addSuccessItem(context, "【整型】数据类型校验;", rule.geteName(), rule.getName(), metaValue.toString());
                    }
                    break;
                case TYPE_DATE_TYPE:
                    if (!isNumeric2(metaValue.toString())) {
                        result = false;
                        addFailItem(context, "数据类型不对，应该为日期时间型;", metaName, rule.getName(), metaValue.toString());
                        //mgs += "数据类型" + metaValue + "不对，应该为" + TYPE_DATE_TYPE + ";";
                    } else {
                        addSuccessItem(context, "【日期时间】数据类型校验;", rule.geteName(), rule.getName(), metaValue.toString());
                    }
                    break;
                case TYPE_CHARACTER:
                    if (!String.class.isAssignableFrom(valueType)) {
                        result = false;
                        addFailItem(context, "数据类型不对，应该为字符串型;", metaName, rule.getName(), metaValue.toString());
                        //mgs += "数据类型" + metaValue + "不对，应该为" + TYPE_CHARACTER + ";";
                    } else {
                        addSuccessItem(context, "【字符串】数据类型校验;", rule.geteName(), rule.getName(), metaValue.toString());
                    }
                    break;
                default:
                    result = false;
                    addFailItem(context, "未识别数据类型", metaName, rule.getName(), metaValue.toString());
                    //mgs += "未识别数据类型" + metaValue + "未识别;";
            }
        }

        if (result) {
            //context.addRecordItem(code(), modeCode());
        }
        //resultmap.put("result", result);
        //resultmap.put("mgs", mgs);
        //return resultmap;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }


    private void detectionMetadata(MetadataRuleTest metadataRuleTest, FormData formData) {
        Object metaValue = formData.get(metadataRuleTest.getMetadata());
        for (TestRule testRule : metadataRuleTest.getRules()) {
            List<TestRule.RuleCondition> conditions = testRule.getConditions();
            //判断校验条件是否都成立,成立则进行校验,不成立则不进行校验  默认成立
            boolean flag = true;
            for (TestRule.RuleCondition ruleCondition : conditions) {
                Object conditionMVal = formData.get(ruleCondition.getMetadata());
                switch (ruleCondition.getCondition()) {
                    case "包含":
                        if (conditionMVal.toString().indexOf(ruleCondition.getVal()) == -1) {
                            flag = false;
                        }
                        break;
                    case "不包含":
                        if (conditionMVal.toString().indexOf(ruleCondition.getVal()) != -1) {
                            flag = false;
                        }
                        break;
                    case "等于":
                        if (!conditionMVal.toString().equals(ruleCondition.getVal())) {
                            flag = false;
                        }
                        break;
                    case "大于":
                        break;
                    case "小于":
                        break;
                    case "不为空":
                        if (metaValue == null) {
                            flag = false;
                        }
                        break;
                }
            }
            if (flag) {

            }
        }
    }

    //
    private void detectionMetadataTerm() {

    }
}
