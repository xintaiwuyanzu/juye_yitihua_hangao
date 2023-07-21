
package com.dr.archive.common.dzdacqbc.bo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 归档（移交）目录元数据
 * 这个是xml
 * <p>
 * <p>
 * 移交目录	必选	不可重复	容器型	——	由行政区划编码、全宗号和批次号组成
 *
 * @author dr
 */
@JacksonXmlRootElement(localName = "transfer_info")
public class TransferInfo {

    /**
     * 移交编码
     */
    @JacksonXmlProperty(localName = "system_code")
    private String systemCode;
    /**
     * 批次号	必选	不可重复	简单型	字符型	归档信息包移交批次号码（以数据交换日期的8位数字表示）	20180707
     */
    @JacksonXmlProperty(localName = "batch_name")
    private String batchName;
    /**
     * 数据交换时间	必选	不可重复	简单型	日期时间型	数据交换时间	2018-07-07 23:59:30
     */
    @JacksonXmlProperty(localName = "send_time")
    private String exchangeTime;
    /**
     * 归档信息包交换数量	必选	不可重复	简单型	字符型	归档信息包中所包含的业务数量	5
     */
    @JacksonXmlProperty(localName = "send_number")
    private String archivalNumber;
    /**
     * 归档信息包总字节数	必选	不可重复	简单型	字符型	所移交归档信息包总字节数	9624776704
     */
    @JacksonXmlProperty(localName = "send_size")
    private String sendSize;
    /**
     * 移交人	必选	不可重复	简单型	字符型	移交人姓名	王五
     */
    @JacksonXmlProperty(localName = "transactor")
    private String transactor;

    /**
     * 电子文件总数
     */
    @JacksonXmlProperty(localName = "File_Number")
    private String fileNumber;

    /**
     * 归档信息包交换目录
     */
    @JacksonXmlProperty(localName = "directory")
    @JacksonXmlElementWrapper(localName = "directories")
    private List<TransferDirectory> directories = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        XmlMapper mapper = new XmlMapper();
        TransferInfo transferInfo = new TransferInfo();
        transferInfo.directories.add(new TransferDirectory());
        String xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(transferInfo);
        System.out.println(xml);
    }

    /**
     * 目录内电子文件归档包的元数据，可用”id”属性值区分
     */
    @JacksonXmlRootElement(localName = "directory")
    public static class TransferDirectory {
        //全宗号
        @JacksonXmlProperty(localName = "fonds_identifier")
        private String fondsIdentifier;
        //立档单位名称
        @JacksonXmlProperty(localName = "fonds_constituting_unit_name")
        private String departmentName;
        //行政区划编码
        @JacksonXmlProperty(localName = "region_code")
        private String regionCode;
        //机构编码
        @JacksonXmlProperty(localName = "social_code")
        private String socialCode;
        //门类编码
        @JacksonXmlProperty(localName = "category_code")
        private String categoryCode;
        //年度
        @JacksonXmlProperty(localName = "year")
        private String year;
        //	电子文件号	必选	允许重复	简单型	字符型	归档信息包的标准命名名称，归档信息包标识的子节点	350100-0059-0100190012-2018-D30-000021
        @JacksonXmlProperty(localName = "unique_identifier")
        private String documentNumber;
        //事项编码
        @JacksonXmlProperty(localName = "task_code")
        private String taskCode;
        //事项版本号
        @JacksonXmlProperty(localName = "task_version")
        private String taskVersion;
        //办件名称   必选	不可重复	简单型	字符型	办件名称	关于某事项的审批
        @JacksonXmlProperty(localName = "project_name")
        private String projectName;
        //归档时间 必选	允许重复	简单型	日期时间型	归档具体时间，归档信息包标识的子节点	2018-02-21 17:21:30
        @JacksonXmlProperty(localName = "archive_time")
        private String archiveTime;
        //归档信息包字节数
        @JacksonXmlProperty(localName = "size")
        private String size;
        //软件环境，就是下载地址
        @JacksonXmlProperty(localName = "software_environment")
        private String softwareEnvironment;
        //数字摘要
        @JacksonXmlProperty(localName = "digital_summary")
        private String digitalSummary;
        //数字摘要算法
        @JacksonXmlProperty(localName = "digital_summarization_algorithm")
        private String digitalSummarizationAlgorithm;

        public String getFondsIdentifier() {
            return fondsIdentifier;
        }

        public void setFondsIdentifier(String fondsIdentifier) {
            this.fondsIdentifier = fondsIdentifier;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getSocialCode() {
            return socialCode;
        }

        public void setSocialCode(String socialCode) {
            this.socialCode = socialCode;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }

        public String getTaskCode() {
            return taskCode;
        }

        public void setTaskCode(String taskCode) {
            this.taskCode = taskCode;
        }

        public String getTaskVersion() {
            return taskVersion;
        }

        public void setTaskVersion(String taskVersion) {
            this.taskVersion = taskVersion;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getArchiveTime() {
            return archiveTime;
        }

        public void setArchiveTime(String archiveTime) {
            this.archiveTime = archiveTime;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSoftwareEnvironment() {
            return softwareEnvironment;
        }

        public void setSoftwareEnvironment(String softwareEnvironment) {
            this.softwareEnvironment = softwareEnvironment;
        }

        public String getDigitalSummary() {
            return digitalSummary;
        }

        public void setDigitalSummary(String digitalSummary) {
            this.digitalSummary = digitalSummary;
        }

        public String getDigitalSummarizationAlgorithm() {
            return digitalSummarizationAlgorithm;
        }

        public void setDigitalSummarizationAlgorithm(String digitalSummarizationAlgorithm) {
            this.digitalSummarizationAlgorithm = digitalSummarizationAlgorithm;
        }
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getArchivalNumber() {
        return archivalNumber;
    }

    public void setArchivalNumber(String archivalNumber) {
        this.archivalNumber = archivalNumber;
    }

    public String getSendSize() {
        return sendSize;
    }

    public void setSendSize(String sendSize) {
        this.sendSize = sendSize;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public List<TransferDirectory> getDirectories() {
        return directories;
    }

    public void setDirectories(List<TransferDirectory> directories) {
        this.directories = directories;
    }
}
