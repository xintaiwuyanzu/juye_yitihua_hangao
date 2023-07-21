package com.dr.archive.init;

import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/8/18 14:56
 */
@Service
public class DefaultArchiveDictManager extends AbstractDictInit {

    @Override
    public String name() {
        return "档案字典";
    }

    @Override
    protected String getResourceLocation() {
        return "/init/archiveDicts.json";
    }

    @Override
    protected String getDataType() {
        return "archive.dict";
    }
}
