package com.jef.service.impl;

import com.jef.service.IPay;
import org.springframework.stereotype.Service;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Service
public class WeixinPay implements IPay {

    @Override
    public void pay() {
        System.out.println("weixinPay start");
    }

    @Override
    public String type() {
        return "weixin";
    }
}