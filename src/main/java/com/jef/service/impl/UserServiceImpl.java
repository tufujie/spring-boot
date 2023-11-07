package com.jef.service.impl;

import com.jef.dao.IUserDao;
import com.jef.entity.User;
import com.jef.service.IUserService;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务
 *
 * @author Jef
 * @create 2018/5/15 19:21
 */
@Service(value = "userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User getByUser(User user) {
        return userDao.getByUser(user);
    }

    @GlobalTransactional
    @Override
    public void updateById(User user) {
        userDao.updateById(user);
    }

}
