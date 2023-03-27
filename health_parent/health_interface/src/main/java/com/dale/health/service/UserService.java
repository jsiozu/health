package com.dale.health.service;

import com.dale.health.pojo.User;

public interface UserService {

    public User findByUsername(String userName);
}
