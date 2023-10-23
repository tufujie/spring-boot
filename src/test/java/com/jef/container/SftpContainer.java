package com.jef.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * @author tufujie
 * @date 2023/9/22
 */
public class SftpContainer extends GenericContainer<SftpContainer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpContainer.class);

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("atmoz/sftp:alpine");

    public SftpContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        withExposedPorts(22);
        withCommand("user:pass:::upload");
        withStartupTimeout(Duration.ofMinutes(5));
    }
}