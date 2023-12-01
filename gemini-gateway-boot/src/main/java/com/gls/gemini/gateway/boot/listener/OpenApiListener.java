package com.gls.gemini.gateway.boot.listener;

import jakarta.annotation.Resource;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * 聚合多个服务的接口文档
     */
    @Resource
    private List<GroupedOpenApi> groupedOpenApis;

    /**
     * 刷新路由事件
     *
     * @param event 路由事件
     */
    @Override
    public void onApplicationEvent(RefreshRoutesEvent event) {
        // 清空接口文档
        groupedOpenApis.clear();
        // 清空接口文档url
        Set<SwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        // 获取路由定义
        routeDefinitionLocator.getRouteDefinitions()
                .map(routeDefinition -> routeDefinition.getUri().getHost())
                .filter(host -> host != null && !host.isBlank())
                .distinct()
                .subscribe(host -> {
                    // 聚合接口文档
                    groupedOpenApis.add(GroupedOpenApi.builder().pathsToMatch("/" + host + "/**").group(host).build());
                    // 聚合接口文档url
                    urls.add(new SwaggerUiConfigProperties.SwaggerUrl(host, "/" + host + "/v3/api-docs", host));
                });
        // 设置接口文档url
        swaggerUiConfigProperties.setUrls(urls);

    }
}
