package com.jef.controller;

import com.alibaba.fastjson.JSONObject;
import com.jef.dao.IOrderDao;
import com.jef.entity.Order;
import com.jef.entity.User;
import com.jef.service.IUserService;
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
    @RequestMapping("/testShardingDBAndTableInsert")
    public String testShardingDBAndTableInsert(@RequestParam Long orderId, @RequestParam String orderNo,
                                               @RequestParam Long userId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        boolean success = false;
        String errorMessage = "";
        try {
            success = orderDao.insert(order);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return success ? "订单id=" + order.getOrderId() :
                "error insert orderId " + orderId + " errorMessage" + errorMessage;
    }

    @ResponseBody
    @RequestMapping("/testShardingDBAndTableGet")
    public String testShardingDBAndTableGet(@RequestParam Long orderId) {
        Order order = orderDao.getByID(orderId);
        return order == null ? "error get orderId " + orderId : "订单号=" + order.getOrderNo();
    }

    @ResponseBody
    @RequestMapping("/testGetDBAndTable")
    public String testGetDBAndTable(@RequestParam Long orderId) {
        int dbIndex = (int) (orderId % 2);
        int tableIndex = (int) (orderId % 3);
        return "ds_" + dbIndex + ".t_order_" + tableIndex;
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