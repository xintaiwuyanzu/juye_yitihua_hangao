package com.dr.archive.init;

import com.dr.framework.common.config.entity.CommonConfig;
import com.dr.framework.common.config.entity.CommonConfigInfo;
import com.dr.framework.common.config.service.CommonConfigService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.util.Constants;
import com.dr.framework.sys.service.InitDataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 用来从系统路径初始化数据
 *
 * @author dr
 */
public abstract class AbstractClassPathResourceDataInit implements InitDataService.DataInit {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    CommonConfigService commonConfigService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void initData(DataBaseService dataBaseService) {
        //是否初始化过的属性放到commonconfig表中
        CommonConfig config = new CommonConfig();
        //系统菜单默认Id
        config.setRefId(Constants.DEFAULT);
        //业务外键类型是菜单
        config.setRefType(getRefType());
        //配置类型是档案
        config.setType(getDataType());
        SqlQuery sqlQuery = SqlQuery.from(CommonConfig.class)
                .equal(CommonConfigInfo.REFID, config.getRefId())
                .equal(CommonConfigInfo.REFTYPE, config.getRefType())
                .equal(CommonConfigInfo.TYPE, config.getType());
        //判断档案类型的菜单配置是否初始化过
        boolean inited = commonMapper.existsByQuery(sqlQuery);
        if (!inited) {
            Resource resource = new ClassPathResource(getResourceLocation());
            if (resource.exists()) {
                try {
                    //读取数据
                    JsonNode node = objectMapper.readTree(resource.getInputStream());
                    doInitData(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //记录数据已经初始化的记录
            CommonService.bindCreateInfo(config);
            commonMapper.insert(config);
        }
    }

    /**
     * 真正执行初始化操作
     *
     * @param node
     */
    protected abstract void doInitData(JsonNode node);

    /**
     * 获取资源路径
     *
     * @return
     */
    protected abstract String getResourceLocation();

    /**
     * 获取业务外键类型
     *
     * @return
     */
    protected abstract String getRefType();

    /**
     * 获取数据类型
     *
     * @return
     */
    protected abstract String getDataType();
}
