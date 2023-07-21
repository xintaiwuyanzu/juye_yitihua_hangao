package com.dr.archive.kufang.entityfiles.service;


import com.alibaba.fastjson.JSON;
import com.dr.archive.kufang.archives.entity.*;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.archive.kufang.entityfiles.bo.EntityBo;
import com.dr.archive.kufang.entityfiles.entity.*;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondInfo;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.CommonTools;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * 实体档案业务实现类
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@Service
public class EntityFilesServiceImpl extends DefaultBaseService<EntityFiles> implements EntityFilesService {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    PositionService positionService;
    @Autowired
    LocationKuFangService locationKuFangService;
    @Autowired
    ImpBatchService impBatchService;
    @Autowired
    ArchiveTypeService archiveTypeService;
    @Autowired
    ImpEntityDetailService impEntityDetailService;

    /**
     * 新增实体档案
     *
     * @param entityBo
     * @param personId
     * @param organiseId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEntityFilesAndProperty(EntityBo entityBo, String personId, String organiseId) {
        checkArchiveCode(entityBo.getArchiveCode());
        String uuid = CommonTools.getUUID();
        setPropertyValue(entityBo, uuid, "insert");
        EntityFiles entityFiles = new EntityFiles();
        entityFiles.setOrganiseId(organiseId);
        entityFiles.setId(uuid);
        insert(setEntityFiles(entityBo, entityFiles));
    }
    /**
     * 档号查重（实体档案库）
     *
     * @param archiveCode
     * @return
     */
    public void checkArchiveCode(String archiveCode) {
        List<EntityFiles> daEntities = commonMapper.selectByQuery(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, archiveCode));
        Assert.isTrue(daEntities.size() == 0, "已存在重复档号【" + archiveCode + "】，请核实后操作");
    }

    /**
     * 添加卷内文件
     */
    @Override
    public void addJNWJ(String title, String archiveCode, String ajdhId) {
        EntityFiles entityFiles = selectOne(ajdhId);
        EntityBo bo = new EntityBo();
        BeanUtils.copyProperties(entityFiles, bo);
        EntityFiles file1 = new EntityFiles();
        bo.setTitle(title);
        bo.setArchiveCode(archiveCode);
        file1.setIsJNWJ("1");
        file1.setAjdh(entityFiles.getArchiveCode());
        file1.setModelType("1");
        file1.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        insert(setEntityFiles(bo, file1));
    }

    /**
     * 修改卷内文件
     */
    @Override
    public void editJNWJ(String title, String archiveCode, String id) {
        EntityFiles entityFiles = selectOne(id);
        entityFiles.setTitle(title);
        entityFiles.setArchiveCode(archiveCode);
        entityFiles.setUpdateDate(System.currentTimeMillis());
        updateById(entityFiles);
    }


    /**
     * 通过entityBo，给propertyValue赋值
     *
     * @param entityBo
     * @param entityId
     */
    private void setPropertyValue(EntityBo entityBo, String entityId, String type) {
        String propertyData = entityBo.getPropertyData();
        List<HashMap> maps = JSON.parseArray(propertyData, HashMap.class);
        for (HashMap<String, String> map : maps) {
            PropertyValue value = new PropertyValue();
            value.setPropertyId(map.get("name"));
            value.setpValue(map.get("value"));
            value.setEntityId(entityId);
            if ("insert".equals(type)) {
                value.setId(UUIDUtils.getUUID());
                commonMapper.insertIgnoreNull(value);
            } else if ("update".equals(type)) {
                value.setId(map.get("id"));
                commonMapper.updateIgnoreNullById(value);
            }
        }
    }


    /**
     * 通过entityBo，给entityFiles赋值
     *
     * @param entityBo
     * @param entityFiles
     * @return
     */
    private EntityFiles setEntityFiles(EntityBo entityBo, EntityFiles entityFiles) {
        entityFiles.setArchiveCode(entityBo.getArchiveCode());
        entityFiles.setArchiveType(entityBo.getArchiveType());
        entityFiles.setTitle(entityBo.getTitle());
        entityFiles.setCaseId(entityBo.getCaseId());
        entityFiles.setFondId(entityBo.getFondId());
        entityFiles.setClassCode(entityBo.getClassCode());
        entityFiles.setLocId(entityBo.getLocId());
        entityFiles.setAreaId(entityBo.getAreaId());
        if (!StringUtils.isBlank(entityBo.getAreaId())) {
            LocationKuFangQu qu = commonMapper.selectOneByQuery(SqlQuery.from(LocationKuFangQu.class).equal(LocationKuFangQuInfo.ID, entityBo.getAreaId()));
            entityFiles.setAreaNo(qu.getAreaName());
        }
        entityFiles.setColumnNum(entityBo.getColumnNum());
        entityFiles.setColumnNo(entityBo.getColumnNo());
        entityFiles.setLayer(entityBo.getLayer());
        entityFiles.setArchiveBox(entityBo.getArchiveBox());

        Fond fond = commonMapper.selectOneByQuery(SqlQuery.from(Fond.class).equal(FondInfo.PARTYID, SecurityHolder.get().currentOrganise().getId()));
        entityFiles.setFondCode(fond.getCode());
        entityFiles.setFondId(fond.getId());

        if (!StringUtils.isBlank(entityBo.getCaseId())) {
            Position position = positionService.getPositionById(entityBo.getCaseId());
            entityFiles.setCaseNo(position.getCaseName());
            entityFiles.setStatus("1");
            //layer 表可以考虑删除
            Layer layer = positionService.getLayerByParam(entityBo.getLocId(), entityBo.getAreaId(), entityBo.getCaseId(), entityFiles.getColumnNo(), entityFiles.getLayer(), entityFiles.getColumnNum());
            Assert.notNull(layer, "您修改的位置不存在");
            entityFiles.setLayerId(layer.getId());
        } else {
            entityFiles.setStatus("0");
        }
        return entityFiles;
    }

    /**
     * 更新实体档案
     *
     * @param entityFiles
     * @param personId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityFiles changeEntityFiles(EntityFiles entityFiles, String personId) {

        if (!StringUtils.isBlank(entityFiles.getCaseId())) {
            Position position = positionService.getPositionById(entityFiles.getCaseId());
            entityFiles.setCaseNo(position.getCaseName());
//            entityFiles.setAreaNo(position.getAreaNo());
            Layer layer = positionService.getLayerByParam(position.getLocId(), entityFiles.getAreaId(), position.getId(), entityFiles.getColumnNo(), entityFiles.getLayer(), entityFiles.getColumnNum());
            entityFiles.setLayerId(layer.getId());
            entityFiles.setStatus("1");
        }
        entityFiles.setUpdateDate(System.currentTimeMillis());
        entityFiles.setUpdatePerson(personId);
        commonMapper.updateIgnoreNullById(entityFiles);
        return entityFiles;
    }

    @Override
    public void batchChange(String ids, String personId, String locId, String areaId,
                            String caseId, String columnNo, String columnNum, String layer) {
        // 库房id locId  层 caseNo  节 columnNo     层号 layer
        String arrayCode[] = ids.split(",");
        for (String id : arrayCode) {
            EntityFiles files = selectOne(id);
            bind(files, locId, areaId, caseId, columnNo, layer, columnNum);
            //查询卷内文件
            List<EntityFiles> list = selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AJDH, files.getArchiveCode()));
            if (list.size() > 0) {
                for (EntityFiles child : list) {
                    //修改卷内文件的位置信息
                    bind(child, files.getLocId(), files.getAreaId(), files.getCaseId(),
                            files.getColumnNo(), files.getLayer(), files.getColumnNum());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityFiles changeEntityFiles(EntityBo entityBo, String personId) {
        EntityFiles entityFiles = selectOne(entityBo.getId());
        setPropertyValue(entityBo, entityFiles.getId(), "update");
        updateById(setEntityFiles(entityBo, entityFiles));
        //查询卷内文件
        List<EntityFiles> list = selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AJDH, entityFiles.getArchiveCode()));
        if (list.size() > 0) {
            for (EntityFiles files : list) {
                if (!StringUtils.isEmpty(entityFiles.getLocId()) && !StringUtils.isEmpty(entityFiles.getAreaId())) {
                    //修改卷内文件的位置信息
                    bind(files, entityFiles.getLocId(), entityFiles.getAreaId(), entityFiles.getCaseId(),
                            entityFiles.getColumnNo(), entityFiles.getLayer(), entityFiles.getColumnNum());
                }
            }
        }
        return entityFiles;
    }

    /**
     * 删除
     *
     * @param id
     * @param personId       日志使用
     * @param deleteProperty 是否删除属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntityFiles(String id, String personId, boolean deleteProperty) {

        if (deleteProperty) {
            EntityFiles entityFiles = selectOne(id);

            List<PropertyValue> attributeValues = selectList(entityFiles.getId());

            if (attributeValues.size() > 0) {
                for (PropertyValue attributeValue : attributeValues) {
                    commonMapper.deleteById(PropertyValue.class, attributeValue.getId());
                }
            }
        }
        EntityFiles entityFiles = selectOne(id);
        //查询卷内文件
        List<EntityFiles> list = selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AJDH, entityFiles.getArchiveCode()));
        if (list.size() > 0) {
            for (EntityFiles files : list) {
                deleteById(files.getId());
            }
        }
        commonMapper.deleteById(EntityFiles.class, id);
        //
    }

    @Override
    public List<String> selectListBySqlquery(String title, String archiveCode, String archiveType, String classCode, String archiveBox, String id) {
        SqlQuery<EntityFiles> sqlQuery = SqlQuery.from(EntityFiles.class, false)
                .column(EntityFilesInfo.ID)
                .orderByDesc(EntityFilesInfo.CREATEDATE);
        if (!StringUtils.isEmpty(id)) {
            sqlQuery.like(EntityFilesInfo.ID, id);
        }
        if (!StringUtils.isEmpty(archiveCode)) {
            sqlQuery.like(EntityFilesInfo.ARCHIVECODE, archiveCode);
        }
        if (!StringUtils.isEmpty(title)) {
            sqlQuery.like(EntityFilesInfo.TITLE, title);
        }
        if (!StringUtils.isEmpty(archiveType)) {
            sqlQuery.like(EntityFilesInfo.ARCHIVETYPE, archiveType);
        }
        if (!StringUtils.isEmpty(archiveBox)) {
            sqlQuery.like(EntityFilesInfo.ARCHIVEBOX, archiveBox);
        }
        if (!StringUtils.isEmpty(classCode)) {
            sqlQuery.like(EntityFilesInfo.CLASSCODE, classCode);
        }
        sqlQuery.equal(EntityFilesInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId());
        return commonMapper.selectByQuery(sqlQuery.setReturnClass(String.class));
    }

    @Override
    public List<String> getTypes() {
        SqlQuery<Property> group = SqlQuery.from(Property.class, false)
                .column(PropertyInfo.ARCHIVETYPEID)
                .groupBy(PropertyInfo.ARCHIVETYPEID);
        return commonMapper.selectByQuery(group.setReturnClass(String.class));
    }

    /**
     * 绑定位置
     *
     * @param id
     * @param locId
     * @param caseId
     * @param cloumnNo
     * @param layer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindPosition(String id, String locId, String areaId, String caseId,
                             String cloumnNo, String layer, String columnNum, String personId, String personName) {
        EntityFiles entityFiles = selectOne(id);
        Assert.isTrue(entityFiles != null, "档案位置信息不存在");
        bind(entityFiles, locId, areaId, caseId, cloumnNo, layer, columnNum);
        //添加入库记录
        addHis(entityFiles, "1", personId, personName, "无");
    }

    /**
     * 只绑定位置信息
     */
    public void bind(EntityFiles entityFiles, String locId, String areaId, String caseId,
                     String cloumnNo, String layer, String columnNum) {
        entityFiles.setLocId(locId);
        entityFiles.setAreaId(areaId);
        if (!StringUtils.isBlank(areaId)) {
            LocationKuFangQu qu = commonMapper.selectOneByQuery(SqlQuery.from(LocationKuFangQu.class).equal(LocationKuFangQuInfo.ID, areaId));
            entityFiles.setAreaNo(qu.getAreaName());
        }
        entityFiles.setCaseId(caseId);
        entityFiles.setColumnNum(columnNum);
        entityFiles.setColumnNo(cloumnNo);
        entityFiles.setLayer(layer);
        entityFiles.setUpdateDate(System.currentTimeMillis());
        Layer layer1 = positionService.getLayerByParam(locId, areaId, caseId, cloumnNo, layer, columnNum);
        Position position = positionService.getPositionById(layer1.getCaseId());
        entityFiles.setLayerId(layer1.getId());
        entityFiles.setCaseNo(position.getCaseName());
//        entityFiles.setAreaNo(position.getAreaNo());
        entityFiles.setStatus("1");
        commonMapper.updateIgnoreNullById(entityFiles);

    }

    /**
     * 绑定位置
     *
     * @param archiveId
     * @param locId
     * @param caseId
     * @param cloumnNo
     * @param layer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindPositionManual(String archiveId, String locId, String areaId, String caseId, String cloumnNo, String layer, String columnNum, String personId, String personName) {
        List<EntityFiles> entityFilesList = selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, archiveId));
        Assert.isTrue(entityFilesList.size() > 0, "档案位置信息不存在");
        EntityFiles entityFiles = entityFilesList.get(0);
        List<EntityFiles> list = selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AJDH, archiveId));
        if (list.size()>0){
            for (EntityFiles files : list) {
                extracted(locId, areaId, caseId, cloumnNo, layer, columnNum, files,personId,personName);
            }
        }
        extracted(locId, areaId, caseId, cloumnNo, layer, columnNum, entityFiles, personId, personName);

    }

    private void extracted(String locId, String areaId, String caseId, String cloumnNo, String layer, String columnNum, EntityFiles entityFiles, String personId, String personName) {
        entityFiles.setLocId(locId);
        entityFiles.setAreaId(areaId);
        if (!StringUtils.isBlank(areaId)) {
            LocationKuFangQu qu = commonMapper.selectOneByQuery(SqlQuery.from(LocationKuFangQu.class).equal(LocationKuFangQuInfo.ID, areaId));
            entityFiles.setAreaNo(qu.getAreaName());
        }
        entityFiles.setCaseId(caseId);
        entityFiles.setColumnNum(columnNum);
        entityFiles.setColumnNo(cloumnNo);
        entityFiles.setLayer(layer);
        entityFiles.setUpdateDate(System.currentTimeMillis());
        Layer layer1 = positionService.getLayerByParam(locId, entityFiles.getAreaId(), caseId, cloumnNo, layer, columnNum);
        Position position = positionService.getPositionById(layer1.getCaseId());
        entityFiles.setLayerId(layer1.getId());
        entityFiles.setCaseNo(position.getCaseName());
//        entityFiles.setAreaNo(position.getAreaNo());
        entityFiles.setStatus("1");
        commonMapper.updateIgnoreNullById(entityFiles);
        //添加入库记录
        addHis(entityFiles, "1", personId, personName, "无");
    }

    @Override
    public void updateHis(String id, String reason, String pId) {
        InAndOutRecords inAndOutRecords = commonMapper.selectById(InAndOutRecords.class, id);
        inAndOutRecords.setReason(reason);
        inAndOutRecords.setUpdateDate(System.currentTimeMillis());
        inAndOutRecords.setUpdatePerson(pId);
        commonMapper.updateIgnoreNullById(inAndOutRecords);
    }

    /**
     * 位置描述
     *
     * @param en
     * @return
     */
    @Override
    public String getPositionDes(EntityFiles en) {
        String locId = en.getLocId();
        LocationKuFang locationKuFang = locationKuFangService.getLocById(locId);
        if (locationKuFang != null) {
            return "位置:" + locationKuFang.getName()
                    + en.getAreaNo()
                    + en.getCaseNo()
                    + en.getColumnNo() + "节" +
                    en.getLayer() + "层";
        } else {
            return "位置:";
        }
    }

    @Override
    public void addBox(String ids, String parentId) {
        EntityFiles entityFiles = selectOne(parentId);
        String[] split = ids.split(",");
        for (String s : split) {
            EntityFiles entityFiles1 = selectOne(s);
            entityFiles1.setLocId(entityFiles.getLocId());
            entityFiles1.setCaseId(entityFiles.getCaseId());
            entityFiles1.setAreaNo(entityFiles.getAreaNo());
            //entityFiles1.setCaseId(entityFiles.getCaseNo());
            entityFiles1.setColumnNo(entityFiles.getColumnNo());
            entityFiles1.setColumnNum(entityFiles.getColumnNum());
            entityFiles1.setCaseNo(entityFiles.getCaseNo());
            entityFiles1.setLayer(entityFiles.getLayer());
            entityFiles1.setLayerId(entityFiles.getLayerId());
            entityFiles1.setParentId(parentId);
            updateById(entityFiles1);
           /* SqlQuery sqlQuery = SqlQuery.from(EntityFiles.class)
                    .set(EntityFilesInfo.LOCID, entityFiles.getLocId())
                    .set(EntityFilesInfo.CASEID, entityFiles.getCaseId())
                    .set(EntityFilesInfo.AREANO, entityFiles.getAreaNo())
                    .set(EntityFilesInfo.CASEID, entityFiles.getCaseId())
                    .set(EntityFilesInfo.COLUMNNO, entityFiles.getColumnNo())
                    .set(EntityFilesInfo.COLUMNNUM, entityFiles.getColumnNum())
                    .set(EntityFilesInfo.LAYER, entityFiles.getLayer())
                    .set(EntityFilesInfo.LAYERID, entityFiles.getLayerId())
                    .equal(EntityFilesInfo.ID,s);
            updateBySqlQuery(sqlQuery);*/
        }
    }


    @Override
    public String addHis(EntityFiles entityFiles, String doType, String personId, String personName, String reason) {
        LocationKuFang locationKuFang = locationKuFangService.getLocById(entityFiles.getLocId());
        InAndOutRecords records = new InAndOutRecords();
        records.setDoType(doType);
        records.setArchiveCode(entityFiles.getArchiveCode());
        records.setArchiveType(entityFiles.getArchiveType());
        records.setTitle(entityFiles.getTitle());
        records.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        records.setCreateDate(System.currentTimeMillis());
        records.setCreatePerson(personId);
        records.setRemarks(getPositionDes(entityFiles));
        records.setId(UUIDUtils.getUUID());
        records.setLocId(entityFiles.getLocId());
        records.setLocName(locationKuFang.getName());
        records.setAreaId(entityFiles.getAreaId());
        records.setAreaName(entityFiles.getAreaNo());
        records.setCaseNo(entityFiles.getCaseNo());
        records.setCaseId(entityFiles.getCaseId());
        records.setColumnNo(entityFiles.getColumnNo());
        records.setColumnNum(entityFiles.getColumnNum());
        records.setLayer(entityFiles.getLayer());
        records.setArchiveId(entityFiles.getId());
        records.setPersonName(personName);
        if (!StringUtils.isEmpty(reason)) {
            records.setReason(reason);
        } else {
            records.setReason("无");
        }
        commonMapper.insertIgnoreNull(records);
        return records.getId();
    }

    private EntityFiles selectOne(String id) {
        return commonMapper.selectById(EntityFiles.class, id);
    }

    private List<PropertyValue> selectList(String id) {
        return commonMapper.selectByQuery(SqlQuery.from(PropertyValue.class).equal(PropertyValueInfo.ENTITYID, id));
    }

    @Override
    public void clearPosition(String caseId) {
        SqlQuery sqlQuery = SqlQuery.from(EntityFiles.class)
                .equal(EntityFilesInfo.CASEID, caseId)
                .set(EntityFilesInfo.CASEID, (Serializable) null)
                .set(EntityFilesInfo.CASENO, (Serializable) null)
                .set(EntityFilesInfo.AREANO, (Serializable) null)
                .set(EntityFilesInfo.COLUMNNO, (Serializable) null)
                .set(EntityFilesInfo.COLUMNNUM, (Serializable) null)
                .set(EntityFilesInfo.LAYER, (Serializable) null)
                .set(EntityFilesInfo.LOCID, (Serializable) null)
                .set(EntityFilesInfo.LAYERID, (Serializable) null);
        commonMapper.updateByQuery(sqlQuery);
    }

    @Override
    public void downloadExp(HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("实体档案模板");
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeightInPoints(30);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("微软雅黑");
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER); // 居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//居中
        HSSFCell cell;
        HSSFRow row = sheet.createRow(0);
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("archiveName");
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("archiveCode");
        ByteArrayOutputStream outBtye = new ByteArrayOutputStream();
        try {
            wb.write(outBtye);
            byte[] byteArray = outBtye.toByteArray();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("实体档案模板.xls", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入正式库时，并添加到实体库
     * modelType:表单类型 1：案卷  0：文件  4：卷内文件
     */
    @Override
    public void addEntityByRuKu(String archiveType, FormData formData, String modelType, String ajdh) {
//        checkArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        EntityFiles entityFiles = selectOne(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.DATAID, formData.getId()));
        if (entityFiles == null) {
            entityFiles = new EntityFiles();
            entityFiles.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            entityFiles.setClassCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE));
            entityFiles.setDataId(formData.getId());
            String fondCode = formData.get(ArchiveEntity.COLUMN_FOND_CODE);
            entityFiles.setFondCode(fondCode);
            Fond fond = commonMapper.selectOneByQuery(SqlQuery.from(Fond.class).equal(FondInfo.CODE, fondCode));
            if (fond != null) {
                entityFiles.setFondId(fond.getId());
            }
            entityFiles.setModelType(modelType);
            ArchiveType type = archiveTypeService.selectById(archiveType);
            ArchiveType ch = null;
            if ("1".equals(modelType)) {//案卷
                ch = archiveTypeService.selectOne(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, type.getId())
                        .equal(ArchiveTypeInfo.ATYPE, ArchiveType.TYPE_案卷));
                entityFiles.setArchiveType(ch.getId());
            } else if ("0".equals(modelType)) {//文件
                ch = archiveTypeService.selectOne(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, type.getId())
                        .equal(ArchiveTypeInfo.ATYPE, ArchiveType.TYPE_文件));
                entityFiles.setArchiveType(ch.getId());
            } else if ("4".equals(modelType)) {//卷内文件
                ArchiveType ajType = archiveTypeService.getChildType(type.getId(), "aj");
                ch = archiveTypeService.selectOne(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, ajType.getId())
                        .equal(ArchiveTypeInfo.ATYPE, ArchiveType.TYPE_卷内文件));
                entityFiles.setArchiveType(ajType.getId());
            }

            entityFiles.setAjdh(ajdh);
            if (!StringUtils.isEmpty(ajdh)) {
                entityFiles.setIsJNWJ("1");
            }
            entityFiles.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
            entityFiles.setOrganiseId(SecurityHolder.get().currentOrganise().getId());

            insert(entityFiles);
            //添加导入记录详情
            //impEntityDetailService.addDetail(entityFiles, impId, ch.getaType());
        }

    }

    /**
     * 位置信息中的档案移除
     */
    @Override
    public void removeEntity(String id) {
        EntityFiles entityFiles = selectById(id);
        if (entityFiles != null) {
            entityFiles.setCaseId("");
            entityFiles.setCaseNo("");
            entityFiles.setColumnNum("");
            entityFiles.setColumnNo("");
            entityFiles.setLayer("");
            entityFiles.setLayerId("");
            entityFiles.setLocId("");
            entityFiles.setAreaId("");
            entityFiles.setAreaNo("");
            entityFiles.setArchiveBox("");
            entityFiles.setStatus("");
            updateById(entityFiles);
        }
    }

    /**
     * 导入卷内文件时，同步位置信息
     */
    @Override
    public void updatePos(String ajdh, EntityFiles child) {
        EntityFiles entityFiles = selectOne(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, ajdh));
        Assert.isTrue(entityFiles != null, "档案位置信息不存在");
        if (!StringUtils.isEmpty(entityFiles.getLocId()) && !StringUtils.isEmpty(entityFiles.getAreaId())) {
            bind(child, entityFiles.getLocId(), entityFiles.getAreaId(), entityFiles.getCaseId(),
                    entityFiles.getColumnNo(), entityFiles.getLayer(), entityFiles.getColumnNum());
        }

    }

}
