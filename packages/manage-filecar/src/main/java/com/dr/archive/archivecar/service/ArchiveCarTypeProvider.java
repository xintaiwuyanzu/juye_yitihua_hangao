package com.dr.archive.archivecar.service;

import com.dr.archive.archivecar.bo.ArchiveCarDetailTag;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.framework.common.form.core.model.FormData;

import java.util.Collections;
import java.util.List;

/**
 * 档案车类型抽象接口
 *
 * @author dr
 */
public interface ArchiveCarTypeProvider {
    /**
     * 获取档案车类型
     *
     * @return
     */
    String getType();

    /**
     * 获取档案车类型名称
     *
     * @return
     */
    String getName();


    /**
     * 该档案车对应的档案标记是否静态。
     * 如果标记是动态的，则前端可以动态添加新的标记
     *
     * @return
     */
    default boolean isTagStatic() {
        return true;
    }

    /**
     * 获取指定用户该类型档案车的标记类型
     *
     * @param personId
     * @return
     */
    default List<ArchiveCarDetailTag> getDetailTags(String personId) {
        return Collections.emptyList();
    }
    /**
     * 在添加指定类型的档案车档案详情数据的时候，回调相关的service
     * TODO 更新和删除的场景也需要联动
     *
     * @param carType
     * @param carDetail
     * @param data
     */
    default void onInsertCar(String carType, ArchiveCarDetail carDetail, FormData data) {
    }
}
