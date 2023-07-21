package com.dr.archive.kufang.entityfiles.service;


import com.dr.archive.kufang.entityfiles.entity.ArchiveType;
import com.dr.archive.kufang.entityfiles.entity.ArchiveTypeInfo;
import com.dr.archive.kufang.entityfiles.utils.CommonTools;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArchiveTypeServiceImpl extends DefaultBaseService<ArchiveType> implements ArchiveTypeService {

    @Override
    public long insert(ArchiveType entity) {
        entity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        return super.insert(entity);
    }

    /**
     * 添加档案类型的案卷和文件
     */
    @Override
    public void addType(String pid, String type) {
        ArchiveType archiveType = new ArchiveType();
        archiveType.setPid(pid);
        archiveType.setaType(type);
        String id = CommonTools.getUUID();
        archiveType.setId(id);
        if (type.equals(ArchiveType.TYPE_案卷)) {
            archiveType.setArchiveTypeCode("aj");
            archiveType.setArchiveTypeName("案卷");
            archiveType.setOrder(1);
            //案卷下添加对应卷内文件
            addType(id, ArchiveType.TYPE_卷内文件);
        } else if (type.equals(ArchiveType.TYPE_文件)) {
            archiveType.setArchiveTypeCode("wj");
            archiveType.setArchiveTypeName("文件");
            archiveType.setOrder(2);
        } else if (type.equals(ArchiveType.TYPE_卷内文件)) {
            archiveType.setArchiveTypeCode("jn");
            archiveType.setArchiveTypeName("卷内文件");
            archiveType.setOrder(3);
        }
        insert(archiveType);

    }
    /**
     * 获得父分类
     */
    @Override
    public ArchiveType getParentType(String id) {
        ArchiveType type = selectById(id);
        ArchiveType pType = selectById(type.getPid());
        return pType;
    }

    /**
     * 获得子分类
     */
    @Override
    public ArchiveType getChildType(String id, String aType){
        return selectOne(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, id).equal(ArchiveTypeInfo.ATYPE, aType));
    }

    /**
     * 获得当前机构的档案类型树
     */
    @Override
    public List<TreeNode> getTypeTree() {
        return commonMapper.selectByQuery(
                SqlQuery.from(ArchiveType.class)
                        .equal(ArchiveTypeInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                        .orderBy(ArchiveTypeInfo.ORDERBY)
        )
                .stream()
                .map(this::toTree)
                .collect(Collectors.toList());
    }

    /**
     * 获得当前机构的所有档案类型树
     */
    @Override
    public ArrayList<TreeNode> getAllTypeTree(String pid, String fondId) {
        ArrayList<TreeNode> treeNodes = getTreeList(pid,fondId);
        return treeNodes;
    }

    public ArrayList<TreeNode> getTreeList(String pid,String fondId) {
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        SqlQuery<ArchiveType> equal = SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, pid);
        //是根的时候查询出门类。其他时候查询出门类下级类型
        if ("root".equals(pid)){
            equal.equal(ArchiveTypeInfo.FONDID,fondId);
        }
        equal.equal(ArchiveTypeInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId()).notEqual(ArchiveTypeInfo.ATYPE, ArchiveType.TYPE_卷内文件)
                .orderBy(ArchiveTypeInfo.ORDERBY);
        List<ArchiveType> list = commonMapper.selectByQuery(equal);
        for (ArchiveType archiveType : list) {
            //设置id，和name
            TreeNode treeNode = toTree(archiveType);
            //设置pid
            treeNode.setParentId(pid);
            boolean isChild = commonMapper.existsByQuery(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.PID, archiveType.getId()));
            //设置树的孩子
            if (isChild) {
                treeNode.setChildren(getTreeList(archiveType.getId(),fondId));
            }
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }


    @Override
    public List<TreeNode> getTreeById(String refId) {
        return null;
    }

    @Override
    public Function<ArchiveType, String> labelCallBack() {
        return ArchiveType::getArchiveTypeName;
    }

    @Override
    public Function<ArchiveType, String> idCallback() {
        return ArchiveType::getId;
    }


}
