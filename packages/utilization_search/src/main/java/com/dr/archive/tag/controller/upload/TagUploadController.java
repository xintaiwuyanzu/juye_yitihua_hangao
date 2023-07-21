package com.dr.archive.tag.controller.upload;

import com.dr.archive.tag.service.TagUploadService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: yang
 * @create: 2022-07-15 09:34
 **/
@RestController
@RequestMapping("/api/tagUpload")
public class TagUploadController {

    @Autowired
    TagUploadService tagUploadService;

    @RequestMapping("/upload")
    ResultEntity upload(@RequestParam MultipartFile file, @RequestParam String tagType) {
        tagUploadService.upload(file, tagType);
        return ResultEntity.success();
    }

    @RequestMapping("/importWordTxt")
    ResultEntity importWordTxt(@RequestParam MultipartFile file, @RequestParam String id, @RequestParam String type) {
        tagUploadService.importWordTxt(file, id, type);
        return ResultEntity.success();
    }
}
