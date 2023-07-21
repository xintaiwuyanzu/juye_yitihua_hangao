package com.dr.archive.service.internal;

import com.dr.archive.model.query.ArchiveFileQuery;
import com.dr.archive.model.to.ArchiveFileTo;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/22 17:38
 */
public interface ArchiveFileConfigService {
    //统计附件表中格式种类
    List<String> fileAccessory() ;

    //统计附件表中文件存储量总和
    Long findCapacity();

    //根据附件表中不同格式种类的存储大小
    Map<String ,Long> findAccessoryCapa();

    Map<String, Long> findSumCapa();

    /**
     * 查询全部附件列表
     * @param query
     * @return
     */
    List<ArchiveFileTo> findList(ArchiveFileQuery query);

    /**
     * 功能描述: 添加一个附件文件信息到索引库
     *
     * @param archiveFile
     * @return : {@link String}
     * @author : tzl
     * @date : 2020/7/2 15:20
     */
    String addElasticsearch(ArchiveFileTo archiveFile);

    String deleteElasticsearch(ArchiveFileTo archiveFileTo);
}
