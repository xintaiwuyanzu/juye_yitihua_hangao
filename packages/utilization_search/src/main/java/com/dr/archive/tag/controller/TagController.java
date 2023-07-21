package com.dr.archive.tag.controller;

import com.dr.archive.tag.service.TagService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tag")
public class TagController {


    @Autowired
    TagService tagService;

    @RequestMapping("insertTag")
    public ResultEntity testTag(String text, Integer count){

        tagService.insertTag(text, count);

        return ResultEntity.success();
    }

    //实体识别和词性分析
    @RequestMapping("getWord")
    public ResultEntity getWord(String text){
        return ResultEntity.success(tagService.getWord(text));
    }

    //关键词提取
    @RequestMapping("getKeywords")
    public ResultEntity getKeywords(String text){
        return ResultEntity.success(tagService.getKeywords(text));
    }

    //关键字提取
    @RequestMapping("getKeysentences")
    public ResultEntity getKeysentences(String text){
        return ResultEntity.success(tagService.getKeysentences(text));
    }
}
