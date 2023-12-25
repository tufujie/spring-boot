package com.jef.controller;

import com.jef.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tufujie
 * @date 2023/12/25
 */
@Controller
@RequestMapping("/designPattern")
public class DesignPatternController {

    @Autowired
    private IPayService payService;


    /**
     * 策略模式
     *
     * @param type 支付渠道
     */
    @ResponseBody
    @RequestMapping("/strategy")
    public String toPay(@RequestParam String type) {
        payService.toPay(type);
        return type;
    }
}