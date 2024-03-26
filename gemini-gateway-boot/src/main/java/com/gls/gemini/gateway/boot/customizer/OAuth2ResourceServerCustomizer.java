package com.gls.gemini.gateway.boot.customizer;

import com.gls.gemini.common.core.constant.CommonConstants;
import com.gls.gemini.gateway.boot.constants.GatewayBootProperties;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

/**
 * OAuth2资源服务器定制器
 */
@Component
public class OAuth2ResourceServerCustomizer implements Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec> {
    @Resource
    private GatewayBootProperties gatewayBootProperties;
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 定制OAuth2资源服务器
     *
     * @param spec OAuth2资源服务器规范
     */
    @Override
    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec spec) {
        spec.jwt(this::jwtCustomize);
    }

    /**
     * 定制jwt
     *
     * @param spec jwt规范
     */
    private void jwtCustomize(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec spec) {
        String jwkSetUri = discoveryClient.getInstances(CommonConstants.UAA_SERVICE_ID).stream()
                .findFirst()
                .map(instance -> instance.getUri() + CommonConstants.JWK_SET_URI)
                .orElse(gatewayBootProperties.getJwkSetUri());
        spec.jwkSetUri(jwkSetUri);
    }
}
