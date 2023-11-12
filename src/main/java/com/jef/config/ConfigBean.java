package com.jef.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jef
 * @date 2023/11/12
 * @Configuration 相当于 Spring中的 application.xml
 */
@Configuration
public class ConfigBean {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
