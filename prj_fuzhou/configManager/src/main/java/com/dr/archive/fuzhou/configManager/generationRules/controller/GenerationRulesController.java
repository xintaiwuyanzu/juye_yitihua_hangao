package com.dr.archive.fuzhou.configManager.generationRules.controller;

import com.dr.archive.fuzhou.configManager.generationRules.service.GenerationRulesService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/genertion")
public class GenerationRulesController {

    @Autowired
    GenerationRulesService generationRulesService;
    @Autowired
    OrganisePersonService organisePersonService;

    @RequestMapping("/getGenerationRules")
    public ResultEntity getGenerationRules(String categoryId, String year) {
        return ResultEntity.success(generationRulesService.getGenerationRules(categoryId, year));
    }

    @RequestMapping({"/organiseTree"})
    public ResultEntity<List<TreeNode>> organiseTree(@Current Organise organise, boolean all, @RequestParam(defaultValue = "default") String groupId) {
        OrganiseQuery.Builder builder = (new OrganiseQuery.Builder()).parentIdEqual(organise.getId()).groupIdEqual(groupId);
        if (!all) {
            builder.statusEqual("1");
        }

        List<Organise> organises = this.organisePersonService.getOrganiseList(builder.build());
        organises.add(organise);
        List<TreeNode> treeNodes = CommonService.listToTree(organises, organise.getId(), Organise::getOrganiseName);
        if (treeNodes.size() == 0) {
            TreeNode treeNode = new TreeNode<Organise>(organise.getId(), organise.getOrganiseName());
            treeNode.setData(organise);
            treeNode.setLevel(0);
            ArrayList<TreeNode> nodeArrayList = new ArrayList<>();
            nodeArrayList.add(treeNode);
            return ResultEntity.success(nodeArrayList);
        }
        return ResultEntity.success(treeNodes);

    }
}
