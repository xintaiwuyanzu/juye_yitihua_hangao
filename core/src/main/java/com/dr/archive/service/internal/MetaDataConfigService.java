package com.dr.archive.service.internal;

import com.dr.archive.model.query.MetadataTempleQuery;
import com.dr.archive.model.to.MetadataFormTo;
import com.dr.archive.model.to.MetadataTempleTo;
import com.dr.archive.model.to.MetadataTo;

import java.util.List;

/**
 * 查询元数据配置相关
 *
 * @author zhangb
 * @author dr
 */
public interface MetaDataConfigService {
    /**
     * 查询单个门类下，单个类型的所有模板
     *
     * @param metadataTempleQuery
     * @return
     */
    List<MetadataTempleTo> findMetaTempleList(MetadataTempleQuery metadataTempleQuery);

    /**
     * 根据元数据模板id查询元数据模板已经生成的表单列表
     *
     * @param tempId
     * @return
     */
    List<MetadataFormTo> findMetaTempFormList(String tempId);

    /**
     * 根据元数据Id查询元数据对象
     *
     * @param id
     * @return
     */
    MetadataTo findMetadataToById(String id);

    /**
     * 查询这个模板下的所有元数据
     *
     * @param templeId
     * @return
     */
    List<MetadataTo> findMetadataToByTempleId(String templeId);

}
