package com.jef.controller;

import com.jef.entity.UserLoginProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jef
 * @date 2021/8/30
 */
// 配置实时生效实时生效
@RefreshScope
@Controller
@RequiredArgsConstructor
@RequestMapping("/nacos")
public class NacosController {

    @Value("${business.mq.message.topic}")
    private String mqMessageTopic;

    private final UserLoginProperty userLoginProperty;

    /**
     * 获取配置信息
     * 以获取MQ消息的topic 为例
     *
     * @return java.lang.String
     * @author Jef
     * @date 2021/3/19
     */
    @ResponseBody
    @RequestMapping("/getConfigInfo")
    public String getConfigInfo() {
        return "mqMessageTopic：" + mqMessageTopic;
    }

    /**
     * 获取实时配置数据
     * 以获取用于加密的rsa公钥为例
     *
     * @return java.lang.String
     * @author Jef
     * @date 2021/3/19
     */
    @ResponseBody
    @RequestMapping("/getSynConfigInfo")
    public String getSynConfigInfo() {
        return "pwdPublicKey：" + userLoginProperty.getPwdPublicKey();
    }


}