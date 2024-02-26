package com.gls.gemini.gateway.boot.customizer;

import com.gls.gemini.common.core.constant.CommonConstants;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ResourceServerCustomizer implements Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec> {

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
                .filter(CommonConstants.UAA_SERVICE_ID::equals)
                .findFirst()
                .map(discoveryClient::getInstances)
                .filter(instances -> !instances.isEmpty())
                .map(instances -> instances.getFirst().getUri().toString())
                .map(uri -> uri + "/oauth2/jwks")
                .orElse(null);
    }
}
