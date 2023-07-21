package com.dr.archive.manage.template.controller;

import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.entity.CompilationTemplateInfo;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/compilationTemplate")
public class CompilationTemplateController extends BaseServiceController<CompilationTemplateService, CompilationTemplate> {

    @Override
    protected SqlQuery<CompilationTemplate> buildPageQuery(HttpServletRequest request, CompilationTemplate entity) {
        return SqlQuery.from(CompilationTemplate.class)
                .like(CompilationTemplateInfo.TEMPLATENAME, entity.getTemplateName())
                .equal(CompilationTemplateInfo.TEMPLATETYPECODE, entity.getTemplateTypeCode())
                .equal(CompilationTemplateInfo.STATUS, entity.getStatus())
                .equal(CompilationTemplateInfo.BUSINESSTYPE, entity.getBusinessType())
                .equal(CompilationTemplateInfo.CREATEPERSON, SecurityHolder.get().currentPerson().getId())
                .orderByDesc(CompilationTemplateInfo.CREATEDATE);
    }

    /**
     * 根据模板分类id查询全部模板
     *
     * @param request
     * @param entity
     * @return
     */
    @RequestMapping("/findAll")
    public ResultEntity findByIdAll(HttpServletRequest request, CompilationTemplate entity) {
        SqlQuery<CompilationTemplate> sqlQuery = buildPageQuery(request, entity);
        Object result = service.selectList(sqlQuery);
        return ResultEntity.success(result);
    }

    /**
     * 根据模板分类id,当前登陆人的id查询全部模板
     *
     * @param request
     * @param entity
     * @return
     */
    @RequestMapping("/findByPersonId")
    public ResultEntity findByPersonId(HttpServletRequest request, CompilationTemplate entity, @Current Person person) {
        SqlQuery<CompilationTemplate> sqlQuery = buildPageQuery(request, entity).equal(CompilationTemplateInfo.CREATEPERSON, person.getId());
        Object result = service.selectList(sqlQuery);
        return ResultEntity.success(result);
    }

    @RequestMapping("/preview")
    public ResultEntity preview(HttpServletRequest request) {
        String compilationContent = request.getParameter("compilationContent");
        Assert.isTrue(!StringUtils.isEmpty(compilationContent), "模板内容不能为空！");
        return ResultEntity.success(service.preview(compilationContent));
    }

    /**
     * 重写分页方法，新曾根据用户查询
     *
     * @param request
     * @param pageIndex
     * @param pageSize
     * @param page
     * @return
     */
    @RequestMapping("/pageByPerson")
    public ResultEntity page(HttpServletRequest request, CompilationTemplate compilationTemplate, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize, @RequestParam(defaultValue = "true") boolean page, @Current Person person) {
        SqlQuery<CompilationTemplate> sqlQuery = buildPageQuery(request, compilationTemplate).equal(CompilationTemplateInfo.CREATEPERSON, person.getId()).orderBy(CompilationTemplateInfo.CREATEDATE);
        Object result = page ? service.selectPage(sqlQuery, pageIndex, pageSize) : service.selectList(sqlQuery);
        return ResultEntity.success(result);
    }

    @Override
    public ResultEntity<CompilationTemplate> insert(HttpServletRequest request, CompilationTemplate entity) {
        String content = "<p style=\\\"text-align:center;\\\">&lt;机构编码&gt; &nbsp;&lt;机构名称&gt; &lt;全宗号&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>默认模板：</p><p>&lt;内容开始&gt;</p><p>&lt;题名&gt;。</p><p>&lt;内容结束&gt;</p>";
        switch (entity.getTemplateTypeCode()) {
            case "common":
                content = "<p style=\\\"text-align:center;\\\">&lt;机构编码&gt; &nbsp;&lt;机构名称&gt; &lt;全宗号&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>默认模板：</p><p>&lt;内容开始&gt;</p><p>&lt;题名&gt;。</p><p>&lt;内容结束&gt;</p>";
                break;
            case "dalyxgdxsl":   //利用情况
                content = "<p style=\\\"text-align:center;\\\">&lt;机构编码&gt; &nbsp;&lt;机构名称&gt; &lt;全宗号&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>利用情况模板：</p><p>&lt;内容开始&gt;</p><p>&lt;题名&gt;。</p><p>&lt;内容结束&gt;</p>";
                break;
            case "dsj":         //大事记
                content = "<p style=\"text-align:center;\">&lt;标题&gt;</p><p>大事记模板：</p><p>&lt;内容开始&gt;</p><p>&lt;题名&gt;。</p><p>&lt;内容结束&gt;</p>";
                break;
            case "dakfykzqk":   //开放与控制
                content = "<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;全宗号&gt;&lt;标题&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>&lt;内容开始&gt;</p><p>&lt;年度&gt;&lt;题名&gt;</p><p>&lt;内容结束&gt;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;当前日期&gt;</p><p>&nbsp;</p><p>&nbsp;</p>";
                break;
            case "dajctjtz":    //统计台账
                content = "<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;全宗号&gt;&lt;标题&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>&lt;内容开始&gt;</p><p>&lt;分类名称&gt; &nbsp;： &nbsp;&lt;数量&gt;</p><p>&lt;内容结束&gt;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;当前日期&gt;</p>";
                break;
            case "bgqxgd":       //归档情况
                content = "<p style=\\\"text-align:center;\\\">&lt;机构编码&gt; &nbsp;&lt;机构名称&gt; &lt;全宗号&gt; (&lt;起始年度&gt;~&lt;终止年度&gt;）</p><p>归档情况模板：</p><p>&lt;内容开始&gt;</p><p>&lt;题名&gt;。</p><p>&lt;内容结束&gt;</p>";
                break;
            case "qzzn":         //全宗指南
                content = "<p style=\"text-align:center;\">&nbsp;</p><p style=\"text-align:center;\"><strong>&lt;机构编码&gt; &lt;机构名称&gt; 全宗指南 &lt;起始年度&gt;～&lt;终止年度&gt;</strong></p><p style=\"margin-left:0cm;\">一、全宗来源简况</p><figure class=\"table\"><table><tbody><tr><td><p style=\"text-align:center;\">全宗构成者</p><p style=\"text-align:center;\">形成</p></td><td>&nbsp;</td></tr><tr><td><p style=\"text-align:center;\">全宗构成者</p><p style=\"text-align:center;\">主要职能</p></td><td>&nbsp;</td></tr><tr><td><p style=\"text-align:center;\">全宗构成者</p><p style=\"text-align:center;\">曾用名称</p></td><td>&nbsp;</td></tr><tr><td><p style=\"text-align:center;\">全宗管理机构</p></td><td><p style=\"text-align:center;\"><strong>&lt;机构名称&gt;</strong></p></td></tr><tr><td><p style=\"text-align:center;\">全宗档案数量</p></td><td><p style=\"text-align:center;\">&nbsp;</p></td></tr><tr><td><p style=\"text-align:center;\">档案接收情况</p></td><td><p>&lt;在线归档接收数量&gt;&nbsp;</p><p>&lt;离线归档接收数量&gt;</p><p>&lt;移交数量&gt;</p><p>&lt;延期移交数量&gt;</p><p>&lt;移交接收数量&gt;</p></td></tr></tbody></table></figure><ol start=\"2\"><li>档案内容与成分介绍</li></ol><p>1.党务（群）类：市委、市委组织部对旅游局干部任免，表彰先进的通知，通报，党、团委工作计划、总结、会议记录，民主生活会有关材料。</p><p>2.领导类：局务会议、全市旅游工作会议，专题会议等重要工作会议形成的纪要，全局年度工作思路，计划、总结，上级部门关于表彰先进的通知，通报。</p><p>3.人事类：机构改革、人事任免、录用、工资，考核、奖惩、离退休、死亡、抚恤、护理费、职务资格、调动、编制、考核、递补的通知、通报等。</p><p>4.行政秘书类：机关各项工作制度、规章、行政事务、档案、接待、督查、政府信息公开、人大建议政协提案，国有资产、采购合同等通知、意见、答复、协议。</p><p>5.发展规划类：旅游规划及编制、旅游绩效评估，旅游调査统计，旅游数据分析报告，旅游专项资金申报，项目建设的请示、批示，通知、意见等。</p><p>6.市场开发类：A级景区评定、复核，星级乡村旅游经营单位评定，旅游景区提升整改、旅游节庆活动，旅游宣传广告投放，旅游推介宣传，旅游交通标志的各项请示，批复、通知、公告。</p><p>7.行业管理类：旅游行业安全生产，旅行社，星级饭店导游员，假日旅游，文明旅游，文明城市创建等相关工作形成的请示、报告、批示，批复，通知、会议纪要等。</p><p>8.星评委类：星级饭店评定、复核、注销的请示，通知。</p><p>9.计财类：财政经费预决算，旅游专项资金申请、拨付管理，财政贴息补助、旅游专项资金管理形成的请示、批复通知，国有固定资产，采购招投标，住房补贴的通知及其他材料。</p><p>10.质监所类：旅游执法检查，市场监管，行政处罚的函、整改通知等。</p><p>11.服务中心类：旅游信息、智慧旅游、旅游网站的请示、批复、意见、通知。　　　　　</p><p>三、检索查阅注意事项</p><p>1.检索注意事项</p><p>该全宗共有案卷级目录<span style=\"background-color:yellow;\"> 条</span>，文件目录<span style=\"background-color:yellow;\"> 条</span>，数字化扫描完成<span style=\"background-color:yellow;\">&nbsp; 画幅</span>，编有机读目录&nbsp; <span style=\"background-color:yellow;\">条</span>，无纸质档案缩微副本和非纸质档案副本。</p><p>2.查阅注意事项</p><p>该全宗档案保存完好，无遗失、销毁情况，案卷组卷、装订、保管良好。全宗起讫时间为1980年至2013年。该全宗档案按照年度——问题——保管期限分类整理，保管期限永久351卷，长期392卷。。该全宗已经过开放鉴定，已开放档案 <span style=\"background-color:yellow;\">件</span>。</p><p>&nbsp;</p>";
                break;
            case "dabyycbqk":     //编研情况
                content = "<p style=\"text-align:center;\"><span style=\"font-size:22px;\">&lt;标题&gt; &nbsp;(&lt;起始年度&gt;~&lt;终止年度&gt;）</span></p><p style=\"text-align:center;\"><span style=\"font-size:22px;\">作者：&lt;作者姓名&gt; &nbsp; &nbsp;来源：&lt;机构名称&gt; &nbsp;全宗号：&lt;全宗号&gt;</span></p><p><span style=\"font-size:22px;\">编研情况模板：</span></p><p><span style=\"font-size:22px;\">&lt;内容开始&gt;</span></p><p><span style=\"font-size:22px;\">&lt;题名&gt;</span></p><p><span style=\"font-size:22px;\">&lt;内容结束&gt;</span></p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;当前日期&gt;</p>";
                break;
            case "DSJ":
            case "ZZJGYG":
            case "ZCSZHJ":
            case "ZTHB":
                content = "<p style=\"text-align:center;\"><span style=\"font-size:22px;\"><strong>&lt;标题&gt;</strong></span></p><p style=\"text-align:center;\"><span style=\"font-size:22px;\">作者：&lt;作者姓名&gt; &nbsp; &nbsp;来源：&lt;机构名称&gt;</span></p><p><span style=\"font-size:22px;\">&lt;内容开始&gt;</span></p><p><span style=\"font-size:22px;\">&lt;题名&gt; &nbsp;&lt;全宗号&gt; &nbsp;&lt;年度&gt; &nbsp; &lt;档号&gt;。</span></p><p><span style=\"font-size:22px;\">&lt;内容结束&gt;</span></p><p>&nbsp;</p><p>&nbsp;</p><p style=\"text-align:right;\"><span style=\"font-size:22px;\">&lt;当前日期&gt;</span></p>";
                break;
            case "QZJS":
                content = "<p style=\"text-align:center;\"><span style=\"font-size:22px;\"><strong>&lt;标题&gt;</strong></span></p><p style=\"text-align:center;\"><span style=\"font-size:22px;\">作者：&lt;作者姓名&gt; &nbsp; &nbsp;来源：&lt;机构名称&gt;</span></p><p><span style=\"font-size:22px;\">&lt;内容开始&gt;&lt;题名&gt;。</span></p><p><span style=\"font-size:22px;\">&lt;内容结束&gt;</span></p><p>&nbsp;</p><p>&nbsp;</p><p style=\"text-align:right;\"><span style=\"font-size:22px;\">&lt;当前日期&gt;</span></p>";
                break;
            case "PICTURE":
                content = "<p style=\"text-align:center;\"><span style=\"color:#067d17;\">&lt;图册开始&gt;</span></p><p style=\"text-align:center;\"><img src=\"{{url}}\">;</p><p style=\"text-align:center;\"><span style=\"color:#067d17;\">&lt;图册名称&gt;</span></p><p style=\"text-align:center;\"><span style=\"color:#067d17;\">&lt;图片描述&gt;</span></p><p style=\"text-align:center;\"><span style=\"color:#067d17;\">&lt;图册结束&gt;</span></p>";
                break;
        }
        entity.setCompilationContent(content);
        entity.setStatus(StatusEntity.STATUS_DISABLE_STR);
        return super.insert(request, entity);
    }

    /**
     * 根据模板类型查询模板list
     *
     * @param businessType
     * @return
     */
    @RequestMapping("/findTemplateByType")
    public ResultEntity findTemplateByType(String businessType, String templateTypeCode) {
        SqlQuery<CompilationTemplate> sqlQuery = SqlQuery.from(CompilationTemplate.class)
                .equal(CompilationTemplateInfo.BUSINESSTYPE, businessType)
                .equal(CompilationTemplateInfo.TEMPLATETYPECODE, templateTypeCode);
        List<CompilationTemplate> compilationTemplates = service.selectList(sqlQuery);
        return ResultEntity.success(compilationTemplates);
    }

}
