package com.jef;

import com.jef.controller.UserController;
import com.jef.dao.IUserDao;
import com.jef.entity.User;
import com.jef.service.IUserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author tufujie
 * @date 2023/9/11
 */
@SpringBootTest
@Disabled
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private IUserDao userDao;

    @Test
    void testShowUser() {
         String showUser = userController.getAllUser();
    }

    @Test
    void testGetAllUser() {
        List<User> users = userService.getAllUser();
    }

    @Test
    void testGetAllUserV2() {
        List<User> users = userDao.getAllUser();
    }

}