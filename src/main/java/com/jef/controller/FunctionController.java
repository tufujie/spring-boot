package com.jef.controller;

import com.jef.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/function")
public class FunctionController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/{key}")
    public String hello(@PathVariable String key) {
        return "html/" + key;
    }

    @ResponseBody
    @RequestMapping("getUserNoChangeList")
    public Map<String, Object> getUserNoChangeList() {
        System.out.println("微信小程序正在调用。。。");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapOne = new HashMap<String, Object>();
        mapOne.put("name", "张三");
        mapOne.put("phone", "13266860001");
        Map<String, Object> mapTwo = new HashMap<String, Object>();
        mapTwo.put("name", "李四");
        mapTwo.put("phone", "13266860002");
        Map<String, Object> mapThree = new HashMap<String, Object>();
        mapThree.put("name", "王五");
        mapThree.put("phone", "13266860003");
        list.add(mapOne);
        list.add(mapTwo);
        list.add(mapThree);
        resultMap.put("list", list);
        System.out.println("微信小程序调用完成。。。");
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/useUtil")
    public String useUtil() {
        return "";
    }
}