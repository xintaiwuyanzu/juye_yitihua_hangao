package com.dr.archive.examine.service.impl;

import com.dr.archive.examine.entity.PublicInformation;
import com.dr.archive.examine.service.PublicInformationService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.query.PersonQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PublicInformationServiceImpl extends DefaultBaseService<PublicInformation> implements PublicInformationService {


    @Autowired
    OrganisePersonService organisePersonService;

    /**
     * 新增消息
     *
     * @param sendId      发送方 人员id
     * @param toUnitId    接收方 机构id
     * @param infoContent 发送的信息
     * @param infoType    信息类型
     */
    @Override
    public void add(String refId, String sendId, String sendUnitId, String toId, String toUnitId, String infoContent, String infoType, String checkType) {

        PublicInformation information = new PublicInformation();
        information.setCheckType(checkType);
        information.setInfoContent(infoContent);
        information.setInfoType(infoType);

        information.setSendTime(System.currentTimeMillis());
        //发送方信息
        information.setSendId(sendId);
        Person person = organisePersonService.getPersonById(sendId);
        Organise organise = SecurityHolder.get().currentOrganise();
        information.setSendName(person.getUserName());
        information.setSendUnitId(organise.getId());
        information.setSendUnitName(organise.getOrganiseName());
        OrganiseQuery build = new OrganiseQuery.Builder().idEqual(toUnitId).build();
        Organise organise1 = organisePersonService.getOrganise(build);
        //        接收方信息
        //如果是发送消息   需要确认双方的机构负责人等    回复则不需要
        if ("".equals(toId)) {
            //TODO 这里的负责人 没找见在哪配置的 先写死了
            PersonQuery fuzeren = new PersonQuery.Builder().dutyLike("fuzeren").defaultOrganiseIdEqual(toUnitId).build();
            List<Person> personList = organisePersonService.getPersonList(fuzeren);
            if (personList.size() > 0) {
                //这里大概率是一个人  但是可能存在没有负责人  和  多个负责人的情况
                for (Person person1 : personList) {
                    information.setToId(person1.getId());
                    information.setToName(person1.getUserName());
                    information.setToUnitName(organise1.getOrganiseName());
                    information.setToUnitId(toUnitId);
                    information.setStatus(PublicInformation.STATUS_WEIDU);//表示未读  1 表示 已阅
                    information.setRefId(refId);//多加了一个业务id
                    insert(information);
                }
            }
        } else {

            Person person2 = organisePersonService.getPersonById(toId);
            information.setToId(toId);
            information.setToName(person2.getUserName());
            information.setToUnitName(organise1.getOrganiseName());
            information.setToUnitId(toUnitId);
            information.setStatus(PublicInformation.STATUS_WEIDU);//表示未读  1 表示 已阅
            information.setRefId(refId);//多加了一个业务id
            insert(information);


        }


    }

    /**
     * 调整反馈
     *
     * @param id    info  Id
     * @param reply 回复
     */
    @Override
    public void reply(String id, String reply, String checkType) {
        PublicInformation information = selectById(id);
        information.setStatus(PublicInformation.STATUS_YIDU);
        //不管状态改没改  都改为已读
        updateById(information);
        //这时新建一条消息
        add(information.getRefId(), information.getToId(), information.getToUnitId(), information.getSendId(), information.getSendUnitId(), reply, information.getInfoType(), checkType);

    }
}
