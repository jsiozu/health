package com.dale.health.job;

import com.dale.health.constant.RedisConstant;
import com.dale.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义Job，实现定时清理垃圾图片
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Jedis jedis = null;
        try {
            //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
            jedis = jedisPool.getResource();
            // 查看set集合的差异，删除垃圾图片
            Set<String> set = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            if (set != null) {
                for (String picName : set) {
                    //删除七牛云服务器上的图片
                    QiNiuUtils.deleteIMG(picName);
                    //从Redis集合中删除图片名称
                    jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                    jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) jedis.close();
        }

    }
}