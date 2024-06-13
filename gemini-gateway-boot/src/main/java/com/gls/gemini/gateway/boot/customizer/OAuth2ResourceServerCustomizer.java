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
    /**
     * 网关启动属性
     */
    @Resource
    private GatewayBootProperties gatewayBootProperties;
    /**
     * 服务发现客户端
     */
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 定制OAuth2资源服务器
     *
     * @param spec OAuth2资源服务器规范
     */
    @Override
    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec spec) {
        // 配置jwt
        spec.jwt(this::jwtCustomize);
    }

    /**
     * 定制jwt
     *
     * @param spec jwt规范
     */
    private void jwtCustomize(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec spec) {
        // 配置jwt
        String jwkSetUri = discoveryClient.getInstances(CommonConstants.UAA_SERVICE_ID)
                .stream()
                // 获取第一个实例
                .findFirst()
                // 获取uri
                .map(instance -> instance.getUri() + CommonConstants.JWK_SET_URI)
                // 如果没有获取到则使用默认值
                .orElse(gatewayBootProperties.getJwkSetUri());
        // 设置jwkSetUri
        spec.jwkSetUri(jwkSetUri);
    }
}
