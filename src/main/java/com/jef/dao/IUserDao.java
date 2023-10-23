package com.jef.dao;

import com.jef.entity.User;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户DAO层
 *
 * @author Jef
 * @create 2018/5/15 19:18
 */
@Repository
public interface IUserDao extends IBaseDao {

    List<User> getAllUser();

    User getByUser(User user);

}
