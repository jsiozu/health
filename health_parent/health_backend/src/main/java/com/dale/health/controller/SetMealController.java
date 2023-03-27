package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dale.health.constant.MessageConstant;
import com.dale.health.constant.RedisConstant;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.entity.Result;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.Setmeal;
import com.dale.health.service.SetMealService;
import com.dale.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @GetMapping("getCheckgroup.do")
    public Result getAllCheckgroup() {
        try {
            List<CheckGroup> checkgroups = setMealService.getAllCheckgroup();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkgroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setMealService.findPage(queryPageBean);
    }

    @RequestMapping("/add.do")
    public Result add(@RequestBody Map<String, String> map) {
        try {
            Setmeal setmeal = JSON.parseObject(map.get("formData"), Setmeal.class);
            List<Integer> ids = JSON.parseObject(map.get("checkgroupIds"), List.class);
            setMealService.addSetmeal(setmeal, ids);
            return new Result(true, "套餐更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "套餐更新失败");
        }

    }

    @RequestMapping("/upload.do")
    public Result uploadIMG(@RequestParam("imgFile") MultipartFile imgFile) {
        // 获取源文件名
        String originalFilename = imgFile.getOriginalFilename();
        // 获得源文件的扩展名
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(index - 1);
        // 使用UUID生成唯一的文件名并保证扩展名相同
        String fileName = UUID.randomUUID().toString() + extension;
        // 上传之七牛云
        Jedis jedis = null;
        try {
            QiNiuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            // 缓存金redis便于删除垃圾图片
            jedis = jedisPool.getResource();
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        } finally {
            assert jedis != null;
            jedis.close();
        }
    }

    @RequestMapping("/selectById.do")
    public Result selectById(@RequestParam("id") Integer setmealId) {
        try {
            Map<String, Object> map = setMealService.selectById(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Map<String, String> map) {
        try {
            Setmeal setmeal = JSON.parseObject(map.get("formData"), Setmeal.class);
            List<Integer> ids = JSON.parseObject(map.get("checkgroupIds"), List.class);
            setMealService.editSetmeal(setmeal, ids);
            return new Result(true, "套餐修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "套餐修改失败");
        }
    }

}
