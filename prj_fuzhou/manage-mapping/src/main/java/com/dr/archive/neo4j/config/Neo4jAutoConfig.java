package com.dr.archive.neo4j.config;

import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * 自动装配neo4j相关dao层
 *
 * @author dr
 */
@EnableNeo4jRepositories(basePackages = "com.dr.archive.neo4j")
public class Neo4jAutoConfig {
}
