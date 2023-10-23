package com.jef.service;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tufujie
 * @date 2023/8/21
 */
@Rule
public class SuspiciousRequestRule {

    static final String SUSPICIOUS = "suspicious";

    @Condition
    public boolean isSuspicious(@Fact("request") HttpServletRequest request) {
        // 可疑的标准可以基于ip、用户代理等。
        // 这里为了简单起见，它是基于请求参数“可疑”的存在
        return request.getParameter(SUSPICIOUS) != null;
    }

    @Action
    public void setSuspicious(@Fact("request") HttpServletRequest request) {
        request.setAttribute(SUSPICIOUS, true);
    }
}