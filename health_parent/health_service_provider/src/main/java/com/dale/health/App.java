package com.dale.health;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan(basePackages = "com.dale.health.dao")
@EnableWebMvc
@EnableAutoConfiguration
@EnableDubbo(scanBasePackages = "com.dale.health.service")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.dale.health")
public class App {

    @Value("${templateLoaderPath}")
    private String templateLoaderPath;
    @Value("${defaultEncoding}")
    private String defaultEncoding;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return new JedisPool(jedisPoolConfig, host, port, timeout, null);
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath(templateLoaderPath);
        freeMarkerConfigurer.setDefaultEncoding(defaultEncoding);
        return freeMarkerConfigurer;
    }

//    @Bean
//    public JedisPool jedisPool() {
//        JedisPool jedisPool = new JedisPool();
//    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
