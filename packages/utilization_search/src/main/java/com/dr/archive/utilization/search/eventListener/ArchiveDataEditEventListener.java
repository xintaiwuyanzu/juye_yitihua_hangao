package com.dr.archive.utilization.search.eventListener;

import com.dr.archive.event.ArchiveDataAddEvent;
import com.dr.archive.event.ArchiveDataDeleteEvent;
import com.dr.archive.event.ArchiveDataEditEvent;
import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.utilization.search.service.ArchiveFileContentService;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.archive.utilization.search.service.impl.AbstractEsHandler;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.form.core.entity.FormField;
import com.dr.framework.common.form.core.model.FormData;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 监听档案数据修改，同步es数据库
 * <p>
 * TODO 这里创建和更新索引重复了，有时间需要详细梳理相关代码
 *
 * @author dr
 */
@Component
public class ArchiveDataEditEventListener extends AbstractEsHandler implements ArchiveDataPlugin, ArchiveDataListener {

    Logger logger = LoggerFactory.getLogger(ArchiveDataEditEventListener.class);
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    ArchiveFileContentService fileContentService;
    @Autowired
    EsDataService esDataService;

    @Async
    @Override
    @EventListener(value = ArchiveDataEditEvent.class)
    public void ArchiveDataEdit(ArchiveDataEditEvent archiveDataEditEvent) {
        try {
            String archive_index_code = indexName(archiveDataEditEvent.getFormDefinitionId());
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(archive_index_code);
            updateRequest.type("_doc");
            updateRequest.id(archiveDataEditEvent.getData().getId());
            updateRequest.doc(transXContentBuilder(archiveDataEditEvent.getData(), archiveDataEditEvent.getUpdateContent()));
            client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XContentBuilder transXContentBuilder(FormData formData, Boolean isUpdateContent) throws IOException {
        List<FormField> formFields = archiveFormService.findFieldList(formData.getFormDefinitionId());
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
        for (FormField formField : formFields) {
            xContentBuilder.field(formField.getFieldCode(), (Object) formData.getFieldValue(formField));
        }
        if (isUpdateContent) {
            String content = fileContentService.getFileContentsByRefId(formData.get(IdEntity.ID_COLUMN_NAME));
            xContentBuilder.field(EsDataService.FILE_CONTENT_KEY, content);
        }
        return xContentBuilder.endObject();
    }

    @Async
    @Override
    @EventListener(value = ArchiveDataDeleteEvent.class)
    public void removeFormData(ArchiveDataDeleteEvent event) {
        try {
            esDataService.removeIndexData(event.getFormDefinitionId(), event.getDataId());
        } catch (Exception e) {
            logger.warn("删除索引数据失败", e);
        }
    }

    /**
     * 表单数据添加
     *
     * @param event
     */
    @Async
    @Override
    @EventListener
    public void addFormData(ArchiveDataAddEvent event) {
        try {
            FormData data = event.getData();
            esDataService.addIndexData(event.getCategoryId(), event.getFormDefinitionId(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新添加通过plugin方式更新索引
     */
    @Override
    public FormData afterInsert(FormData data, ArchiveDataContext context) {
        try {
            esDataService.addIndexData(context.getCategory().getId(), data.getFormDefinitionId(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ArchiveDataPlugin.super.afterInsert(data, context);
    }

    /**
     * @param data
     * @param context
     * @return
     */
    @Async
    @Override
    public FormData afterUpdate(FormData data, ArchiveDataContext context) {
        try {
            esDataService.removeIndexData(data.getFormDefinitionId(), data.getId());
            esDataService.addIndexData(context.getCategory().getId(), data.getFormDefinitionId(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ArchiveDataPlugin.super.afterUpdate(data, context);
    }

//    @Async
//    @Override
//    public Long afterDelete(String archiveIds, ArchiveDataContext context) {
//        String[] ids = archiveIds.split(",");
//        for (String id : ids) {
//            try {
//                //删除索引
//                esDataService.removeIndexData(context.getFormModel().getId(), id);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return ArchiveDataPlugin.super.afterDelete(archiveIds, context);
//    }
}
