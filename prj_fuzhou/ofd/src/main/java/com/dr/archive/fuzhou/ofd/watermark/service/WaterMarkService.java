package com.dr.archive.fuzhou.ofd.watermark.service;


import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMark;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 水印管理
 *
 * @author cuiyj
 * @Date 2021-08-17
 */
public interface WaterMarkService extends BaseService<WaterMark> {

    /**
     * 水印模板渲染
     *
     * @param mobId
     */
    String showViewForMob(String mobId, String fileId, String keyWord, String tools);

    String showViewForMob(String mobId, String fileId, String keyWord, String tools, String fileUrl);

    default String showViewForMob(String mobId) {
        return showViewForMob(mobId, "/mb.ofd", "PDF", "all");
    }

    WaterMark getWaterMarkByMobId(String mobId);


    List<WaterMark> getWaterMarkByOrganiseId(String organiseId, String moren);

    /**
     * 默认机构水印
     *
     * @param organiseId
     * @return
     */
    default List<WaterMark> getWaterMarkByOrganiseId(String organiseId) {
        return getWaterMarkByOrganiseId(organiseId, "1");
    }

    /**
     * 修改水印状态
     *
     * @param waterMark
     * @return
     */
    WaterMark updateStatus(WaterMark waterMark);

    /**
     * 获取水印配置
     *
     * @param mobId
     */
    WaterMarkBo getWaterMark(String mobId);


    /**
     * 档案文件加水印
     *
     * @param refId
     * @param mobId
     * @param person
     * @param response
     * @throws Exception
     */
    void showViewForFile(String refId, String mobId, Person person, String organiseId, HttpServletResponse response) throws Exception;
}
