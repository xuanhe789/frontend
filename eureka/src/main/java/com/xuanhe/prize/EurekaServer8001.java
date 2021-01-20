package com.xuanhe.prize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer8001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer8001.class,args);
    }
}
