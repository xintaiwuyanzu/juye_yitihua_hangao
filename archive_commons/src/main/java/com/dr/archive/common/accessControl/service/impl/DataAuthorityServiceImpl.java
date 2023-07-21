package com.dr.archive.common.accessControl.service.impl;

import com.dr.archive.common.accessControl.entity.DataAuthority;
import com.dr.archive.common.accessControl.entity.DataAuthorityInfo;
import com.dr.archive.common.accessControl.service.DataAuthorityService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:36
 */
@Service
public class DataAuthorityServiceImpl extends DefaultBaseService<DataAuthority> implements DataAuthorityService {

    @Override
    public Map<String,List<DataAuthority>> getAllDataAuthority() {
        //获取权限对象
        List<DataAuthority> list = commonMapper.selectAll(DataAuthority.class);
        HashMap<String, List<DataAuthority>> map = new HashMap<>();
        //遍历对象写入map
        list.forEach(i->{
            //获取对象对应的权限名
            String accessLevel = i.getAccessLevel();
            //创建权限对应map的value数组
            List<DataAuthority> levels = map.get(accessLevel);
            //如果权限名已存在，加入对应value数组
            if(map.containsKey(accessLevel)){
                levels.add(i);
            }else {
                //如果权限不存在，创建权限对应map的value数组
                List<DataAuthority> objects = new ArrayList<>();
                objects.add(i);
                map.put(accessLevel,objects);
            }
        });
        return map;
    }

    @Override
    public boolean findDataLevelCode(String accessLevel,String levelCode) {
        SqlQuery<DataAuthority> levelCodeSql = SqlQuery.from(DataAuthority.class);
        levelCodeSql.equal(DataAuthorityInfo.LEVELCODE,levelCode)
                .equal(DataAuthorityInfo.ACCESSLEVEL,accessLevel);
        List<DataAuthority> list = commonMapper.selectByQuery(levelCodeSql);
        return list.size()>0;
    }


}
