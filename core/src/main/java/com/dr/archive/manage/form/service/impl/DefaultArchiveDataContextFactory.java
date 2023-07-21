package com.dr.archive.manage.form.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataContextFactory;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认实现类
 */
@Component
public class DefaultArchiveDataContextFactory extends WebApplicationObjectSupport implements ArchiveDataContextFactory {
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FormDefinitionService formDefinitionService;
    /**
     * TODO 线程上下文销毁
     * 线程变量放context上下文
     */
    private static final ThreadLocal<ArchiveDataContext> contextThreadLocal = new NamedThreadLocal<>("ArchiveDataContextFactoryHolder");

    /**
     * 根据参数创建上下文
     *
     * @param categoryId
     * @param formDefinitionId
     * @return
     */
    @Override
    public ArchiveDataContext buildContext(String categoryId, String formDefinitionId) {
        Category category = categoryService.selectById(categoryId);
        Fond fond = fondService.selectById(category.getFondId());
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);

        ArchiveDataContext parent = contextThreadLocal.get();
        String command = null;
        Map<String, Object> requestParams = new HashMap<>();
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                command = request.getParameter(ArchiveDataContext.COMMAND_KEY);
                if (parent == null) {
                    Enumeration<String> parameter = request.getParameterNames();
                    while (parameter.hasMoreElements()) {
                        String key = parameter.nextElement();
                        requestParams.put(key, request.getParameter(key));
                    }
                }
            }
        } catch (Exception e) {
            //非controller线程会抛出异常
        }
        ArchiveDataContext context = new ArchiveDataContext(parent, command, fond, category, formModel);
        context.addParams(requestParams);
        contextThreadLocal.set(context);
        return context;
    }
}
