package com.dr.archive.init;

import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.sys.entity.SysDict;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 菜单初始化父类
 *
 * @author dr
 */
public abstract class AbstractDictInit extends AbstractClassPathResourceDataInit {

    @Autowired
    CommonService commonService;

    @Override
    protected String getRefType() {
        return "dict";
    }

    @Override
    protected void doInitData(JsonNode node) {
        try {
            List<SysDict> dicts = JsonUtils.stringToList(objectMapper, node.toString(), SysDict.class);
            dicts.forEach(d -> {
                if (d.getStatus() == null) {
                    d.setStatus(StatusEntity.STATUS_ENABLE);
                }
                commonService.insert(d);
            });
        } catch (Exception ignored) {

        }
    }


}
