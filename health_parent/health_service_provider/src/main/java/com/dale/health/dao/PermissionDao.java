package com.dale.health.dao;

import com.dale.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface PermissionDao {

    public Set<Permission> selectByRoleId(@Param("id") Integer id);
}
