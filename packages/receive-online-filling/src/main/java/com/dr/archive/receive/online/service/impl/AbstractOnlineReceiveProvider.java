package com.dr.archive.receive.online.service.impl;

import com.dr.archive.enums.CategoryType;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.model.query.CategoryFormQuery;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.service.OnLineReceiveProvider;
import com.dr.archive.receive.online.service.OnlineReceiveBatchContext;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * 在线接收抽象父类
 * <p>
 * <p>
 * //先接收交接单据
 * //在接收目录
 * //在接收原文
 * //在执行四性检测
 *
 * @author dr
 */
public abstract class AbstractOnlineReceiveProvider<T> implements OnLineReceiveProvider<T>, InitializingBean {
    static final int TIME_OUT = 10000;

    @Autowired
    protected ObjectMapper objectMapper;
    protected ObjectMapper xmlObjectMapper;
    @Autowired
    MappingJackson2XmlHttpMessageConverter converter;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected CategoryConfigService categoryConfigService;
    @Autowired
    protected FormDefinitionService formDefinitionService;

    /**
     * 根据年度构造数据在线接收上下文
     *
     * @param context
     * @param fondsIdentifier
     * @param categoryCode
     * @param year
     * @return
     */
    protected OnlineReceiveBatchContext.ReceiveDataContext newDataContext(OnlineReceiveBatchContext context, String fondsIdentifier, String categoryCode, String year) {
        OnlineReceiveBatchContext.ReceiveDataContext dataContext = context.newDataContext();

        //全宗
        Fond fond = fondService.findFondByCode(fondsIdentifier);
        dataContext.setFond(fond);

        //门类
        Category category = categoryService.findCategoryByCode(categoryCode, fond.getId());
        dataContext.setCategory(category);
        CategoryFormQuery query = new CategoryFormQuery();
        query.setCategoryId(category.getId());
        query.setCategoryCode(category.getCode());
        query.setFondId(fond.getId());
        query.setCategoryType(CategoryType.FILE.getValue());
        //TODO 这里条件有问题 ，在线接收暂时只考虑文件类的接收
        //表单
        CategoryConfig categoryConfig = categoryConfigService.getCategoryForms(query);
        FormDefinition formDefinition = (FormDefinition) formDefinitionService.selectFormDefinitionById(categoryConfig.getFileFormId());

        dataContext.setFormDefinition(formDefinition);
        //接收详情 并放到上下文中
        ArchiveBatchDetailReceiveOnline detailReceiveOnline = new ArchiveBatchDetailReceiveOnline();
        detailReceiveOnline.setId(UUID.randomUUID().toString());
        detailReceiveOnline.setBatchId(context.getBatchReceiveOnline().getId());
        dataContext.setDetailReceiveOnline(detailReceiveOnline);

        return dataContext;
    }

    /**
     * TODO,这里文件下载可以缓存的，还没想好怎么缓存文件
     * 从指定的网络地址下载文件到指定的文件夹内
     *
     * @param fileUrl
     * @param targetFile
     * @return
     */
    protected boolean downLoadFile(String fileUrl, File targetFile) {
        File parent = targetFile.getParentFile();
        if (parent.exists()) {
            parent.mkdirs();
        }
        try {
            URL url = new URL(fileUrl);
            return downLoadWithRetry(url, targetFile, 0);
        } catch (MalformedURLException e) {
            //不是网络地址,直接复制
            try {
                File sourceFile = new File(fileUrl);
                FileUtils.copyFile(sourceFile, targetFile);
                return true;
            } catch (IOException ioe) {
                return false;
            }

        }
    }

    /**
     * 重试五次下载文件
     *
     * @param url
     * @param targetFile
     * @param count
     * @return
     */
    private boolean downLoadWithRetry(URL url, File targetFile, int count) {
        if (count < 5) {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            try {
                FileUtils.copyURLToFile(url, targetFile, TIME_OUT, TIME_OUT);
                return true;
            } catch (IOException e) {
                return downLoadWithRetry(url, targetFile, count + 1);
            }
        }
        return false;
    }


    @Override
    public void afterPropertiesSet() {
        xmlObjectMapper = converter.getObjectMapper();
    }
}
