package com.jef.service.impl;

import com.jef.service.IManyImplService;

import org.springframework.stereotype.Service;

/**
 * @author tufujie
 * @date 2023/8/23
 */
@Service(value = "manyImplServiceTwo")
public class ManyImplServiceImplTwo implements IManyImplService {

    @Override
    public void doSomething() {
        System.out.println("do Two");
    }
}