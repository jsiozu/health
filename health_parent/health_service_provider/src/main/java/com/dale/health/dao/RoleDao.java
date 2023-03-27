package com.dale.health.dao;

import com.dale.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface RoleDao {

    public Set<Role> selectByUserId(@Param("id") Integer id);
}
