package com.dr.archive.init;

import com.dr.archive.util.Constants;
import org.springframework.stereotype.Service;

/**
 * 用来初始化档案相关菜单
 *
 * @author dr
 */
@Service
public class DefaultArchiveMenuManager extends AbstractMenuInit {

    @Override
    public String name() {
        return "档案菜单";
    }

    @Override
    protected String getResourceLocation() {
        return "/init/archiveMenus.json";
    }

    @Override
    protected String getDataType() {
        return Constants.MODULE_NAME;
    }

}
