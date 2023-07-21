package com.dr.archive.service.impl;

import com.dr.archive.entity.Atlas;
import com.dr.archive.service.AtlasService;
import com.dr.archive.tag.entity.TagLibArchive;
import com.dr.archive.tag.entity.TagLibArchiveInfo;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: yang
 * @create: 2022-05-25 11:26
 **/
@Service
public class AtlasServiceImpl extends DefaultBaseService<Atlas> implements AtlasService {
    @Autowired
    CommonFileService commonFileService;

    @Override
    public Page<Map> getImgArchives(String atlasId, int pageIndex, int pageSize) {
        Atlas atlas = selectById(atlasId);
        //Page<List> archives = new Page;
        //根据关键词标签 查找档案
        if (!StringUtils.isEmpty(atlas.getAtlasKeyWord())) {
            List<String> keyWordIds = Arrays.asList(atlas.getAtlasKeyWord().split(","));
            SqlQuery sqlQuery = SqlQuery.from(TagLibArchive.class)
                    .in(TagLibArchiveInfo.TAGLIBID, keyWordIds)
                    .equal(TagLibArchiveInfo.CATEGORYCODE, "ZP")   //固定只查门类为照片的档案
                    .orderBy(TagLibArchiveInfo.ARCHIVECODE)
                    .orderByDesc(TagLibArchiveInfo.CREATEDATE)
                    .setReturnClass(Map.class);
            Page<Map> archives = commonMapper.selectPageByQuery(sqlQuery, pageIndex * pageSize, (pageIndex + 1) * pageSize);

            List<String> imgSuffixs = new ArrayList<>();

            imgSuffixs.add("png");
            imgSuffixs.add("jpg");
            imgSuffixs.add("jpeg");
            imgSuffixs.add("bmp");
            imgSuffixs.add("gif");
            //根据档案去找图片原文
            for (int i = 0; i < archives.getData().size(); i++) {
                List<FileInfo> fileInfos = commonFileService.list(archives.getData().get(i).get("formDataId").toString(), "archive", "default");
                for (FileInfo fileInfo : fileInfos) {
                    if (imgSuffixs.contains(fileInfo.getSuffix())) {
                        archives.getData().get(i).put("imgFileid", fileInfo.getId());
                        archives.getData().get(i).put("imgFilePath", fileInfo.getBaseFileId());
                        break;
                    }
                }
            }
            return archives;
        } else {
            return null;
        }

    }
}
