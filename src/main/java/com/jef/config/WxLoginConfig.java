package com.jef.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Jef
 * @date 2023/11/12
 */
@Data
@Component
public class WxLoginConfig {

    @Value("${wx.login.appid}")
    private String appId;        //公众号标识

    @Value("${wx.login.appsecret}")
    private String appSecret;    //公众号密码

    @Value("${wx.login.server}")
    private String server;        //服务器域名地址，用于微信服务器回调。

    @Value("${wx.login.qrCodeUrl}")
    private String qrCodeUrl;    //获取code接口

    @Value("${wx.login.tokenUrl}")
    private String tokenUrl;    //获取token接口

    @Value("${wx.login.openIdUrl}")
    private String openIdUrl;   //获取openid接口

    @Value("${wx.login.userInfoUrl}")
    private String userInfoUrl;   //获取用户信息接口

    @Value("${wx.login.token}")
    private String token;

    @Value("${wx.login.showqrcode}") //通过ticket获取二维码
    private String showQrCode;

}
