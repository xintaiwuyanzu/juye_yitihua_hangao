package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.entityfiles.bo.EntityBo;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EntityFilesService extends BaseService<EntityFiles> {
    /**
     * 新增实体档案
     *
     * @param entityFiles
     * @return
     */
//    EntityFiles addEntityFiles(EntityFiles entityFiles, String personId);

    /**
     * 新增实体档案加属性
     *
     * @param entityBo
     */
    void addEntityFilesAndProperty(EntityBo entityBo, String personId, String organiseId);

    /**
     * 添加卷内文件
     */
    void addJNWJ(String title, String archiveCode, String ajdhId);

    /**
     * 修改卷内文件
     */
    void editJNWJ(String title, String archiveCode, String id);

    /**
     * 更新实体档案
     *
     * @param entityFiles
     * @return
     */
    EntityFiles changeEntityFiles(EntityFiles entityFiles, String personId);

    void batchChange(String ids, String personId, String locId, String areaId, String caseId, String columnNo, String columnNum, String layer);

    EntityFiles changeEntityFiles(EntityBo entityBo, String personId);

    /**
     * 删除实体档案
     *
     * @param id
     */
    default void deleteEntityFiles(String id, String personId) {
        deleteEntityFiles(id, personId, true);
    }

    /**
     * 删除实体档案
     *
     * @param id
     * @param deleteProperty 是否删除属性
     */
    void deleteEntityFiles(String id, String personId, boolean deleteProperty);

    List<String> selectListBySqlquery(String title, String archiveCode, String archiveType, String classCode, String archiveBox, String id);

    List<String> getTypes();

    void bindPosition(String id, String locId, String areaId, String caseId, String cloumnNo, String layer, String columnNum, String personId, String personName);

    void bindPositionManual(String archiveId, String locId, String areaId, String caseId, String cloumnNo, String layer, String columnNum, String personId, String personName);

    void updateHis(String id, String reason, String pId);

    String addHis(EntityFiles entityFiles, String doType, String personId, String personName, String reason);

    String getPositionDes(EntityFiles en);

    void addBox(String ids, String parentId);

    void clearPosition(String caseId);

    void downloadExp(HttpServletResponse response);

    /**
     * 进入正式库时，并添加到实体库
     * modelType:表单类型
     */
    void addEntityByRuKu(String archiveType, FormData formData, String modelType, String ajdh);

    /**
     * 位置信息中的档案移除
     */
    void removeEntity(String id);

    /**
     * 导入卷内文件时，同步位置信息
     */
    void updatePos(String ajdh, EntityFiles child);
}
