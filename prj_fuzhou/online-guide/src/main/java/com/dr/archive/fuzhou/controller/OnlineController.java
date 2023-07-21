package com.dr.archive.fuzhou.controller;


import com.alibaba.fastjson.JSONObject;
import com.dr.archive.fuzhou.im.service.OnlineService;
import com.dr.archive.fuzhou.im.service.WordProClient;
import com.dr.archive.service.impl.ArchiveOrganisePersonService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.service.OrganisePersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("api/online")
public class OnlineController {

    @Autowired
    WordProClient wordProClient;
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    ArchiveOrganisePersonService archiveOrganisePersonService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    OnlineService onlineService;
    @Value("${fuzhou.im.word_pro_url}")
    private String url;
    @Value("${fuzhou.im.word_pro_base}")
    private String base;

    /**
     * 房间号 -> 组成员信息
     */
    private static ConcurrentHashMap<String, List<Session>> groupMemberInfoMap = new ConcurrentHashMap<>();
    /**
     * 房间号 -> 在线人数
     */
    private static ConcurrentHashMap<String, Set<Integer>> onlineUserMap = new ConcurrentHashMap<>();

    /**
     * 收到消息调用的方法，群成员发送消息
     *
     * @param sid:房间号
     * @param userId：用户id
     * @param message：发送消息
     */
    @OnMessage
    public void onMessage(@PathParam("sid") String sid, @PathParam("userId") Integer userId, String message) {
        List<Session> sessionList = groupMemberInfoMap.get(sid);
        Set<Integer> onlineUserList = onlineUserMap.get(sid);
        // 先一个群组内的成员发送消息
        sessionList.forEach(item -> {
            try {
                // json字符串转对象
                //MsgVO msg = JSONObject.parseObject(message, MsgVO.class);
                //msg.setCount(onlineUserList.size());
                // json对象转字符串
                String text = JSONObject.toJSONString("");
                item.getBasicRemote().sendText(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 建立连接调用的方法，群成员加入
     *
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("userId") Integer userId) {
        List<Session> sessionList = groupMemberInfoMap.computeIfAbsent(sid, k -> new ArrayList<>());
        Set<Integer> onlineUserList = onlineUserMap.computeIfAbsent(sid, k -> new HashSet<>());
        onlineUserList.add(userId);
        sessionList.add(session);
        // 发送上线通知
        sendInfo(sid, userId, onlineUserList.size(), "上线了~");
    }


    public void sendInfo(String sid, Integer userId, Integer onlineSum, String info) {
        // 获取该连接用户信息
        // User currentUser = ApplicationContextUtil.getApplicationContext().getBean(UserMapper.class).selectById(userId);
        // 发送通知
       /* MsgVO msg = new MsgVO();
        msg.setCount(onlineSum);
        msg.setUserId(userId);
        msg.setAvatar(currentUser.getAvatar());
        msg.setMsg(currentUser.getNickName() + info);
        // json对象转字符串
        String text = JSONObject.toJSONString(msg);
        onMessage(sid, userId, text);*/
    }

    /**
     * 关闭连接调用的方法，群成员退出
     *
     * @param session
     * @param sid
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid, @PathParam("userId") Integer userId) {
        List<Session> sessionList = groupMemberInfoMap.get(sid);
        sessionList.remove(session);
        Set<Integer> onlineUserList = onlineUserMap.get(sid);
        onlineUserList.remove(userId);
        // 发送离线通知
        sendInfo(sid, userId, onlineUserList.size(), "下线了~");
    }

    /**
     * 传输消息错误调用的方法
     *
     * @param error
     */
    @OnError
    public void OnError(Throwable error) {
        //log.info("Connection error");
    }

    //即时通讯 获取token
    @RequestMapping("/getWorkProToken")
    public ResultEntity getWorkProToken() {
        return onlineService.getWorkProToken();
    }

    /**
     * 创建群组
     * @param
     * @param batchId
     * @return
     */
    @RequestMapping("/getWorkProGroup")
    public ResultEntity getWorkProGroup(String batchId) {
        return onlineService.getWorkProGroup(batchId);
    }

    /**
     * 漫游
     * @param groupId
     * @return
     */
    @RequestMapping("getWorkProGroupMsg")
    public ResultEntity getWorkProGroupMsg(String groupId, String batchId,String personId){
        return onlineService.getWorkProGroupMsg(groupId, batchId,personId);
    }

    @RequestMapping("getWordProGroupGet")
    public ResultEntity getWordProGroupGet(String groupId){
        return onlineService.getWordProGroupGet(groupId);
    }

    /**
     * 获取url地址
     * @param groupId
     * @return
     */
    @RequestMapping("/getWorkProOpenChat")
    public ResultEntity getWorkProOpenChat(String groupId){
        return onlineService.getWorkProOpenChat(groupId);
    }
    /**
     * 发送群组消息formview
     * @param groupId
     * @param text
     * @return
     */
    @RequestMapping("/getWorkProSend")
    public ResultEntity getWorkSend(String groupId, String text,String name,String title) {
        return onlineService.getWorkSend(groupId, text, name,title);
    }

    /**
     * 解散群
     * @param groupId
     * @return
     */
    @RequestMapping("/DeWordProGroup")
    public ResultEntity DeWordProGroup(String groupId){
       return onlineService.DeWordProGroup(groupId);
    }

}
