package com.gls.gemini.gateway.boot.config;

import com.gls.gemini.gateway.boot.customizer.AuthorizeExchangeCustomizer;
import com.gls.gemini.gateway.boot.customizer.OAuth2ResourceServerCustomizer;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Resource
    private AuthorizeExchangeCustomizer authorizeExchangeCustomizer;
    @Resource
    private OAuth2ResourceServerCustomizer oauth2ResourceServerCustomizer;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorizeExchangeCustomizer);
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.oauth2ResourceServer(oauth2ResourceServerCustomizer);
        return http.build();
    }

}
