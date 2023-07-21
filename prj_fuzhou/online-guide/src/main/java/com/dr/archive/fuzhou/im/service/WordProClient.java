package com.dr.archive.fuzhou.im.service;

import com.dr.archive.fuzhou.im.bo.*;
import com.dr.archive.fuzhou.im.entity.*;
import com.dr.archive.fuzhou.im.service.impl.FeignClientImConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 在线交流接口
 *
 * @author dr
 * @date 2022-01-04 18:12   ${fuzhou.ocr.wordPro_url}
 */
@FeignClient(name = "word-pro-client", url = "${fuzhou.im.word_pro_url}", configuration = FeignClientImConfig.class)
public interface WordProClient {

    /**
     * 通用文字识别
     *
     * @param wordProBo
     * @return
     */
    @PostMapping(value = "api/token/create.html")
    WordProResultEntity wordPro(@RequestBody WordProBo wordProBo);

    /**
     * 创建群组
     *
     * @param wordProBoGroup
     * @return
     */
    @PostMapping(value = "api/groups/create.html")
    WordProGroupResultEntity wordProGroup(@RequestBody String wordProBoGroup);

    @PostMapping(value = "api/message/send_msg_formview.html")
    WordProBoSendEntity wordProSend(@RequestBody WordProBoSend wordProBoSend);

    @PostMapping(value = "api/message/group_msg_list.html")
    WordProBoGroupMsgEntity wordProGroupMsg(@RequestBody WordProBoGroupMsg wordProBoGroupMsg);

    @PostMapping(value = "api/message/send_msg_formview.html")
    WordProBoSendEntity wordProSendGroup(@RequestBody String wordProBoSendGroup);

    @PostMapping(value = "api/groups/get.html")
    WordProBoGroupGetEntity wordProGroupGet(@RequestBody WordProBoGroupGet wordProBoGroupGet);

    @PostMapping(value = "api/groups/delete.html")
    WordProDeGroupEntity wordProDeGroup(@RequestBody WordProDeGroup wordProDeGroup);

    @PostMapping(value = "api/chat/getnew.html")
    WordProBoOpenEntity wordProOpenChat(@RequestBody WordProBoOpenChat wordProOpenChat);
}
