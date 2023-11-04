package com.jef.controller;

import com.jef.constant.BasicConstant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jef
 * @date 2023/11/4
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @RequestMapping("/setGetFromRedis")
    public String setGetFromRedis() {
        redisTemplate.opsForValue().set(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        String userName = redisTemplate.opsForValue().get(BasicConstant.USER_NAME_KEY);
        return userName;
    }

}