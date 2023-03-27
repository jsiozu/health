package com.dale.health.dao;

import com.dale.health.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    public User selectByUserName(@Param("userName") String userName);
}
