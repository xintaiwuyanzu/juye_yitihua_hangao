package com.dr.archive.kufang.archives.service.impl;

import com.dr.archive.kufang.archives.entity.*;
import com.dr.archive.kufang.archives.service.HoldingService;
import com.dr.archive.kufang.archives.service.LayerService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.archive.kufang.archives.vo.DataCountOne;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 书架业务实现类
 *
 * @author dr
 */
@Service
public class PositionServiceImpl extends DefaultBaseService<Position> implements PositionService {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    HoldingService holdingService;
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    LayerService layerService;


    /***
     * 获得密集架位置
     */
    @Override
    public Position getPosition(String bookCaseNo) {
        Assert.isTrue(!StringUtils.isEmpty(bookCaseNo), "密集架号不能为空！");
        return commonMapper.selectOneByQuery(SqlQuery.from(Position.class).equal(PositionInfo.BOOKCASENO, bookCaseNo));
    }

    @Override
    public Position getPositionById(String id) {
        return commonMapper.selectById(Position.class, id);
    }

    @Override
    public long insert(Position entity) {
        long count = 0;
        //判断密集架号是否重复
        Assert.isTrue(!commonMapper.existsByQuery(SqlQuery.from(Position.class)
                .equal(PositionInfo.LOCID, entity.getLocId())
                .equal(PositionInfo.AREAID,entity.getAreaId())
                .equal(PositionInfo.CASENAME, entity.getCaseName())), "密集架号已存在");
        //设置状态
        entity.setPositionStatus(StatusEntity.STATUS_ENABLE_STR);
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        String uuid = UUIDUtils.getUUID();
        entity.setId(uuid);
        count = super.insert(entity);
        //新增格子
        for (int i = 1; i <= Integer.parseInt(entity.getColumnNum()); i++) {
            for (int j = 1; j <= Integer.parseInt(entity.getColumnNo()); j++) {
                for (int k = 1; k <= Integer.parseInt(entity.getLayer()); k++) {

                    //新增格子
                    layerService.add(entity.getLocId(), entity.getAreaId(), uuid, String.valueOf(i),
                            String.valueOf(j), String.valueOf(k));

                }
            }
        }
        return count;
    }


    /**
     * 添加层信息
     *
     * @param position
     */
    private void addLayer(Position position) {
        LocationKuFang location = commonMapper.selectOneByQuery(SqlQuery.from(LocationKuFang.class).equal(LocationKuFangInfo.ID, position.getLocId()));
        Assert.notNull(location, "档案室为空！");
        Layer layer = new Layer();
        for (int i = 0; i < Integer.parseInt(position.getColumnNum()); i++) {
            for (int m = 0; m < Integer.parseInt(position.getColumnNo()); m++) {
                for (int j = 0; j < Integer.parseInt(position.getLayer()); j++) {
                    layer.setId(UUIDUtils.getUUID());
                    layer.setCaseId(position.getId());
//                    layer.setCaseNo(position.getBookCaseNo());
                    layer.setColumnNum((i + 1)+"");
                    layer.setColumnNo((m + 1)+"");
                    layer.setLayer((j + 1)+"");
                    //layer.setLayerNo(location.getId() + position.getBookCaseNo() + ("1".equals(String.valueOf(i + 1)) ? "A" : "B") + (m + 1) + (j + 1));
                    layer.setRemarks(position.getBookCaseNo() + "号架" + ("1".equals(String.valueOf(i + 1)) ? "A" : "B") + "面" + (m + 1) + "列" + (j + 1) + "层");
                    layer.setLocId(position.getLocId());
                    getCommonService().insert(layer);
                }
            }
        }
    }

    /**
     * 更细密集架
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateById(Position entity) {
        deleteLayerByPositionId(entity.getId());
        addLayer(entity);
        entityFilesService.clearPosition(entity.getId());
        return super.updateById(entity);
    }

    /**
     * 根据密集架id删除层信息
     *
     * @param positionId
     */
    private void deleteLayerByPositionId(String positionId) {
        commonMapper.deleteByQuery(SqlQuery.from(Layer.class).equal(LayerInfo.CASEID, positionId));
    }

    /**
     * 删除密集架
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(String positionId, String personId, boolean isdelete) {
        if (isdelete) {
            //如果是物理删除，需要判断该密集架上是否有档案
            Position position = commonMapper.selectById(Position.class, positionId);
            SqlQuery<Layer> sqlQuery = SqlQuery.from(Layer.class)
//                    .equal(LayerInfo.CASENO, position.getBookCaseNo())
                    .equal(LayerInfo.LOCID, position.getLocId())
                    .equal(LayerInfo.CASEID, position.getId());
            List<Holding> holdList = commonMapper.selectByQuery(SqlQuery.from(Holding.class)
                    .in(
                            HoldingInfo.BOOKCASENO,
                            SqlQuery.from(Layer.class, false).column(LayerInfo.ID)
//                                    .equal(LayerInfo.CASENO, position.getBookCaseNo())
                                    .equal(LayerInfo.LOCID, position.getLocId()).equal(LayerInfo.CASEID, position.getId())
                    ));
            Assert.isTrue(holdList.size() == 0, "该密集架上档案，不能删除");
            commonMapper.deleteByQuery(sqlQuery);
            //物理删除
            commonMapper.deleteByQuery(SqlQuery.from(Position.class).equal(PositionInfo.ID, positionId));
        } else {
            //逻辑删除 改状态为“0”
            commonMapper.updateIgnoreNullByQuery(SqlQuery.from(Position.class)
                    .set(PositionInfo.POSITIONSTATUS, "0")
                    .set(PositionInfo.UPDATEDATE, System.currentTimeMillis())
                    .set(PositionInfo.LASTUPDATE, System.currentTimeMillis())
                    .set(PositionInfo.UPDATEPERSON, personId)
                    .equal(PositionInfo.ID, positionId));
        }

    }


    /**
     * 更换书架状态
     *
     * @param positionId 书架id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePositionStatus(String positionId, String personId) {
        Position position = commonMapper.selectById(Position.class, positionId);
        if ("1".equals(position.getPositionStatus())) {//停用
            position.setPositionStatus("0");
            //删除此书架对应馆藏信息的位置
            List<Layer> list = commonMapper.selectByQuery(SqlQuery.from(Layer.class)
//                    .equal(LayerInfo.CASENO, position.getBookCaseNo())
                    .equal(LayerInfo.LOCID, position.getLocId()));
            for (Layer layer : list) {
                List<Holding> holdList = commonMapper.selectByQuery(SqlQuery.from(Holding.class).equal(HoldingInfo.BOOKCASENO, layer.getId()));
                if (!holdList.isEmpty()) {
                    for (Holding holding : holdList) {
                        holding.setUpdateDate(System.currentTimeMillis());
                        holding.setUpdatePerson(personId);
                        holding.setBookCaseNo("");
                        holding.setPositionInfo("");
                        //状态直接改为待上架w
                        holding.setHoldStatus(Holding.STATUS_待上架);
                        commonMapper.updateIgnoreNullById(holding);
                    }
                }
            }
        } else {//启用
            position.setPositionStatus("1");
        }
        position.setUpdateDate(System.currentTimeMillis());
        position.setUpdatePerson(personId);
        position.setLastUpdate(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(position);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPositionForBox(String libId, String locId, String bookCaseNo, String ABsurface, int layerNo,
                                  int layerNum, String cn084, boolean update, String personId) {

        ArchiveBox archiveBox = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveBox.class).equal(ArchiveBoxInfo.ID, cn084));
        Assert.notNull(archiveBox, "档案盒不存在");
        SqlQuery<Position> equal = SqlQuery.from(Position.class).equal(PositionInfo.BOOKCASENO, bookCaseNo);
        Assert.isTrue(commonMapper.existsByQuery(equal), "密集架不存在");
        Assert.isTrue(commonMapper.existsByQuery(equal.greaterThanEqual(PositionInfo.LAYER, layerNum)), "层数不存在");
        if (!update) {
            Assert.isTrue(archiveBox.getBookCaseNo() == null || "".equals(archiveBox.getBookCaseNo()), "该档案已经有位置了哦，请不要重复添加");
        }
        Layer layer = commonMapper.selectOneByQuery(SqlQuery.from(Layer.class)
//                .equal(LayerInfo.CASENO, bookCaseNo)
                .equal(LayerInfo.COLUMNNUM, ABsurface)
                .equal(LayerInfo.COLUMNNO, layerNo)
                .equal(LayerInfo.LAYER, layerNum)
                .equal(LayerInfo.LOCID, locId));
        Assert.notNull(layer, "密集架信息不存在");
        archiveBox = commonMapper.selectOneByQuery(SqlQuery.from(ArchiveBox.class).equal(ArchiveBoxInfo.ID, cn084));
        Assert.notNull(archiveBox, "当前档案室不存在这个档案");
        archiveBox.setPositionInfo(layer.getRemarks());
        commonMapper.updateIgnoreNullById(archiveBox);

    }

    /**
     * 书柜磁条绑定和更换
     *
     * @param tidNum
     * @param labelNum
     */
    @Override
    public String bindLabel(String personId, String layerId, String tidNum, String labelNum, String type) {
        Assert.isTrue(!StringUtils.isEmpty(labelNum), "读取的标签号不能为空");
        if ("bind".equals(type)) {
            Assert.isTrue(tidNum == null || "".equals(tidNum), "密集柜的标签已经存在");
        } else if ("change".equals(type)) {
            Assert.isTrue(tidNum != null && !"".equals(tidNum), "密集的标签不存在,请先绑定！");
        }
        List<Layer> layers = commonMapper.selectByQuery(SqlQuery.from(Layer.class).equal(LayerInfo.TID, labelNum));
        Assert.isTrue(layers.size() == 0, "当前磁条已被占用");
        commonMapper.updateIgnoreNullByQuery(SqlQuery.from(Layer.class)
                .set(LayerInfo.TID, labelNum).set(LayerInfo.UPDATEPERSON, personId).set(LayerInfo.UPDATEDATE, System.currentTimeMillis())
                .equal(LayerInfo.ID, layerId));
        return labelNum;
    }

    /**
     * 根据层架号查询层架
     *
     * @param layerNo
     * @return
     */
    @Override
    public Layer getLayerByLayerNo(String layerNo) {
        boolean b = commonMapper.existsByQuery(SqlQuery.from(Layer.class)
//                .equal(LayerInfo.LAYERNO, layerNo)
        );
        Assert.isTrue(b, "层架号不存在");
        return commonMapper.selectOneByQuery(SqlQuery.from(Layer.class)
//                .equal(LayerInfo.LAYERNO, layerNo)
        );
    }


    /**
     * 根据 书架号和AB面和书架层号 查询目前存书的数量信息
     *
     * @return
     */
    @Override
    public long getCountByLayer(String layerId) {
        Layer layer = commonMapper.selectById(Layer.class, layerId);
        return holdingService.getBookCountByBookCaseNo(layer.getId());
    }

    /**
     * 盘点之 查询该层架的图书列表
     *
     * @param layerNo
     * @return
     */
    @Override
    public List<Holding> inventoryByLayerNo(String layerNo) {
        Assert.isTrue(!StringUtils.isEmpty(layerNo), "层架号不能为空");
        Layer layer = getLayerByLayerNo(layerNo);
        Assert.notNull(layer, "层架不存在");
        return holdingService.getHoldingVoListByBookCaseNo(layer.getId());
    }

    /**
     * 倒架
     *
     * @param layerId
     * @param barCodes
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeLayer(String layerId, String barCodes, String personId, String personName) {
        Assert.isTrue(!StringUtils.isEmpty(layerId), "记录号不能为空");
        Assert.isTrue(!StringUtils.isEmpty(barCodes), "倒架的档案为0本");
        Layer layer = getLayerById(layerId);
        Assert.notNull(layer, "找不到对应的层架");
        String[] split = barCodes.split(",");
        for (String o : split) {
            Holding vo = holdingService.getHoldingbyCode(o);
            Assert.notNull(layer, "档案不存在");
            vo.setBookCaseNo(layer.getId());
            vo.setPositionInfo(layer.getRemarks());
            //查出旧书柜
            Layer oldlayer = getLayerById(vo.getBookCaseNo());
            holdingService.updateHolding(personId, personName, vo);
        }
    }

    /**
     * 根据id查询层架
     *
     * @param id
     * @return
     */
    @Override
    public Layer getLayerById(String id) {
        Assert.isTrue(!StringUtils.isEmpty(id), "记录号不能为空");
        return commonMapper.selectById(Layer.class, id);
    }

    /**
     * 根据tid查询层架
     *
     * @param tid
     * @return
     */
    @Override
    public Layer getLayerByTid(String tid) {
        Assert.isTrue(!StringUtils.isEmpty(tid), "找不到对应的层架");
        return commonMapper.selectOneByQuery(SqlQuery.from(Layer.class).equal(LayerInfo.TID, tid.toUpperCase())
                .or().equal(LayerInfo.TID, tid.toLowerCase()));
    }

    @Override
    public Layer getLayerByParam(String locId, String areaId, String caseId, String columnNo, String layer,String columnNum) {

        return commonMapper.selectOneByQuery(SqlQuery.from(Layer.class)
                .equal(LayerInfo.CASEID, caseId)
                .equal(LayerInfo.LAYER, layer)
                .equal(LayerInfo.COLUMNNUM,columnNum)
                .equal(LayerInfo.COLUMNNO, columnNo)
                .equal(LayerInfo.LOCID, locId)
                .equal(LayerInfo.AREAID, areaId)
        );
    }

    /**
     * 根据position id 查这一面
     *
     * @param positionId
     * @param ab
     * @return
     */
    @Override
    public Map getThisAB(String positionId, String ab) {

        List<DataCountOne> list = commonMapper.selectByQuery(SqlQuery.from(Layer.class, false)
                .column(LayerInfo.ID.alias("name"))
                .column(LayerInfo.COLUMNNO.alias("info_one"))
                .column(LayerInfo.LAYER.alias("info_two"))
//                .column(LayerInfo.LAYERNO.alias("info_three"))
                .equal(LayerInfo.CASEID, positionId)
                .equal(LayerInfo.COLUMNNUM, ab)
                .orderBy(LayerInfo.COLUMNNO)
                .orderBy(LayerInfo.LAYER)
                .setReturnClass(DataCountOne.class));
        for (DataCountOne dataCountOne : list) {
            dataCountOne.setCount(getCountByLayer(dataCountOne.getName()));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("layers", list);
        Position position = commonMapper.selectById(Position.class, positionId);
        map.put("position", position);
        return map;
    }


    /**
     * 批量上架
     *
     * @param layerNo
     * @param barCodes
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void putaway(String layerNo, String barCodes, String personId, String personName) {
        Assert.isTrue(!StringUtils.isEmpty(layerNo), "层架号不能为空");
        Assert.isTrue(!StringUtils.isEmpty(barCodes), "上架的档案为0本");
        Layer layer = getLayerByLayerNo(layerNo);
        Assert.notNull(layer, "层架不存在");
        String[] split = barCodes.split(",");
        for (String o : split) {
            Holding vo = holdingService.getHoldingbyCode(o);
            Assert.notNull(layer, "档案不存在");
            vo.setBookCaseNo(layer.getId());
            vo.setHoldStatus(Holding.STATUS_入藏);
            vo.setPositionInfo(layer.getRemarks());
            vo.setLocationId(layer.getLocId());
            holdingService.updateHolding(personId, personName, vo);
        }
    }
    /**
     * 删除密集架
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delPosition(String areaId) {
        List<Position> list = selectList(SqlQuery.from(Position.class).equal(PositionInfo.AREAID, areaId));
        for (Position position : list) {
            //删除层 列
            layerService.deleteLayer(position.getId());
            deleteById(position.getId());
        }
    }


}
