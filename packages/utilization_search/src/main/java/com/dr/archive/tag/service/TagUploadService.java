package com.dr.archive.tag.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: yang
 * @create: 2022-07-15 09:50
 **/
public interface TagUploadService {

    void upload(MultipartFile file, String tagType);

    void importWordTxt(MultipartFile file, String id, String type);
}
