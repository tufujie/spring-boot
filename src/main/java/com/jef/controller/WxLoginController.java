package com.jef.controller;

import com.jef.config.WxLoginConfig;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jef
 * @date 2023/11/12
 */
@Controller
@RequestMapping("/wx/login")
public class WxLoginController {

    private static final Logger logger = LogManager.getLogger(WxLoginController.class);

    @Autowired
    private WxLoginConfig wxLoginConfig;
    @Autowired
    private RestTemplate restTemplate;

    //生成二维码
    @GetMapping(value = "/qrCode")
    public void getQrCode(@RequestParam(value = "isRememberMe", required = false, defaultValue = "1") String isRememberMe,
                          @RequestParam(value = "sign", required = false) String sign, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginScope = "snsapi_userinfo";//写死
        String url = wxLoginConfig.getQrCodeUrl().replace("APPID", wxLoginConfig.getAppId())
                .replace("REDIRECT_URI", URLEncoder.DEFAULT.encode(wxLoginConfig.getServer() + "/wx/login/callback", Charset.defaultCharset()))
                .replace("SCOPE", loginScope);
        //生成二维码的，扫描后跳转上面的REDIRECT_URL地址
        QrCodeUtil.generate(url, 300, 300, "jpg", response.getOutputStream());
    }

    //回调
    @RequestMapping(value = "/callback")
    @ResponseBody
    public String pcCallback(String code, String state) throws Exception {
        String openId = getOpenId(code);
        System.out.println(openId);
        //获取微信用户信息
        getUserInfo(openId);
        //业务处理...
        return "登录成功Jef网站";
    }

    /**
     * 获取openId
     *
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        String url = wxLoginConfig.getOpenIdUrl().replace("APPID", wxLoginConfig.getAppId())
                .replace("APPSECRET", wxLoginConfig.getAppSecret()).replace("CODE", code);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String body = responseEntity.getBody();
        logger.info("微信openid信息={}", body);
        JSONObject object = JSONObject.parseObject(body);
        return object.getString("openid");
    }

    /**
     * 获取用户信息
     *
     * @param openId
     * @return
     */
    private JSONObject getUserInfo(String openId) {
        //从微信上中拉取用户信息
        String url = wxLoginConfig.getUserInfoUrl().replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openId);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String result = forEntity.getBody();
        logger.info("微信用户信息={}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * pc点击微信登录，生成登录二维码
     *
     * @throws Exception
     */
    @GetMapping(value = "/pcQrCode")
    @ResponseBody
    public void wxLoginPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //获取token
            String accessToken = getAccessToken();
            //获取 ticket
            String ticket = getTicket(accessToken);
            //获取二维码
            String qrCodeUrl = wxLoginConfig.getShowQrCode().replace("TICKET", ticket);
            ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(qrCodeUrl, byte[].class);
            byte[] body1 = forEntity.getBody();
            response.getOutputStream().write(body1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    public String getAccessToken() {

        //根据appid和appsecret获取access_token
        wxLoginConfig.getTokenUrl();
        String url = wxLoginConfig.getTokenUrl().replace("APPID", wxLoginConfig.getAppId()).replace("APPSECRET", wxLoginConfig.getAppSecret());
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), JSONObject.class);
        JSONObject object = responseEntity.getBody();
        String accessToken = "";
        accessToken = object.getString("access_token");
        return accessToken;
    }

    /**
     * 获取ticket
     *
     * @param accessToken
     * @return
     */
    public String getTicket(String accessToken) {
        //请求地址
        String getQrCodeUrl = wxLoginConfig.getQrCodeUrl().replace("TOKEN", accessToken);

        HttpHeaders headers = new HttpHeaders();
        //参数设置
        Map<String, Object> map = new HashMap<>();
        //二维码的过期时间，单位为秒，最大2592000（即30天）不填，则默认有效期为60秒。
        map.put("expire_seconds", "604800");
        //二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
        map.put("action_name", "QR_LIMIT_STR_SCENE");
        Map<String, Object> innerThenMap = new HashMap<>();
        //扫码回调时自定义要传输的数据
        innerThenMap.put("scene_str", "elwin123");
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("scene", innerThenMap);
        //二维码详细信息
        map.put("action_info", innerMap);
        // 组装请求体
        HttpEntity<Map<String, Object>> sendMap =
                new HttpEntity<Map<String, Object>>(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(getQrCodeUrl, sendMap, String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String ticket = jsonObject.getString("ticket");
        return ticket;
    }


    @RequestMapping("/checkSign")
    public String checkSign(HttpServletRequest request) throws Exception {

        //获取微信请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //参数排序。 token 就要换成自己实际写的 token
        String[] params = new String[]{timestamp, nonce, "weixin"};
        Arrays.sort(params);
        //拼接
        String paramstr = params[0] + params[1] + params[2];
        //加密
        //获取 shal 算法封装类
        MessageDigest Sha1Dtgest = MessageDigest.getInstance("SHA-1");
        //进行加密
        byte[] digestResult = Sha1Dtgest.digest(paramstr.getBytes("UTF-8"));
        //拿到加密结果
        String mysignature = bytes2HexString(digestResult);
        mysignature = mysignature.toLowerCase(Locale.ROOT);
        //是否正确
        boolean signsuccess = mysignature.equals(signature);
        //逻辑处理
        if (signsuccess && echostr != null) {
            //验证签名，接入服务器
            return echostr;
        } else {
            //接入成功后,下次回调过来就可以进行正常业务处理
            return callback(request);
        }
    }

    private String bytes2HexString(byte... bytes) {
        char[] hexChars = new char[bytes.length * 2];
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 回调业务处理
     *
     * @param request
     * @return
     * @throws Exception
     */
    private String callback(HttpServletRequest request) throws Exception {
        //解析
        WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());//获取消息流,并解析xml
        String messageType = message.getMsgType();                                //消息类型
        String messageEvent = message.getEvent();                                    //消息事件
        String openId = message.getFromUser();                                    //发送者帐号
        String touser = message.getToUser();                                        //开发者微信号
        String text = message.getContent();                                        //文本消息  文本内容
        String eventKey = message.getEventKey();                                    //二维码参数

        if (messageType.equals("event")) {
            //获取微信用户信息
            JSONObject userInfo = this.getUserInfo(openId);
            //根据不同的回调事件处理各自的业务
            switch (messageEvent) {
                case "SCAN": //扫码
                    System.out.println("扫码");
                    //业务处理...
                    return "result";

                case "subscribe": //关注公众号
                    System.out.println("关注公众号");
                    //业务处理...
                    return "result";

                case "unsubscribe": //取消关注公众号
                    System.out.println("取消关注公众号");
                    //业务处理...
                    return "result";
            }
        }
        return "111";
    }

}
