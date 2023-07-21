package com.dr.archive.util;

import com.dr.framework.common.page.Page;

/**
 * 存放项目常量
 *
 * @author dr
 */
public class Constants {
    /**
     * 模块名称
     */
    public static final String MODULE_NAME = "archive";
    /**
     * 表名称默认前缀
     */
    public static final String TABLE_PREFIX = "ARCHIVE_";
    /**
     * 上传模板名前缀
     */
    public static final String TEMPLATE_PREFIX = "template/";

    public static final String DETECTION = "detection";

    public static final String UPLOAD_IMG = "D:/img/";

    public static final String UPLOAD_FILE = "D:/file/";

    public static final String UPLOAD_ADJUNCT = "D:/adjunct/";


    /**
     * 开启关闭状态（1：开启，0：关闭）
     */
    public static final Integer ON = 1;
    public static final Integer OFF = 0;

    /**
     * 是否（1：是，0：否）
     */
    public static final boolean YES = true;
    public static final boolean NO = false;
    /**
     * 默认页码default
     */
    public static final String DEFAULT_PAGE = "1";
    /**
     * 默认每页数据条数
     */
    public static final String DEFAULT_SIZE = Page.DEFAULT_PAGE_SIZE_STR;

    /**
     * 操作成功返回值
     */
    public static final String SUCCESS = "0";
    /**
     * 导出最大数据量-100000条
     */
    public static final double EXP_DATA_MAX = 100000;

    /**
     * 每个工作簿最大数据量-100000条
     */
    public static final double BOOK_DATA_MAX = 10;

    /**
     * 每个工作表最大数据量-20000条
     */
    public static final double SHEET_DATA_MAX = 20000;

    /**
     * 每个工作簿最大工作表个数-5个
     */
    public static final int BOOK_SHEET_SIZE = 5;

    /**
     * 机构类型档案馆
     */
    public static String ORG_TYPE_DAG = "dag";
    /**
     * 机构类型档案局
     */
    public static String ORG_TYPE_DAJ = "daj";
    /**
     * 机构类型档案室
     */
    public static String ORG_TYPE_DAS = "das";
    /**
     * 档案定期移交定时任务配置人员
     */
    public static final String ROLE_TRANSFER = "dqyj";
    /**
     * 档案开放鉴定定时任务配置人员
     */
    public static final String ROLE_KFJD = "KFJD";
    /**
     * 档案到期鉴定定时任务配置人员
     */
    public static final String ROLE_DQJD = "DQJD";
    /**
     * 附件refType类型
     * FILE_REFTYPE_FONDS_DESCRIPTIVE_FILE:全宗卷附件（refType）类型
     */
    public static final String FILE_REFTYPE_FONDS_DESCRIPTIVE_FILE = "fondsdescriptivefile";
    /**
     * 附件refType类型
     * 资料库
     */
    public static final String FILE_REFTYPE_MANAGE_FILE = "manageFile";
    /**
     * 档案车类型
     */
    public static final String FILE_REFTYPE_COMPILATION_FILE = "compilation";

    /**
     * 流程业务类型
     * FONDS_DESCRIPTIVE_FILE：全宗卷审批流程
     */
    public static final String PROCESS_TYPE_FONDS_DESCRIPTIVE_FILE = "fondsdescriptivefile";
    /**
     * 流程业务类型
     * PROCESS_TYPE_COMPILATION：编研内容审批流程
     */
    public static final String PROCESS_TYPE_COMPILATION = "compilation";
    /**
     * 流程业务类型
     * 电子档案长期保存 出库流程
     */
    public static final String PROCESS_TYPE_DELIVERY = "delivery";
    /**
     * 档案车类型
     */
    public static final String ARCHIVE_CAR_TYPE = "compilation";
    /**
     * 流程业务类型
     * FONDS_DESCRIPTIVE_FILE：全宗卷审批流程
     */
    public static final String PROCESS_TYPE_APPRAISAL = "appraisal";
    /**
     * 流程业务类型
     * PROCESS_TYPE_ARCHIVECHANGE：档案调整流程
     */
    public static final String PROCESS_TYPE_ARCHIVECHANGE = "archivechange";
}
