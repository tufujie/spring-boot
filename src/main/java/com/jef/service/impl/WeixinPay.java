package com.jef.service.impl;

import com.jef.constant.PayTypeEnum;
import com.jef.dao.IOrderDao;
import com.jef.service.IPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Service
public class WeixinPay implements IPay {

    @Autowired
    private IOrderDao orderDao;

    @Override
    public void pay() {
        System.out.println("weixinPay start");
    }

    @Override
    public String type() {
        return PayTypeEnum.WEIXIN.getType();
    }
}