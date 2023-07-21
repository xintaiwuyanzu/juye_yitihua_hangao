package com.dr.archive.fuzhou.ofd.vo;

/**
 * @Title: WebOFDPermissionInfo
 * @Description 云阅读可控权限项名称及值定义
 * @Author zhaoJing
 * @Date Create on 2021/3/12 14:07
 */
public class WebOFDPermissionInfo {
    /* 本项为权限总开关 即：当只有本项为‘1’时,表示拥有权限信息,否则阅读器不解析权限 */
    private String permission;

    private String head;
    private String openFileBtn;
    private String saveBtn;
    private String printBtn;
    private String goToPageBox;
    private String zoomPageBox;
    private String pageLayoutBtn;
    private String handToolBtn;
    private String textSelectBtn;
    private String heightLightBtn;
    private String underlineBtn;
    private String pencilBtn;
    private String drawingAnnotBtn;
    private String commentsBtn;
    private String elecSignatureBtn;
    private String checkElecSignatureBtn;
    private String rotateSwitchBtn;
    private String exportBtn;

    private String outlineBtn;
    private String thumbnailBtn;
    private String commentListBtn;
    private String searchBtn;
    private String semanticTreeBtn;
    private String attachmentBtn;


    private String pageRange;
    private String timeModel;
    private String onlineReadBeginTime;
    private String onlineReadTime;


    /* 是否添加隐写溯源 1为添加，0不添加*/
    private String isImplicit;
    /* 是否显示隐写溯源 1为显示，0不显示 */
    private String isShowImplicit;
    /* 隐写溯源打印开关 1为打印，0不打印 */
    private String isPrintImplicit;

    /* 打印时间控制字段 */
    private String printTime;

    /* 遮盖与预遮盖权限控制字段 */
    private String areaSelectBtn;       /* 区域选择 - 遮盖相关功能开关 */
    private String cover;               /* 遮盖 */
    private String preCover;            /* 预遮盖 */


    private String autoPageDown;        /* 自动翻页功能权限 */


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getOpenFileBtn() {
        return openFileBtn;
    }

    public void setOpenFileBtn(String openFileBtn) {
        this.openFileBtn = openFileBtn;
    }

    public String getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(String saveBtn) {
        this.saveBtn = saveBtn;
    }

    public String getPrintBtn() {
        return printBtn;
    }

    public void setPrintBtn(String printBtn) {
        this.printBtn = printBtn;
    }

    public String getGoToPageBox() {
        return goToPageBox;
    }

    public void setGoToPageBox(String goToPageBox) {
        this.goToPageBox = goToPageBox;
    }

    public String getZoomPageBox() {
        return zoomPageBox;
    }

    public void setZoomPageBox(String zoomPageBox) {
        this.zoomPageBox = zoomPageBox;
    }

    public String getPageLayoutBtn() {
        return pageLayoutBtn;
    }

    public void setPageLayoutBtn(String pageLayoutBtn) {
        this.pageLayoutBtn = pageLayoutBtn;
    }

    public String getHandToolBtn() {
        return handToolBtn;
    }

    public void setHandToolBtn(String handToolBtn) {
        this.handToolBtn = handToolBtn;
    }

    public String getTextSelectBtn() {
        return textSelectBtn;
    }

    public void setTextSelectBtn(String textSelectBtn) {
        this.textSelectBtn = textSelectBtn;
    }

    public String getHeightLightBtn() {
        return heightLightBtn;
    }

    public void setHeightLightBtn(String heightLightBtn) {
        this.heightLightBtn = heightLightBtn;
    }

    public String getUnderlineBtn() {
        return underlineBtn;
    }

    public void setUnderlineBtn(String underlineBtn) {
        this.underlineBtn = underlineBtn;
    }

    public String getPencilBtn() {
        return pencilBtn;
    }

    public void setPencilBtn(String pencilBtn) {
        this.pencilBtn = pencilBtn;
    }

    public String getDrawingAnnotBtn() {
        return drawingAnnotBtn;
    }

    public void setDrawingAnnotBtn(String drawingAnnotBtn) {
        this.drawingAnnotBtn = drawingAnnotBtn;
    }

    public String getCommentsBtn() {
        return commentsBtn;
    }

    public void setCommentsBtn(String commentsBtn) {
        this.commentsBtn = commentsBtn;
    }

    public String getElecSignatureBtn() {
        return elecSignatureBtn;
    }

    public void setElecSignatureBtn(String elecSignatureBtn) {
        this.elecSignatureBtn = elecSignatureBtn;
    }

    public String getCheckElecSignatureBtn() {
        return checkElecSignatureBtn;
    }

    public void setCheckElecSignatureBtn(String checkElecSignatureBtn) {
        this.checkElecSignatureBtn = checkElecSignatureBtn;
    }

    public String getRotateSwitchBtn() {
        return rotateSwitchBtn;
    }

    public void setRotateSwitchBtn(String rotateSwitchBtn) {
        this.rotateSwitchBtn = rotateSwitchBtn;
    }

    public String getExportBtn() {
        return exportBtn;
    }

    public void setExportBtn(String exportBtn) {
        this.exportBtn = exportBtn;
    }

    public String getOutlineBtn() {
        return outlineBtn;
    }

    public void setOutlineBtn(String outlineBtn) {
        this.outlineBtn = outlineBtn;
    }

    public String getThumbnailBtn() {
        return thumbnailBtn;
    }

    public void setThumbnailBtn(String thumbnailBtn) {
        this.thumbnailBtn = thumbnailBtn;
    }

    public String getCommentListBtn() {
        return commentListBtn;
    }

    public void setCommentListBtn(String commentListBtn) {
        this.commentListBtn = commentListBtn;
    }

    public String getSearchBtn() {
        return searchBtn;
    }

    public void setSearchBtn(String searchBtn) {
        this.searchBtn = searchBtn;
    }

    public String getSemanticTreeBtn() {
        return semanticTreeBtn;
    }

    public void setSemanticTreeBtn(String semanticTreeBtn) {
        this.semanticTreeBtn = semanticTreeBtn;
    }

    public String getAttachmentBtn() {
        return attachmentBtn;
    }

    public void setAttachmentBtn(String attachmentBtn) {
        this.attachmentBtn = attachmentBtn;
    }

    public String getPageRange() {
        return pageRange;
    }

    public void setPageRange(String pageRange) {
        this.pageRange = pageRange;
    }

    public String getTimeModel() {
        return timeModel;
    }

    public void setTimeModel(String timeModel) {
        this.timeModel = timeModel;
    }

    public String getOnlineReadBeginTime() {
        return onlineReadBeginTime;
    }

    public void setOnlineReadBeginTime(String onlineReadBeginTime) {
        this.onlineReadBeginTime = onlineReadBeginTime;
    }

    public String getOnlineReadTime() {
        return onlineReadTime;
    }

    public void setOnlineReadTime(String onlineReadTime) {
        this.onlineReadTime = onlineReadTime;
    }

    public String getIsShowImplicit() {
        return isShowImplicit;
    }

    public void setIsShowImplicit(String isShowImplicit) {
        this.isShowImplicit = isShowImplicit;
    }

    public String getIsPrintImplicit() {
        return isPrintImplicit;
    }

    public void setIsPrintImplicit(String isPrintImplicit) {
        this.isPrintImplicit = isPrintImplicit;
    }

    public String getIsImplicit() {
        return isImplicit;
    }

    public void setIsImplicit(String isImplicit) {
        this.isImplicit = isImplicit;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPreCover() {
        return preCover;
    }

    public void setPreCover(String preCover) {
        this.preCover = preCover;
    }

    public String getAreaSelectBtn() {
        return areaSelectBtn;
    }

    public void setAreaSelectBtn(String areaSelectBtn) {
        this.areaSelectBtn = areaSelectBtn;
    }

    public String getAutoPageDown() {
        return autoPageDown;
    }

    public void setAutoPageDown(String autoPageDown) {
        this.autoPageDown = autoPageDown;
    }
}
