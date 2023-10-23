package com.jef.service;

import com.yomahub.liteflow.core.NodeForComponent;
import org.springframework.stereotype.Component;

/**
 * @author tufujie
 * @date 2023/8/22
 */
@Component("forCmpA")
public class ForCmpA extends NodeForComponent {
    @Override
    public int processFor() throws Exception {
        //这里根据业务去返回for的结果
        return 3;
    }
}
