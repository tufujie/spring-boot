package com.jef.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author qihuaiyuan
 * @since 2023/6/28
 */
@TestConfiguration
public class DependentMiddlewareInitializer {

    private static final MySQLContainer<?> MYSQL = ContainerHelper.createMySqlContainer();

    private static final GenericContainer<?> REDIS = ContainerHelper.createRedisContainer();

    static {
        System.setProperty("spring.datasource.url", MYSQL.getJdbcUrl());
        System.setProperty("spring.datasource.username", MYSQL.getUsername());
        System.setProperty("spring.datasource.password", MYSQL.getPassword());
    }

}
