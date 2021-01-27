package com.xuanhe.prize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.xuanhe.prize.commons.db.mapper")
@ComponentScan("com.xuanhe.prize.commons")
public class MsgServer8086 {
    public static void main(String[] args) {
        SpringApplication.run(MsgServer8086.class,args);
    }
}
