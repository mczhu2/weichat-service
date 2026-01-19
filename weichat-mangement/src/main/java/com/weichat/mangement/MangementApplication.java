package com.weichat.mangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 管理服务启动类
 */
@SpringBootApplication
@ComponentScan("com.weichat")
public class MangementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangementApplication.class, args);
    }

}
