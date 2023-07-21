package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.DbSearchService;
import com.dr.archive.utilization.search.to.DbSearchResultTo;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultDataBaseService;
import com.dr.framework.core.orm.database.DataBaseMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caor
 * @date 2021-03-10 15:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DbSearchServiceImpl implements DbSearchService {
    @Autowired
    ArchiveFormDefinitionService archiveFormDefinitionService;
    @Autowired
    FormDataService formDataService;

    @Autowired
    DefaultDataBaseService dataBaseService;
    final Logger logger = LoggerFactory.getLogger(DbSearchServiceImpl.class);

    String searchSessionId = "sqlBuffer";

    @Override
    public Page<DbSearchResultTo> getDbSearchResult(HttpServletRequest request, DbSearchResultTo to, long start, long size) {
        List keyWordAllValues = getKeyWordValue(to.getTitle());
        HttpSession session = request.getSession();

        Page<DbSearchResultTo> page = new Page<>(start, size, 0);
        FormDefinitionQuery formDefinitionQuery = new FormDefinitionQuery();
        List<FormDefinition> formDefinitionList = (List<FormDefinition>) archiveFormDefinitionService.findFormList(formDefinitionQuery);
        DataBaseMetaData metaData = dataBaseService.getAllDatabases().get(0);
        //带分页查询结果
        StringBuffer sql = new StringBuffer();
        //查询总数
        StringBuffer sqlCount = new StringBuffer();
        if (formDefinitionList.size() > 0) {
            sql.append("SELECT * from ( ");
            sqlCount.append("SELECT count(tempTable.id) from ( ");

            String searchType = request.getParameter("searchType");
            if (!StringUtils.isEmpty(searchType) && "second".equals(searchType)) {
                Assert.isTrue(!StringUtils.isEmpty(session.getAttribute(searchSessionId)), "请先执行检索，再进行二次检索操作！");
                String oldSqlBuffer = session.getAttribute(searchSessionId) + "";
                String tempSql = oldSqlBuffer.toUpperCase();
                if (tempSql.lastIndexOf("LIMIT") != -1) {
                    tempSql = oldSqlBuffer.substring(0, tempSql.lastIndexOf("LIMIT"));
                }
                sql.append(tempSql);
                sqlCount.append(tempSql);
            } else {
                for (int i = 0; i < formDefinitionList.size(); i++) {
                    sql.append("select " + IdEntity.ID_COLUMN_NAME + "," + AbstractArchiveEntity.COLUMN_TITLE + "," + AbstractArchiveEntity.COLUMN_SAVE_TERM + "," + AbstractArchiveEntity.COLUMN_YEAR + "," + AbstractArchiveEntity.COLUMN_FILETIME + "," + AbstractArchiveEntity.COLUMN_ARCHIVE_CODE + "," + AbstractArchiveEntity.COLUMN_FOND_CODE + "," + AbstractArchiveEntity.COLUMN_CATEGORY_CODE + " from f_").append(formDefinitionList.get(i).getFormTable());

                    sqlCount.append("select " + IdEntity.ID_COLUMN_NAME + "," + AbstractArchiveEntity.COLUMN_TITLE + "," + AbstractArchiveEntity.COLUMN_SAVE_TERM + "," + AbstractArchiveEntity.COLUMN_YEAR + "," + AbstractArchiveEntity.COLUMN_FILETIME + "," + AbstractArchiveEntity.COLUMN_ARCHIVE_CODE + "," + AbstractArchiveEntity.COLUMN_FOND_CODE + "," + AbstractArchiveEntity.COLUMN_CATEGORY_CODE + " from f_").append(formDefinitionList.get(i).getFormTable());
                    if (formDefinitionList.size() - 1 - i > 0) {
                        sql.append(" union all ");
                        sqlCount.append(" union all ");
                    }
                }
            }
            sql.append(" ) tempTable where 1=1 ");
            sqlCount.append(" ) tempTable where 1=1 ");
            //带查询条件
            if (keyWordAllValues != null && keyWordAllValues.size() > 0) {
                keyWordAllValues.forEach(keyWord -> {
                    sql.append(" and ( tempTable." + AbstractArchiveEntity.COLUMN_TITLE + " like '%").append(keyWord).append("%' ");
                    sqlCount.append(" and ( tempTable." + AbstractArchiveEntity.COLUMN_TITLE + " like '%").append(keyWord).append("%' ");

                    sql.append(" or tempTable." + AbstractArchiveEntity.COLUMN_SAVE_TERM + " like '%").append(keyWord).append("%' ");
                    sqlCount.append(" or tempTable." + AbstractArchiveEntity.COLUMN_SAVE_TERM + " like '%").append(keyWord).append("%' ");

                    sql.append(" or tempTable." + AbstractArchiveEntity.COLUMN_YEAR + " like '%").append(keyWord).append("%' ");
                    sqlCount.append(" or tempTable." + AbstractArchiveEntity.COLUMN_YEAR + " like '%").append(keyWord).append("%' ");

                    sql.append(" or tempTable." + AbstractArchiveEntity.COLUMN_FILETIME + " like '%").append(keyWord).append("%' ");
                    sqlCount.append(" or tempTable." + AbstractArchiveEntity.COLUMN_FILETIME + " like '%").append(keyWord).append("%' ");

                    sql.append(" or tempTable." + AbstractArchiveEntity.COLUMN_ARCHIVE_CODE + " like '%").append(keyWord).append("%' ) ");
                    sqlCount.append(" or tempTable." + AbstractArchiveEntity.COLUMN_ARCHIVE_CODE + " like '%").append(keyWord).append("%' ) ");
                });
            }
            sql.append(" LIMIT ").append(15 * (start - 1)).append(",").append(size);
        }
        try (
                Connection connection = metaData.openSelfManagedConnection();
                PreparedStatement statement = connection.prepareStatement(sql + "");
                PreparedStatement statementCount = connection.prepareStatement(sqlCount + "");
                ResultSet resultSet = statement.executeQuery();
                ResultSet resultSetCount = statementCount.executeQuery()
        ) {
            connection.setAutoCommit(false);
            long total = 0;
            while (resultSetCount.next()) {
                total = resultSetCount.getInt(1);
            }
            page.setTotal(total);
            page.setData(this.resultToList(resultSet));
        } catch (SQLException e) {
            logger.warn("执行sql语句失败", e);
        }
        session.setAttribute(searchSessionId, sql);
        return page;
    }

    public List<DbSearchResultTo> resultToList(ResultSet resultSet) {
        List<DbSearchResultTo> dbSearchResultToList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                DbSearchResultTo dbSearchResultTo = new DbSearchResultTo();
                dbSearchResultTo.setId(resultSet.getString(1));
                dbSearchResultTo.setTitle(resultSet.getString(2));
                dbSearchResultTo.setSaveTerm(resultSet.getString(3));
                dbSearchResultTo.setYear(resultSet.getString(4));
                dbSearchResultTo.setFileTime(resultSet.getString(5));
                dbSearchResultTo.setArchiveCode(resultSet.getString(6));
                dbSearchResultTo.setFondCode(resultSet.getString(7));
                dbSearchResultTo.setCategoryCode(resultSet.getString(8));
                dbSearchResultToList.add(dbSearchResultTo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dbSearchResultToList;
    }

    /**
     * 根据空格把关键词分割
     *
     * @param keyWordValue
     * @return
     */
    private List getKeyWordValue(String keyWordValue) {
        List list = new ArrayList();
        if (!StringUtils.isEmpty(keyWordValue)) {
            String[] value = keyWordValue.trim().split("\\s");
            for (String s : value) {
                if (!StringUtils.isEmpty(s) && !"%".equals(s) && !"_".equals(s)) {
                    list.add(s);
                }
            }
        }
        return list;
    }
}
