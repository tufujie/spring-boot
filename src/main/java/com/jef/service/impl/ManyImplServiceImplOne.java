package com.jef.service.impl;

import com.jef.service.IManyImplService;

import org.springframework.stereotype.Service;

/**
 * @author tufujie
 * @date 2023/8/23
 */
@Service(value = "manyImplServiceOne")
public class ManyImplServiceImplOne implements IManyImplService {

    @Override
    public void doSomething() {
        System.out.println("do One");
    }
}