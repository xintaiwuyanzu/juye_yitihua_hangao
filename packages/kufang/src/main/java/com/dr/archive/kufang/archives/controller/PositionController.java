package com.dr.archive.kufang.archives.controller;

import com.dr.archive.kufang.archives.entity.*;
import com.dr.archive.kufang.archives.service.LayerService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/api/position")
public class PositionController extends BaseServiceController<PositionService, Position> {
    @Autowired
    PositionService positionService;
    @Autowired
    CommonService commonService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    LayerService layerService;

    @Override
    protected SqlQuery<Position> buildPageQuery(HttpServletRequest httpServletRequest, Position position) {
        return SqlQuery.from(Position.class)
                .like(PositionInfo.CASENAME, position.getCaseName())
                .like(PositionInfo.BOOKCASENO, position.getBookCaseNo())
                .equal(PositionInfo.LOCID, position.getLocId())
                .equal(PositionInfo.AREAID, position.getAreaId())
                .orderBy(PositionInfo.ORDERBY);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, Position entity) {
        List<EntityFiles> list = commonMapper.selectByQuery(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.CASEID, entity.getId()));
        if(list.size() > 0){
            return ResultEntity.error("当前密集下有档案，暂不能删除");
        }
        layerService.deleteLayer(entity.getId());
        return super.delete(request, entity);
    }

    /**
     * 删除密集架
     *
     * @param positionId 书架id
     * @param isdelete   是物理删除吗 true->物理删除，false->逻辑删除
     */
    @RequestMapping("/delPosition")
    public ResultEntity deletePosition(@Current Person person, String positionId, boolean isdelete) {
        ResultEntity res = ResultEntity.success();
        Position position = service.selectById(positionId);
        positionService.deletePosition(positionId, person.getId(), isdelete);
        return res;
    }

    /**
     * 获取全部书架号
     *
     * @param locId
     * @return
     */
    @RequestMapping("/getBookCaseNum")
    public ResultEntity getBookCaseNum(String locId) {
        List<Position> num = commonService.selectList(SqlQuery.from(Position.class).equal(PositionInfo.LOCID, locId).orderBy(PositionInfo.BOOKCASENO));
        return ResultEntity.success(num);
    }

    @RequestMapping("/addPositionForBox")
    public ResultEntity addPositionForBox(@Current Person person, String libId, String locId, String bookCaseNo,
                                          String ABsurface, int layerNo, int layerNum, String cn084, boolean update) {
        ResultEntity res = ResultEntity.success();
        Assert.isTrue(StringUtils.hasText(bookCaseNo), "请输入密集架");
        Assert.isTrue(!ObjectUtils.isEmpty(layerNum), "请输入层数");
        //TODO AB面写死
        ABsurface = "1";
        Position position = commonService.selectOne(SqlQuery.from(Position.class, false)
                .column(PositionInfo.POSITIONSTATUS).equal(PositionInfo.LOCID, locId).equal(PositionInfo.BOOKCASENO, bookCaseNo));
        Assert.isTrue(!"0".equals(position.getPositionStatus()), "该密集架已停用！");
        positionService.addPositionForBox(libId, locId, bookCaseNo, ABsurface, layerNo, layerNum, cn084, update, person.getId());
        return res;
    }

    /**
     * 根据位置、书架号、AB面，获取列信息
     *
     * @param locId
     * @return
     */
    @RequestMapping("/getLines")
    public ResultEntity getLines(String locId, String bookCaseNo) {
        Position position = commonService.selectOne(SqlQuery.from(Position.class, false)
                .column(PositionInfo.COLUMNNO).equal(PositionInfo.LOCID, locId).equal(PositionInfo.BOOKCASENO, bookCaseNo));
        Assert.isTrue(position != null, "选择的密集架没有列信息！");
        List list = new ArrayList();
        if (position.getColumnNo() != null && !"".equals(position.getColumnNo())) {
            for (int i = 0; i < Integer.parseInt(position.getColumnNo()); i++) {
                Map map = new HashMap();
                map.put("id", i);
                map.put("name", i + 1);
                list.add(map);
            }
        }
        return ResultEntity.success(list);
    }

    /**
     * 获取书架层数信息
     *
     * @param caseId
     * @param column
     * @param locId
     * @return
     */
    @RequestMapping("/getLayers")
    public ResultEntity getLayers(String caseId, String column, String colounNo, String locId) {
        List<Layer> layer = commonService.selectList(SqlQuery.from(Layer.class)
//                .equal(LayerInfo.CASENO, caseId)
                .equal(LayerInfo.COLUMNNUM, column).equal(LayerInfo.LOCID, locId).equal(LayerInfo.COLUMNNO, colounNo).orderBy(LayerInfo.LAYER));
        return ResultEntity.success(layer);
    }

    /**
     * 获取该馆藏是最大的书架号
     *
     * @param locId
     * @return
     */
    @RequestMapping("/getMaxCaseNo")
    public ResultEntity getMaxCaseNo(String locId) {
        long count = commonService.countByQuery(SqlQuery.from(Position.class)
                .max(PositionInfo.BOOKCASENO).equal(PositionInfo.LOCID, locId));
        return ResultEntity.success(count);
    }

    /**
     * 书柜标签绑定
     *
     * @param layerId
     * @param tidNum
     * @param labelNum
     * @return
     */

    @RequestMapping("/bindLabel")
    public ResultEntity bindLabel(@Current Person person, String layerId, String tidNum, String labelNum, String type) {
        String newTid = positionService.bindLabel(person.getId(), layerId, tidNum, labelNum, type);
        String msg = "";
        if ("bind".equals(type)) {
            msg = "绑定一个密集柜的磁条";
        } else if ("change".equals(type)) {
            msg = "更换一个密集的磁条";
        }
        return ResultEntity.success(newTid);
    }

    /**
     * 根据位置ID和AB面，获得书柜信息
     *
     * @param positionId
     * @param ab
     * @return
     */
    @RequestMapping("/getLayerDetail")
    public ResultEntity getLayerDetail(String positionId, String ab) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(positionId)) return ResultEntity.error("该档案无位置信息");
        Position position = commonService.selectOne(SqlQuery.from(Position.class, false)
                .column(PositionInfo.COLUMNNO).column(PositionInfo.LAYER).equal(PositionInfo.ID, positionId));
        Assert.isTrue(position != null, "选择的密集架号不存在！");
        List<Layer> layerList = commonService.selectList(SqlQuery.from(Layer.class)
                        .equal(LayerInfo.CASEID, positionId).equal(LayerInfo.COLUMNNUM, ab))
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Assert.isTrue(layerList.size() > 0, "选择的密集架层数不存在！");
        List list = new ArrayList();
        for (Layer layer : layerList) {
            if (list.size() < Integer.parseInt(layer.getLayer())) {
                List listone = new ArrayList();
                list.add(listone);
            }
            Map layerMap = new HashMap();
            layerMap.put("id", layer.getLayer());
            layerMap.put("tid", layer.getTid());
            layerMap.put("name", layer.getLayer());
            layerMap.put("detail", layer.getColumnNo() + "列" + layer.getLayer() + "层");
//            layerMap.put("no", layer.getLayerNo());
            layerMap.put("layerId", layer.getId());
            layerMap.put("remarks", layer.getRemarks());
            ((List) list.get(Integer.parseInt(layer.getLayer()) - 1)).add(layerMap);
        }
        return ResultEntity.success(list);
    }

    /**
     * 根据书架id查询书架层数及每层信息
     *
     * @param positionId
     * @param ab
     * @return
     */
    @RequestMapping("/thisAB")
    public ResultEntity getThisAB(String positionId, String ab) {
        Map map = positionService.getThisAB(positionId, ab);
        return ResultEntity.success(map);
    }


    /**
     * 盘点1.查询该层架的图书
     *
     * @param layerNo
     * @return
     */
    @RequestMapping("/inventoryByLayerNo")
    public ResultListEntity inventoryByLayerNo(String layerNo) {
        List<Holding> list = positionService.inventoryByLayerNo(layerNo);
        return ResultListEntity.success(list);
    }

    /**
     * 倒架
     *
     * @param layerId
     * @param barCodes
     * @return
     */
    @RequestMapping("/changeShelf")
    public ResultListEntity changeLayer(@Current Person person, String layerId, String barCodes) {
        positionService.changeLayer(layerId, barCodes, person.getId(), person.getUserName());
        return ResultListEntity.success();
    }

    /**
     * 根据层tid查询层架
     *
     * @param tid
     * @return
     */
    @RequestMapping("/getLayerByTid")
    public ResultEntity getLayerByTid(String tid) {
        Layer layer = positionService.getLayerByTid(tid);
        return ResultEntity.success(layer);
    }

    /**
     * 根据layerNo（层架号）查询层架
     *
     * @param layerNo
     * @return
     */
    @RequestMapping("/getLayerByLayerNo")
    public ResultEntity getLayerByLayerNo(String layerNo) {
        Layer layer = positionService.getLayerByLayerNo(layerNo);
        return ResultEntity.success(layer);
    }

    /**
     * 根据id 查询层架
     *
     * @param layerNo
     * @return
     */
    @RequestMapping("/getLayerById")
    public ResultEntity getLayerById(String layerNo) {
        Layer layer = positionService.getLayerById(layerNo);
        return ResultEntity.success(layer);
    }

    /**
     * 上架
     *
     * @param layerNo  层架号
     * @param barCodes 图书barCode
     * @return
     */
    @RequestMapping("/putaway")
    public ResultEntity putaway(@Current Person person, String layerNo, String barCodes) {
        positionService.putaway(layerNo, barCodes, person.getId(), person.getUserName());
        return ResultEntity.success();
    }

    /**
     * 根据id查询位置详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/getPositionbyId")
    public ResultEntity getPositionbyId(String id) {
        Position position = commonService.findById(Position.class, id);
        return ResultEntity.success(position);
    }

    /**
     * 查询该书架已经放了多少本书
     *
     * @param id
     * @return
     */
    @RequestMapping("/getBookCountByPositionId")
    public ResultEntity getBookCountByPositionId(String id) {
        //Position position = commonService.findById(Position.class, id);
        long count = commonService.countByQuery(SqlQuery.from(EntityFiles.class)
                .equal(EntityFilesInfo.CASEID, id).isNull(EntityFilesInfo.PARENTID));
        return ResultEntity.success(count);
    }

}
