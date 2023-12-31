package com.jef.controller;

import com.jef.constant.BasicConstant;
import com.jef.entity.User;
import com.jef.service.IUserService;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @RequestMapping("/getAllUser")
    public String getAllUser() {
        List<User> userList = userService.getAllUser();
        logger.info("用户数量={}", userList.size());
        return "用户数量=" + userList.size() + "，用户信息=" + JSONObject.toJSONString(userList);
    }

    @ResponseBody
    @RequestMapping("/setCookie")
    public String setContext(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        response.addCookie(cookie);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/getCookie")
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (BasicConstant.USER_NAME_KEY.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        System.out.println("cookies.length = " + cookies.length);
        String jssssionId = null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                System.out.println("JSESSIONID=" + cookie.getValue());
                jssssionId = cookie.getValue();
            }
        }
        User user = User.getBasicUser();
        request.getSession().setAttribute(jssssionId, user);
        // 分布式Session
        redisTemplate.opsForValue().set(jssssionId, JSONObject.toJSONString(user));
        String userJson = redisTemplate.opsForValue().get(jssssionId);
        User userRedis = JSONObject.parseObject(userJson, User.class);
        return userRedis.getName();
    }

    @ResponseBody
    @RequestMapping("/setSession")
    public String setSession(HttpServletRequest request, HttpServletResponse response) {
        // 调用getSession后会产生一个key为JSESSIONID的Cookie，所以要先产生这个Cookie，然后在获取Cookie
        request.getSession();
        return "success";
    }

    @ResponseBody
    @RequestMapping("/getSession")
    public String getSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String jssssionId = null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                System.out.println("JSESSIONID=" + cookie.getValue());
                jssssionId = cookie.getValue();
            }
        }
        User user = (User) request.getSession().getAttribute(jssssionId);
        return JSONObject.toJSONString(user);
    }

}