package com.dr.archive.kufang.archives.service;


import com.dr.archive.kufang.archives.entity.Holding;

import java.util.List;

public interface HoldingService {

    /**
     * 查询当前密集架的档案
     *
     * @param bookCaseNo
     * @return
     */
//    Page<Holding> getBooksBybookCaseNo(String bookCaseNo, String ABsurface, int layerNo, int layer, String libId, String locId);

    /**
     * 根据档号查询档案基本信息
     *
     * @param cn084 条码号
     * @return
     */
    Holding getHoldingbyCode(String cn084);

    /**
     * 磁条绑定
     *
     * @param bookId
     * @param labelId
     * @param personId
     */
//    String registerRfid(String bookId, String labelId, String personId);

    /**
     * 根据rfid查询档案基本信息
     * 1、根据rfid在本馆内查询档案基本信息
     *
     * @param tid rfid tid数据
     * @return
     */
    Holding getHoldingbyRfid(String tid);

    void updateLibAndLoc(String barCode, String libId, String locId, String id, String userName);

    /**
     * 更换RFID
     *
     * @param newtid
     */
    String replaceRFID(String bookId, String newtid, String personId);

    Long getBookCountByBookCaseNo(String bookCaseNo);

    List<Holding> getHoldingVoListByBookCaseNo(String layerId);

    /**
     * 修改图书信息
     *
     * @param personId
     */
    void updateHolding(String personId, String personName, Holding holding);

    void underShelf(String barCodes, String personId, String personName);

    /**
     * 档案注销
     *
     * @param cn084 条码号
     */
    void bookLogout(String cn084);
}
