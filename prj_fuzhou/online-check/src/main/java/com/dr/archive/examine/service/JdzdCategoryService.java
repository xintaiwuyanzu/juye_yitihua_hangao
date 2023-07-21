package com.dr.archive.examine.service;

import com.dr.archive.examine.entity.JdzdCategory;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface JdzdCategoryService extends BaseService<JdzdCategory> {

    JdzdCategory getOneById(String id);

    List<JdzdCategory> getListCategoryByLargeCategory(String largeCategory);

    List<TreeNode> cateTree(String pId);
}
