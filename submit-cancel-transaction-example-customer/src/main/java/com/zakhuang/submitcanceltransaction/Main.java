package com.zakhuang.submitcanceltransaction;

import com.zakhuang.submitcanceltransaction.service.ExampleAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created on 2016/8/10.
 */
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = "com.zakhuang.submitcanceltransaction")
@Configuration
public class Main {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public static void main(String[] a) throws InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, a);
        ExampleAService bean = run.getBean(ExampleAService.class);
        try {
            bean.begin("测试1");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bean.begin("测试2");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            bean.begin("测试3");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Thread.sleep(20000);
        bean.begin("测试4");
    }


    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
