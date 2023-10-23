package com.jef.service;

import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

/**
 * @author tufujie
 * @date 2023/8/21
 */
public class HighTemperatureCondition implements Condition {

    /**
     * 达到某个条件触发，直到条件不满足
     *
     * @param facts 事实
     * @return 是否触发
     */
    @Override
    public boolean evaluate(Facts facts) {
        Integer temperature = facts.get("temperature");
        return temperature > 25;
    }

    public static HighTemperatureCondition itIsHot() {
        return new HighTemperatureCondition();
    }

}