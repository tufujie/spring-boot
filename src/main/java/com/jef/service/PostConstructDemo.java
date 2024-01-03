package com.jef.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author tufujie
 * @date 2024/1/3
 */
@Component
public class PostConstructDemo {

    @PostConstruct
    void init() {
        System.out.println("开始执行PostConstruct进行初始化设置");
    }

}