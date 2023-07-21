package com.dr.archive.manage.template.service.impl;


import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.entity.CompilationTemplateInfo;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompilationTemplateServiceImpl extends DefaultBaseService<CompilationTemplate> implements CompilationTemplateService {
    @Override
    public String preview(String compilationContent) {
        List<TemplateVo> templateVoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            templateVoList.add(new TemplateVo("内容部分" + (i + 1), "全宗" + (i + 1), "年度" + (i + 1), "档号" + (i + 1)));
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("archiveCarDetailList", templateVoList);
        dataMap.put("compilationTitle", "主题");
        return getHtml(dataMap, compilationContent);
    }

    @Override
    public String getHtml(Map<String, Object> dataMap, String compilationContent) {

        //这里的compilationContent包含汉字 需要处理  todo 这代码得优化
        compilationContent = replaceJJH(compilationContent);
        //当前登录人姓名
        dataMap.put("userName", SecurityHolder.get().currentPerson().getUserName());
        //当前登录人所属机构名称
        dataMap.put("organiseName", SecurityHolder.get().currentOrganise().getOrganiseName());
        //机构编码
        dataMap.put("organiseCode", SecurityHolder.get().currentOrganise().getOrganiseCode());
        //系统当前时间
        dataMap.put("currentYearMonthDay", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(compilationContent), "compilationTemplate");
        StringWriter writer = new StringWriter();
        try {
            mustache.execute(writer, dataMap).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    @Override
    public List<CompilationTemplate> getCompilationTemplateByCode(String templateCode) {
        Assert.isTrue(StringUtils.hasText(templateCode), "模板编码不能为空！");
        //TODO 这边先取全局，不分个人模板了
        return commonMapper.selectByQuery(SqlQuery.from(CompilationTemplate.class).equal(CompilationTemplateInfo.TEMPLATETYPECODE, templateCode));
    }


    @Override
    public List<CompilationTemplate> getCompilationTemplateByCodeAndPerson(String templateCode, String createPerson) {
        Assert.isTrue(StringUtils.hasText(templateCode), "模板编码不能为空！");
        return commonMapper.selectByQuery(SqlQuery.from(CompilationTemplate.class)
                .equal(CompilationTemplateInfo.TEMPLATETYPECODE, templateCode)
                .equal(CompilationTemplateInfo.CREATEPERSON, createPerson));
    }


    /**
     * 替换尖角号内容
     * @param compilationContent
     * @return
     */
    private String replaceJJH(String compilationContent){
        compilationContent= compilationContent
                //编研全宗卷 等需要的字段

                .replace("&lt;当前日期&gt;", "{{currentYearMonthDay}}")
                .replace("&lt;机构名称&gt;", "{{organiseName}}")
                .replace("&lt;机构编码&gt;", "{{organiseCode}}")
                .replace("&lt;起始年度&gt;", "{{vintagesStart}}")
                .replace("&lt;终止年度&gt;", "{{vintagesEnd}}")
                .replace("&lt;作者姓名&gt;", "{{userName}}")
                .replace("&lt;摘要&gt;", "{{compilationAbstract}}")
                .replace("&lt;标题&gt;", "{{compilationTitle}}")
                .replace("&lt;编研时间&gt;", "{{publishDate}}")

                .replace("&lt;内容开始&gt;", "{{#archiveCarDetailList}}")
                .replace("&lt;内容结束&gt;", "{{/archiveCarDetailList}}")
                .replace("&lt;题名&gt;", "{{title}}")
                .replace("&lt;全宗号&gt;", "{{fondName}}")
                .replace("&lt;年度&gt;", "{{year}}")
                .replace("&lt;档号&gt;", "{{archiveCode}}")
                //图册<
                .replace("&lt;图册开始&gt;", "{{#imgList}}")
                .replace("&lt;图册结束&gt;", "{{/imgList}}")
                .replace("&lt;图片地址&gt;", "{{url}}")
                .replace("&lt;图册名称&gt;", "{{name}}")
                .replace("&lt;图片描述&gt;", "{{description}}")
                //移交相关
                .replace("&lt;在线归档接收数量&gt;", "{{zxgdjssl}}")
                .replace("&lt;离线归档接收数量&gt;", "{{lxgdjssl}}")
                .replace("&lt;移交数量&gt;", "{{yjsl}}")
                .replace("&lt;延期移交数量&gt;", "{{yqyjsl}}")
                .replace("&lt;移交接收数量&gt;", "{{yjjssl}}")
                //台账
                .replace("&lt;分类名称&gt;", "{{categoryName}}")
                .replace("&lt;数量&gt;", "{{fileArchiveNum}}")
        ;
        return compilationContent;
    }

    static class TemplateVo {
        String title, fondName, year, archiveCode;

        public TemplateVo(String title, String fondName, String year, String archiveCode) {
            this.title = title;
            this.fondName = fondName;
            this.year = year;
            this.archiveCode = archiveCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFondName() {
            return fondName;
        }

        public void setFondName(String fondName) {
            this.fondName = fondName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getArchiveCode() {
            return archiveCode;
        }

        public void setArchiveCode(String archiveCode) {
            this.archiveCode = archiveCode;
        }
    }
}
