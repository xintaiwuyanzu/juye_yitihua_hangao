package com.dr.archive.neo4j.utils;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: yang
 * @create: 2022-07-11 08:45
 **/
public class Neo4jUtils {
    static final Logger logger = LoggerFactory.getLogger(Neo4jUtils.class);

    public static Result saveNodes(Session session, String name, String label) {
        String sql = String.format("MERGE (node:%s {name:'%s'})", label, name.replaceAll("'", " "));
        logger.info(sql);
        return session.run(sql);
    }

    public static Result saveRelations(Session session, String source, String target, String sourceName, String targetName, String relationName) {
        String sql = String.format("MATCH (p:%s),(p2:%s) " +
                "WHERE p.name = '%s' and p2.name ='%s' " +
                "Merge (p)-[r:%s]->(p2)", source, target, sourceName, targetName, relationName);
        logger.info(sql);
        return session.run(sql);
    }
}