package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dale.health.constant.MessageConstant;
import com.dale.health.constant.RedisConstant;
import com.dale.health.entity.Result;
import com.dale.health.pojo.Member;
import com.dale.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login.do")
    public Result login(@RequestBody Map<String, String> map,
                        HttpServletResponse response,
                        HttpSession session) {
        String telephone = map.get("telephone");
        String validateCode = map.get("validateCode");
        Jedis jedis = jedisPool.getResource();
        String key = (String) session.getAttribute("key");
        String code = jedis.get(key);
        System.out.println("key: " + key);
        System.out.println("用户输入: " + validateCode);
        System.out.println("系统生成: " + code);
        if (code != null && code.equals(validateCode)) {
            // 验证码校验成功
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                // 不是会员，自动完成注册
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            // 向客户端写入Cookie，内容为手机号
            Cookie cookie = new Cookie("login-member-telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 *30);
            response.addCookie(cookie);
            // 保存会员信息到redis
            String memberJson = JSON.toJSON(member).toString();
            jedis.setex(telephone, 60 * 30, memberJson);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            // 验证码校验失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


}
