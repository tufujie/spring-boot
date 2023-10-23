package com.jef.controller;

import com.jef.entity.User;
import com.jef.service.IDubboDemoService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/dubbo")
public class DubboController {
    @DubboReference(version = "1.0.1")
    private IDubboDemoService dubboDemoService;

    @ResponseBody
    @GetMapping("/testUseDubbo")
    public String getUserNameV2() {
        System.out.println("消费者1号 获取权限" + dubboDemoService.getPermissions(1L));
        User user = dubboDemoService.getByID("1");
        System.out.println("消费者2号 获取用户名称=" + user.getName());
        User userNameAndPhone = dubboDemoService.getByNameAndPhone("Jef", "13266860001");
        System.out.println("消费者3号 获取用户名称=" + userNameAndPhone.getName());
        List<User> userListUseDubbo = dubboDemoService.getAllUser();
        return userListUseDubbo.stream().map(User::getName).collect(Collectors.joining(","));
    }

}