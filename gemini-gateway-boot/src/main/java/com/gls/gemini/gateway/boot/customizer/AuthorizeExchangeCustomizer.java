package com.gls.gemini.gateway.boot.customizer;

import com.gls.gemini.common.core.constant.CommonConstants;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * 请求授权定制器
 */
@Component
public class AuthorizeExchangeCustomizer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {
    /**
     * 定制请求授权
     *
     * @param spec 请求授权规范
     */
    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec spec) {
        // 配置请求授权
        spec.pathMatchers("/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/*/v3/api-docs/**", "/" + CommonConstants.UAA_SERVICE_ID + "/**").permitAll()
                .anyExchange().authenticated();
    }
}
