package com.jef.service;

import com.yomahub.liteflow.core.NodeBreakComponent;
import org.springframework.stereotype.Component;

/**
 * @author tufujie
 * @date 2023/8/22
 */
@Component("breakCmpA")
public class BreakCmpA extends NodeBreakComponent {
    int i = 0;

    @Override
    public boolean processBreak() throws Exception {
        //这里根据业务去返回break的结果
        return i++ > 5;
    }
}
