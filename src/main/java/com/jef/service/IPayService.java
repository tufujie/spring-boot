package com.jef.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Service
public class IPayService implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    private List<IPay> payList = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (payList == null) {
            payList = new ArrayList<>();
            Map<String, IPay> beansOfType = applicationContext.getBeansOfType(IPay.class);
            beansOfType.forEach((key, value) -> {
                payList.add(value);
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 具体的支付调用
     *
     * @param type 支付渠道
     */
    public void toPay(String type) {
        for (IPay iPay : payList) {
            if (iPay.type().equals(type)) {
                iPay.pay();
            }
        }
    }
}