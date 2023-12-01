package com.gls.gemini.gateway.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关启动类
 */
@SpringBootApplication
public class GatewayBootApplication {
    /**
     * 网关启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayBootApplication.class, args);
    }
}