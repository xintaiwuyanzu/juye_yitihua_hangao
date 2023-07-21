package com.dr.archive.archivecar.bo;

import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 档案车类型逻辑类
 *
 * @author dr
 */
public class ArchiveCarType {
    /**
     * 档案车类型编码
     */
    private String type;
    /**
     * 档案车类型名称
     */
    private String name;
    /**
     * 档案车对应的标记是否为静态的
     */
    private boolean tagStatic;

    /**
     * 档案车对应的所有标记
     */
    private List<ArchiveCarDetailTag> children;
    private boolean withTag;
    /**
     * 档案车类型提供类
     */
    @JsonIgnore
    private transient ArchiveCarTypeProvider typeProvider;

    public ArchiveCarType(ArchiveCarTypeProvider typeProvider, String personId, boolean withTag) {
        setTypeProvider(typeProvider);
        setType(typeProvider.getType());
        setName(typeProvider.getName());
        setTagStatic(typeProvider.isTagStatic());
        setWithTag(withTag);
        if (withTag) {
            setChildren(typeProvider.getDetailTags(personId));
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTagStatic() {
        return tagStatic;
    }

    public void setTagStatic(boolean tagStatic) {
        this.tagStatic = tagStatic;
    }

    public List<ArchiveCarDetailTag> getChildren() {
        return children;
    }

    public void setChildren(List<ArchiveCarDetailTag> children) {
        this.children = children;
    }

    public boolean isWithTag() {
        return withTag;
    }

    public void setWithTag(boolean withTag) {
        this.withTag = withTag;
    }

    public ArchiveCarTypeProvider getTypeProvider() {
        return typeProvider;
    }

    public void setTypeProvider(ArchiveCarTypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }
}
