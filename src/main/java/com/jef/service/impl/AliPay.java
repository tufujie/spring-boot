package com.jef.service.impl;

import com.jef.service.IPay;
import org.springframework.stereotype.Service;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Service
public class AliPay implements IPay {

    @Override
    public void pay() {
        System.out.println("alipay start");
    }

    @Override
    public String type() {
        return "ali";
    }
}