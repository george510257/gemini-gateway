package com.gls.gemini.gateway.boot;

import com.gls.gemini.gateway.boot.constants.GatewayBootProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Gemini Gateway 启动类
 *
 * @author gemini 自动生成
 * @version 0.0.1-SNAPSHOT
 */
@SpringBootApplication
@EnableConfigurationProperties({GatewayBootProperties.class})
public class GatewayBootApplication {
    /**
     * Gemini Gateway启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayBootApplication.class, args);
    }
}