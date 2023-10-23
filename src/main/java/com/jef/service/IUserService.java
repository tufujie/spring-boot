package com.jef.service;

import com.jef.entity.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUser();

    User getByUser(User user);
}
