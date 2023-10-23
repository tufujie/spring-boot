package com.jef.service;

import com.jef.entity.User;

import java.util.List;

/**
 * dubbo接口
 *
 * @author Jef
 * @date 2021/4/26
 */
public interface IDubboDemoService {
    /**
     * 通过用户ID获取权限
     *
     * @param id 用户ID
     * @return java.util.List<java.lang.String>
     * @author Jef
     * @date 2021/9/13
     */
    List<String> getPermissions(Long id);

    /**
     * 通过用户ID获取用户信息，模拟数据库交互
     *
     * @param id 用户ID
     * @return com.jef.dubbo.entity.User
     * @author Jef
     * @date 2021/9/13
     */
    User getByID(String id);

    /**
     * 通过用户名称和手机获取用户信息，模拟数据库交互
     *
     * @param name  用户名称
     * @param phone 用户手机号
     * @return com.jef.dubbo.entity.User
     * @author Jef
     * @date 2021/9/13
     */
    User getByNameAndPhone(String name, String phone);

    List<User> getAllUser();

}
