package com.jef.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * @author qihuaiyuan
 * @since 2023/6/18
 */
public class ContainerHelper {

    public static final String MYSQL_IMAGE = "mysql:8.0.33";

    public static final String REDIS_IMAGE = "redis:7.0.11-alpine";

    public static final String ARM64_PREFIX = "arm64v8/";

    public static MySQLContainer<?> createMySqlContainer() {
        return createMySqlContainer("jef_db_test");
    }

    public static MySQLContainer<?> createMySqlContainer(String dbName) {
        // choose image according to platform
        MySQLContainer<?> container;
        if (isArm64()) {
            container = new MySQLContainer<>(DockerImageName.parse(ARM64_PREFIX + MYSQL_IMAGE)
                    .asCompatibleSubstituteFor("mysql"));
        } else {
            container = new MySQLContainer<>(MYSQL_IMAGE);
        }
        container.withDatabaseName(dbName)
                .withUsername("root")
                .withPassword("root")
                .withStartupTimeout(Duration.ofMinutes(5))
                .withExposedPorts(3306);
        container.start();
        return container;
    }

    public static boolean isArm64() {
        return System.getProperty("os.arch").contains("aarch64");
    }

    public static RocketMQContainer createRocketMQContainer() {
        String imageName = "apache/rocketmq:5.1.1";
        RocketMQContainer container = new RocketMQContainer(DockerImageName.parse(imageName));
        container.start();
        return container;
    }

    public static GenericContainer<?> createRedisContainer() {
        GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(6379)
                .withStartupTimeout(Duration.ofMinutes(5));
        container.start();
        return container;
    }

    public static SftpContainer createSftpContainer() {
        String imageName = "atmoz/sftp:alpine";
        SftpContainer container = new SftpContainer(DockerImageName.parse(imageName));
        container.start();
        return container;
    }


}
