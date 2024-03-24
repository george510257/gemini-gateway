package com.gls.gemini.gateway.boot.config;

import com.gls.gemini.gateway.boot.filter.SpringDocFilter;
import jakarta.annotation.Resource;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SpringDocConfig {

    /**
     * 路由定义定位器
     */
    @Resource
    private RouteDefinitionLocator routeDefinitionLocator;
    /**
     * swagger ui 配置属性
     */
    @Resource
    private SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * SpringDoc 配置属性
     */
    @Resource
    private SpringDocConfigProperties springDocConfigProperties;

    /**
     * 刷新路由事件
     *
     * @param event 路由事件
     */
    @EventListener(classes = RefreshRoutesEvent.class)
    public void onApplicationEvent(RefreshRoutesEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        String apiDocsPath = springDocConfigProperties.getApiDocs().getPath();
        // 获取路由定义
        routeDefinitionLocator.getRouteDefinitions()
                .subscribe(definition -> {
                    String host = definition.getUri().getAuthority();
                    // 聚合接口文档url
                    urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(host, host + apiDocsPath, host));
                });
        // 设置接口文档url
        swaggerUiConfigProperties.setUrls(urls);
    }

    @Bean
    public SpringDocFilter springDocFilter() {
        return new SpringDocFilter(springDocConfigProperties.getApiDocs().getPath());
    }
}
