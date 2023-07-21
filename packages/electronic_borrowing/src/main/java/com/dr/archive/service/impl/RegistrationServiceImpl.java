package com.dr.archive.service.impl;

import com.dr.archive.entity.BorrowingRegistration;
import com.dr.archive.entity.BorrowingRegistrationInfo;
import com.dr.archive.entity.BorrowingStrategy;
import com.dr.archive.service.RegistrationService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.vo.CountsVo;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lych
 * @date 2023/2/1 上午 11:52
 * @mood happy
 */
@Service
public class    RegistrationServiceImpl extends DefaultBaseService<BorrowingRegistration> implements RegistrationService {

    @Autowired
    CommonService commonService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    SysDictService sysDictService;

    /**
     * 添加申请，添加申请人，申请组织机构
     * @param entity
     * @return
     */
    @Override
    public long insert(BorrowingRegistration entity) {


        entity.setId(null);
        entity.setCreateDate(null);
        entity.setCreatePerson(null);
        entity.setUpdateDate(null);
        entity.setUpdatePerson(null);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式

        String newsNo = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
//        String random = String.valueOf((Math.random()*9000+1000));//随机生成一个四位整数
        entity.setBorrowingCode(newsNo);

        //需要当前登录人， 登录人组织机构，
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        if (organise!=null){
            entity.setOrganizeId(organise.getId());
            entity.setOrganizeName(organise.getOrganiseName());
        }
        entity.setApplicantId(person.getId());
        entity.setApplicant(person.getUserName());
        return commonService.insert(entity);
    }

    /**
     * 提交修改时的操作判断
     * @param entity
     * @return
     */

    /**
     * 借阅申请  { 状态：status }字典项
     * 0：暂存（申请人只有在0时修改申请条件，其他状态可以将编辑按钮去掉，为0时候可以考虑有删除按钮）
     * 1:  已提交
     * 2：办理中（借阅人员给申请人员查询档案时）
     * 3：办结（借阅人员查询完后，点击办结，办结后申请人可以查看档案）
     * 4：不通过（失败选项）
     */

    @Override
    public long updateById(BorrowingRegistration entity) {
        if (entity!=null){
            BorrowingRegistration borrowingRegistration = commonMapper.selectOneByQuery(SqlQuery.from(BorrowingRegistration.class).equal(BorrowingRegistrationInfo.ID, entity.getId()));
            //判断是否是办结操作,办结操作查询是否有策略配置，没有则不允许添加。
            //办结时添加办结时间
            if (StringUtils.hasText(entity.getStatus())&&"3".equals(entity.getStatus())){
                Assert.isTrue(StringUtils.hasText(entity.getStrategyId()),"请添加策略后再进行办结");
                entity.setFinishTime(System.currentTimeMillis());
                BorrowingStrategy borrowingStrategy = commonMapper.selectById(BorrowingStrategy.class, entity.getStrategyId());
                entity.setWatermarkState(borrowingStrategy.getWatermarkState());
                entity.setPrintState(borrowingStrategy.getPrintState());
                entity.setDownloadState(borrowingStrategy.getDownloadState());
            }
            //撤回功能
            if(entity.getStatus().equals("0")){
                //判断档案申请策略状态是否是 已提交，只有已提交时候才能撤回。
                Assert.isTrue(borrowingRegistration.getStatus().equals("1"),"当前申请正在被处理中，请刷新页面查看。");
            }
        }
        return commonService.update(entity);
    }

    /**
     * 借阅统计
     */
    @Override
    public List<CountsVo> getBorrowData(String startDate, String endDate) {
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery<BorrowingRegistration> sqlQuery = SqlQuery.from(BorrowingRegistration.class,false)
                .count(BorrowingRegistrationInfo.ID, "count")
                .column(BorrowingRegistrationInfo.STATUS.alias("name"));
        if("dag".equals(organise.getOrganiseType())){
            List<Organise> childrenOrganiseList = organisePersonService.getChildrenOrganiseList(organise.getId());
            List<Person> personList = new ArrayList<>();
            for (Organise organise1 : childrenOrganiseList) {
                List<Person> cList = organisePersonService.getOrganiseDefaultPersons(organise1.getId());
                personList.addAll(cList);
            }
            List<String> personIdList = personList.stream().map(Person::getId).collect(Collectors.toList());
            if (personIdList.size() > 0) {
                sqlQuery.in(BorrowingRegistrationInfo.CREATEPERSON, personIdList);
            }
        }else{
            sqlQuery.equal(BorrowingRegistrationInfo.ORGANIZEID, organise.getId());
        }
        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            sqlQuery.greaterThanEqual(BorrowingRegistrationInfo.CREATEDATE, DateTimeUtils.stringToMillis(startDate, "yyyy-MM-dd"))
                    .lessThan(BorrowingRegistrationInfo.CREATEDATE, DateTimeUtils.stringToMillis(endDate, "yyyy-MM-dd") + 24 * 3600 * 1000);
        }
        sqlQuery.groupBy(BorrowingRegistrationInfo.STATUS);
        List<CountsVo> vos = commonMapper.selectByQuery(sqlQuery.setReturnClass(CountsVo.class));
        //字典表数据
        Map<String, String> defaultKeyMap = sysDictService.dict("borrow.status").stream().collect(Collectors.toMap(TreeNode::getId, TreeNode::getLabel));
        vos.forEach(i -> {
            i.setName(defaultKeyMap.get(i.getName()));
        });
        return vos;
    }
}
