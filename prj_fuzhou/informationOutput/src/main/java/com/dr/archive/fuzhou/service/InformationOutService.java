package com.dr.archive.fuzhou.service;

import com.dr.archive.fuzhou.vo.InformaintionVO;

import java.util.List;

/**
 * 接口
 */
public interface InformationOutService {

     List<InformaintionVO> queryArchivesInformation(String categoryCode , String organise, List<String> startTime , List<String> endTime);


}
