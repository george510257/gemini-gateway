package com.gls.gemini.gateway.boot.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringDoc 过滤器
 */
public class SpringDocFilter implements GlobalFilter, Ordered {
    /**
     * 接口文档路径
     */
    private final String apiDocsPath;
    /**
     * 网关过滤器
     */
    private final GatewayFilter delegate;

    /**
     * 构造方法
     *
     * @param apiDocsPath 接口文档路径
     * @param factory     网关过滤器工厂
     */
    public SpringDocFilter(String apiDocsPath, ModifyResponseBodyGatewayFilterFactory factory) {
        // 设置接口文档路径
        this.apiDocsPath = apiDocsPath;
        // 设置网关过滤器
        this.delegate = factory.apply(config -> config.setRewriteFunction(String.class, String.class, this::rewriteFunction));
    }

    /**
     * 重写函数 用于修改响应数据
     * 添加网关地址
     *
     * @param exchange 请求和响应的交换信息
     * @param result   响应数据
     * @return Publisher<String> 对象
     */
    private Publisher<String> rewriteFunction(ServerWebExchange exchange, String result) {
        // 获取响应数据
        Map<String, Object> map = JSONUtil.toBean(result, new TypeReference<>() {
        }, false);
        // 获取接口文档地址
        List<Map<String, String>> servers = BeanUtil.getProperty(map, "servers");
        // 设置网关地址
        Map<String, String> gatewayServer = new HashMap<>();
        // 设置网关地址
        gatewayServer.put("url", exchange.getRequest().getURI().toString().replace(apiDocsPath, ""));
        // 设置网关描述
        gatewayServer.put("description", "Gateway server url");
        // 添加网关地址
        servers.add(gatewayServer);
        // 返回修改后的响应数据
        return Mono.just(JSONUtil.toJsonStr(map));

    }

    /**
     * 过滤器
     *
     * @param exchange 请求和响应的交换信息
     * @param chain    过滤器链
     * @return Mono<Void> 对象
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 如果请求路径不包含接口文档路径，则直接放行
        if (!exchange.getRequest().getURI().getPath().contains(apiDocsPath)) {
            return chain.filter(exchange);
        }
        // 修改响应数据
        return delegate.filter(exchange, chain);
    }

    /**
     * 获取过滤器顺序
     *
     * @return 过滤器顺序
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
