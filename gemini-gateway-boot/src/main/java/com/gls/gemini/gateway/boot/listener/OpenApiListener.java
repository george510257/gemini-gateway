package com.gls.gemini.gateway.boot.listener;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * open api 监听器
 */
@Component
public class OpenApiListener implements ApplicationListener<RefreshRoutesEvent> {
    /**
     * 路由定位器
     */
    @Resource
    private RouteDefinitionLocator routeDefinitionLocator;
    /**
     * swagger ui 配置属性
     */
    @Resource
    private SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * 刷新路由事件
     *
     * @param event 路由事件
     */
    @Override
    public void onApplicationEvent(RefreshRoutesEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        // 获取路由定义
        routeDefinitionLocator.getRouteDefinitions()
                .map(routeDefinition -> routeDefinition.getUri().getHost())
                .filter(StrUtil::isNotBlank)
                .distinct()
                .subscribe(host -> {
                    // 聚合接口文档url
                    urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(host, "/" + host + "/v3/api-docs", host));
                });
        // 设置接口文档url
        swaggerUiConfigProperties.setUrls(urls);
    }
}
