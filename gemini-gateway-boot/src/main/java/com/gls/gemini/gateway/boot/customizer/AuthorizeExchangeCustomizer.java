package com.gls.gemini.gateway.boot.customizer;

import com.gls.gemini.common.core.constant.CommonConstants;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeExchangeCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {
    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec spec) {
        // 不需要认证的请求 /gemini-uaa/**
        spec.pathMatchers(CommonConstants.UAA_SERVICE_ID + "/**").permitAll();
        // 不需要认证的请求 /actuator/**
        spec.pathMatchers("/actuator/**").permitAll();
        // 不需要认证的请求 /webjars/** /swagger-ui.html
        spec.pathMatchers("/webjars/**", "/swagger-ui.html").permitAll();
        // 不需要认证的请求 /*/v3/api-docs/** /v3/api-docs/**
        spec.pathMatchers("/*/v3/api-docs/**", "/v3/api-docs/**").permitAll();
        // 所有请求都需要认证
        spec.anyExchange().authenticated();
    }
}
