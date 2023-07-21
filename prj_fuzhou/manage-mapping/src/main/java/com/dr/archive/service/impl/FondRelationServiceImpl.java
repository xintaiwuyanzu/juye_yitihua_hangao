package com.dr.archive.service.impl;

import com.dr.archive.entity.FondRelation;
import com.dr.archive.entity.FondRelationInfo;
import com.dr.archive.service.FondRelationService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class FondRelationServiceImpl extends DefaultBaseService<FondRelation> implements FondRelationService {


}
