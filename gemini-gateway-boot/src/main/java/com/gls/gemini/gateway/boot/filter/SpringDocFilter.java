package com.gls.gemini.gateway.boot.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.reactivestreams.Publisher;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringDoc 过滤器
 */
@Component
public class SpringDocFilter implements GlobalFilter, Ordered {
    /**
     * SpringDoc 配置属性
     */
    @Resource
    private SpringDocConfigProperties springDocConfigProperties;

    /**
     * 过滤器
     *
     * @param exchange 请求和响应的交换信息
     * @param chain    过滤器链
     * @return Mono<Void> 对象
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiDocsPath = springDocConfigProperties.getApiDocs().getPath();
        // 如果请求路径不包含接口文档路径，则直接放行
        if (!exchange.getRequest().getURI().getPath().contains(apiDocsPath)) {
            return chain.filter(exchange);
        }
        // 获取请求和响应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        URI uri = request.getURI();
        // 获取网关地址
        String gatewayUrl = uri.toString().replace(apiDocsPath, "");
        // 获取响应数据
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // 如果响应数据是 Flux 类型
                if (body instanceof Flux<? extends DataBuffer> fluxBody) {
                    return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                        // 获取响应数据
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffer);
                        // 读取响应数据
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);
                        // 修改响应数据
                        String result = new String(content, StandardCharsets.UTF_8);
                        Map<String, Object> map = JSONUtil.toBean(result, new TypeReference<>() {
                        }, false);
                        // 获取接口文档地址
                        List<Map<String, String>> servers = BeanUtil.getProperty(map, "servers");
                        // 设置网关地址
                        Map<String, String> gatewayServer = new HashMap<>();
                        gatewayServer.put("url", gatewayUrl);
                        gatewayServer.put("description", "Gateway server url");
                        servers.add(gatewayServer);
                        // 返回修改后的响应数据
                        result = JSONUtil.toJsonStr(map);
                        byte[] uppedContent = result.getBytes(StandardCharsets.UTF_8);
                        response.getHeaders().setContentLength(uppedContent.length);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
