package com.jef.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/apollo")
public class ApolloController {
    @ApolloConfig
    private Config config;

    // 这种方式不需要改任何代码，支持热更新
    @Value("${username:无法读取到值}")
    private String username;

    @Value("${age:0}")
    private Long age;

    @ResponseBody
    @GetMapping("/getUserName")
    public String getUserName() {
        return config.getProperty("username", "fujie_tu");
    }

    @ResponseBody
    @GetMapping("/getUserNameV2")
    public String getUserNameV2() {
        Config config = ConfigService.getAppConfig();
        String key = "username";
        String defaultValue = "fujie_tu";
        return config.getProperty(key, defaultValue);
    }

    /**
     * 启动监听apollo更新
     * @author Jef
     * @date 2021/3/19
     * @return java.lang.String
     */
    @ResponseBody
    @GetMapping("/addChangeListener")
    public String getUserNameV3() {
        Config config = ConfigService.getAppConfig();
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    System.out.println(String.format("发现新的变更项 - key: %s, oldValue: %s, newValue: %s, changeType: %s",
                            change.getPropertyName(), change.getOldValue(),
                            change.getNewValue(), change.getChangeType()));
                }
            }
        });
        return "启动监听apollo更新";
    }

    /**
     * 直接获取用户信息
     * @author Jef
     * @date 2021/3/19
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("/getConfigInfo")
    public String getConfigInfo() {
        return "用户民：" + username + " 年龄：" + age;
    }

}