package com.dr.archive.common.file.service;

import com.dr.framework.common.entity.TreeNode;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-01-09 15:43
 * @Description:
 */
public interface FileViewService {
    /**
     * @param refId
     * @param refType
     * @param groupCode
     * @return
     */
    List<TreeNode> getFileTree(String refId, String refType, String groupCode);

    /**
     * 根据文件id，和文件路径获取文件信息
     *
     * @param fileId
     * @param filePath
     * @param keyWord     高亮关键字 可以为空
     * @param systemModel 使用哪个系统的 回调配置  可以为空
     * @param watermark   是否加水印
     * @param tools       工具栏展示情况  tools {none 不展示工具栏 all 都展示 print 只展示打印}
     *                    1、先判断是不是数据包格式，数据包和单个数据文件处理方式有所区别
     *                    2、ofd、pdf需要调用福昕云阅读展示，需要单独处理
     */
    String getFile(HttpServletResponse response, String fileId, String filePath, String keyWord, String systemModel, boolean watermark, String tools);

    String getXml(String fileId, String filePath);
}
