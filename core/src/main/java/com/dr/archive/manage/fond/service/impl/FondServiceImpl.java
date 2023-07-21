package com.dr.archive.manage.fond.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.*;
import com.dr.archive.manage.fond.service.FondChangeHistoryService;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.service.impl.BasePermissionResourceService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.bo.PermissionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 全宗内部实现类
 *
 * @author caor
 * @author tuzl
 * @author dr
 */
@Service
public class FondServiceImpl extends BasePermissionResourceService<Fond> implements FondService {
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondChangeHistoryService fondChangeHistoryService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Autowired
    CategoryConfigService categoryConfigService;

    /**
     * TODO 如果机构为空，需要添加默认机构
     * TODO 需要往全宗-机构中添加关联数据
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(Fond entity) {
        String id = UUIDUtils.getUUID();
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(id);
        }
        bindFondOrganise(entity);
        //全宗历史变更
        fondChangeHistoryService.add(entity);
        //TODO 需要把全宗表中机构id，机构编码删除掉
//        entity.setPartyId(null);
        return super.insert(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateById(Fond entity) {
        //添加变更记录
        fondChangeHistoryService.updateByChange(selectById(entity.getId()), entity);
        //删除旧的全宗机构记录信息
        fondOrganiseService.delete(SqlQuery.from(FondOrganise.class).equal(FondOrganiseInfo.FONDID, entity.getId()));
        bindFondOrganise(entity);
        //修改全宗信息
        return super.updateById(entity);
    }

    /**
     * 全宗编码正则校验
     */
    static final Pattern FOND_CODE_REGEX = Pattern.compile("^[a-z0-9A-Z]+$");

    /**
     * 1、判断全宗号不能为空
     * 2、判断全宗号不能重复
     * 3、判断全宗号0-9a-zA-Z
     * 4、全宗名称可以从组织机构树中选择
     *
     * @param entity
     * @param excludeId
     */
    @Override
    public void validateCode(Fond entity, String... excludeId) {
        Assert.isTrue(entity != null, "参数不能为空！");
        Assert.isTrue(!(StringUtils.isEmpty(entity.getCode()) || StringUtils.isEmpty(entity.getName())), "全宗号或全宗名称不能为空！");
        //全宗号只能是数字和字母的组合，可以是存数字
        //Assert.isTrue(FOND_CODE_REGEX.matcher(entity.getCode()).matches(), "全宗号只能是字母和数字的组合！");
        //判断全宗号是否存在
        super.validateCode(entity, excludeId);
    }

    /**
     * TODO 删除全宗下所有分类关联的相关信息 分类-表单信息  存储方案 档号规则方案 显示方案
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long deleteById(String... ids) {
        long count = 0;
        Arrays.stream(ids).collect(Collectors.toList()).forEach(id -> {
            for (String fondId : id.split(",")) {
                // 删除全宗机构表
                fondOrganiseService.delete(SqlQuery.from(FondOrganise.class).equal(FondOrganiseInfo.FONDID, fondId));
                // 删除全宗历史记录表 TODO 全宗记录表设计的有问题
                fondChangeHistoryService.delete(SqlQuery.from(FondChangeHistory.class).equal(FondChangeHistoryInfo.FONDID, selectById(fondId).getCode()));
                //查询全宗下所有分类树
                List<Category> categories = categoryService.selectList(SqlQuery.from(Category.class).in(CategoryInfo.FONDID, fondId));
                categories.forEach(categorie -> {
                    //删除全宗分类下的配置
                    categoryConfigService.deleteByCategoryId(categorie.getId());
                    //删除全宗下的分类树
                    categoryService.deleteCategory(categorie.getId(), true);
                });
                //删除全宗信息
                super.deleteById(fondId);
            }
        });
        return count;
    }

    /**
     * ============================================================================
     * 上面的方法是给controller使用的
     * 下面的方法是给其他模块使用的
     * ============================================================================
     */
    /**
     * 根据全宗code查询全宗
     *
     * @param fondCode 全宗id TODO 添加查询日志，添加查询事件
     * @return
     */
    @Override
    public Fond findFondByCode(String fondCode) {
        Assert.isTrue(!StringUtils.isEmpty(fondCode), "参数不能为空！");
        return selectOne(SqlQuery.from(Fond.class).equal(FondInfo.CODE, fondCode));
    }

    protected void bindFondOrganise(Fond entity) {
        if (StringUtils.hasText(entity.getPartyId())) {
            for (String organiseId : entity.getPartyId().split(",")) {
                FondOrganise fondOrganise = new FondOrganise();
                fondOrganise.setFondId(entity.getId());
                fondOrganise.setFondCode(entity.getCode());
                fondOrganise.setOrganiseId(organiseId);
                fondOrganiseService.insert(fondOrganise);
            }
        }
    }

    @Override
    protected String getCacheName() {
        return "archive.fond";
    }
    /**
     * 根据组织机构查询全宗信息
     *
     * @param partyId
     * @return
     */
    @Override
    public Fond findFondByPartyId(String partyId) {
        Assert.isTrue(!StringUtils.isEmpty(partyId), "参数不能为空！");
        return selectOne(SqlQuery.from(Fond.class).equal(FondInfo.PARTYID, partyId));
    }
    @Override
    public List<? extends PermissionResource> getResources(String s) {
        return selectList(SqlQuery.from(Fond.class));
    }

    @Override
    public String getType() {
        return RESOURCE_TYPE_FOND;
    }

    @Override
    public String getName() {
        return "全宗";
    }

}
