package com.dr.archive.kufang.entityfiles.service;

import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.entity.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 能够返回树结构的接口
 *
 * @param <T>
 * @author dr
 */
public interface BaseTreeNodeService<T extends IdEntity> {

    /**
     * 简单封装，根据业务外键查询树结构
     *
     * @param refId
     * @return
     */
    List<TreeNode> getTreeById(String refId);

    /**
     * 对象获取treeId的方法
     *
     * @return
     */
    default Function<T, String> idCallback() {
        return T::getId;
    }

    /**
     * 实体对象获取label的方法
     *
     * @return
     */
    Function<T, String> labelCallBack();

    default TreeNode toTree(T entity) {
        return toTree(entity, Collections.EMPTY_LIST);
    }

    default TreeNode toTree(T entity, List<TreeNode> childrens) {
        return toTree(entity, idCallback(), labelCallBack(), childrens);
    }


    default TreeNode toTree(T entity, Function<T, String> labelCallBack) {
        return toTree(entity, labelCallBack, Collections.EMPTY_LIST);
    }

    default TreeNode toTree(T entity, Function<T, String> labelCallBack, List<TreeNode> childrens) {
        return toTree(entity, T::getId, labelCallBack, childrens);
    }

    default TreeNode toTree(T entity, Function<T, String> idCallBack, Function<T, String> labelCallBack) {
        return toTree(entity, idCallBack, labelCallBack, Collections.EMPTY_LIST);
    }

    default TreeNode toTree(T entity, Function<T, String> idCallBack, Function<T, String> labelCallBack, List<TreeNode> childrens) {
        return new TreeNode(idCallBack.apply(entity), labelCallBack.apply(entity), entity, childrens);
    }
}
