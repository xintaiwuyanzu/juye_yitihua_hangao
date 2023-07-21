package com.dr.archive.controller;

import com.dr.archive.service.impl.ArchiveOrganisePersonService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 档案业务组织机构权限相关通用接口，好多模块都用到了，
 * 统一业务逻辑
 * TODO 将来要移到平台
 *
 * @author dr
 */
@RestController
@RequestMapping("/api/archiveOrganisePerson/")
public class ArchiveOrganisePersonController {
    @Autowired
    ArchiveOrganisePersonService archiveOrganisePersonService;

    /**
     * 获取当前登录人所在机构下的人员
     *
     * @param organise
     * @return
     */
    @RequestMapping("/getPersonsByOrganise")
    public ResultEntity<List<Person>> getPersonsByOrganise(@Current Organise organise) {
        return ResultEntity.success(archiveOrganisePersonService.getOrganiseAllPersons(organise.getId()));

    }

    /**
     * 获取机构人员树
     *
     * @return
     */
    @RequestMapping("/getOrganisePersonTree")
    public ResultListEntity<TreeNode> getOrganisePersonTree() {
        return ResultListEntity.success(archiveOrganisePersonService.getOrganisePersonTree());
    }

}
