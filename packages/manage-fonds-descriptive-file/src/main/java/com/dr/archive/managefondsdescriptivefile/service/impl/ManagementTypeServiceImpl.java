package com.dr.archive.managefondsdescriptivefile.service.impl;

import com.dr.archive.managefondsdescriptivefile.entity.DossierType;
import com.dr.archive.managefondsdescriptivefile.entity.DossierTypeInfo;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementType;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementTypeInfo;
import com.dr.archive.managefondsdescriptivefile.service.DossierTypeService;
import com.dr.archive.managefondsdescriptivefile.service.ManagementTypeService;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagementTypeServiceImpl extends DefaultBaseService<ManagementType> implements ManagementTypeService {

    @Autowired
    DossierTypeService dossierTypeService;

    @Override
    public List<ManagementType> findAll() {
        SqlQuery<ManagementType> sqlQuery = SqlQuery.from(ManagementType.class).orderBy(ManagementTypeInfo.ORDERBY);
        List<ManagementType> managementTypes = selectList(sqlQuery);
        return managementTypes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long deleteByIds(String ids) {
        Assert.isTrue(StringUtils.hasText(ids), "未选择数据");
        long count = 0;
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            if (StringUtils.hasText(id)) {
                count += delete(SqlQuery.from(ManagementType.class).equal(ManagementTypeInfo.ID, id));
                long l = dossierTypeService.deleteByFondId(id);
            }
        }
        return count;
    }

    @Override
    public List<TreeNode> getManagementTypeTree() {
        List<TreeNode> managementTypeTree = new ArrayList<>();
        getManageMentTyps().forEach(managementType -> {
            TreeNode treeNode = new TreeNode(managementType.getId(), managementType.getName(), managementType.getCode(), managementType.getCode());
            managementTypeTree.add(treeNode);
        });
        return managementTypeTree;
    }

    @Override
    public List<TreeNode> getManagementTypeAndDossierTree() {
        List<TreeNode> managementTypeAndDossierTree = new ArrayList<>();
        getManageMentTyps().forEach(managementType -> {
            TreeNode treeNode = new TreeNode(managementType.getId(), managementType.getName(), managementType, setTreeNode(managementType.getId()));
            treeNode.setDescription(managementType.getCode());
            managementTypeAndDossierTree.add(treeNode);
        });
        return managementTypeAndDossierTree;
    }

    @Override
    public ManagementType getManagementTypeByCode(String code) {
        return commonMapper.selectOneByQuery(SqlQuery.from(ManagementType.class).equal(ManagementTypeInfo.CODE, code));
    }

    List<ManagementType> getManageMentTyps() {
        return selectList(SqlQuery.from(ManagementType.class).orderBy(ManagementTypeInfo.ORDERBY));
    }

    List<DossierType> getDossierTyps(String bussinessId) {
        return dossierTypeService.selectList(SqlQuery.from(DossierType.class).equal(DossierTypeInfo.BUSINESSID, bussinessId).orderBy(DossierTypeInfo.ORDERBY));
    }

    /**
     * 子分类TreeNode
     *
     * @return
     */
    List<TreeNode> setTreeNode(String managementTypeId) {
        List<TreeNode> dossierTree = new ArrayList<>();
        getDossierTyps(managementTypeId).forEach(dossierType -> {
            TreeNode treeNode = new TreeNode(dossierType.getId(), dossierType.getName(), dossierType.getCode(), dossierType.getCode());
            treeNode.setParentId(managementTypeId);
            dossierTree.add(treeNode);
        });
        return dossierTree;
    }
}
