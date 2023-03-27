package com.dale.health.controller;

import com.dale.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername.do")
    public Result getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) return new Result(true, "查询用户名失败");
        String username = user.getUsername();
        return new Result(true, "查询用户名成功", username);
    }

}
