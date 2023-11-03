package com.jef.controller;

import com.jef.common.annotations.MyAnnotation;
import com.jef.service.IManyImplService;

import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Log
@Controller
@RequestMapping("/annotation")
public class AnnotationController {
    private static final Logger logger = LogManager.getLogger(AnnotationController.class);

    @Autowired
    private List<IManyImplService> manyImplServiceList;
    @Autowired
    private IManyImplService manyImplServiceOne;
    @Autowired
    private IManyImplService manyImplServiceTwo;

    @ResponseBody
    @RequestMapping("/manyImpl")
    public String manyImpl() {
        manyImplServiceOne.doSomething();
        manyImplServiceTwo.doSomething();
        return "实现类数量=" + manyImplServiceList.size();
    }

    @RequestMapping("/testCustomAnnotation")
    @MyAnnotation(value = "自定义注解-controller", menuId = "1")
    public String test(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("MyAnnotation- test");
//        testCustomAnnotation2();
        ((AnnotationController) AopContext.currentProxy()).testCustomAnnotation2();
        return "success";
    }

    @MyAnnotation(value = "自定义注解-method", menuId = "2")
    public String testCustomAnnotation2() {
        logger.info("MyAnnotation- test2");
        return "success";
    }

}