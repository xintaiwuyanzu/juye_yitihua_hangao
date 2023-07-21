package com.dr.archive.model.entity;

import com.dr.framework.common.entity.StatusEntity;

/**
 * 抽象接口
 * 用来定义档案相关变量和基础类
 *
 * @author dr
 */
public interface ArchiveEntity extends StatusEntity<String> {
    /*
     *=====================================
     * 这里定义档案表单常用的变量名称
     *=====================================
     *
     */

    /**
     * 全宗编码
     */
    String COLUMN_FOND_CODE = "FOND_CODE";
    /**
     * 全宗名称
     */
    String COLUMN_FOND_NAME = "FOND_NAME";
    /**
     * 目录号
     */
    String COLUMN_CATALOGUE_CODE = "CATALOGUE_CODE";
    /**
     * 分类编码列
     */
    String COLUMN_CATEGORY_CODE = "CATE_GORY_CODE";
    /**
     * 分类名称
     */
    String COLUMN_CATEGORY_NAME = "CATE_GORY_NAME";
    /**
     * 机构或问题编码
     */
    String COLUMN_ORG_CODE = "ORG_CODE";
    /**
     * 档案号
     */
    String COLUMN_ARCHIVE_CODE = "ARCHIVE_CODE";
    /**
     * 数据来源类型
     */
    String COLUMN_SOURCE_TYPE = "SOURCE_TYPE";
    /**
     * 数据来源Id
     */
    String COLUMN_SOURCE_ID = "SOURCE_ID";
    /**
     * 题名
     */
    String COLUMN_TITLE = "TITLE";
    /**
     * 主题词
     */
    String COLUMN_KEY_WORDS = "KEY_WORDS";
    /**
     * 备注
     */
    String COLUMN_NOTE = "NOTE";
    /**
     * 年度
     */
    String COLUMN_YEAR = "VINTAGES";
    /**
     * 时间（卷内文件所属的起止年月）
     */
    String COLUMN_TIME = "TIME";
    /**
     * 文件形成日期
     */
    String COLUMN_FILETIME = "FILETIME";
    /**
     * 保管期限
     */
    String COLUMN_SAVE_TERM = "SAVE_TERM";
    /**
     * 密级
     */
    String COLUMN_SECURITY_LEVEL = "S_LEVEL";
    /**
     * 责任者
     */
    String COLUMN_DUTY_PERSON = "DUTY_PERSON";
    /**
     * 当前状态
     */
    String COLUMN_STATUS = STATUS_COLUMN_KEY;
    /**
     * 子状态，
     * 档案数据应该会有很多状态
     * 使用status存储大阶段状态
     * subStatus存储小阶段状态
     */
    String COLUMN_SUB_STATUS = "SUB_STATUS";
    /**
     * 开放范围
     * 1：开放
     * 2：不开放
     */
    String COLUMN_OPEN_SCOPE = "OPEN_SCOPE";

    /**
     * 案卷号
     */
    String COLUMN_AJH = "AJH";
    /**
     * 案卷档号
     */
    String COLUMN_AJDH = "AJDH";
    /**
     * 页号
     */
    String COLUMN_YH = "YH";
    /**
     * 页数
     */
    String COLUMN_YS = "YS";
    /**
     * 文号
     */
    String COLUMN_FILECODE = "FILECODE";
    /**
     * 件号
     */
    String COLUMN_JH = "JH";
    /**
     * 盒号
     */
    String COLUMN_HH = "HH";
    /**
     * 人名
     */
    String COLUMN_RM = "PERSONNAME";
    /**
     * 位置
     */
    String COLUMN_WZ = "POSITION";
    /**
     * 是否有原文信息
     */
    String COLUMN_YW_HAVE = "YW_HAVE";

    /**
     * 文件类型
     */

    String COLUMN_WJLX = "WJLX";

    /**
     * 件数
     */

    String COLUMN_WJFS = "JS";

    /**
     * 门类代码
     */

    String COLUMN_MLDM = "MLDM";
    /**
     * 立卷人
     */

    String COLUMN_LJR = "LJR";
    /**
     * 检查人
     */

    String COLUMN_JCR = "JCR";
    /**
     * 立卷时间
     */

    String COLUMN_LJSJ = "LJSJ";

    /**
     * 标签
     */
    String COLUMN_BQ = "TAG";

    /**
     * 当前所在机构ID,档案馆查询档案室移交后的档案数据有用
     */
    String COLUMN_ORGANISEID = "ORGANISEID";

    /**
     * 是否移交
     */
    String COLUMN_IS_TRANSFER = "IS_TRANSFER";

    /**
     * 原文数量
     */
    String COLUMN_FILE_COUNTS = "FILE_COUNTS";

    /**
     * 到期鉴定时间（最近一次的时间）
     */
    String COLUMN_SAVE_APPRAISAL_DATE = "SAVE_APPRAISAL_DATE";

    /**
     * 开放鉴定时间（最近一次的时间）
     */
    String COLUMN_OPEN_APPRAISAL_DATE = "OPEN_APPRAISAL_DATE";
    /**
     * 立档单位名称
     */
    String COLUMN_CRE_ORG_NAME = "CRE_ORG_NAME";
    /**
     * 业务行为
     */
    String COLUMN_BUSINESS_BEHAVIOR = "BUSINESS_BEHAVIOR";
    /**
     * 行为依据
     */
    String COLUMN_BEHAVIOR_BASIS = "BEHAVIOR_BASIS";
    /**
     * 行为描述
     */
    String COLUMN_BEHAVIOR_DESCRIPTION = "BEHAVIOR_DESCRIPTION";
    /**
     * 行为描述
     */
    String COLUMN_AUTHORITY_MANAGEMENT = "AUTHORITY_MANAGEMENT";
    /**
     * 事项类型
     */
    String COLUMN_TASK_TYPE = "TASK_TYPE";
    /**
     * 事项名称
     */
    String COLUMN_TASK_NAME = "TASK_NAME";
}
