package com.dr.archive.manage.archiveMetadata.service;

import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.framework.common.service.BaseService;

public interface ArchiveMetadataRecordService extends BaseService<ArchiveMetadataRecord> {
    //手动添加
    String MANAGE_METADATA_TYPE_ADD = "ADD";
    //编辑
    String MANAGE_METADATA_TYPE_EDIT = "EDIT";
    //删除
    String MANAGE_METADATA_TYPE_DELETE = "MANAGE_METADATA_TYPE_DELETE";
    //入库
    String MANAGE_METADATA_TYPE_INGL = "INGL";
    //出库
    String MANAGE_METADATA_TYPE_OUTGL = "OUTGL";
    //在线归档接收
    String MANAGE_METADATA_TYPE_ONLINE_RECEIVE = "ONLINE_RECEIVE";
    //离线归档接收
    String MANAGE_METADATA_TYPE_OFFLINE_RECEIVE = "OFFLINE_RECEIVE";
    //调整
    String MANAGE_METADATA_TYPE_TIAOZHENG = "TIAOZHENG";
    //进入回收站
    String MANAGE_METADATA_TYPE_RECYCLE = "RECYCLE";
    //恢复
    String MANAGE_METADATA_TYPE_RECOVERY = "RECOVERY";
    //鉴定
    String MANAGE_METADATA_TYPE_JIANDING = "JIANDING";
    //到期鉴定
    String MANAGE_METADATA_TYPE_JIANDING_DAOQI = "JIANDING_DAOQI";
    //开放鉴定
    String MANAGE_METADATA_TYPE_JIANDING_KAIFANG = "JIANDING_KAIFANG";
    //销毁
    String MANAGE_METADATA_TYPE_XIAOHUI = "XIAOHUI";
    //在线移交
    String MANAGE_METADATA_TYPE_ONLINE_YIJIAO = "ONLINE_YIJIAO";
    //离线移交
    String MANAGE_METADATA_TYPE_OFFLINE_YIJIAO = "OFFLINE_YIJIAO";
    //批量修改
    String MANAGE_METADATA_TYPE_PILIANGXIUGAI = "PILIANGXIUGAI";

}