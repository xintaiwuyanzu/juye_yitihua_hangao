package com.dr.archive.managefondsdescriptivefile.service;

import com.dr.archive.managefondsdescriptivefile.entity.ManagementType;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.BaseService;

import java.util.List;


public interface ManagementTypeService extends BaseService<ManagementType> {
    /**
     * 查询所有全宗卷分类
     *
     * @return
     */
    List<ManagementType> findAll();

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    long deleteByIds(String ids);

    /**
     * 获取全宗卷大类树
     *
     * @return
     */
    List<TreeNode> getManagementTypeTree();

    /**
     * 获取全宗卷大类和分类树
     *
     * @return
     */
    List<TreeNode> getManagementTypeAndDossierTree();

    /**
     * 根据编码查询大类
     *
     * @param code
     * @return
     */
    ManagementType getManagementTypeByCode(String code);
}
