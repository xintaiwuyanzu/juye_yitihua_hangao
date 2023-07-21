package com.dr.archive.manage.fond.service.impl;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondOrganise;
import com.dr.archive.manage.fond.entity.FondOrganiseInfo;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author caor
 * @date 2021-09-03 9:41
 */
@Service
public class FondOrganiseServiceImpl extends DefaultBaseService<FondOrganise> implements FondOrganiseService {
    @Autowired
    FondService fondService;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(FondOrganise entity) {
//        Assert.isTrue(!StringUtils.isEmpty(entity.getOrganiseId()), "机构id不能为空！");
        long count = 0;
        if (!StringUtils.isEmpty(entity.getOrganiseId())) {
            String[] organiseIdS = entity.getOrganiseId().split(",");
            for (String organiseId : organiseIdS) {
                FondOrganise fondOrganise = new FondOrganise();
                fondOrganise.setOrganiseId(organiseId);
                fondOrganise.setFondId(entity.getFondId());
                fondOrganise.setFondCode(entity.getFondCode());
                if (entity.getOrganiseCode() == null) {
                    Organise organise = organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(organiseId).statusEqual("1").getQuery());
                    fondOrganise.setOrganiseCode(organise.getOrganiseCode());
                }
                count++;
                super.insert(fondOrganise);
            }
        }
        return count;
    }

    @Override
    public List<Fond> getFondListByOrganiseId(String organiseId) {
        SqlQuery<FondOrganise> sqlQuery = SqlQuery.from(FondOrganise.class).equal(FondOrganiseInfo.ORGANISEID, organiseId);
        List<FondOrganise> fondOrganiseList = selectList(sqlQuery);
        List<Fond> fondList = new ArrayList<>();
        fondOrganiseList.forEach(fondOrganise -> {
            //这里根据全宗id查询的，因此关联表中得存全宗id
            fondList.add(fondService.selectById(fondOrganise.getFondId()));
        });
        return fondList;
    }

    @Override
    public List<Fond> getFondListByOrganiseCode(String organiseCode) {
        SqlQuery<FondOrganise> sqlQuery = SqlQuery.from(FondOrganise.class).equal(FondOrganiseInfo.ORGANISECODE, organiseCode);
        List<FondOrganise> fondOrganiseList = selectList(sqlQuery);
        List<Fond> fondList = new ArrayList<>();
        fondOrganiseList.forEach(fondOrganise -> {
            //这里根据全宗id查询的，因此关联表中得存全宗id
            fondList.add(fondService.selectById(fondOrganise.getFondId()));
        });
        return fondList;
    }

    @Override
    public List<Organise> getOrganiseListByFondCode(String fondCode) {
        SqlQuery<FondOrganise> sqlQuery = SqlQuery.from(FondOrganise.class).equal(FondOrganiseInfo.FONDCODE, fondCode);
        List<FondOrganise> fondOrganiseList = selectList(sqlQuery);
        List<Organise> organiseList = new ArrayList<>();

        fondOrganiseList.forEach(fondOrganise -> {
            Assert.isTrue(null != fondOrganise.getOrganiseId(), "全宗机构表中不存在机构id");
            //这里根据组织机构id,查询组织机构列表的
            OrganiseQuery query = new OrganiseQuery.Builder().idEqual(fondOrganise.getOrganiseId()).statusEqual("1").getQuery();
            List<Organise> organises = organisePersonService.getOrganiseList(query);
            if (null != organises && !organises.isEmpty()) {
                organiseList.add(organises.get(0));
            }
        });
        return organiseList;
    }
}
