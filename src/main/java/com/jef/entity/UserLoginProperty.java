package com.jef.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tufujie
 * @date 2023/7/28
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "password.rsa")
public class UserLoginProperty {
    private String pwdPublicKey;
    private String pwdPrivateKey;
}