package com.dr.archive.fuzhou.configManager.service;

import com.dr.archive.fuzhou.configManager.bo.*;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.ApproveItemConfig;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.MetadataRuleTest;
import com.dr.archive.fuzhou.configManager.bo.itemconfig.SaveItemRule;
import com.dr.framework.rpc.ResultMapper;
import feign.Request;
import feign.Retryer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 智能归档配置中心客户端
 *
 * @author dr
 */
@ResultMapper
@FeignClient(url = "${fuzhou.config.base-url}", name = "configManager", configuration = ConfigManagerClient.ClientConfig.class)
public interface ConfigManagerClient {
    @Configuration
    class ClientConfig {

        /**
         * 配置请求重试
         */
        @Bean
        public Retryer feignRetryer() {
            return new Retryer.Default(200, TimeUnit.MILLISECONDS.toMillis(1), 0);
        }


        /**
         * 设置请求超时时间
         * 默认
         * public Options() {
         * this(10 * 1000, 60 * 1000);
         * }
         */
        @Bean
        Request.Options feignOptions() {
            return new Request.Options(1, TimeUnit.MILLISECONDS, 1, TimeUnit.MILLISECONDS, true);
        }
    }
    /**
     * 获取事项全部配置信息(政务服务系统)
     *
     * @param itemCode   事项Code 0100001005
     * @param orgCode    全宗号 12350100MB03153366
     * @param regionCode 区划代码 350100000000
     * @param version    版本 53
     * @param arcType    门类编码
     * @param qzNo       全宗号
     */
    @GetMapping("/getConfigDetailsByItemId")
    ApproveItemConfig getConfigDetailsByItemId(@RequestParam("itemCode") String itemCode, @RequestParam("orgCode") String orgCode, @RequestParam("regionCode") String regionCode, @RequestParam("version") String version, @RequestParam("arcType") String arcType, @RequestParam("qzNo") String qzNo);

    /*
     * 获取 四性检测条目 配置信息(除政务服务系统)
     * @param orgCode    全宗号 12350100MB03153366
     * @param regionCode 区划代码 350100000000
     * @param arcType    门类Id
     */
    @GetMapping("/getConfigDetails")
    List<ApproveItemConfig> getConfigDetails(@RequestParam("arcType") String arcType, @RequestParam("orgCode") String orgCode);

    /**
     * 获取所有可用的门类信息
     *
     * @return
     */
    @GetMapping("/archiveType/getArchiveType")
    List<CategoryInfo> getCategoryInfo();

    /**
     * 根据门类Id获取指定的配置信息
     *
     * @param id
     * @return
     */
    @GetMapping("/archiveType/getArchiveType")
    CategoryInfo getCategoryInfoById(@RequestParam("id") String id);

    /**
     * 根据门类 获取指定的配置信息
     *
     * @param code
     * @param classify
     * @return
     */
    @GetMapping("/metadata/getMetadata")
    List<FieldConfig> getMetadata(@RequestParam("code") String code, @RequestParam("classify") String classify);

    /**
     * 根据门类编码,标准 获取，年度获取所有字段
     *
     * @param code
     * @param classify 方案类型 1是电子档案 2是纸质化副本
     * @param standard 标准规范
     * @return
     */
    @GetMapping("metadata/getMetadata")
    List<FieldConfig> getCategoryMetadata(@RequestParam("code") String code, @RequestParam("classify") String classify, @RequestParam("standard") String standard);

    /**
     * 根据门类编码,当前时间 获取，获取所有字段
     *
     * @param code     门类编码
     * @param classify 1电子 2纸质
     * @param arrange  1案件 2案卷
     * @param time     当前时间
     * @return
     */
    @GetMapping("metadata/getMetadata")
    List<FieldConfig> getCategoryMetadata(@RequestParam("code") String code, @RequestParam("classify") String classify, @RequestParam("arrange") String arrange, @RequestParam("time") String time);

    /**
     * 根据门类code查询门类所有年度配置
     *
     * @param code
     * @return
     */
    @GetMapping("metadata/getArchiveTypeSchema")
    List<CategoryYear> getArchiveTypeYear(@RequestParam("code") String code);

    /**
     * 获取元数据类别信息
     *
     * @return
     */
    @GetMapping("/metadata/getMetadataType")
    List<MetaType> getMetadataType();


    /**
     * 获取指定全宗号的门类和元数据信息
     *
     * @param orgCode
     * @return
     */
    @GetMapping("/metadata/generalArchive")
    List<FondInfo> getMetadataConfig(@RequestParam("orgCode") String orgCode);

    /**
     * 获取四性检测配置信息
     * TODO 这个接口提供的参数不能用
     *
     * @param pageNum
     * @param pageSize
     * @param fondCode
     * @return
     */
    @GetMapping("/system/getTestItems")
    List<TestItem> getTestItems(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("fondCode") String fondCode);

    /**
     * 获取对应门类编码下保管期限配置
     *
     * @param typeCode 门类编码
     * @return 保管期限配置实体类
     */
    @GetMapping("/RetentionPeriod/getRetentionPeriod")
    List<SaveItemRule> getSaveTermConfig(String typeCode);

    /**
     * 根据年度和门类编码获取档号生成规则
     *
     * @param typeCode 门类编码
     * @param year     年度
     * @return
     */
    @GetMapping("/ArchivedTypeNumber/getArchivedTypeNumber")
    List<GenerationRules> getGenerationRules(@RequestParam("typeCode") String typeCode, @RequestParam("year") String year);

    /**
     * 根据机构编码获取保管期限
     *
     * @param orgCode
     * @return
     */
    @GetMapping("/RetentionPeriod/getRetentionPeriod")
    List<SaveTermBo> getSaveTermBo(@RequestParam("orgCode") String orgCode);

    /*
     * 获取方案元数据四性检测规则
     * (有条件的检测规则从这取,例 检测作者字段最大长度为10,条件 年度等于2019年)
     *
     * @param code 门类编码
     * @param classify 载体类型 1电子 2纸质化副本
     * @param arrange 1案件 2案卷
     * */
    @GetMapping("/metadata/metadataRuleTest")
    List<MetadataRuleTest> getMetadataRuleTest(@RequestParam("code") String code, @RequestParam("classify") String classify, @RequestParam("arrange") String arrange);
}

