package com.dale.health.controller;

import com.dale.health.constant.RedisConstant;
import com.dale.health.entity.Result;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/get.do")
    public void yzmImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("-----生成验证码-----");
        // HttpSession session = request.getSession();
        // String preCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        log.info("-----生成验证码-----前一个验证码："+preCode);
        // System.out.println("-----生成验证码-----前一个验证码："+preCode);

        //生成验证码
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();
        System.err.println("生成的验证码: " + capText);

        String key = UUID.randomUUID().toString();
        Jedis jedis = jedisPool.getResource();
        jedis.setex(key, 60 * 5, capText);
        HttpSession session = request.getSession();
        session.setAttribute("key", key);
        jedis.close();

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping("/send4Login.do")
    public void send4Login(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        //生成验证码
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();
        System.err.println("生成的验证码: " + capText);

        String key = UUID.randomUUID().toString();
        Jedis jedis = jedisPool.getResource();
        jedis.setex(key, 60 * 5, capText);
        HttpSession session = request.getSession();
        session.setAttribute("key", key);
        jedis.close();

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping("/verify.do")
    public Result verifyCaptcha(@RequestBody Map<String, String> map, HttpSession session) throws IOException {
        String validateCode = map.get("validateCode");
        String key = (String) session.getAttribute("key");
        System.out.println("用户输入的验证码: " + validateCode);
        if (validateCode == null || key == null) return new Result(false, "请输入验证码");
//        String ipAddress = IPUtils.getIpAddress(request);
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get(key);
        if (validateCode.equals(s)) {
            return new Result(true, "验证码正确");
        } else {
            return new Result(false, "验证码错误");
        }
    }

}
