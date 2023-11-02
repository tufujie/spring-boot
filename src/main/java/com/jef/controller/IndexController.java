package com.jef.controller;

/**
 * @author Jef
 * @date 2019/6/29
 */
import com.jef.util.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

    @RequestMapping("")
    public String index(){
        System.out.println("组件引入，判断字符串是否为空=" + StringUtils.isEmpty(""));
        return "html/index";
    }

}