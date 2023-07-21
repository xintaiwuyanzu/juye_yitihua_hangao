package com.dr.archive.service.impl;

import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.service.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 封装档案相关组织机构人员常用逻辑
 *
 * @author dr
 */
@Component
public class ArchiveOrganisePersonService {
    /**
     * 平台组织机构
     */
    @Autowired
    protected OrganisePersonService organisePersonService;
    /**
     * 平台权限
     */
    @Autowired
    protected SecurityManager securityManager;

    /**
     * 获取当前登录人所在机构下的人员
     *
     * @param organiseId
     * @return
     */
    public List<Person> getOrganiseAllPersons(String organiseId) {
        if (StringUtils.hasText(organiseId)) {
            return organisePersonService.getOrganiseDefaultPersons(organiseId);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 获取人员所在默认机构所有人员列表
     *
     * @param personId
     * @return
     */
    public List<Person> getPersonDefaultOrganiseAllPersons(String personId) {
        Organise organise = organisePersonService.getPersonDefaultOrganise(personId);
        if (organise != null) {
            return organisePersonService.getOrganiseDefaultPersons(organise.getId());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 获取当前登录人所在父級机构下的人员
     *
     * @param organiseId 机构Id
     * @return
     */
    public List<Person> getParentOrganiseAllPersons(String organiseId) {
        Organise organise = getOrganise(organiseId);
        Organise parentOrganise = organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(organise.getParentId()).getQuery());
        if (!StringUtils.isEmpty(organise)) {
            return organisePersonService.getOrganiseDefaultPersons(parentOrganise.getId());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 根据机构id查询机构
     *
     * @param organiseId
     * @return
     */
    public Organise getOrganise(String organiseId) {
        return organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(organiseId).build());
    }

    /**
     * 获取指定机构带有指定权限编码的所有用户
     *
     * @param organiseId
     * @param roleCodes
     * @return
     */
    public List<Person> getOrganisePersonsWithRole(String organiseId, String... roleCodes) {
        return getOrganiseAllPersons(organiseId).stream().filter(p -> securityManager.hasRole(p.getId(), roleCodes)).collect(Collectors.toList());
    }

    /**
     * 获取指定机构带有指定权限编码的所有用户
     * <p>
     * 业务明确知道条件下只会返回一个用户，只是为了调用方法方便使用
     *
     * @param organiseId
     * @param roleCodes
     * @return
     */
    public Person getOrganisePersonWithRole(String organiseId, String... roleCodes) {
        List<Person> people = getOrganisePersonsWithRole(organiseId, roleCodes);
        Assert.isTrue(people.size() < 2, "查询到多条数据");
        return people.isEmpty() ? null : people.get(0);
    }

    /**
     * 获取指定人员所在机构带有指定权限的所有用户
     *
     * @param personId
     * @param roleCodes
     * @return
     */
    public List<Person> getPersonDefaultOrganisePersonsWithRole(String personId, String... roleCodes) {
        return getPersonDefaultOrganiseAllPersons(personId).stream().filter(p -> securityManager.hasRole(p.getId(), roleCodes)).collect(Collectors.toList());
    }

    /**
     * 获取指定人员所在机构带有指定权限的所有用户
     * <p>
     * 业务明确知道条件下只会返回一个用户，只是为了调用方法方便使用
     *
     * @param personId
     * @param roleCodes
     * @return
     */
    public Person getPersonDefaultOrganisePersonWithRole(String personId, String... roleCodes) {
        List<Person> people = getPersonDefaultOrganisePersonsWithRole(personId, roleCodes);
        Assert.isTrue(people.size() < 2, "查询到多条数据");
        return people.isEmpty() ? null : people.get(0);
    }

    /**
     * 获取组织结构人员树
     *
     * @return
     */
    public List<TreeNode> getOrganisePersonTree() {
        List<TreeNode> treeNodeList = new ArrayList<>();
        List<Organise> organiseList = this.organisePersonService.getOrganiseList((new OrganiseQuery.Builder()).build());
        for (Organise organise : organiseList) {
            TreeNode treeNode = new TreeNode(organise.getId(), organise.getOrganiseName(), organise, personTreeByOrgId(organise.getId()));
            treeNodeList.add(treeNode);
        }
        return treeNodeList;
    }

    /**
     * 根据机构id获取人员树
     *
     * @param orgId
     * @return
     */
    List<TreeNode> personTreeByOrgId(String orgId) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        List<Person> personList = getOrganiseAllPersons(orgId);
        for (Person person : personList) {
            TreeNode treeNode = new TreeNode(person.getId(), person.getUserName(), person);
            treeNodeList.add(treeNode);
        }
        return treeNodeList;
    }
}
