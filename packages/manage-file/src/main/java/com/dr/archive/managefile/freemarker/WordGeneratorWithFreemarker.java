package com.dr.archive.managefile.freemarker;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @version V1.0
 * @Description:word导出帮助类 通过freemarker模板引擎来实现
 */
@EnableWebMvc
public class WordGeneratorWithFreemarker {
    private static Configuration configuration = null;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassicCompatible(true);
        configuration.setClassForTemplateLoading(
                WordGeneratorWithFreemarker.class,
                "/ftl");

    }

    private WordGeneratorWithFreemarker() {

    }

    public static void createDoc(Map<String, Object> dataMap, String templateName, OutputStream out) throws Exception {
        Template t = configuration.getTemplate(templateName);
        t.setEncoding("utf-8");
        WordHtmlGeneratorHelper.handleAllObject(dataMap);

        try {
            Writer w = new OutputStreamWriter(out,Charset.forName("utf-8"));
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}