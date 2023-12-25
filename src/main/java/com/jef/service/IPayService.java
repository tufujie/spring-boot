package com.jef.service;

import com.jef.util.SpringBeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Service
public class IPayService implements InitializingBean {

    private Map<String, IPay> payMap = null;

    @Autowired
    private SpringBeanUtils springBeanUtils;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (payMap == null) {
            payMap = new HashMap<>();
            Map<String, IPay> beansOfType = springBeanUtils.getApplicationContext().getBeansOfType(IPay.class);
            beansOfType.values().forEach(obj -> {
                payMap.put(obj.type(), obj);
            });
        }
    }


    /**
     * 具体的支付调用
     *
     * @param type 支付渠道
     */
    public void toPay(String type) {
        payMap.get(type).pay();
    }
}