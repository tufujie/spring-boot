package com.jef.controller;

import com.jef.service.IManyImplService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/annotation")
public class AnnotationController {
    private static final Logger logger = LogManager.getLogger(AnnotationController.class);

    @Autowired
    private List<IManyImplService> manyImplServiceList;

    @ResponseBody
    @RequestMapping("/manyImpl")
    public String manyImpl() {
        return "实现类数量=" + manyImplServiceList.size();
    }

}