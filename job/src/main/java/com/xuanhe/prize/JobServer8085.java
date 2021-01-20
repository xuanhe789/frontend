package com.xuanhe.prize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class JobServer8085 {
    public static void main(String[] args) {
        SpringApplication.run(JobServer8085.class,args);
    }
}
