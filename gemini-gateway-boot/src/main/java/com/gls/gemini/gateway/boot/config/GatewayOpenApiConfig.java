package com.gls.gemini.gateway.boot.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * open api 配置
 */
@Configuration
public class GatewayOpenApiConfig {

    /**
     * 聚合多个服务的接口文档
     *
     * @return 接口文档列表
     */
    @Bean
    public List<GroupedOpenApi> groupedOpenApis() {
        return new ArrayList<>();
    }
}
