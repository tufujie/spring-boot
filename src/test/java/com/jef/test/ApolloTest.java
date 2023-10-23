package com.jef.test;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.junit.jupiter.api.Test;

/**
 * Apollo测试
 *
 * @author Jef
 * @date 2021/3/9
 */
public class ApolloTest {

    @Test
    public void testApollo() {
        Config config = ConfigService.getAppConfig();
        String key = "username";
        String defaultValue = "fujie_tu";
        String username = config.getProperty(key, defaultValue);
        System.out.println("用户名=" + username);
    }

}