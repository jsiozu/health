package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.Result;
import com.dale.health.pojo.Order;
import com.dale.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findById.do")
    public Result findById(@RequestParam("id") Integer id) {
        try {
            Map<Object, Object> map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/submit.do")
    public Result submit(@RequestBody Map<String, Object> map, HttpSession session) {
        try {
            String key = (String) session.getAttribute("key");
            String validateCode = (String) map.get("validateCode");
            boolean b = verifyCaptcha(validateCode, key);
            if (!b) throw new RuntimeException();
            map.put("orderType", "啊哈哈哈");
            Integer orderId = orderService.checkIn(map);
            return new Result(true, MessageConstant.ORDER_SUCCESS, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FULL);
        }
    }

    public boolean verifyCaptcha(String validateCode, String key) throws IOException {
        System.out.println("用户输入的验证码: " + validateCode);
        System.out.println("用户输入key: " + key);
        if (validateCode == null || key == null) return false;
//        String ipAddress = IPUtils.getIpAddress(request);
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get(key);
        return validateCode.equals(s);
    }

}
