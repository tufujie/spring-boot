package com.jef.container;

import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.SneakyThrows;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * We not use the FixedHostPortGenericContainer here, because the multiple instances often exist on the same machine.
 * fork from <link><a href="https://github.com/echooymxq/testcontainers-rocketmq">https://github.com/echooymxq/testcontainers-rocketmq</a></link><br>
 * Thanks for giving
 */
public class RocketMQContainer extends GenericContainer<RocketMQContainer> {

    @lombok.Generated
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RocketMQContainer.class);

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("apache/rocketmq");

    public static final String INITIALIZE_FILE_PATH_PROPERTY_NAME = "tc.rocketmq.initialize";
    private static final String DEFAULT_INITIALIZE_FILE_PATH = "classpath:tc/rocketmq/initialize.json";
    private static final String DEFAULT_TAG = "4.9.4";
    // READ and WRITE
    private static final int defaultBrokerPermission = 6;
    public static final int NAMESRV_PORT = 9876;
    public static final int BROKER_PORT = 10911;

    @Deprecated
    public RocketMQContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
    }

    @Deprecated
    public RocketMQContainer(String rocketVersion) {
        this(DEFAULT_IMAGE_NAME.withTag(rocketVersion));
    }

    public RocketMQContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        withExposedPorts(NAMESRV_PORT, BROKER_PORT, BROKER_PORT - 2);
    }

    @Override
    protected void configure() {
        String command = "#!/bin/bash\n";
        command += "./mqnamesrv &\n";
        command += "./mqbroker -n localhost:" + NAMESRV_PORT;
        withCommand("sh", "-c", command);
    }

    @Override
    @SneakyThrows
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        List<String> updateBrokerConfigCommands = new ArrayList<>();
        // Update the brokerAddr and the clients can use the mapped address to connect the broker.
        updateBrokerConfigCommands.add(updateBrokerConfig("brokerIP1", getHost()));
        updateBrokerConfigCommands.add(updateBrokerConfig("listenPort", getMappedPort(BROKER_PORT)));
        // Make the changes take effect immediately.
        updateBrokerConfigCommands.add(updateBrokerConfig("brokerPermission", defaultBrokerPermission));

        final String command = String.join(" && ", updateBrokerConfigCommands);
        ExecResult result = execInContainer(
                "/bin/sh",
                "-c",
                command
        );
        if (result.getExitCode() != 0) {
            throw new IllegalStateException(result.toString());
        }
    }

    private String updateBrokerConfig(final String key, final Object val) {
        final String brokerAddr = "localhost:" + BROKER_PORT;
        return "./mqadmin updateBrokerConfig -b " + brokerAddr + " -k " + key + " -v " + val;
    }

    public String getNamesrvAddr() {
        return String.format("%s:%s", getHost(), getMappedPort(NAMESRV_PORT));
    }

    private void executeCommand(String command) {
        log.info("executing command: " + command);
        try {
            ExecResult execResult = execInContainer("/bin/sh", "-c", command);
            if (execResult.getExitCode() != 0) {
                throw new IllegalStateException(execResult.toString());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String createTopic(String topicName) {
        final String brokerAddr = "localhost:" + BROKER_PORT;
        return "./mqadmin updateTopic -b " + brokerAddr + " -t " + topicName;
    }

    private String createConsumerGroup(String consumerGroupName) {
        final String brokerAddr = "localhost:" + BROKER_PORT;
        return "./mqadmin updateSubGroup -b " + brokerAddr + " -g " + consumerGroupName;
    }

}