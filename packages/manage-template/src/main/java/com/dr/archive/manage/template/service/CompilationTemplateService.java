package com.dr.archive.manage.template.service;

import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

public interface CompilationTemplateService extends BaseService<CompilationTemplate> {
    /**
     * 预览模板
     *
     * @param compilationContent
     * @return
     */
    String preview(String compilationContent);

    /**
     * 根据模板，和数据生成html
     *
     * @param dataMap
     * @param compilationContent
     * @return
     */
    String getHtml(Map<String, Object> dataMap, String compilationContent);

    /**
     * 根据模板编码获取模板
     *
     * @param templateCode
     * @return
     */
    List<CompilationTemplate> getCompilationTemplateByCode(String templateCode);

    /**
     * 根据模板编码和人员获取模板
     *
     * @param templateCode
     * @return
     */
    List<CompilationTemplate> getCompilationTemplateByCodeAndPerson(String templateCode,String createPerson);
}
