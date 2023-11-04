package com.jef.controller;

import com.jef.constant.BasicConstant;
import com.jef.entity.User;
import com.jef.service.IUserService;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
        Cookie cookie = request.getCookies()[0];
        return cookie.getValue();
    }

    @ResponseBody
    @RequestMapping("/setSession")
    public String setSession(HttpServletRequest request, HttpServletResponse response) {
        User user = User.getBasicUser();
        request.getSession().setAttribute(BasicConstant.USER_NAME_KEY, user);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/getSession")
    public String getSession(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute(BasicConstant.USER_NAME_KEY);
        return JSONObject.toJSONString(user);
    }

}