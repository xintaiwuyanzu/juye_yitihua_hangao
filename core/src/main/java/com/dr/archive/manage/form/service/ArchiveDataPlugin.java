package com.dr.archive.manage.form.service;

import com.dr.framework.common.form.core.model.FormData;
import org.springframework.core.Ordered;

/**
 * 拦截档案相关的表单数据操作
 *
 * @author dr
 */
public interface ArchiveDataPlugin extends Ordered {

    /**
     * 档案数据插入前拦截
     *
     * @param data
     * @param context
     * @return
     */
    default FormData beforeInsert(FormData data, ArchiveDataContext context) {
        return data;
    }

    /**
     * 档案数据插入后拦截
     *
     * @param data
     * @param context
     * @return
     */
    default FormData afterInsert(FormData data, ArchiveDataContext context) {
        return data;
    }

    /**
     * 档案数据更新前拦截
     *
     * @param data
     * @param context
     * @return
     */
    default FormData beforeUpdate(FormData data, ArchiveDataContext context) {
        return data;
    }

    /**
     * 档案数据更新后拦截
     *
     * @param data
     * @param context
     * @return
     */
    default FormData afterUpdate(FormData data, ArchiveDataContext context) {
        return data;
    }

    /**
     * 档案数据删除前拦截
     *
     * @param archiveIds
     * @param context
     * @return
     */
    default Long beforeDelete(String archiveIds, ArchiveDataContext context) {
        return 0L;
    }

    /**
     * 档案数据删除后拦截
     *
     * @param archiveIds
     * @param context
     * @return
     */
    default Long afterDelete(String archiveIds, ArchiveDataContext context) {
        return 0L;
    }

    /**
     * 能否处理指定类型的命令
     *
     * @param context
     * @return
     */
    default boolean acceptCommand(ArchiveDataContext context) {
        return true;
    }

    /**
     * 默认排序最低
     *
     * @return
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
