package com.jef.service;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author tufujie
 * @date 2023/8/21
 */
@WebFilter("/easyRule/*")
public class SuspiciousRequestFilter implements Filter {

    private Rules rules;
    private RulesEngine rulesEngine;

    @Override
    public void init(FilterConfig filterConfig) {
        rulesEngine = new DefaultRulesEngine();
        rules = new Rules();
        rules.register(new SuspiciousRequestRule());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Facts facts = new Facts();
        facts.put("request", request);
        rulesEngine.fire(rules, facts);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}