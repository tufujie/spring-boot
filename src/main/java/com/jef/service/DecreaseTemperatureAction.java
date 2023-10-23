package com.jef.service;

import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;

/**
 * @author tufujie
 * @date 2023/8/21
 */
public class DecreaseTemperatureAction implements Action {

    @Override
    public void execute(Facts facts) throws Exception {
        Integer temperature = facts.get("temperature");
        System.out.println("当前温度，" + temperature + "，太热了，开始1度1度地降温，要降到25度..");
        facts.put("temperature", temperature - 1);
    }

    public static DecreaseTemperatureAction decreaseTemperature() {
        return new DecreaseTemperatureAction();
    }
}