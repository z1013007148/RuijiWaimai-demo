package com.example.waimai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@ServletComponentScan // 为了扫描到过滤器
@EnableCaching
public class MyWaimaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyWaimaiApplication.class, args);
        log.info("Waimai application started");
    }
}
