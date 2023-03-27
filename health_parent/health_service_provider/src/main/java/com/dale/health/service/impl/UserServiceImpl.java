package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.PermissionDao;
import com.dale.health.dao.RoleDao;
import com.dale.health.dao.UserDao;
import com.dale.health.pojo.Permission;
import com.dale.health.pojo.Role;
import com.dale.health.pojo.User;
import com.dale.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String userName) {
        // 根据用户名查询用户信息
        User user = userDao.selectByUserName(userName);
        if (user == null) return null;
        Integer userId = user.getId();
        // 根据用户信息查询用户角色
        Set<Role> roles = roleDao.selectByUserId(userId);
        // 根据用户角色查询用户权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionDao.selectByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }


}
