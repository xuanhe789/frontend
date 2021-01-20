package com.xuanhe.prize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiServer8088 {
    public static void main(String[] args) {
        SpringApplication.run(ApiServer8088.class,args);
    }
}
