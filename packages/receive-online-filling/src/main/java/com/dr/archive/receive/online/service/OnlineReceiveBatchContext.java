package com.dr.archive.receive.online.service;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveReceiveOnlineSysManage;
import com.dr.archive.util.SessionMapContext;
import com.dr.framework.common.file.FileResource;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在线接收上下文
 *
 * @author dr
 */
public class OnlineReceiveBatchContext extends SessionMapContext {
    /**
     * 从哪个系统接收数据
     */
    private ArchiveReceiveOnlineSysManage sysManage;
    /**
     * 在线接收批次信息
     */
    private ArchiveBatchReceiveOnline batchReceiveOnline;
    /**
     * 请求信息
     */
    private HttpServletRequest request;
    /**
     * 返回信息
     */
    private HttpServletResponse response;
    /*
    * 接收的参数信息*/
    private ArchiveReceiveBo archiveReceiveBo;

    public ReceiveDataContext newDataContext() {
        return new ReceiveDataContext();
    }

    public static class ReceiveDataContext extends SessionMapContext {
        /**
         * 在线接收详情数据
         */
        private ArchiveBatchDetailReceiveOnline detailReceiveOnline;
        /**
         * 全宗
         */
        private Fond fond;
        /**
         * 门类
         */
        private Category category;
        /**
         * 表单定义
         */
        private FormDefinition formDefinition;
        /**
         * 表单数据
         */
        private FormData formData;
        /**
         * 附件数据
         */
        private List<FileResource> fileInfos;
        /**
         * 数据是否需要四性检测
         */
        private boolean needTest = true;

        public Fond getFond() {
            return fond;
        }

        public void setFond(Fond fond) {
            this.fond = fond;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public FormDefinition getFormDefinition() {
            return formDefinition;
        }

        public void setFormDefinition(FormDefinition formDefinition) {
            this.formDefinition = formDefinition;
        }

        public FormData getFormData() {
            return formData;
        }

        public void setFormData(FormData formData) {
            this.formData = formData;
        }

        public List<FileResource> getFileInfos() {
            return fileInfos;
        }

        public void setFileInfos(List<FileResource> fileInfos) {
            this.fileInfos = fileInfos;
        }

        public void setNeedTest(boolean needTest) {
            this.needTest = needTest;
        }

        public boolean isNeedTest() {
            return needTest;
        }

        public ArchiveBatchDetailReceiveOnline getDetailReceiveOnline() {
            return detailReceiveOnline;
        }

        public void setDetailReceiveOnline(ArchiveBatchDetailReceiveOnline detailReceiveOnline) {
            this.detailReceiveOnline = detailReceiveOnline;
        }
    }

    public ArchiveReceiveOnlineSysManage getSysManage() {
        return sysManage;
    }

    public void setSysManage(ArchiveReceiveOnlineSysManage sysManage) {
        this.sysManage = sysManage;
    }

    public ArchiveBatchReceiveOnline getBatchReceiveOnline() {
        return batchReceiveOnline;
    }

    public void setBatchReceiveOnline(ArchiveBatchReceiveOnline batchReceiveOnline) {
        this.batchReceiveOnline = batchReceiveOnline;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public ArchiveReceiveBo getArchiveReceiveBo() {
        return archiveReceiveBo;
    }

    public void setArchiveReceiveBo(ArchiveReceiveBo archiveReceiveBo) {
        this.archiveReceiveBo = archiveReceiveBo;
    }
}
