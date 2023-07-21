package com.dr.archive.fuzhou.im.service;

import com.dr.framework.common.entity.ResultEntity;

public interface OnlineService {

    //token地址
    ResultEntity getWorkProToken();

    //创建群组
    ResultEntity getWorkProGroup(String batchId);

    //获取群组消息
    ResultEntity getWorkProGroupMsg(String groupId, String batchId,String personId);

    //获取群组
    ResultEntity getWordProGroupGet(String groupId);

    //获取url地址，（前半段）
    ResultEntity getWorkProOpenChat(String groupId);

    //发送群组消息formview
    ResultEntity getWorkSend(String groupId, String text,String name,String title);

    //解散群
    ResultEntity DeWordProGroup(String groupId);


}
