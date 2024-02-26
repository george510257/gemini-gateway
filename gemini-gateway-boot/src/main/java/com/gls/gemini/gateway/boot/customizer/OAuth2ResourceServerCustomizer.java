package com.gls.gemini.gateway.boot.customizer;

import cn.hutool.core.util.StrUtil;
import com.gls.gemini.common.core.constant.CommonConstants;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ResourceServerCustomizer implements Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec> {

    private static final String URL_TEMPLATE = "http://{}:{}/oauth2/jwks";

    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec spec) {
        spec.jwt(this::jwtCustomize);
    }

    private void jwtCustomize(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec spec) {
        spec.jwkSetUri(getJwkSetUri());
    }

    private String getJwkSetUri() {
        return discoveryClient.getServices().stream()
                .filter(serviceId -> serviceId.contains(CommonConstants.UAA_SERVICE_ID))
                .flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream())
                .map(serviceInstance -> StrUtil.format(URL_TEMPLATE, serviceInstance.getHost(), serviceInstance.getPort()))
                .findAny()
                .orElse("http://localhost:8080/oauth2/jwks");
    }
}
