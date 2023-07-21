package com.dr.archive.common.accessControl.service.impl;

import com.dr.archive.common.accessControl.entity.MemberAuthority;
import com.dr.archive.common.accessControl.entity.MemberAuthorityInfo;
import com.dr.archive.common.accessControl.service.MemberAuthorityService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.statistics.entity.ResourceStatistics;
import com.dr.archive.statistics.entity.ResourceStatisticsInfo;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:36
 */
@Service
public class MemberAuthorityServiceImpl extends DefaultBaseService<MemberAuthority> implements MemberAuthorityService {
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    FondService fondService;

    @Autowired
    ResourceManager resourceManager;

    @Autowired
    CategoryService categoryService;


    @Override
    public void setEntity(MemberAuthority entity) {
        if (!entity.getFondId().toString().isEmpty()){
            String [] getFondIds = entity.getFondId().split(",");
            String [] getFondCodes = new String[getFondIds.length];
            String [] getFondNames = new String[getFondIds.length];
            //根据字符串数组中对应的全宗id，查全宗id
            for (int i=0;i<getFondIds.length;i++){
                Fond fond = fondService.selectById(getFondIds[i]);
                getFondCodes[i] = fond.getCode();
                getFondNames[i] = fond.getName();
            }
            //把字符串数组转为字符串  fondId
            StringBuffer getFondIdStr = new StringBuffer();
            for(String s: getFondIds){
                getFondIdStr.append(s+",");
            }
            entity.setFondId(getFondIdStr.toString());
            //fondCode
            StringBuffer getFondCodeStr = new StringBuffer();
            for(String s: getFondCodes){
                getFondCodeStr.append(s+",");
            }
            entity.setFondCode(getFondCodeStr.toString());
            //fondName
            StringBuffer getFondNameStr = new StringBuffer();
            for(String s: getFondNames){
                getFondNameStr.append(s+",");
            }
            entity.setFondName(getFondNameStr.toString());
        }


//        //门类同理
//        String [] getCategoryIds = entity.getCategoryId().split(",");
//        String [] getCategoryCodes = new String[getCategoryIds.length];
//        String [] getCategoryNames = new String[getCategoryIds.length];
//        for (int i=0;i<getCategoryIds.length;i++){
//            Category category = categoryService.selectById(getCategoryIds[i]);
//            getCategoryCodes[i]=category.getCode();
//            getCategoryNames[i]=category.getName();
//        }
//
//        StringBuffer getCategoryIdStr = new StringBuffer();
//        for(String s : getCategoryIds){
//            getCategoryIdStr.append(s+",");
//        }
//        entity.setCategoryId(getCategoryIdStr.toString());
//
//        StringBuffer getCategoryNameStr = new StringBuffer();
//        for(String s : getCategoryNames){
//            getCategoryNameStr.append(s+",")
//        ;}
//        entity.setCategoryName(getCategoryNameStr.toString());
//
//        StringBuffer getCategoryCodeStr = new StringBuffer();
//        for(String s : getCategoryCodes){
//            getCategoryCodeStr.append(s+",")
//        ;}
//        entity.setCategoryCode(getCategoryCodeStr.toString());
//


        //设置权限code

        String[] fondPeronList = entity.getFondPeron().split(",");
        String[] fondPeronIdList = entity.getFondPeronId().split(",");
        List<MemberAuthority> list = new ArrayList<>();
        for (int i=0;i<fondPeronIdList.length;i++){
            entity.setId(UUIDUtils.getUUID());
            entity.setAccessCode(UUIDUtils.getUUID());
            entity.setFondPeron(fondPeronList[i]);
            entity.setFondPeronId(fondPeronIdList[i]);
            insert(entity);
        }

    }

}
