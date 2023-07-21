package com.dr.archive.manage.codingscheme.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.codingscheme.entity.CodingScheme;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeInfo;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItem;
import com.dr.archive.manage.codingscheme.entity.CodingSchemeItemInfo;
import com.dr.archive.manage.codingscheme.service.CodingSchemeItemService;
import com.dr.archive.manage.codingscheme.service.CodingSchemeService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.service.impl.BaseYearServiceImpl;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 大段没用的代码
 * describe
 * 编码方案主表
 *
 * @author tzl
 * @date 2020/5/29 16:34
 */
@Service
public class CodingSchemeServiceImpl extends BaseYearServiceImpl<CodingScheme> implements CodingSchemeService, ArchiveDataPlugin {
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondService fondService;
    @Autowired
    CodingSchemeItemService codingSchemeItemService;
    @Autowired
    FormDataService formDataService;

    @Override
    public Page<CodingScheme> findSchemeByFondId(String fondId, Integer index, Integer size) {
        List<Category> categories = commonMapper.selectByQuery(SqlQuery.from(Category.class)
                .equal(CategoryInfo.FONDID, fondId));
        List<String> businessIdList = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        if (businessIdList.size() < 1) {
            businessIdList.add("");
        }
        Page<CodingScheme> page = commonMapper.selectPageByQuery(SqlQuery.from(CodingScheme.class)
                .in(CodingSchemeInfo.BUSINESSID, businessIdList), (index - 1) * size, index * size);
        page.getData().stream().peek(category -> {
            String businessId = category.getBusinessId();
            Category categoryTo = categoryService.selectById(businessId);
            category.setCategoryName(categoryTo.getName());
        }).collect(Collectors.toList());
        return page;
    }

    @Override
    public List<CodingSchemeItem> findSchemeByBusinessId(String businessId) {
        CodingScheme scheme = getScheme(businessId, Calendar.getInstance().get(Calendar.YEAR));
        List<CodingSchemeItem> codingSchemeItems = null;
        if (scheme != null) {
            codingSchemeItems = codingSchemeItemService.selectList(SqlQuery.from(CodingSchemeItem.class).equal(CodingSchemeItemInfo.BUSINESSID, scheme.getId()));
        }
        return codingSchemeItems;
    }

    @Override
    public void deleteByBusinessId(String businessId) {
        List<CodingScheme> codingSchemes = commonMapper.selectByQuery(SqlQuery.from(CodingScheme.class)
                .equal(CodingSchemeInfo.BUSINESSID, businessId));
        for (CodingScheme codingScheme : codingSchemes) {
            commonMapper.deleteByQuery(SqlQuery.from(CodingSchemeItem.class)
                    .equal(CodingSchemeItemInfo.BUSINESSID, codingScheme.getId()));
            commonMapper.deleteById(CodingScheme.class, codingScheme.getId());
//            deleteByIds(codingScheme.getId());
        }
    }

    //TODO
    @Override
    public String genArchiveCode(String cateGoryId, Integer year, ArchiveEntity archive) {
        return null;
    }

    //TODO
    @Override
    public void validateCode(String cateGoryId, Integer year, String code) {

    }

    private CodingScheme getScheme(String categoryId, Integer year) {
        //根据分类id查询分类下所有编码方案
        List<CodingScheme> codingSchemes = this.selectList(SqlQuery.from(CodingScheme.class)
                .equal(CodingSchemeInfo.BUSINESSID, categoryId));
        if (codingSchemes != null && codingSchemes.size() > 0) {
            CodingScheme scheme = null;
            for (CodingScheme codingScheme : codingSchemes) {
                //选取编码方案在档案年度时间段的编码方案返回
                int startYear = codingScheme.getStartYear();
                int endYear = codingScheme.getEndYear();
                boolean b = (startYear == 0 && year <= endYear) || (endYear == 0 && year >= startYear) || (year >= startYear && year <= endYear);
                if (b) {
                    return codingScheme;
                    //年度条件内不存在方案选取默认方案
                } else if (codingScheme.isDefault()) {
                    scheme = codingScheme;
                }
            }
            return scheme;
            //都不存在返回空
        } else {
            //分类无编码方案返回空
            return null;
        }
    }

    @Override
    public FormData builderArchiveCode(FormData formData) {
        //根据分类查询默认档号生成方案
        String categoryCode = formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE);
        String fondCode = formData.get(ArchiveEntity.COLUMN_FOND_CODE);
        Fond fond = fondService.findFondByCode(fondCode);
        Category category = categoryService.findCategoryByCode(categoryCode, fond.getId());
        String vintages = formData.get(ArchiveEntity.COLUMN_YEAR);

        if (StringUtils.isEmpty(vintages)) {
            vintages = "0";
        }
        CodingScheme codingScheme = getScheme(category.getId(), Integer.parseInt(vintages));
        if (codingScheme == null) {
//            formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE,"");
            return formData;
        }
        Integer digit = codingScheme.getDigit();
        StringBuilder code = new StringBuilder();
        String archiveCode = formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
        //存在档号为卷内目录所在案卷的档号，做为卷内目录的基础档号
        if (!StringUtils.isEmpty(archiveCode)) {
            code = new StringBuilder(archiveCode + "-");
        } else {
            //根据档号生成方案生成基础档号
            List<CodingSchemeItem> codingSchemeItems = codingSchemeItemService.selectList(SqlQuery.from(CodingSchemeItem.class)
                    .equal(CodingSchemeItemInfo.BUSINESSID, codingScheme.getId())
                    .orderBy(CodingSchemeItemInfo.ORDERBY));
            if (codingSchemeItems == null || codingSchemeItems.size() <= 0) {
                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, "");
                return formData;
            }
            for (CodingSchemeItem codingSchemeItem : codingSchemeItems) {
                String itemCode = codingSchemeItem.getCode();
                String connector = codingSchemeItem.getConnector();
                String name = codingSchemeItem.getName();
                if (StringUtils.isEmpty(name)) {
                    code.append(itemCode).append(connector);
                } else {
                    String serializable = formData.get(itemCode);
                    if (StringUtils.isEmpty(serializable)) {
                        Assert.isTrue(false, name + "字段不可为空");
                    }
                    code.append(serializable).append(connector);
                }
            }
        }
        //根据基础档号查询基础档号存在的档案
        String finalCode = code.toString();
        String status = formData.get(ArchiveEntity.COLUMN_STATUS);
        List<FormData> formDataList = formDataService.selectFormData(formData.getFormDefinitionId(), (sqlQuery, formRelationWrapper) -> {
            sqlQuery.like(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE), finalCode);
            sqlQuery.equal(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_STATUS), status);
            sqlQuery.orderByDesc(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        });

        //已有序号根据序号生成档号，无序号根据已有档号排序生成档号
        //顺号位数得到最后一位，完成生成
        String order = formData.get(ArchiveEntity.ORDER_COLUMN_NAME);

        if (!StringUtils.isEmpty(order)) {
            int length = (order + "").length();
            if (digit > length) {
                for (int i = 0; i < digit - length; i++) {
                    code.append("0");
                }
            }
            code.append(order);
        } else if (StringUtils.isEmpty(order) && formDataList.size() > 0) {
            FormData data = formDataList.get(0);
            String danghao = data.get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
            danghao = danghao.replaceAll(code.toString(), "");
            if (danghao.indexOf("-") > 0) {
                danghao = danghao.substring(0, danghao.indexOf("-"));
            }
            order = (Integer.parseInt(danghao) + 1) + "";
            int length = (order + "").length();
            if (digit > length) {
                for (int i = 0; i < digit - length; i++) {
                    code.append("0");
                }
            }

            code.append(order);
            formData.put(ArchiveEntity.ORDER_COLUMN_NAME, order);
        } else {
            for (int i = 0; i < digit - 1; i++) {
                code.append("0");
            }
            code.append("1");
            formData.put(ArchiveEntity.ORDER_COLUMN_NAME, 1);
        }

        formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, code.toString());
        return formData;
    }

    @Override
    public FormData beforeInsert(FormData data, ArchiveDataContext context) {
        //TODO 拆分 根据条件判断生成档号
        return ArchiveDataPlugin.super.beforeInsert(data, context);
    }

    @Override
    protected Class getSubTableClass() {
        return CodingSchemeItem.class;
    }

    @Override
    protected Column getRelateColumn() {
        return CodingSchemeItemInfo.BUSINESSID;
    }
}
