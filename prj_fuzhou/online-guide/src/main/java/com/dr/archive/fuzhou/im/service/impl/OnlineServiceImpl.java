package com.dr.archive.fuzhou.im.service.impl;

import camundajar.impl.com.google.gson.JsonArray;
import com.alibaba.fastjson.JSONObject;
import com.dr.archive.fuzhou.im.bo.*;
import com.dr.archive.fuzhou.im.entity.*;
import com.dr.archive.fuzhou.im.service.OnlineService;
import com.dr.archive.fuzhou.im.service.WordProClient;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetailInfo;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceRecord;
import com.dr.archive.service.impl.ArchiveOrganisePersonService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OnlineServiceImpl implements OnlineService{
    @Autowired
    WordProClient wordProClient;
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    ArchiveOrganisePersonService archiveOrganisePersonService;
    @Autowired
    CommonMapper commonMapper;
    @Value("${fuzhou.im.word_pro_url}")
    private String url;
    @Value("${fuzhou.im.word_pro_base}")
    private String base;


    //即时通讯 获取token
    @Override
    public ResultEntity getWorkProToken() {
        Person person = SecurityHolder.get().currentPerson();
        WordProBo wordProBo = new WordProBo();
        wordProBo.setUser_id(person.getId());
        wordProBo.setFlag("0");
        wordProBo.setPlatform("1");
        WordProResultEntity wordProResultEntity = wordProClient.wordPro(wordProBo);
        wordProResultEntity.getData().setBase_url(url);
        return ResultEntity.success(wordProResultEntity);
    }

    @Override
    public ResultEntity getWorkProGroup(String batchId) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organiseNow = SecurityHolder.get().currentOrganise();
        String parentId = organiseNow.getParentId();
        List<Person> personList = organisePersonService.getOrganiseDefaultPersons(parentId);
        JsonArray array = new JsonArray();

        List<String> list = new ArrayList<>();
        String[] s = new String[personList.size()];
        for (int i = 0; i < personList.size(); i++) {
            s[i] = personList.get(i).getId();
        }
//        personList.stream().map(Person::getUserCode).collect(Collectors.toList());
        for (Person person1 : personList) {
            list.add(person1.getId());
            array.add(person1.getId());
        }

        WordProBoGroup wordProBoGroup = new WordProBoGroup();
        wordProBoGroup.setName(person.getUserName() + "请求的的业务指导群");
        wordProBoGroup.setOwner(person.getId());

        wordProBoGroup.setMembers(s);

        WordProGroupResultEntity wordProResultEntity = wordProClient.wordProGroup(JSONObject.toJSONString(wordProBoGroup));
        wordProResultEntity.getData().setBase_url(url);
        BusinessGuidanceBatch businessGuidanceBatch = commonMapper.selectById(BusinessGuidanceBatch.class, batchId);

        businessGuidanceBatch.setGid(wordProResultEntity.getData().getGid());
        businessGuidanceBatch.setGidName(wordProResultEntity.getData().getName());
        commonMapper.updateIgnoreNullById(businessGuidanceBatch);
        return ResultEntity.success(wordProResultEntity);
    }

    @Override
    @Transactional
    public ResultEntity getWorkProGroupMsg(String groupId, String batchId,String personId) {
        WordProBoGroupMsg wordProBoGroupMsg = new WordProBoGroupMsg();
        wordProBoGroupMsg.setGroup_id(groupId);
        wordProBoGroupMsg.setPage(1);
        wordProBoGroupMsg.setPage_size(50);
        //WordProBoGroupMsgEntity wordProBoGroupMsgEntity = wordProClient.wordProGroupMsg(wordProBoGroupMsg);

        BusinessGuidanceBatchDetail businessGuidanceBatchDetail = commonMapper.selectOneByQuery(SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID, batchId));

/*        if(wordProBoGroupMsgEntity.getStatus() == 0){
            return ResultEntity.error("失败");
        }
        for (WordProBoGroupMsgEntity.Datas w: wordProBoGroupMsgEntity.getData().getList()){
            String text = w.getText();
            addBusinessGuidanceRecord(text,businessGuidanceBatchDetail.getFormDataId(),businessGuidanceBatchDetail.getId(),batchId,w.getSenddate());
        }*/
        int count =2;
/*        for (int i=50;wordProBoGroupMsgEntity.getData().getCount()>i;i+=50){
            wordProBoGroupMsg.setPage(count);
            WordProBoGroupMsgEntity wordProBoGroupMsgEntityT = wordProClient.wordProGroupMsg(wordProBoGroupMsg);
            for (WordProBoGroupMsgEntity.Datas w: wordProBoGroupMsgEntityT.getData().getList()){
                String text = w.getText();
                addBusinessGuidanceRecord(text,businessGuidanceBatchDetail.getFormDataId(),businessGuidanceBatchDetail.getId(),batchId,w.getSenddate());
            }
            count++;
        }*/
        BusinessGuidanceBatch businessGuidanceBatch = commonMapper.selectById(BusinessGuidanceBatch.class,batchId);
        businessGuidanceBatch.setStatus("2");
        businessGuidanceBatch.setUpdateDate(System.currentTimeMillis());
        businessGuidanceBatch.setUpdatePerson(personId);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("0");
        List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetails = commonMapper.selectByQuery(SqlQuery.from(BusinessGuidanceBatchDetail.class)
                .equal(BusinessGuidanceBatchDetailInfo.BATCHID, batchId)
                .equal(BusinessGuidanceBatchDetailInfo.CREATEPERSON, personId)
                .in(BusinessGuidanceBatchDetailInfo.STATUS, arrayList));
        businessGuidanceBatchDetails.forEach(i->{
            i.setStatus("2");
            commonMapper.updateIgnoreNullById(i);
        });
        commonMapper.updateIgnoreNullById(businessGuidanceBatch);
        return ResultEntity.success();
    }

    @Override
    public ResultEntity getWordProGroupGet(String groupId) {
        WordProBoGroupGet wordProBoGroupGet = new WordProBoGroupGet();
        wordProBoGroupGet.setGroup_id(groupId);
        WordProBoGroupGetEntity wordProBoGroupGetEntity = wordProClient.wordProGroupGet(wordProBoGroupGet);
        if("群组不存在".equals(wordProBoGroupGetEntity.getErrmsg())){
            return ResultEntity.success(0);
        }else {
            return ResultEntity.success(wordProBoGroupGetEntity);
        }
    }

    @Override
    public ResultEntity getWorkProOpenChat(String groupId) {
        //Person person = SecurityHolder.get().currentPerson();
        WordProBoOpenChat wordProBoOpenChat = new WordProBoOpenChat();

//        wordProBoOpenChat.setConversion_id(groupId);
//        wordProBoOpenChat.setConversion_type("1");
//        System.out.println(groupId);
        //WordProBoOpenEntity wordProBoOpenEntity = wordProClient.wordProOpenChat(wordProBoOpenChat);
        String urlBase = url + base;
        return ResultEntity.success(urlBase);
    }

    @Override
    public ResultEntity getWorkSend(String groupId, String text, String name,String title) {
        Person person = SecurityHolder.get().currentPerson();
        WordProBoSend wordProBoSendGroup = new WordProBoSend();
        List list = new ArrayList<>();
        WordProBoSend.Recver recver = new WordProBoSend.Recver();
        WordProBoSend.ExtData extData = new WordProBoSend.ExtData();
        recver.setId(groupId);
        recver.setName(name);
        list.add(recver);
        wordProBoSendGroup.setRecver(list);
        wordProBoSendGroup.setCtype("1");
        wordProBoSendGroup.setFlag("2");

        ArrayList headerList = new ArrayList();
        Map headerMap = new HashMap();
        headerMap.put("title",title);
        headerList.add(headerMap);

        Map footerMap = new HashMap();
        footerMap.put("title","查看");

        Map targetMap = new HashMap();
        targetMap.put("url_mobile",text);
        targetMap.put("url_pc",text);
        targetMap.put("opentype","2");
        extData.setHeader(headerList);
        extData.setFooter(footerMap);
        extData.setTarget(targetMap);
        wordProBoSendGroup.setExtdata(extData);

        WordProBoSend.Refer refer = new WordProBoSend.Refer();
        refer.setId(person.getId());
        refer.setName(person.getUserCode());
        wordProBoSendGroup.setRefer(refer);

        WordProBoSendEntity wordProBoSendEntity = wordProClient.wordProSendGroup(JSONObject.toJSONString(wordProBoSendGroup));
        return ResultEntity.success(wordProBoSendEntity);
    }

    @Override
    public ResultEntity DeWordProGroup(String groupId) {
        WordProDeGroup wordProDeGroup = new WordProDeGroup();
        wordProDeGroup.setGroup_id(groupId);

        WordProDeGroupEntity wordProDeGroupEntity = wordProClient.wordProDeGroup(wordProDeGroup);
        return ResultEntity.success(wordProDeGroupEntity);
    }



    void addBusinessGuidanceRecord(String text,String formDataId,String batchDetailId , String batchId, String sendTime) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        //添加指导记录表
        BusinessGuidanceRecord businessGuidanceRecord = new BusinessGuidanceRecord();
        businessGuidanceRecord.setId(UUIDUtils.getUUID());
        businessGuidanceRecord.setSendUserId(person.getId());
        businessGuidanceRecord.setSendUserName(person.getUserName());
        businessGuidanceRecord.setSendOrgId(organise.getId());
        businessGuidanceRecord.setSendOrgName(organise.getOrganiseName());

        sendTime = sendTime.substring(0,sendTime.length()-3);

        businessGuidanceRecord.setCreateDate(Long.valueOf(sendTime));
        businessGuidanceRecord.setUpdateDate(Long.valueOf(sendTime));
        businessGuidanceRecord.setCreateUserName(person.getUserName());
        businessGuidanceRecord.setCreateOrgId(organise.getId());
        businessGuidanceRecord.setCreateOrgName(organise.getOrganiseName());

        Assert.isTrue(StringUtils.hasText(organise.getParentId()), "当前登录不是档案室账号或未查询到该账号所属档案馆！");
        businessGuidanceRecord.setReceiveOrgId(organise.getParentId());
        businessGuidanceRecord.setReceiveOrgName(archiveOrganisePersonService.getOrganise(organise.getParentId()).getOrganiseName());
        businessGuidanceRecord.setBusinessId(formDataId);//目前只能跟档案id绑定，无法跟详情批次id绑定，因为从管理库、暂存库查看单条档案时无法带着批次或详情id
        businessGuidanceRecord.setMessage(text);
        businessGuidanceRecord.setDetailId(batchDetailId);
        businessGuidanceRecord.setBatchId(batchId);
        commonMapper.insert(businessGuidanceRecord);
    }
}
