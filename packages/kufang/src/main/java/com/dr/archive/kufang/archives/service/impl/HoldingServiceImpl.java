package com.dr.archive.kufang.archives.service.impl;

import com.dr.archive.kufang.archives.entity.Holding;
import com.dr.archive.kufang.archives.entity.HoldingInfo;
import com.dr.archive.kufang.archives.service.HoldingService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 图书增改业务类
 *
 * @author dr
 */
@Service
public class HoldingServiceImpl implements HoldingService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    PositionService positionService;

    /**
     * 查询当前密集架的档案
     *
     * @param bookCaseNo
     * @return
     */
//    @Override
//    public Page<Holding> getBooksBybookCaseNo(String bookCaseNo, String ABsurface, int layerNo, int layer, String libId, String locId) {
//        SqlQuery<Holding> sqlQuery = SqlQuery.from(Holding.class, false)
//                .column(HoldingInfo.CP005.alias("cp005"),
//                        HoldingInfo.CN084.alias("cn084"),
//                        HoldingInfo.CP006.alias("cp006"),
//                        HoldingInfo.LOCATIONID.alias("locationId"),
//                        LayerInfo.CASEID)
//                .join(HoldingInfo.BOOKCASENO, LayerInfo.ID)
//                .equal(HoldingInfo.LOCATIONID, locId)
//                .equal(LayerInfo.CASENO, bookCaseNo)
//                .equal(LayerInfo.COLUMNNUM, ABsurface)
//                .equal(LayerInfo.COLUMNNO, layerNo)
//                .equal(LayerInfo.LAYER, layer);
//        int count = (int) commonMapper.countByQuery(sqlQuery);
//        int end;
//        if (count > 0) {
//            end = count;
//        } else {
//            end = 15;
//        }
//        return commonMapper.selectPageByQuery(sqlQuery, 0, end);
//    }

    /**
     * 根据档号查询档案基本信息
     *
     * @param cn084
     * @return
     */
    @Override
    public Holding getHoldingbyCode(String cn084) {
        Assert.isTrue(!StringUtils.isEmpty(cn084), "档号不能为空！");
        return commonMapper.selectOneByQuery(SqlQuery.from(Holding.class)
                .equal(HoldingInfo.CN084, cn084)
        );
    }

    /**
     * 根据rfid查询档案基本信息
     * 1、根据rfid在本馆内查询档案基本信息
     *
     * @param tid tid数据
     * @return
     */
    @Override
    public Holding getHoldingbyRfid(String tid) {
        Assert.isTrue(!StringUtils.isEmpty(tid), "档案RFID不能为空！");
        //Assert.notNull(holdingvo,"未根据rfid查询到图书信息!");
        return commonMapper.selectOneByQuery(SqlQuery.from(Holding.class)
                .equal(HoldingInfo.TID, tid)
                .or().equal(HoldingInfo.TID, tid.toUpperCase())
                .or().equal(HoldingInfo.TID, tid.toLowerCase())
        );
    }
    /**
     * 磁条绑定
     *
     * @param bookId
     * @param labelId
     * @param personId
     */
//    @Override
//    @Transactional(rollbackFor = {Exception.class})
//    public String registerRfid(String bookId, String labelId, String personId) {
//        Assert.isTrue(!StringUtils.isEmpty(bookId), "档案id不能为空");
//        Assert.isTrue(!StringUtils.isEmpty(labelId), "标签号不能为空");
//        //检查本馆中此磁条是否存在
//        Holding holdingvo = getHoldingbyRfid(labelId);
//        Assert.isNull(holdingvo, "磁条已被注册！");
//        //不存在就查询图书信息
//        Holding holding = commonMapper.selectOneByQuery(SqlQuery.from(Holding.class)
//                .equal(HoldingInfo.CN084, bookId));
//        Assert.notNull(holding, "未找到档案");
//        Assert.isTrue(StringUtils.isEmpty(holding.getTid()), "此本档案当前已有磁条");
//        //根据条码号获取epc
//        String epc = bookIdToEpcOrder(holding.getCn084());
//        holding.setEpc(String.valueOf(epc));
//        holding.setTid(labelId);
//        holding.setUpdatePerson(personId);
//        holding.setUpdateDate(System.currentTimeMillis());
//        commonMapper.updateIgnoreNullById(holding);
//        return epc;
//    }

    /**
     * 根据档号号获取epc
     *
     * @param szBookID
     * @return
     */
    private String bookIdToEpcOrder(String szBookID) {
        String str = "";
        if (szBookID.length() > 0) {
            int strLen = szBookID.length();
            StringBuilder szBookIDBuilder = new StringBuilder(szBookID);
            while (strLen < 20) {
                szBookIDBuilder.append("A.xml");
                strLen = szBookIDBuilder.length();
            }
            szBookID = szBookIDBuilder.toString();
            str = szBookID + "0004";
        }
        return str;
    }

    @Override
    public void updateLibAndLoc(String barCode, String libId, String locId, String id, String userName) {
        commonMapper.updateIgnoreNullByQuery(SqlQuery.from(Holding.class)
                .set(HoldingInfo.UPDATEPERSON, id)
                .set(HoldingInfo.UPDATEDATE, System.currentTimeMillis())
                .set(HoldingInfo.LOCATIONID, locId)
                .equal(HoldingInfo.CN084, barCode));
    }

    /**
     * 更换RFID
     *
     * @param newTid
     * @param personId
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String replaceRFID(String bookId, String newTid, String personId) {
        Assert.isTrue(!StringUtils.isEmpty(newTid), "RFID不能为空！");
        Holding holdingVo = getHoldingbyRfid(newTid);
        Assert.isNull(holdingVo, "档号已存在！");
        Holding holding = commonMapper.selectOneByQuery(SqlQuery.from(Holding.class)
                .equal(HoldingInfo.CN084, bookId));
        Assert.notNull(holding, "未找到档案");
        Assert.isTrue(!StringUtils.isEmpty(holding.getTid()), "此本档案当前磁条为空！无需更换");
        String epc = bookIdToEpcOrder(holding.getCn084());
        holding.setEpc(String.valueOf(epc));
        holding.setTid(newTid);
        holding.setUpdatePerson(personId);
        holding.setUpdateDate(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(holding);
        return epc;
    }

    /**
     * 根据bookCaseNo查询档案数量
     *
     * @param bookCaseNo
     * @return
     */
    @Override
    public Long getBookCountByBookCaseNo(String bookCaseNo) {
        return commonMapper.countByQuery(SqlQuery.from(Holding.class).equal(HoldingInfo.BOOKCASENO, bookCaseNo));
    }

    /**
     * 根据图书位置(即layer 的 id)查询图书
     *
     * @param layerId
     * @return
     */
    @Override
    public List<Holding> getHoldingVoListByBookCaseNo(String layerId) {
        return commonMapper.selectByQuery(SqlQuery.from(Holding.class).equal(HoldingInfo.BOOKCASENO, layerId));
    }

    /**
     * 更新档案
     *
     * @param personId
     * @param personName
     * @param holdingNew
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateHolding(String personId, String personName, Holding holdingNew) {
        Assert.notNull(holdingNew, "档案基本信息不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(holdingNew.getCn084()), "档号不能为空");
        Holding holding = commonMapper.selectOneByQuery(SqlQuery.from(Holding.class)
                .equal(HoldingInfo.CN084, holdingNew.getCn084()));
        Assert.notNull(holding, "档案不能为空！");
        SqlQuery query = SqlQuery.from(Holding.class).equal(HoldingInfo.ID, holding.getId());

        if (!StringUtils.isEmpty(holdingNew.getLocationId())) {
            query.set(HoldingInfo.LOCATIONID, holdingNew.getLocationId());
        }
        if (!StringUtils.isEmpty(personId)) {
            query.set(HoldingInfo.UPDATEPERSON, personId);
        }
        if (!StringUtils.isEmpty(holdingNew.getBookCaseNo())) {
            query.set(HoldingInfo.BOOKCASENO, holdingNew.getBookCaseNo());
        }
        if (!StringUtils.isEmpty(holdingNew.getPositionInfo())) {
            query.set(HoldingInfo.POSITIONINFO, holdingNew.getPositionInfo());
        }
        if (!StringUtils.isEmpty(holdingNew.getHoldStatus())) {
            query.set(HoldingInfo.HOLDSTATUS, holdingNew.getHoldStatus());
        }
        query.set(HoldingInfo.UPDATEDATE, System.currentTimeMillis());
        commonMapper.updateIgnoreNullByQuery(query);
    }

    /**
     * 根据tid下架图书
     *
     * @param barCodes
     * @param personId
     * @param personName
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void underShelf(String barCodes, String personId, String personName) {
        Assert.isTrue(barCodes.length() > 0, "档案号不能为空");
        String[] split = barCodes.split(",");
        for (String s : split) {
            Holding vo = getHoldingbyCode(s);
            Assert.isTrue(Holding.STATUS_入藏.equals(vo.getHoldStatus()), vo.getCp005() + "不是入藏状态，不支持下架");
            commonMapper.updateIgnoreNullByQuery(SqlQuery.from(Holding.class)
                    .set(HoldingInfo.BOOKCASENO, "")
                    .set(HoldingInfo.POSITIONINFO, "")
                    .set(HoldingInfo.HOLDSTATUS, Holding.STATUS_待上架)
                    .set(HoldingInfo.UPDATEDATE, System.currentTimeMillis())
                    .set(HoldingInfo.UPDATEPERSON, personId)
                    .set(HoldingInfo.LOCATIONID, "")
                    .equal(HoldingInfo.CN084, s));
            /*Layer oldlayer = positionService.getLayerById(vo.getBookCaseNo());
            logService.checkCarHistory(personName, "下架", vo.getTitle(),
                    oldlayer.getLayerNo() + "->[]",
                    "-", "200", SqlQuery.getTableInfo(Holding.class).table(),
                    "Holding", "underShelf", Log.STATUS_下架);*/
        }
    }

    /**
     * 档案注销(只解除关系，不进行删除操作)
     *
     * @param cn084 条码号
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void bookLogout(String cn084) {
        Assert.isTrue(!StringUtils.isEmpty(cn084), "档案号不能为空！");
        Holding holding = getHoldingbyCode(cn084);
        Assert.notNull(holding, "档案为空！");
        holding.setUpdateDate(System.currentTimeMillis());
        holding.setUpdatePerson("admin");
        holding.setEpc("");
        holding.setLocationId("");
        holding.setBookCaseNo("");
        holding.setPositionInfo("");
        holding.setTid("");
        holding.setHoldStatus(Holding.STATUS_注销);
        commonMapper.updateIgnoreNullById(holding);
    }
}
