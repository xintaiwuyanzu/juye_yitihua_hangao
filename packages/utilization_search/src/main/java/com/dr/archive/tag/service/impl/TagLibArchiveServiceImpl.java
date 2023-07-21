package com.dr.archive.tag.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.tag.TagConfig;
import com.dr.archive.tag.entity.*;
import com.dr.archive.tag.service.StdTagService;
import com.dr.archive.tag.service.TagLibArchiveService;
import com.dr.archive.tag.service.TagLibService;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_CATEGORY_CODE;
import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_FOND_CODE;
import static com.dr.framework.common.entity.StatusEntity.STATUS_ENABLE_STR;
import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

/**
 * @author: qiuyf
 * @date: 2022/3/15 11:37
 */
@Service
public class TagLibArchiveServiceImpl extends DefaultBaseService<TagLibArchive> implements TagLibArchiveService {
    @Autowired
    TagLibService tagLibService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    ArchiveFileContentService fileContentService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TagConfig tagConfig;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    StdTagService stdTagService;
    @Autowired
    protected Executor executor;

    @Override
    public long insert(TagLibArchive entity) {
        //TODO 先判断该档案是否打过标签了
        if (StringUtils.hasText(entity.getFormDataId()) && StringUtils.hasText(entity.getFormDefinitionId()) && StringUtils.hasText(entity.getTagName().trim())) {
            long count = commonMapper.countByQuery(SqlQuery.from(TagLibArchive.class).equal(TagLibArchiveInfo.FORMDATAID, entity.getFormDataId()).equal(TagLibArchiveInfo.FORMDEFINITIONID, entity.getFormDefinitionId()).equal(TagLibArchiveInfo.TAGNAME, entity.getTagName()));
            if (count == 0) {
                entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
                entity.setStatus(Optional.ofNullable(entity.getStatus()).orElse("1"));
                return super.insert(entity);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public long updateById(TagLibArchive entity) {
        //TODO 更新es
        if (STATUS_ENABLE_STR.equals(entity.getStatus())) {

        }
        return commonMapper.updateIgnoreNullById(entity);
    }

    /**
     * 根据提供的标签 与 档案信息 创建 标签与档案的关系信息 (除固体标签)
     *
     * @param tags             提供的标签数据
     * @param formDataId       数据id
     * @param formDefinitionId 表单id
     */
    @Override
    public int addTagLibArchives(List<TagLib> tags, String formDataId, String formDefinitionId, String type) {
        int intCount = 0;
        for (TagLib tag : tags) {
            tag = tagLibService.getTagLib(tag);
            List<TagLibArchive> tagLibArchives = commonMapper.selectByQuery(SqlQuery.from(TagLibArchive.class).equal(TagLibArchiveInfo.FORMDATAID, formDataId).equal(TagLibArchiveInfo.FORMDEFINITIONID, formDefinitionId).equal(TagLibArchiveInfo.TAGLIBID, tag.getId()));
            if (tagLibArchives.size() == 0) {
                FormData formData = dataManager.selectOneFormData(formDefinitionId, formDataId);
                TagLibArchive tagLibArchive = new TagLibArchive();
                tagLibArchive.setTagLibId(tag.getId());
                tagLibArchive.setTagName(tag.getTagName());
                tagLibArchive.bindFormData(formData);
                tagLibArchive.setSourceType(type);
                Fond fond = fondService.findFondByCode(formData.getString(COLUMN_FOND_CODE));
                if (fond != null) {
                    tagLibArchive.bindFondInfo(fond);
                    Category category = categoryService.findCategoryByCode(formData.getString(COLUMN_CATEGORY_CODE), fond.getId());
                    if (category != null) {
                        tagLibArchive.bindCategoryInfo(category);
                    }
                }
                intCount += insert(tagLibArchive);
            }
        }
        return intCount;
    }

    public int addTagLibArchives2(List<TagLib> tags, String tagName, String formDataId, String formDefinitionId) {
        int intCount = 0;
        for (TagLib tag : tags) {
            List<TagLibArchive> tagLibArchives = commonMapper.selectByQuery(SqlQuery.from(TagLibArchive.class).equal(TagLibArchiveInfo.FORMDATAID, formDataId).equal(TagLibArchiveInfo.FORMDEFINITIONID, formDefinitionId).equal(TagLibArchiveInfo.TAGLIBID, tag.getId()).equal(TagLibArchiveInfo.TAGNAME, tag.getTagName()));
            if (tagLibArchives.size() == 0) {
                FormData formData = dataManager.selectOneFormData(formDefinitionId, formDataId);
                TagLibArchive tagLibArchive = new TagLibArchive();
                tagLibArchive.setTagLibId(tag.getId());
                tagLibArchive.setTagName(tagName);
                tagLibArchive.setFullLable(tag.getFullLable() + ',' + tag.getTagName());
                tagLibArchive.bindFormData(formData);
                intCount += insert(tagLibArchive);
            }
        }
        return intCount;
    }

    /**
     * 根据档案信息 查 标签id列表,用于es插入数据时
     *
     * @param formDataId       数据id
     * @param formDefinitionId 表单id
     * @return 标签id列表
     */
    @Override
    public List<Map> selectByForm(String formDataId, String formDefinitionId) {
        List<Map> tags = commonMapper.selectByQuery(
                SqlQuery.from(TagLibArchive.class, false)
                        .join(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID)
                        .column(TagLibInfo.ID.alias("id"), TagLibArchiveInfo.TAGNAME.alias("name"), TagLibInfo.STTYPE.alias("type"))
                        .equal(TagLibArchiveInfo.FORMDATAID, formDataId)
                        .equal(TagLibArchiveInfo.FORMDEFINITIONID, formDefinitionId)
                        .setReturnClass(Map.class));
        return tags;
    }

    /**
     * 标签档案关系列表 求每个标签关联的档案数
     *
     * @param tagLib
     */
    @Override
    public SqlQuery selectByTag(TagLib tagLib) {
        return SqlQuery.from(TagLib.class, false)
                .leftOuterJoin(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID)
                .column(TagLibInfo.ID.alias("tagLibId"))
                .column(TagLibInfo.CTYPE)
                .column(TagLibInfo.TAGNAME)
                .column(TagLibInfo.STTYPE)
                .column(TagLibArchiveInfo.FORMDATAID.count().alias("archiveCount"))
                .like(TagLibInfo.TAGNAME, tagLib.getTagName())
                .like(TagLibInfo.CTYPE, tagLib.getCtype())
                .groupBy(TagLibInfo.ID, TagLibInfo.CTYPE, TagLibInfo.TAGNAME, TagLibInfo.STTYPE)
                .orderByDesc(TagLibArchiveInfo.FORMDATAID.count().alias("archiveCount"), TagLibInfo.CREATEDATE.max())
                .setReturnClass(Map.class);
    }

    //档案详情页面中的 知识图谱数据
    @Override
    public List<Map> archiveKnowledgeGraph(String formDefinitionId, String formDataId, String tagid) {
        List<Map> mapList = new ArrayList<>();
        if (StringUtils.hasText(tagid)) { //如果有标签id 根据标签id查
            SqlQuery sqlQuery = SqlQuery.from(TagLib.class, false).join(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID).column(TagLibArchiveInfo.TITLE, TagLibArchiveInfo.ARCHIVECODE, TagLibArchiveInfo.FORMDATAID, TagLibArchiveInfo.FORMDEFINITIONID, TagLibInfo.TAGNAME, TagLibInfo.ID).equal(TagLibArchiveInfo.TAGLIBID, tagid).orderByDesc(TagLibArchiveInfo.CREATEDATE).setReturnClass(Map.class);
            mapList = commonMapper.selectByQuery(sqlQuery);
        } else {
            List<String> tagIds = commonMapper.selectByQuery(SqlQuery.from(TagLib.class, false).join(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID).column(TagLibInfo.ID).equal(TagLibArchiveInfo.FORMDATAID, formDataId).equal(TagLibArchiveInfo.FORMDEFINITIONID, formDefinitionId).orderByDesc(TagLibArchiveInfo.CREATEDATE).setReturnClass(String.class));
            if (tagIds.size() > 0) {
                SqlQuery sqlQuery = SqlQuery.from(TagLib.class, false).join(TagLibInfo.ID, TagLibArchiveInfo.TAGLIBID).column(TagLibArchiveInfo.TITLE, TagLibArchiveInfo.ARCHIVECODE, TagLibArchiveInfo.FORMDATAID, TagLibArchiveInfo.FORMDEFINITIONID, TagLibInfo.TAGNAME, TagLibInfo.ID).in(TagLibArchiveInfo.TAGLIBID, tagIds).orderByDesc(TagLibArchiveInfo.CREATEDATE).setReturnClass(Map.class);
                mapList = commonMapper.selectByQuery(sqlQuery);
            }
        }
        return mapList;
    }

    public void formAutoAddTag(String formDefinitionId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        Relation relation = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        if (null != relation) {
            SqlQuery sqlQuery = SqlQuery.from(relation).setReturnClass(HashMap.class);
            List<Map> list = commonMapper.selectByQuery(sqlQuery);
            for (Map map : list) {
                autoAddTag(map.get("id").toString(), formDefinitionId);
            }
        }
    }

    public void formTagAddTag(String formDefinitionId, String type) {
        executor.execute(() -> {
            // 先清空 标签档案关系内容，标签需要分类型，国家库、事实库、专题库、自定义标签
            if (StringUtils.hasText(type)) {
                delete(SqlQuery.from(TagLibArchive.class).equal(TagLibArchiveInfo.SOURCETYPE, type));
            }
            FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
            Relation relation = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
            if (null != relation) {
                SqlQuery sqlQuery = SqlQuery.from(relation).setReturnClass(HashMap.class);
                List<Map> list = commonMapper.selectByQuery(sqlQuery);
                for (Map map : list) {
                    tagAddTag(map.get("id").toString(), formDefinitionId, type);
                }
            }
        });
    }

    //档案入库时添加标签(固体标签+实体标签)
    public int autoAddTag(String formDataId, String formDefinitionId) {
        //获取内容
        String content = fileContentService.getFileContentsByRefId(formDataId);
        if (!StringUtils.isEmpty(content)) {
            List<String> types = new ArrayList<>();

            types.add("PRE");
            types.add("LOC");
            types.add("ORG");
            types.add("TIME");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("text", content);

            params.add("remove_stop_words", "a");
            try {
                JsonNode tagNode = objectMapper.readTree(restTemplate.postForEntity(tagConfig.getTagword(), params, String.class).getBody());
                List<Map<String, String>> list = objectMapper.readValue(tagNode.get("data").traverse(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HashMap.class));
                //去重 类型只取 人,地点,机构,时间
                list = list.stream().filter(item -> types.contains(item.get("type"))).distinct().collect(Collectors.toList());
                //将获取的标签与 固体标签进行对比,不在固体标签库的为不确定标签 用trie 对齐
                for (Map map : list) {
                    List<TagLib> tagLibs = duiji(map.get("name").toString().trim().replaceAll("\r\n", ""));
                    if (tagLibs.size() > 0) {
                        addTagLibArchives2(tagLibs, map.get("name").toString().trim().replaceAll("\r\n", ""), formDataId, formDefinitionId);
                    } else { //不确定标签
                        List<TagLib> tagLibs2 = new ArrayList<>();
                        TagLib tagLib = new TagLib();
                        tagLib.setCtype("st");
                        tagLib.setStType(map.get("type").toString());
                        tagLib.setTagName(map.get("name").toString().trim().replaceAll("\r\n", ""));
                        tagLibs2.add(tagLib);
                        //TODO 目前是根据 标签引擎，后面需要改成，从标签库打标签
                        addTagLibArchives(tagLibs2, formDataId, formDefinitionId, TagLibArchive.SOURCETYPE_ZIDINGYI);
                    }
                }
            } catch (Exception e) {

            }
        }

        //根据元数据标签配置 取档案数据中的实体标签
        List<FormTagConfigure> formTagConfigures = commonMapper.selectByQuery(SqlQuery.from(FormTagConfigure.class).equal(FormTagConfigureInfo.FORMDEFINITIONID, formDefinitionId));
        FormData formData = archiveDataManager.selectOneFormData(formDefinitionId, formDataId);
        List<TagLib> tagLibs = new ArrayList<>();
        for (FormTagConfigure formTagConfigure : formTagConfigures) {
            String fieldval = formData.getString(formTagConfigure.getFieldCode());
            if (!StringUtils.isEmpty(fieldval)) {
                TagLib tagLib = new TagLib();
                tagLib.setTagName(fieldval);
                tagLib.setCtype("st");
                tagLib.setStType(formTagConfigure.getStType());
                tagLibs.add(tagLib);
            }
        }
        if (tagLibs.size() > 0) {
            addTagLibArchives(tagLibs, formDataId, formDefinitionId, TagLibArchive.SOURCETYPE_ZIDINGYI);
        }
        return 1;
    }

    //档案入库时基于标签库打标签
    public int tagAddTag(String formDataId, String formDefinitionId, String type) {
        List<TagLib> tagLibs = new ArrayList<>();
        //获取内容
        String content = fileContentService.getFileContentsByRefId(formDataId);
        //获取标签
        List<StdTag> stdTagList = stdTagService.selectList(SqlQuery.from(StdTag.class));
        stdTagList.forEach(stdTag -> {
            if (StringUtils.hasText(stdTag.getContent())) {
                String[] tagArr = stdTag.getContent().split("\\r?\\n");
                for (String tag : tagArr) {
                    if (StringUtils.hasText(tag)) {
                        if (content.contains(tag)) {
                            //给档案打标签  默认未通过
                            TagLib tagLib = new TagLib();
                            tagLib.setCtype(type);//国家标签
                            tagLib.setStType(stdTag.getLabelName2nd());
                            tagLib.setTagName(tag);
                            tagLibs.add(tagLib);
                        }
                    }
                }
            }
        });
        int count = tagLibs.size();
        if (count > 0) {
            addTagLibArchives(tagLibs, formDataId, formDefinitionId, type);
        }
        return count;
    }

    public List<TagLib> duiji(String tagName) {
        List<TagLib> tagLibs2 = new ArrayList<>();
        List<TagLib> tagLibs = commonMapper.selectByQuery(SqlQuery.from(TagLib.class).equal(TagLibInfo.CTYPE, "gt").equal(TagLibInfo.LEAF, "1"));
        for (TagLib tagLib : tagLibs) {
            String[] aa = tagLib.getTagDescribe().split("\n");
            List<String> list = Arrays.asList(aa);
            if (list.contains(tagName)) {
                tagLibs2.add(tagLib);
            }
        }
        return tagLibs2;
    }

    /*根据关键词查标签返回档案数据*/
    public List<TagLibArchive> getArchivesByKeyword(String keyword) {
        SqlQuery sqlQuery = SqlQuery.from(TagLibArchive.class).like(TagLibArchiveInfo.TAGNAME, keyword).isNotNull(TagLibArchiveInfo.ARCHIVECODE).orderBy(TagLibArchiveInfo.ARCHIVECODE).orderByDesc(TagLibArchiveInfo.CREATEDATE);
        List<TagLibArchive> tagLibArchives = selectList(sqlQuery);
        return tagLibArchives;
    }
}
