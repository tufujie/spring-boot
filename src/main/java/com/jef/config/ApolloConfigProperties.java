package com.jef.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jef
 * @date 2023/11/1
 */
@Configuration
@EnableConfigurationProperties(ApolloConfigProperties.class)
@ConfigurationProperties(prefix = "config")
@Setter
@Getter
@RefreshScope
public class ApolloConfigProperties {
    private int configValue;
}
