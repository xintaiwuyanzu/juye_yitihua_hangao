package com.dr.archive.fuzhou.portal.controller;

import com.dr.archive.fuzhou.portal.service.PeopleLivelihoodService;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/people")
public class PeopleLivelihoodController {

    @Autowired
    PeopleLivelihoodService peopleLivelihoodService;

    @RequestMapping("/searchPeopleLivelihood")
    public ResultEntity search(String idNo, String type,
                               @RequestParam(value = "index", defaultValue = "0") Integer index,
                               @RequestParam(value = "size", defaultValue = "15") Integer size) {

        return ResultEntity.success(peopleLivelihoodService.searchPeopleLivelihood(idNo, type, index, size));
    }
}
