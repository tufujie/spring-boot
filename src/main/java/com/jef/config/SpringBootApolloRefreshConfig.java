package com.jef.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Jef
 * @date 2023/11/1
 */
@Slf4j
@Component
public class SpringBootApolloRefreshConfig {

    private final ApolloConfigProperties apolloConfigProperties;
    private final RefreshScope refreshScope;

    public SpringBootApolloRefreshConfig(ApolloConfigProperties apolloConfigProperties, RefreshScope refreshScope) {
        this.apolloConfigProperties = apolloConfigProperties;
        this.refreshScope = refreshScope;
    }

    // interestedKeyPrefixes = {"key_prefix"}
    @ApolloConfigChangeListener(value = {"namespace"}, interestedKeys = {"key_prefix.key"})
    public void onChange(ConfigChangeEvent changeEvent) {
        log.info("before refresh {}", apolloConfigProperties.toString());
        refreshScope.refresh("configProperties");
        log.info("after refresh {}", apolloConfigProperties.toString());
    }
}
