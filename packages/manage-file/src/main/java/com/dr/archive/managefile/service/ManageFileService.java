package com.dr.archive.managefile.service;

import com.dr.archive.managefile.entity.ManageFile;
import com.dr.framework.common.entity.TreeNode;
import com.dr.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-04-07 17:25
 * @Description:
 */
public interface ManageFileService extends BaseService<ManageFile> {
    /**
     * @param refId
     * @return
     */
    List<TreeNode> getFileTree(String refId);

    /**
     * @param refId
     * @return
     */
    long deleteByRefId(String refId);

    /**
     * 转换成pdf 并添加到该资料库中
     *
     * @param refId
     * @param html
     * @param onlyBody
     */
    void htmlToPdf(String refId, String fileName, String html, boolean onlyBody);

    /**
     * 根据html 转pdf 返回流
     *
     * @param fileName
     * @param html
     * @param onlyBody
     * @param response
     */
    void htmlToPdf(String fileName, String html, boolean onlyBody, HttpServletResponse response);

    /**
     * 根据业务id 查询资料库列表
     *
     * @param bussinessId
     * @return
     */
    List<ManageFile> getManageFileListByBussinessId(String bussinessId);

    /**
     * 根据html转world
     * @param TitleName 题名
     * @param html html文件内容
     */
    void htmlToWords(String TitleName, String html, HttpServletResponse response, HttpServletRequest request) throws Exception;
}
