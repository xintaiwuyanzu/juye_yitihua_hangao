package com.dr.archive.tag.service;

import java.util.List;

public interface TagService {
    //TODO 暂时先传这两个参数，等写页面时根据页面逻辑修改参数
    void insertTag(String text, Integer count);

    List getWord(String text);

    List getKeywords(String text);

    List getKeysentences(String text);
}
