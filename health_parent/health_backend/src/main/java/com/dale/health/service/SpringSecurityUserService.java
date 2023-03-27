package com.dale.health.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.pojo.Permission;
import com.dale.health.pojo.Role;
import com.dale.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    /**
     * 根据用户名查询数据库获取用户信息
     * @param username 用于查询的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            // 用户不存在
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 遍历角色集合，为用户授予角色
            authorities.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            // 遍历权限集合，为用户授予权限
            for (Permission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        // 返回数据，便于框架校验密码
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }


}
