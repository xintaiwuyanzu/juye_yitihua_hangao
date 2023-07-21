package com.dr.archive.examine.service.impl;


import com.dr.archive.examine.entity.JdzdCategory;
import com.dr.archive.examine.entity.JdzdCategoryInfo;
import com.dr.archive.examine.service.JdzdCategoryService;
import com.dr.archive.examine.service.ZfjcResultService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.sys.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dr.archive.examine.service.ZfjcCheckService.JDJC_ZFJC_DICT_JCNR;

@Service
@Transactional(rollbackFor = Exception.class)
public class JdzdCategoryServiceImpl extends DefaultBaseService<JdzdCategory> implements JdzdCategoryService {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ZfjcResultService zfjcResultService;
    @Autowired
    protected SysDictService sysDictService;

    @Override
    public JdzdCategory getOneById(String id) {
        return commonMapper.selectById(JdzdCategory.class, id);
    }

    @Override
    public long insert(JdzdCategory entity) {
        Organise organise = SecurityHolder.get().currentOrganise();
        if (entity.getLargeCategory().equals("1")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_one);
        } else if (entity.getLargeCategory().equals("2")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_two);
        } else if (entity.getLargeCategory().equals("3")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_three);
        }
        entity.setStatus(StringUtils.isNotBlank(entity.getStatus()) ? entity.getStatus() : "1");
        entity.setOrganiseId(organise.getId());
        entity.setOrganiseName(organise.getOrganiseName());
        return super.insert(entity);
    }

    @Override
    public long updateById(JdzdCategory entity) {
        if (entity.getLargeCategory().equals("1")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_one);
        } else if (entity.getLargeCategory().equals("2")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_two);
        } else if (entity.getLargeCategory().equals("3")) {
            entity.setLargeCategoryValue(JdzdCategory.largeCategory_three);
        }
        return super.updateById(entity);
    }

    @Override
    public List<JdzdCategory> getListCategoryByLargeCategory(String largeCategory) {
        return commonMapper.selectByQuery(SqlQuery.from(JdzdCategory.class).equal(JdzdCategoryInfo.LARGECATEGORY, largeCategory));
    }

    /**
     * 这里的pid 如果有值 就表示只查剩下的类型 如果无值 查全部
     *
     * @param pId
     * @return
     */
    @Override
    public List<TreeNode> cateTree(String pId) {

//        String cateId = "";
//        if (StringUtils.isNotBlank(pId)) {
//            List<ZfjcCheckResult> resultList = zfjcResultService.getCheckResultListByPId(pId);
//            for (ZfjcCheckResult zfjcCheckResult : resultList) {
//                cateId += zfjcCheckResult.getCategoryId();
//            }
//        }
//
//        //外层list树
//        List<TreeNode> treeNodes = new ArrayList<>();
//        SqlQuery<JdzdCategory> group = SqlQuery.from(JdzdCategory.class, false).column(JdzdCategoryInfo.LARGECATEGORY).column(JdzdCategoryInfo.LARGECATEGORYVALUE).groupBy(JdzdCategoryInfo.LARGECATEGORY, JdzdCategoryInfo.LARGECATEGORYVALUE);
//        List<JdzdCategory> groupList = commonMapper.selectByQuery(group);
//        for (JdzdCategory jdzdCategory : groupList) {
//
//            TreeNode<JdzdCategory> treeNode = new TreeNode<>(jdzdCategory.getLargeCategory(), jdzdCategory.getLargeCategoryValue());
//            List<JdzdCategory> categoryList = getListCategoryByLargeCategory(jdzdCategory.getLargeCategory());
//            //            内层list树
//            List<TreeNode> childrenNodes = new ArrayList<>();
//            for (JdzdCategory category : categoryList) {
//
//                if (StringUtils.isNotBlank(pId)) {
//                    //只有不包括的才加
//                    if (!cateId.contains(category.getId())) {
//                        TreeNode<JdzdCategory> childrenNode = new TreeNode<>(category.getId(), category.getSmallCategory());
//                        childrenNodes.add(childrenNode);
//                    }
//                } else {
//                    //否则全加
//                    TreeNode<JdzdCategory> childrenNode = new TreeNode<>(category.getId(), category.getSmallCategory());
//                    childrenNodes.add(childrenNode);
//                }
//
//
//            }
//            treeNode.setChildren(childrenNodes);
//            if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0) {
//                treeNodes.add(treeNode);
//            }
//
//        }
//        return treeNodes;

        //从检查项管理获取大类
        List<TreeNode> treeNodeList = sysDictService.dict(JDJC_ZFJC_DICT_JCNR);

        treeNodeList.forEach(treeNode -> {
            List<TreeNode> jianChaXiangListTreeNode = new ArrayList<>();
            List<JdzdCategory> jdzdCategoryList = selectList(SqlQuery.from(JdzdCategory.class).equal(JdzdCategoryInfo.LARGECATEGORY, treeNode.getId()).orderBy(JdzdCategoryInfo.LARGECATEGORY).orderBy(JdzdCategoryInfo.SMALLCATEGORY));
            jdzdCategoryList.forEach(jdzdCategory -> {
                TreeNode jianChaTreeNode = new TreeNode<>(jdzdCategory.getId(), jdzdCategory.getSmallCategory(), jdzdCategory);
                jianChaXiangListTreeNode.add(jianChaTreeNode);
            });
            treeNode.setChildren(jianChaXiangListTreeNode);
        });
        return treeNodeList;
    }
}
