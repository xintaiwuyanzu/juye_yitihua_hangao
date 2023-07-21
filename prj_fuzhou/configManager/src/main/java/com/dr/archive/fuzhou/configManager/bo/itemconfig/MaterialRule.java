package com.dr.archive.fuzhou.configManager.bo.itemconfig;

/**
 * 所有审批事项材料清单
 * 规则父类
 *
 * @author dr
 */
public class MaterialRule {
    private String id;
    /**
     * 材料编码
     */
    private String code;
    /**
     * 配置ID
     */
    private String conId;
    /**
     * 材料名称
     */
    private String name;
    /**
     * "01",——共享方式（01：无条件共享，02：有条件共享，03：不共享）
     */
    private String shares;
    /**
     * 本部门共享（0否1是，仅在共享方式为02时有该字段）
     */

    private String bbm;

    private String sortOrder;

    /**
     * 审批事项申请材料清单
     */
    public static class ApplyMaterialRule extends MaterialRule {
        /**
         * 情形名称
         * "不分情形"
         */
        private String situation;

        private String sbzt;

        private String zwfwbmj;

        public String getSituation() {
            return situation;
        }

        public void setSituation(String situation) {
            this.situation = situation;
        }

        public String getSbzt() {
            return sbzt;
        }

        public void setSbzt(String sbzt) {
            this.sbzt = sbzt;
        }

        public String getZwfwbmj() {
            return zwfwbmj;
        }

        public void setZwfwbmj(String zwfwbmj) {
            this.zwfwbmj = zwfwbmj;
        }
    }


    /**
     * 审批事项过程材料
     * 审批过程中打印出来的材料
     */
    public static class PrintMaterialRule extends MaterialRule {

        /**
         * 申报主体共享（0否1是，仅在共享方式为02时有该字段）
         */
        private String sbzt;
        /**
         * 政务服务部门间共享（0否1是，仅在共享方式为02时有该字段）
         */
        private String zwfwbmj;

        public String getSbzt() {
            return sbzt;
        }

        public void setSbzt(String sbzt) {
            this.sbzt = sbzt;
        }

        public String getZwfwbmj() {
            return zwfwbmj;
        }

        public void setZwfwbmj(String zwfwbmj) {
            this.zwfwbmj = zwfwbmj;
        }
    }

    /**
     * 审批结果材料
     */
    public static class ResultMaterialRule extends MaterialRule {
        private String sbzt;
        private String zwfwbmj;

        public String getSbzt() {
            return sbzt;
        }

        public void setSbzt(String sbzt) {
            this.sbzt = sbzt;
        }

        public String getZwfwbmj() {
            return zwfwbmj;
        }

        public void setZwfwbmj(String zwfwbmj) {
            this.zwfwbmj = zwfwbmj;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getBbm() {
        return bbm;
    }

    public void setBbm(String bbm) {
        this.bbm = bbm;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }


}
