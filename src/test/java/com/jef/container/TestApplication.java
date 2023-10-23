package com.jef.container;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author liangpengyu
 * @since 2023/8/4 2:46
 */
@Profile("integrated")
@SpringBootApplication
@ComponentScan("com.jef")
@MapperScan("com.jef.dao")
@Import(DependentMiddlewareInitializer.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
