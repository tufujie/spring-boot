package com.jef.controller;

import com.jef.dao.IOrderDao;
import com.jef.entity.Order;
import com.jef.entity.User;
import com.jef.service.IUserService;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Jef
 * @date 2021/8/30
 */
@Controller
@RequestMapping("/db")
public class DBController {
    private static final Logger logger = LogManager.getLogger(DBController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderDao orderDao;

    @ResponseBody
    @RequestMapping("/getAllUser")
    public String getAllUser() {
        List<User> userList = userService.getAllUser();
        logger.info("用户数量={}", userList.size());
        return "用户数量=" + userList.size() + "，用户信息=" + JSONObject.toJSONString(userList);
    }

    @ResponseBody
    @RequestMapping("/testShardingDB")
    public String testShardingDB() {
        Order order = orderDao.getByID(1L);
        return order == null ? "error" : "订单号=" + order.getOrderNo();
    }

    @ResponseBody
    @RequestMapping("/testShardingTable")
    public String testShardingTable() {
        Order order = orderDao.getByID(1L);
        return order == null ? "error" : "订单号=" + order.getOrderNo();
    }

    @ResponseBody
    @RequestMapping("/testDistributedTransactional")
    public String testDistributedTransactional(@RequestParam Long id) {
        User user = User.getBasicUser();
        user.setId(id);
        userService.updateById(user);
        return "success";
    }

}