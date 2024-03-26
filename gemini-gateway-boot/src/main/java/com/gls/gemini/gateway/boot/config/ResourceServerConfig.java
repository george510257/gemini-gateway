package com.gls.gemini.gateway.boot.config;

import com.gls.gemini.gateway.boot.customizer.AuthorizeExchangeCustomizer;
import com.gls.gemini.gateway.boot.customizer.OAuth2ResourceServerCustomizer;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class ResourceServerConfig {

    @Resource
    private AuthorizeExchangeCustomizer authorizeExchangeCustomizer;
    @Resource
    private OAuth2ResourceServerCustomizer oauth2ResourceServerCustomizer;

    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {
        // 配置请求授权
        http.authorizeExchange(authorizeExchangeCustomizer);
        // 关闭csrf
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        // 配置资源服务器
        http.oauth2ResourceServer(oauth2ResourceServerCustomizer);
        // 返回http安全
        return http.build();
    }
}
