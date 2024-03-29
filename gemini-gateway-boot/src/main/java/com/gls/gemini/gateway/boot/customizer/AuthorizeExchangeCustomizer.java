package com.gls.gemini.gateway.boot.customizer;

import com.gls.gemini.gateway.boot.constants.GatewayBootProperties;
import jakarta.annotation.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 请求授权定制器
 */
@Component
public class AuthorizeExchangeCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {
    @Resource
    private GatewayBootProperties gatewayBootProperties;

    /**
     * 定制请求授权
     *
     * @param spec 请求授权规范
     */
    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec spec) {
        // 配置请求授权
        List<String> ignoreUrls = gatewayBootProperties.getIgnoreUrls();
        if (ignoreUrls != null && !ignoreUrls.isEmpty()) {
            spec.pathMatchers(ignoreUrls.toArray(new String[0])).permitAll();
        }
        spec.anyExchange().authenticated();
    }
}
