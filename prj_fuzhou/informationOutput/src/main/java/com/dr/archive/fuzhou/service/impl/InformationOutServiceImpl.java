package com.dr.archive.fuzhou.service.impl;

import com.dr.archive.fuzhou.service.InformationOutService;
import com.dr.archive.fuzhou.vo.InformaintionVO;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.model.FormRelationWrapper;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.query.OrganiseQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.camunda.feel.syntaxtree.In;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  根据门类编码、单位编码和归档时间组查询
 * @author wx
 */
@Service
public class InformationOutServiceImpl implements InformationOutService {

    @Autowired
    FondOrganiseService fondOrganiseService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryConfigService categoryConfigService;

    @Autowired
    ArchiveDataManager archiveDataManager;

    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    FormDataService formDataService;

    /**
     * 逻辑处理方法
     * @param categoryCode //门类编码
     * @param organiseCode // 单位编码
     * @param startTime //归档时间
     * @param endTime //归档时间
     *  fileFormId 文件表单id
     * @return
     */
    @Override
    public List<InformaintionVO> queryArchivesInformation(String categoryCode , String organiseCode, List<String> startTime , List<String> endTime){

        Organise organise = organisePersonService.getOrganise(new OrganiseQuery.Builder().codeEqual(organiseCode).statusEqual("1").getQuery());
        List<Fond> fondListByOrganiseId = fondOrganiseService.getFondListByOrganiseCode(organiseCode);
        // Fond fond = fondListByOrganiseId.get(0);

        Fond fond = null;
        for (int i = 0;i<fondListByOrganiseId.size();i++){
            if(organise.getId().equals(fondListByOrganiseId.get(i).getPartyId())){
                fond = fondListByOrganiseId.get(i);
                break;
            }
        }

        List<Category> categoryByFondId = categoryService.getCategoryByFondId(fond.getId());
        Category category = new Category();
        for (int i = 0 ; i < categoryByFondId.size();i++){
            if(categoryByFondId.get(i).getCode().equals(categoryCode)){
                category = categoryByFondId.get(i);
            }
        }

        List<CategoryConfig> categoryConfigs = categoryConfigService.selectByCategoryId(category.getId());
        CategoryConfig categoryConfig = null;
        for (int i = 0;i<categoryConfigs.size();i++){
            if(category.getId().equals(categoryConfigs.get(i).getBusinessId())){
                categoryConfig = categoryConfigs.get(i);
                break;
            }
        }

        //CategoryConfig categoryConfig1 = categoryConfigs.get(0);
        List<FormData> dataByQuery = formDataService.selectFormData(categoryConfig.getFileFormId(), (sqlQuery, formRelationWrapper) -> {
            sqlQuery.equal(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID),organise.getId()).equal(formRelationWrapper.getColumn(ArchiveEntity.COLUMN_CATEGORY_CODE),categoryCode);
            int count = startTime.size();
            if (startTime.size()<endTime.size()){
                count = endTime.size();
            }

            sqlQuery.andNew();
            for (int i=0 ; i < count; i++ ){
                if(i<startTime.size() && !"".equals(startTime.get(i))){
                    sqlQuery.greaterThanEqual(formRelationWrapper.getColumn("createDate"),startTime.get(i));
                }
                if(i<endTime.size() && !"".equals(endTime.get(i))){
                    sqlQuery.lessThanEqual(formRelationWrapper.getColumn("createDate"),endTime.get(i));
                }
                sqlQuery.or();
            }
        });

        //List<FormData> dataByQuery1 = archiveDataManager.findDataByQuery(categoryConfig.getFileFormId());
        List<InformaintionVO> informaintionVOs = new ArrayList<>();
        dataByQuery.forEach(i-> {

            InformaintionVO informaintionVO = new InformaintionVO();
            informaintionVO.setArchivesTitle(i.get(AbstractArchiveEntity.COLUMN_TITLE));
            informaintionVO.setArchivesId(i.getString(AbstractArchiveEntity.ID_COLUMN_NAME));
            informaintionVO.setFormDataId(i.getString(IdEntity.ID_COLUMN_NAME));
            informaintionVO.setArchiveTime(i.getString("createDate"));
            informaintionVOs.add(informaintionVO);
        });
        return informaintionVOs;
    }

    public void queryShareInformation(String organiseCode){

    }

}
