package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.impl.CategoryServiceImpl;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.impl.FondServiceImpl;
import com.dr.archive.manage.form.entity.RebuildIndexRecord;
import com.dr.archive.tag.service.TagLibArchiveService;
import com.dr.archive.utilization.search.entity.EsTemplate;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.archive.utilization.search.service.EsTemplateService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.FormNameGenerator;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.form.util.Constants;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.cluster.storedscripts.DeleteStoredScriptRequest;
import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dr.framework.common.form.util.Constants.MODULE_NAME;

@Service
public class EsTemplateServiceImpl extends DefaultBaseService<EsTemplate> implements EsTemplateService {
    @Autowired
    protected RestHighLevelClient client;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    DataBaseService dataBaseService;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FormNameGenerator formNameGenerator;
    @Autowired
    ArchiveFileContentService fileContentService;
    @Autowired
    FondServiceImpl fondService;
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    TagLibArchiveService tagLibArchiveService;

    String ARCHIVE_INDEX_TEMPLATE_NAME = "archive_index_template";
    final Logger logger = LoggerFactory.getLogger(EsTemplateService.class);

    @Override
    public ResultEntity rebuildTemplate(String id) {
        //DeleteComposableIndexTemplateRequest request = new DeleteComposableIndexTemplateRequest("archive_index_template");
        EsTemplate esTemplate = selectById(id);
        if (esTemplate.getTemplateType().equals(TEMPLATE_TYPE_INDEX)) {
            //索引模板
            return ResultEntity.success(createIndexTemplate(esTemplate));
        } else {
            //查询模板
            return ResultEntity.success(createSearchTemplate(esTemplate));
        }
    }

    @Override
    public boolean deleteTemplate(String id) {
        boolean result = true;
        EsTemplate esTemplate = selectById(id);

        try {
            if (esTemplate.getTemplateType().equals(TEMPLATE_TYPE_INDEX)) {
                DeleteComposableIndexTemplateRequest request3 = new DeleteComposableIndexTemplateRequest(esTemplate.getTemplateName());
                client.indices().deleteIndexTemplate(request3, RequestOptions.DEFAULT);
            } else {
                DeleteStoredScriptRequest deleteStoredScriptRequest = new DeleteStoredScriptRequest(esTemplate.getTemplateName());
                client.deleteScript(deleteStoredScriptRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /*
     * 已存在索引则,进行复制,无索引,重新建*/
    @Override
    @Async
    public void rebuildIndex(String formDefinitionId) {
        //可能当前线程会没有当前登录人，没有的话当作参数传过来
        Person person = SecurityHolder.get().currentPerson();
        ResultEntity result = ResultEntity.success();
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        RebuildIndexRecord rebuildIndex = new RebuildIndexRecord();
        rebuildIndex.setFormCode(formModel.getFormCode());
        rebuildIndex.setFormName(formModel.getFormName());
        rebuildIndex.setRebuildType(StatusEntity.STATUS_DISABLE_STR);
        rebuildIndex.setStartTime(System.currentTimeMillis());
        rebuildIndex.setCreatePersonName(person.getUserName());
        //判断数据库中是否真实存在该表 不存在则退出操作
        if (!dataBaseService.tableExist(formNameGenerator.genTableName(formModel), Constants.MODULE_NAME)) {
            result.setCode("202");
            result.setMessage("表结构不存在，不可重建es索引！");
            rebuildIndex.setStatus(StatusEntity.STATUS_DISABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
            //return result;
        }
        try {
            //索引名称为 archive_ + 表单编码
            String indexName = "archive_" + formModel.getFormCode();

            //判断是否已存在索引，则复制索引
            //复制逻辑:es中是不允许对索引修改,所以先创建一个新索引,将旧索引数据移动到新索引中,然后再删除旧索引,再将新索引的数据迁移到旧索引名下
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            if (client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName + "_bar");
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                //旧索引数据复制到新索引上
                ReindexRequest reindexRequest = new ReindexRequest();
                reindexRequest.setSourceIndices(indexName);
                reindexRequest.setDestIndex(indexName + "_bar");
                client.reindex(reindexRequest, RequestOptions.DEFAULT);
                //删除旧索引
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
                client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                //重建旧索引
                CreateIndexRequest createIndexRequest2 = new CreateIndexRequest(indexName);
                client.indices().create(createIndexRequest2, RequestOptions.DEFAULT);
                //将新索引数据复制到旧索引上
                ReindexRequest reindexRequest2 = new ReindexRequest();
                reindexRequest2.setSourceIndices(indexName + "_bar");
                reindexRequest2.setDestIndex(indexName);
                client.reindex(reindexRequest2, RequestOptions.DEFAULT);
                //删除新索引
                DeleteIndexRequest deleteIndexRequest2 = new DeleteIndexRequest(indexName + "_bar");
                client.indices().delete(deleteIndexRequest2, RequestOptions.DEFAULT);
            } else {
                //无索引 则 直接创建索引,并同步表单数据
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                ComposableIndexTemplateExistRequest cerequest = new ComposableIndexTemplateExistRequest(ARCHIVE_INDEX_TEMPLATE_NAME);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                //同步表单数据
                addDocument(formModel, formDefinitionId, indexName);
            }
            rebuildIndex.setStatus(StatusEntity.STATUS_ENABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
        } catch (IOException e) {
            result.setCode("201");
            result.setMessage(e.getMessage());
            rebuildIndex.setStatus(StatusEntity.STATUS_DISABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
        }

        //添加重建索引记录
        getCommonService().insert(rebuildIndex);
        //return result;
    }

    /*
     * 删除,重建
     */
    @Override
    @Async
    public void rebuildIndex2(String formDefinitionId) {
        //可能当前线程会没有当前登录人，没有的话当作参数传过来
        Person person = SecurityHolder.get().currentPerson();
        ResultEntity result = ResultEntity.success();
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        RebuildIndexRecord rebuildIndex = new RebuildIndexRecord();
        rebuildIndex.setFormCode(formModel.getFormCode());
        rebuildIndex.setFormName(formModel.getFormName());
        rebuildIndex.setRebuildType(StatusEntity.STATUS_DISABLE_STR);
        rebuildIndex.setStartTime(System.currentTimeMillis());
        rebuildIndex.setCreatePersonName(person.getUserName());
        String indexName = "archive_" + formModel.getFormCode();
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        //判断数据库中是否真实存在该表 不存在则退出操作
        if (!dataBaseService.tableExist(formNameGenerator.genTableName(formModel), Constants.MODULE_NAME)) {
            result.setCode("202");
            result.setMessage("表结构不存在，不可重建es索引！");
            rebuildIndex.setStatus(StatusEntity.STATUS_DISABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
            //return result;
        }
        try {
            boolean indexExist = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            //如果索引存在
            if (indexExist) {
                //删除旧索引
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
                client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            }
            //如果索引没存在则直接创建索引
            client.indices().create(new CreateIndexRequest(indexName), RequestOptions.DEFAULT);
            //同步表单数据
            addDocument(formModel, formDefinitionId, indexName);
            rebuildIndex.setStatus(StatusEntity.STATUS_ENABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
        } catch (IOException e) {
            result.setCode("201");
            result.setMessage(e.getMessage());
            rebuildIndex.setStatus(StatusEntity.STATUS_DISABLE_STR);
            rebuildIndex.setEndTime(System.currentTimeMillis());
        }
        //添加重建索引记录
        getCommonService().insert(rebuildIndex);
        //return result;
    }


    private void addDocument(FormModel formModel, String formDefinitionId, String indexName) {
        Relation relation = dataBaseService.getTableInfo(formNameGenerator.genTableName(formModel), MODULE_NAME);
        if (null != relation) {
            SqlQuery sqlQuery = SqlQuery.from(relation).setReturnClass(HashMap.class);
            List<Map> list = commonMapper.selectByQuery(sqlQuery);
            for (Map map : list) {
                Fond fond = fondService.findFondByCode(map.get("FOND_CODE").toString());
                if (null != fond) {
                    Category category = categoryService.findCategoryByCode(map.get("CATE_GORY_CODE").toString(), fond.getId());
                    map.put("CONTENT", fileContentService.getFileContentsByRefId(map.get("id").toString()));
                    map.put("formType", formModel.getFormType());
                    map.put("FOND_NAME", fond.getName());
                    map.put("CATEGORY_NAME", category.getName());
                    map.put("formDefinitionId", formDefinitionId);
                    //取标签数据
                    List<Map> tags = tagLibArchiveService.selectByForm(map.get("id").toString(), formDefinitionId);
                    map.put("tag", tags);
                    //创建文档
                    IndexRequest request = new IndexRequest(indexName).id(map.get("id").toString());
                    try {
                        request.source(objectMapper.writeValueAsString(map), XContentType.JSON);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    try {
                        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean createIndexTemplate(EsTemplate esTemplate) {
        boolean ms = true;
        try {
            XContentParser parser = XContentType.YAML.xContent().createParser(NamedXContentRegistry.EMPTY, DeprecationHandler.THROW_UNSUPPORTED_OPERATION, esTemplate.getTemplateContent());
            ComposableIndexTemplate template = ComposableIndexTemplate.parse(parser);

            //发送创建模板请求
            PutComposableIndexTemplateRequest request2 = new PutComposableIndexTemplateRequest()
                    .name(esTemplate.getTemplateName())
                    .cause("档案元数据索引模板")
                    .indexTemplate(template);
            client.indices().putIndexTemplate(request2, RequestOptions.DEFAULT);
        } catch (IOException e) {
            ms = false;
            logger.warn("创建索引模板失败:{}", e.getMessage());
        }
        return ms;
    }

    private boolean createSearchTemplate(EsTemplate esTemplate) {
        boolean ms = true;
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("script").field("lang", "mustache");
            builder.field("source", esTemplate.getTemplateContent());
            builder.endObject().endObject();
            PutStoredScriptRequest request = new PutStoredScriptRequest()
                    .id(esTemplate.getTemplateName())
                    .content(BytesReference.bytes(builder), builder.contentType());
            client.putScript(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            ms = false;
        }
        return ms;
    }
}
