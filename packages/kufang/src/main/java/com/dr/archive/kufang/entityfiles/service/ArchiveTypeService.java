package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.entityfiles.entity.ArchiveType;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.BaseService;

import java.util.ArrayList;
import java.util.List;

public interface ArchiveTypeService extends BaseService<ArchiveType>,BaseTreeNodeService<ArchiveType> {

    List<TreeNode> getTypeTree();

    ArrayList<TreeNode> getAllTypeTree(String pid, String fondId);

    void addType(String pid, String type);

    ArchiveType getParentType(String id);

    ArchiveType getChildType(String id, String aType);
}
