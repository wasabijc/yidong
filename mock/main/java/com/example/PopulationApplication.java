package com.example;

import com.example.controller.DataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.example", exclude = {DataSourceAutoConfiguration.class}) // 标识该类为Spring Boot应用的启动类，并指定扫描的基础包路径
@EnableTransactionManagement // 启用Spring事务管理
@EnableScheduling // 启用Spring的定时任务调度功能
@EnableAsync // 启用Spring的异步调用功能
@EnableAspectJAutoProxy // 启用AspectJ切面代理
public class PopulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PopulationApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(DataLoader dataLoader) {
        return args -> {
            // 打印加载的数据摘要
            System.out.println("Loaded " + dataLoader.getUsers().size() + " users");
            System.out.println("Loaded " + dataLoader.getCells().size() + " cells");
            System.out.println("Loaded " + dataLoader.getRegions().size() + " regions");
        };
    }
}
