package com.dr.archive.transfer.bo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本信息元数据
 */
@JacksonXmlRootElement(localName = "base_info")
public class EArchiveBaseInfo {
    //标签
    @JacksonXmlProperty(localName = "TAG")
    private String tag;
    //是否有原文信息
    @JacksonXmlProperty(localName = "YW_HAVE")
    private String yw_have;
    //是否移交
    @JacksonXmlProperty(localName = "IS_TRANSFER")
    private String is_transfer;
    //数据来源Id
    @JacksonXmlProperty(localName = "SOURCE_ID")
    private String source_id;
    //案卷档号
    @JacksonXmlProperty(localName = "AJDH")
    private String ajdh;
    //更新人ID
    @JacksonXmlProperty(localName = "updatePerson")
    private String updatePerson;
    //密级
    @JacksonXmlProperty(localName = "S_LEVEL")
    private String s_level;
    //保管期限
    @JacksonXmlProperty(localName = "SAVE_TERM")
    private String save_term;
    //文件类型
    @JacksonXmlProperty(localName = "WJLX")
    private String wjlx;
    //主题词
    @JacksonXmlProperty(localName = "KEY_WORDS")
    private String key_words;
    //文号
    @JacksonXmlProperty(localName = "FILECODE")
    private String filecode;
    //机构或问题
    @JacksonXmlProperty(localName = "ORG_CODE")
    private String org_code;
    //价值
    @JacksonXmlProperty(localName = "HAVE_VALUE")
    private String have_value;
    //案卷号
    @JacksonXmlProperty(localName = "AJH")
    private String ajh;
    //责任者
    @JacksonXmlProperty(localName = "DUTY_PERSON")
    private String duty_person;
    //状态
    @JacksonXmlProperty(localName = "status_info")
    private String status_info;
    //排序
    @JacksonXmlProperty(localName = "order_info")
    private String order_info;
    //创建日期
    @JacksonXmlProperty(localName = "createDate")
    private String createDate;
    //开放鉴定时间
    @JacksonXmlProperty(localName = "OPEN_APPRAISAL_DATE")
    private String open_appraisal_date;
    //题名
    @JacksonXmlProperty(localName = "TITLE")
    private String title;
    //机构id
    @JacksonXmlProperty(localName = "ORGANISEID")
    private String organiseid;
    //全宗号
    @JacksonXmlProperty(localName = "FOND_CODE")
    private String fond_code;
    //原文数量
    @JacksonXmlProperty(localName = "FILE_COUNTS")
    private String file_counts;
    //创建人ID
    @JacksonXmlProperty(localName = "createPerson")
    private String createPerson;
    //页号
    @JacksonXmlProperty(localName = "YH")
    private String yh;
    //状态
    @JacksonXmlProperty(localName = "SUB_STATUS")
    private String sub_status;
    //分类编码
    @JacksonXmlProperty(localName = "CATE_GORY_CODE")
    private String cate_gory_code;
    //档号
    @JacksonXmlProperty(localName = "ARCHIVE_CODE")
    private String archive_code;
    //文件形成日期
    @JacksonXmlProperty(localName = "FILETIME")
    private String filetime;
    //数据来源类型
    @JacksonXmlProperty(localName = "SOURCE_TYPE")
    private String source_type;
    //开放范围
    @JacksonXmlProperty(localName = "OPEN_SCOPE")
    private String open_scope;
    //年度
    @JacksonXmlProperty(localName = "VINTAGES")
    private String vintages;
    //备注
    @JacksonXmlProperty(localName = "NOTE")
    private String note;
    //到期鉴定时间
    @JacksonXmlProperty(localName = "SAVE_APPRAISAL_DATE")
    private String save_appraisal_date;
    //目录号
    @JacksonXmlProperty(localName = "CATALOGUE_CODE")
    private String catalogue_code;
    //更新日期
    @JacksonXmlProperty(localName = "updateDate")
    private String updateDate;



    @JacksonXmlProperty(localName = "file")
    @JacksonXmlElementWrapper(localName = "fileset")
    private List<File> files = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        XmlMapper mapper = new XmlMapper();
        EArchiveBaseInfo baseInfo = new EArchiveBaseInfo();
        baseInfo.files.add(new File());
        String xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseInfo);
        System.out.println(xml);
    }

    /**
     * 目录内电子文件归档包的元数据，可用”id”属性值区分
     */
    @JacksonXmlRootElement(localName = "file")
    public static class File {
        @JacksonXmlProperty(localName = "file_code")
        private String file_code;
        @JacksonXmlProperty(localName = "file_standard_name")
        private String file_standard_name;
        @JacksonXmlProperty(localName = "file_actual_name")
        private String file_actual_name;

        @JacksonXmlProperty(localName = "format_information")
        private String format_information;

        @JacksonXmlProperty(localName = "computer_file_name")
        private String computer_file_name;

        @JacksonXmlProperty(localName = "computer_file_size")
        private String computer_file_size;

        @JacksonXmlProperty(localName = "computer_file_creation_time")
        private String computer_file_creation_time;

        public String getFile_code() {
            return file_code;
        }

        public void setFile_code(String file_code) {
            this.file_code = file_code;
        }

        public String getFile_standard_name() {
            return file_standard_name;
        }

        public void setFile_standard_name(String file_standard_name) {
            this.file_standard_name = file_standard_name;
        }

        public String getFile_actual_name() {
            return file_actual_name;
        }

        public void setFile_actual_name(String file_actual_name) {
            this.file_actual_name = file_actual_name;
        }

        public String getFormat_information() {
            return format_information;
        }

        public void setFormat_information(String format_information) {
            this.format_information = format_information;
        }

        public String getComputer_file_name() {
            return computer_file_name;
        }

        public void setComputer_file_name(String computer_file_name) {
            this.computer_file_name = computer_file_name;
        }

        public String getComputer_file_size() {
            return computer_file_size;
        }

        public void setComputer_file_size(String computer_file_size) {
            this.computer_file_size = computer_file_size;
        }

        public String getComputer_file_creation_time() {
            return computer_file_creation_time;
        }

        public void setComputer_file_creation_time(String computer_file_creation_time) {
            this.computer_file_creation_time = computer_file_creation_time;
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getYw_have() {
        return yw_have;
    }

    public void setYw_have(String yw_have) {
        this.yw_have = yw_have;
    }

    public String getIs_transfer() {
        return is_transfer;
    }

    public void setIs_transfer(String is_transfer) {
        this.is_transfer = is_transfer;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getAjdh() {
        return ajdh;
    }

    public void setAjdh(String ajdh) {
        this.ajdh = ajdh;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getS_level() {
        return s_level;
    }

    public void setS_level(String s_level) {
        this.s_level = s_level;
    }

    public String getSave_term() {
        return save_term;
    }

    public void setSave_term(String save_term) {
        this.save_term = save_term;
    }

    public String getWjlx() {
        return wjlx;
    }

    public void setWjlx(String wjlx) {
        this.wjlx = wjlx;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getHave_value() {
        return have_value;
    }

    public void setHave_value(String have_value) {
        this.have_value = have_value;
    }

    public String getAjh() {
        return ajh;
    }

    public void setAjh(String ajh) {
        this.ajh = ajh;
    }

    public String getDuty_person() {
        return duty_person;
    }

    public void setDuty_person(String duty_person) {
        this.duty_person = duty_person;
    }

    public String getStatus_info() {
        return status_info;
    }

    public void setStatus_info(String status_info) {
        this.status_info = status_info;
    }

    public String getOrder_info() {
        return order_info;
    }

    public void setOrder_info(String order_info) {
        this.order_info = order_info;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOpen_appraisal_date() {
        return open_appraisal_date;
    }

    public void setOpen_appraisal_date(String open_appraisal_date) {
        this.open_appraisal_date = open_appraisal_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganiseid() {
        return organiseid;
    }

    public void setOrganiseid(String organiseid) {
        this.organiseid = organiseid;
    }

    public String getFond_code() {
        return fond_code;
    }

    public void setFond_code(String fond_code) {
        this.fond_code = fond_code;
    }

    public String getFile_counts() {
        return file_counts;
    }

    public void setFile_counts(String file_counts) {
        this.file_counts = file_counts;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getYh() {
        return yh;
    }

    public void setYh(String yh) {
        this.yh = yh;
    }

    public String getSub_status() {
        return sub_status;
    }

    public void setSub_status(String sub_status) {
        this.sub_status = sub_status;
    }

    public String getCate_gory_code() {
        return cate_gory_code;
    }

    public void setCate_gory_code(String cate_gory_code) {
        this.cate_gory_code = cate_gory_code;
    }

    public String getArchive_code() {
        return archive_code;
    }

    public void setArchive_code(String archive_code) {
        this.archive_code = archive_code;
    }

    public String getFiletime() {
        return filetime;
    }

    public void setFiletime(String filetime) {
        this.filetime = filetime;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getOpen_scope() {
        return open_scope;
    }

    public void setOpen_scope(String open_scope) {
        this.open_scope = open_scope;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSave_appraisal_date() {
        return save_appraisal_date;
    }

    public void setSave_appraisal_date(String save_appraisal_date) {
        this.save_appraisal_date = save_appraisal_date;
    }

    public String getCatalogue_code() {
        return catalogue_code;
    }

    public void setCatalogue_code(String catalogue_code) {
        this.catalogue_code = catalogue_code;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
