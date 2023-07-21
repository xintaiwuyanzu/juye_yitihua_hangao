package com.dr.archive.examine.service.impl;

import com.dr.archive.examine.entity.JdzdWeight;
import com.dr.archive.examine.entity.JdzdWeightInfo;
import com.dr.archive.examine.service.JdzdWeightService;
import com.dr.archive.examine.service.KeyWordService;
import com.dr.archive.kufang.entityfiles.utils.CommonTools;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class JdzdWeightServiceImpl extends DefaultBaseService<JdzdWeight> implements JdzdWeightService {
    @Autowired
    KeyWordService keyWordService;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public JdzdWeight getOneById(String id) {
        return commonMapper.selectById(JdzdWeight.class, id);
    }

    @Override
    public JdzdWeight getOneByOId(String oId) {
        return commonMapper.selectOneByQuery(SqlQuery.from(JdzdWeight.class).equal(JdzdWeightInfo.ORGANISEID, oId));
    }

    @Override
    public long insert(JdzdWeight entity) {
        Assert.isTrue(!StringUtils.isEmpty(entity.getOrganiseId()), "机构id不能为空");
        boolean isExit = commonMapper.countByQuery(SqlQuery.from(JdzdWeight.class).equal(JdzdWeightInfo.ORGANISEID, entity.getOrganiseId())) > 0;
        OrganiseQuery build = new OrganiseQuery.Builder().idEqual(entity.getOrganiseId()).build();
        Organise organise = organisePersonService.getOrganise(build);
        entity.setOrganiseCode(organise.getOrganiseCode());
        entity.setOrganiseName(organise.getOrganiseName());
        if (entity.getWeight() == null || entity.getWeight() == 0) {
            entity.setWeight(JdzdWeight.defaultWeight);
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("1");
        }
        Assert.isTrue(!isExit, "该机构已添加！");
        return super.insert(entity);
    }

    /**
     * 清空单位权重表
     */
    @Override
    public void clear() {
        List<JdzdWeight> jdzdWeights = commonMapper.selectAll(JdzdWeight.class);
        for (JdzdWeight jdzdWeight : jdzdWeights) {
            commonMapper.deleteById(JdzdWeight.class, jdzdWeight.getId());
        }
    }

    /**
     * 一键新增单位权重表
     */
    @Override
    public void addAuto() {
        List<Organise> organises = keyWordService.getOrganise();
        for (Organise organise : organises) {

            //加校验 存在就不加了
            JdzdWeight weight = getOneByOId(organise.getId());
            if (weight == null) {
                JdzdWeight jdzdWeight = new JdzdWeight();
                jdzdWeight.setOrganiseId(organise.getId());
                jdzdWeight.setOrganiseName(organise.getOrganiseName());
                jdzdWeight.setOrganiseCode(organise.getOrganiseCode());
                jdzdWeight.setWeight(JdzdWeight.defaultWeight);
                jdzdWeight.setStatus("1");
                jdzdWeight.setCreateDate(System.currentTimeMillis());
                jdzdWeight.setUpdateDate(System.currentTimeMillis());
                jdzdWeight.setId(CommonTools.getUUID());
                commonMapper.insertIgnoreNull(jdzdWeight);
            }


        }
    }

    @Override
    public List<String> random(Integer num) {
        SqlQuery<JdzdWeight> query = SqlQuery.from(JdzdWeight.class, false).column(JdzdWeightInfo.WEIGHT).equal(JdzdWeightInfo.STATUS, "1").orderByDesc(JdzdWeightInfo.WEIGHT).groupBy(JdzdWeightInfo.WEIGHT);
        List<JdzdWeight> jdzdWeights = commonMapper.selectByQuery(query);
        //初始化 个数与集合
        long l = 0;
        List<String> weightId = new ArrayList<>();
        for (JdzdWeight jdzdWeight : jdzdWeights) {
            l += getWeightLong(jdzdWeight.getWeight());
            if (l < num) {
                // 如果不够  继续循环
                weightId = getWeightId(weightId, jdzdWeight.getWeight(), 0L);
                continue;
            } else if (l == num) {
                //如果相等 则跳出循环 返回集合
                weightId = getWeightId(weightId, jdzdWeight.getWeight(), 0L);
                break;
            } else {
                weightId = getWeightId(weightId, jdzdWeight.getWeight(), getWeightLong(jdzdWeight.getWeight()) - (l - num));
                break;
            }
        }
        return weightId;
    }

    private long getWeightLong(Long weight) {
        return commonMapper.countByQuery(SqlQuery.from(JdzdWeight.class).equal(JdzdWeightInfo.STATUS, "1").equal(JdzdWeightInfo.WEIGHT, weight));
    }

    /**
     * @param weightIds
     * @param weight
     * @param count     抽取个数  为0 表示 全抽  为其它数  就是其它数
     * @return
     */
    private List<String> getWeightId(List<String> weightIds, Long weight, Long count) {
        List<String> strings = commonMapper.selectByQuery(SqlQuery.from(JdzdWeight.class, false).column(JdzdWeightInfo.ORGANISEID).equal(JdzdWeightInfo.STATUS, "1").equal(JdzdWeightInfo.WEIGHT, weight).setReturnClass(String.class));

        if (count > 0) {
            //这时就是要在 strings 数组中  抽  count  个
            Random r = new Random();
            String[] st = new String[Math.toIntExact(count)];
            for (int i = 0; i < st.length; i++) {
                String str = strings.get(r.nextInt(strings.size()));
                //这是为了防止重复
                for (int j = 0; j < st.length; j++) {
                    while (str.equals(st[j])) {
                        j--;
                        str = strings.get(r.nextInt(strings.size()));
                    }
                }
                st[i] = str;
            }
            strings = Arrays.asList(st);
        }
        weightIds.addAll(strings);
        return weightIds;
    }
}
