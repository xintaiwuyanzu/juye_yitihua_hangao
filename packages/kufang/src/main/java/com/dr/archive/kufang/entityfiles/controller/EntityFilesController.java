package com.dr.archive.kufang.entityfiles.controller;


import com.dr.archive.kufang.archives.entity.*;
import com.dr.archive.kufang.archives.service.LocationKuFangQuService;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.kufang.entityfiles.bo.EntityBo;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.archive.kufang.entityfiles.entity.ReasonDictionary;
import com.dr.archive.kufang.entityfiles.entity.ReasonDictionaryInfo;
import com.dr.archive.kufang.entityfiles.service.ArchiveTypeService;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.kufang.entityfiles.utils.CommonTools;
import com.dr.archive.kufang.entityfiles.utils.ExportWordUtil;
import com.dr.archive.kufang.entityfiles.utils.QRImageUtil;
import com.dr.archive.kufang.entityfiles.vo.ExportCountVo;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import com.dr.framework.util.ExportExcelUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体档案
 *
 * @author cuiyj
 * @Date 2021-08-12
 */
@RestController
@RequestMapping("api/entityFiles")
public class EntityFilesController extends BaseController<EntityFiles> {
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    LocationKuFangService locationKuFangService;
    @Autowired
    LocationKuFangQuService locationKuFangQuService;
    @Autowired
    ArchiveTypeService archiveTypeService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<EntityFiles> sqlQuery, EntityFiles entity) {
        sqlQuery.like(EntityFilesInfo.TITLE, entity.getTitle())
                .like(EntityFilesInfo.ARCHIVECODE, entity.getArchiveCode())
                .equal(EntityFilesInfo.ARCHIVETYPE, entity.getArchiveType())
                .equal(EntityFilesInfo.COLUMNNUM, entity.getColumnNum())
                .equal(EntityFilesInfo.COLUMNNO, entity.getColumnNo())
                .equal(EntityFilesInfo.CLASSCODE, entity.getClassCode())
                .equal(EntityFilesInfo.FONDID, entity.getFondId())
                .equal(EntityFilesInfo.LAYER, entity.getLayer())
                .equal(EntityFilesInfo.CASEID, entity.getCaseId())
                .equal(EntityFilesInfo.ARCHIVEBOX, entity.getArchiveBox())
                .equal(EntityFilesInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                .equal(EntityFilesInfo.BATCHID, entity.getBatchId())
                // .equal(EntityFilesInfo.PARENTID,entity.getParentId())
                .orderByDesc(EntityFilesInfo.UPDATEDATE);
        if (!StringUtils.isEmpty(entity.getParentId())) {
            sqlQuery.equal(EntityFilesInfo.PARENTID, entity.getParentId());
        } else {
            sqlQuery.isNull(EntityFilesInfo.PARENTID);
        }
        if(!StringUtils.isEmpty(entity.getAjdh()) && "all".equals(entity.getAjdh())){
            sqlQuery.isNotNull(EntityFilesInfo.ISJNWJ);
        }else if(!StringUtils.isEmpty(entity.getAjdh())){
            sqlQuery.equal(EntityFilesInfo.AJDH, entity.getAjdh());
        }else{
            sqlQuery.isNull(EntityFilesInfo.ISJNWJ);
        }
    }


    /**
     * 新增
     *
     * @param person
     * @param entityBo
     * @return
     */
    @PostMapping("/addBo")
    public ResultEntity add(@Current Person person, @Current Organise organise, EntityBo entityBo) {
        entityFilesService.addEntityFilesAndProperty(entityBo, person.getId(), organise.getId());
        return ResultEntity.success();
    }
    /**
     * 添加卷内文件
     */
    @RequestMapping("/addJNWJ")
    public ResultEntity addJNWJ(String title, String archiveCode, String ajdhId){
        entityFilesService.addJNWJ(title, archiveCode, ajdhId);
        return ResultEntity.success();
    }
    /**
     * 修改卷内文件
     */
    @RequestMapping("/editJNWJ")
    public ResultEntity editJNWJ(String title, String archiveCode, String id){
        entityFilesService.editJNWJ(title, archiveCode, id);
        return ResultEntity.success();
    }

    /**
     * 修改
     *
     * @param person
     * @param entityBo
     * @return
     */
   /* @PostMapping("/change")
    public ResultEntity change(@Current Person person, EntityFiles entityFiles) {
        EntityFiles files = entityFilesService.changeEntityFiles(entityFiles, person.getId());
        return ResultEntity.success(files);
    }*/
    @PostMapping("/change")
    public ResultEntity change(@Current Person person, EntityBo entityBo) {
        EntityFiles files = entityFilesService.changeEntityFiles(entityBo, person.getId());
        return ResultEntity.success(files);
    }

    /**
     * 批量修改
     */
    @PostMapping("/batchChange")
    public ResultEntity batchChange(@Current Person person, String ids, String locId, String areaId, String caseId,
                                    String columnNo, String columnNum, String layer) {
        entityFilesService.batchChange(ids, person.getId(),
                locId, areaId, caseId, columnNo, columnNum, layer);
        return ResultEntity.success();
    }

    /**
     * 删除
     *
     * @param person
     * @param id
     * @return
     */
    @PostMapping("/del")
    public ResultEntity del(@Current Person person, String id) {
        entityFilesService.deleteEntityFiles(id, person.getId(), true);
        return ResultEntity.success();
    }
    /**
     * 位置信息中的移除
     */
    @RequestMapping("/removeEntity")
    public ResultEntity removeEntity(String id){
        entityFilesService.removeEntity(id);
        return ResultEntity.success();
    }

    /**
     * 类型数组
     *
     * @return
     */
    @PostMapping("/getTypes")
    public ResultEntity getTypes() {
        List<String> properties = entityFilesService.getTypes();
        if (properties.size() > 0) {
            return ResultEntity.success(properties);
        } else {
            return ResultEntity.success();
        }
    }

    /**
     * 绑定位置（入库）
     *
     * @param id
     * @param locId
     * @param caseId
     * @param columnNo
     * @param layer
     * @return
     */
    @PostMapping("/bindPosition")
    public ResultEntity bindPosition(@Current Person person, String id, String locId, String areaId, String caseId,
                                     String columnNo, String layer, String columnNum) {
        entityFilesService.bindPosition(id, locId, areaId, caseId, columnNo, layer, columnNum, person.getId(), person.getUserName());
        return ResultEntity.success();
    }

    /**
     * 绑定位置（入库手动）
     *
     * @param archiveId
     * @param locId
     * @param caseId
     * @param columnNo
     * @param layer
     * @return
     */
    @PostMapping("/bindPositionManual")
    public ResultEntity bindPositionManual(@Current Person person, String archiveId, String locId,  String areaId, String caseId,
                                           String columnNo, String layer, String columnNum) {

        entityFilesService.bindPositionManual(archiveId, locId, areaId, caseId, columnNo, layer, columnNum, person.getId(), person.getUserName());
        return ResultEntity.success();
    }

    /**
     * 出库(手动)
     *
     * @param person
     * @param archiveCode
     * @return
     */
    @PostMapping("/unbindPositionManual")
    public ResultEntity unbindPositionManual(@Current Person person, String archiveCode, String reason) {
        try {
            EntityFiles entityFiles = commonService.selectOne(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, archiveCode));
            List<EntityFiles> files = commonService.selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AJDH, archiveCode));
            if (files.size()>0){
                for (EntityFiles file : files) {
                    extracted(person, reason, file);
                }
            }
            extracted(person, reason, entityFiles);
        } catch (Exception e) {
            return ResultEntity.error("出库错误");
        }
        return ResultEntity.success();
    }

    private void extracted(Person person, String reason, EntityFiles entityFiles) {
        entityFiles.setStatus("0");
        entityFiles.setUpdatePerson(person.getId());
        entityFiles.setUpdateDate(System.currentTimeMillis());
        commonService.update(entityFiles);
        entityFilesService.addHis(entityFiles, "0", person.getId(), person.getUserName(), reason);
    }

    /**
     * 出库
     *
     * @param person
     * @param id
     * @return
     */
    @PostMapping("/unbindPosition")
    public ResultEntity unbindPosition(@Current Person person, String id) {
        EntityFiles entityFiles = commonService.findById(EntityFiles.class, id);
        String uId = null;
        try {
            entityFiles.setStatus("0");
            entityFiles.setUpdatePerson(person.getId());
            entityFiles.setUpdateDate(System.currentTimeMillis());
            commonService.update(entityFiles);
            uId = entityFilesService.addHis(entityFiles, "0", person.getId(), person.getUserName(), "无");
        } catch (Exception e) {
            return ResultEntity.error("出库错误");
        }
        return ResultEntity.success(uId);
    }

    /**
     * 添加出库原因
     *
     * @return
     */
    @PostMapping("/addReason")
    public ResultEntity addReason(@Current Person person, String id, String reason) {

        try {
            entityFilesService.updateHis(id, reason, person.getId());
        } catch (Exception e) {
            return ResultEntity.error("未能添加出库原因");
        }
        return ResultEntity.success();
    }

    @PostMapping("/reasonDis")
    public ResultEntity reasonDis() {

        List<ReasonDictionary> reasonDictionaries = commonService.selectList(SqlQuery.from(ReasonDictionary.class, false).column(ReasonDictionaryInfo.REASONDICTIONARY));
        List<Map<String, String>> r = new ArrayList();
        reasonDictionaries.forEach(i -> {
            Map<String, String> map = new HashMap<>();
            map.put("value", i.getReasonDictionary());
            r.add(map);
        });
        return ResultEntity.success(r);
    }

    @PostMapping("/getEntityFiles")
    public ResultEntity getEntityFiles(String archiveCode) {
        EntityFiles entityFiles = commonService.selectOne(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.ARCHIVECODE, archiveCode));
        if (entityFiles != null) {
            return ResultEntity.success(entityFiles);
        } else {
            return ResultEntity.error("找不到指定记录！");
        }
    }

    /**
     * 导出二维码
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/erweima")
    public void erweima(HttpServletRequest request, HttpServletResponse response, String title, String
            archiveCode, String archiveType, String archiveBox, @RequestParam(defaultValue = "") String id) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            Map<String, Object> reuslts = new HashMap<>();
            List<ExportCountVo> images1 = new ArrayList<>();
            List<String> assetsList = entityFilesService.selectListBySqlquery(title, archiveCode, archiveType, "", archiveBox, id);
            List<List<String>> lists = CommonTools.fixedGrouping(assetsList, 4);
            for (List<String> list : lists) {
                ExportCountVo exportCount1 = new ExportCountVo();
                List<ExportCountVo> images = new ArrayList<>();
                ExportCountVo exportCount = new ExportCountVo();
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        EntityFiles en = getEntityFilesById(list.get(i));
                        exportCount.setName1(entityFilesService.getPositionDes(en));
                        exportCount.setCode1(en.getArchiveCode());
                        exportCount.setImage1(getImgBase64(list.get(i)).getData());
                    } else if (i == 1) {
                        EntityFiles en = getEntityFilesById(list.get(i));
                        exportCount.setName2(entityFilesService.getPositionDes(en));
                        exportCount.setCode2(en.getArchiveCode());
                        exportCount.setImage2(getImgBase64(list.get(i)).getData());
                    } else if (i == 2) {
                        EntityFiles en = getEntityFilesById(list.get(i));
                        exportCount.setName3(entityFilesService.getPositionDes(en));
                        exportCount.setCode3(en.getArchiveCode());
                        exportCount.setImage3(getImgBase64(list.get(i)).getData());
                    } else if (i == 3) {
                        EntityFiles en = getEntityFilesById(list.get(i));
                        exportCount.setName4(entityFilesService.getPositionDes(en));
                        exportCount.setCode4(en.getArchiveCode());
                        exportCount.setImage4(getImgBase64(list.get(i)).getData());
                    }
                }
                images.add(exportCount);
                exportCount1.setChildren(images);
                images1.add(exportCount1);
            }
            reuslts.put("assetsList", images1);
            if (reuslts == null) {
//                return ResultEntity.error("二维码导出失败！");
                new Throwable().printStackTrace();
            }
            ExportWordUtil word = new ExportWordUtil("/word", "erweima.ftl");
            word.setHeader(request, response, "实体档案二维码" + ".doc");
            word.doExport(out, reuslts);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return ResultEntity.success();
    }

    @PostMapping("/getImgBase64")
    public ResultEntity getImgBase64(String id) {
        Assert.isTrue(!StringUtils.isEmpty(id), "id不能为空！");
        try {
            String imgBase64 = makeQrcodeBase4(id);
            return ResultEntity.success(imgBase64);
        } catch (Exception e) {
            return ResultEntity.error("生成二维码失败:" + e.getMessage());
        }
    }

    @PostMapping("/getDes")
    public ResultEntity getDes(String id) {
        Assert.isTrue(!StringUtils.isEmpty(id), "id不能为空！");
        try {
            EntityFiles entityFiles = getEntityFilesById(id);
            if (!StringUtils.isEmpty(entityFiles.getLocId())&&!StringUtils.isEmpty(entityFiles.getAreaId())&&!StringUtils.isEmpty(entityFiles.getCaseId())) {
                LocationKuFang locationKuFang = locationKuFangService.getLocById(entityFiles.getLocId());
                LocationKuFangQu qu = locationKuFangQuService.selectById(entityFiles.getAreaId());
                String des = "位置:" + locationKuFang.getName() + qu.getAreaName()
                        + entityFiles.getCaseNo()
                        + (entityFiles.getColumnNum().equals("1") ? "A" : "B") + "面"
                        + entityFiles.getColumnNo() + "节" +
                        entityFiles.getLayer() + "层";

                return ResultEntity.success(des);
            } else {
                return ResultEntity.success("");
            }
        } catch (Exception e) {
            return ResultEntity.error("");
        }
    }

    public String makeQrcodeBase4(String id) {
        try {
            EntityFiles entityFiles = getEntityFilesById(id);
//            String locId = entityFiles.getLocId();
            String code2 = id + "#";
//            ArchiveType archiveType = archiveTypeService.selectById(entityFiles.getArchiveType());
//            LocationKuFang locationKuFang = locationKuFangService.getLocById(locId);
//            if (locationKuFang != null) {
//                code2 = entityFiles.getId() + "#"
//                        + "位置:" + locationKuFang.getName()
//                        + entityFiles.getAreaNo() + "区"
//                        + entityFiles.getCaseNo()
//                        + entityFiles.getColumnNo() + "节" +
//                        entityFiles.getLayer() + "层endshow";
//            } else {
//                code2 = entityFiles.getId() + "#" + "\r\n"
//                        + "档号:" + entityFiles.getArchiveCode() + ";"
//                        + "类型:" + archiveType.getArchiveTypeName() + ";"
//                        + "&位置:" + "&endshow";
//            }
            //生成一个二维码图片
            MultiFormatWriter multiFormatWrite = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            //
            BitMatrix bitMatrix = multiFormatWrite.encode(code2, BarcodeFormat.QR_CODE, 300, 300, hints);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            QRImageUtil.writeToStream(bitMatrix, "jpg", baos);
            return Base64Utils.encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private EntityFiles getEntityFilesById(String id) {
        return commonService.findById(EntityFiles.class, id);
    }

    /**
     * 查询该层已经放了多少档案
     *
     * @param id
     * @return
     */
    @RequestMapping("/getBookCountByPositionId")
    public ResultEntity getBookCountByPositionId(String id) {
        Position position = commonService.findById(Position.class, id);
        long count = commonService.countByQuery(SqlQuery.from(Holding.class)
                .join(HoldingInfo.BOOKCASENO, LayerInfo.ID)
//                .equal(LayerInfo.CASENO, position.getBookCaseNo())
                .equal(HoldingInfo.LOCATIONID, position.getLocId()));
        return ResultEntity.success(count);
    }

    /**
     * 入盒
     *
     * @param ids
     * @param parentId
     * @return
     */
    @RequestMapping("/addBox")
    public ResultEntity addBox(String ids, String parentId) {
        entityFilesService.addBox(ids, parentId);
      /*  Position position = commonService.findById(Position.class, id);
        long count = commonService.countByQuery(SqlQuery.from(Holding.class)
                .join(HoldingInfo.BOOKCASENO, LayerInfo.ID)
                .equal(LayerInfo.CASENO, position.getBookCaseNo())
                .equal(HoldingInfo.LOCATIONID, position.getLocId()));*/
        return ResultEntity.success();
    }

    @RequestMapping("/downloadEntityFilesExp")
    public void downloadEntityFilesExp(HttpServletResponse response) {
        entityFilesService.downloadExp(response);
    }

    /**
     * 实体档案模板导出
     * type:案卷/文件   卷内文件
     */
    @RequestMapping("/expEntity")
    public void expEntity(HttpServletResponse response, String type) {
        List list = new ArrayList();
        String[] columnNames;
        String[] columnCodes;
        if("jnwj".equals(type)){
            columnNames = new String[]{"档案名称", "档案编号", "案卷档号"};
            columnCodes = new String[]{"archiveName", "archiveCode", "ajdh"};
        }else{
            columnNames = new String[]{"档案名称", "档案编号"};
            columnCodes = new String[]{"archiveName", "archiveCode"};
        }
        String[] convertFields = {};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.exportExcel(response, "实体档案模板", columnNames, list, columnCodes, convertFields);
    }
}
