package com.dr.archive.kufang.entityfiles.controller;


import com.dr.archive.kufang.entityfiles.entity.ArchiveType;
import com.dr.archive.kufang.entityfiles.entity.ArchiveTypeInfo;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.archive.kufang.entityfiles.service.ArchiveTypeService;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 档案类别    包含关系：档案类别--》档案属性--》属性值
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@RestController
@RequestMapping("api/archiveType")
public class ArchiveTypeController extends BaseServiceController<ArchiveTypeService, ArchiveType> {

    @Autowired
    EntityFilesService entityFilesService;

    @Override
    protected SqlQuery<ArchiveType> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveType archiveType) {
        SqlQuery<ArchiveType> sqlQuery = SqlQuery.from(ArchiveType.class)
                .equal(ArchiveTypeInfo.FONDID,archiveType.getFondId())
                .equal(ArchiveTypeInfo.ARCHIVETYPENAME, archiveType.getArchiveTypeName())
                .equal(ArchiveTypeInfo.ORDERBY,archiveType.getOrder())
                .equal(ArchiveTypeInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId());
        if (StringUtils.hasText(archiveType.getaType())) {
            sqlQuery.equal(ArchiveTypeInfo.ATYPE, archiveType.getaType());
        }
        sqlQuery.orderBy(ArchiveTypeInfo.ORDERBY);
        return sqlQuery;
    }
    /**
     * 获得当前机构的档案类型树
     */
    @RequestMapping("/getTypeTree")
    public ResultEntity getTypeTree(){
        return ResultEntity.success(service.getTypeTree());
    }

    /**
     * 获得当前机构的所有档案类型树
     */
    @RequestMapping("/getAllTypeTree")
    public ResultEntity getAllTypeTree(String pid,String fondId) {
        return ResultEntity.success(service.getAllTypeTree(pid,fondId));
    }
    /**
     * 获得父分类
     */
    @RequestMapping("/getParentType")
    public ResultEntity getParentType(String id){
        return ResultEntity.success(service.getParentType(id));
    }

    @Override
    public ResultEntity<ArchiveType> insert(HttpServletRequest request, ArchiveType entity) {
        //添加实体档案类型，并同时添加案卷和文件类型
        String uuid = UUIDUtils.getUUID();
        entity.setId(uuid);
        entity.setPid("root");
        entity.setaType(ArchiveType.TYPE_根类别);
        super.insert(request, entity);
        service.addType(uuid, ArchiveType.TYPE_案卷);
        service.addType(uuid, ArchiveType.TYPE_文件);
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, ArchiveType entity) {
        List<EntityFiles> list = entityFilesService.selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVETYPE, entity.getId()));
        if(list.size() > 0){
            return ResultEntity.error("当前档案类型下有档案，暂不能删除！");
        }
        SqlQuery<ArchiveType> children = SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, entity.getId());
        service.delete(children);
        return super.delete(request, entity);
    }
}
