package com.gls.gemini.gateway.boot.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * demo controller
 */
@Slf4j
@RestController
@RequestMapping("/demo")
@Tag(name = "demo", description = "demo数据")
public class DemoController {

    /**
     * test
     *
     * @param name name
     * @return hello name
     */
    @GetMapping("/test")
    @Operation(summary = "test", description = "test")
    @Parameter(name = "name", description = "name", required = true)
    public Mono<String> test(@RequestParam String name) {
        return Mono.just("hello " + name);
    }

    /**
     * getAuthentication
     *
     * @param authentication authentication
     * @return authentication
     */
    @GetMapping("/getAuthentication")
    @Operation(summary = "getAuthentication", description = "getAuthentication")
    public Mono<Object> getAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return Mono.just(jwtAuthenticationToken.getToken().getClaims().get("user_info"));
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            return Mono.just(usernamePasswordAuthenticationToken.getPrincipal());
        }
        return Mono.just(authentication.getPrincipal());
    }
}
