package com.xuanhe.prize.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableEurekaClient
@EnableRedisHttpSession
@MapperScan("com.xuanhe.prize.commons.db.mapper")
@ComponentScan({"com.xuanhe.prize"})
public class ApiServer8088 {
    public static void main(String[] args) {
        SpringApplication.run(ApiServer8088.class,args);
    }
}
