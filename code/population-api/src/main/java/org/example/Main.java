package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用主类
 * - 启动入口
 * - 自动扫描 com.example.api 包下的组件
 */
@SpringBootApplication
public class PopulationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PopulationApiApplication.class, args);
    }
}