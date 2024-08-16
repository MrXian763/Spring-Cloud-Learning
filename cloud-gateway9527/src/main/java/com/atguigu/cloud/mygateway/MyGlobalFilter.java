package com.atguigu.cloud.mygateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器：统计接口调用时长
 */
@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {

    public static final String BEGIN_VISIT_TIME = "begin_visit_time"; // 访问开始时间

    /**
     * 统计
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 先记录访问接口开始时间
        exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long beginVisitTime = exchange.getAttribute(BEGIN_VISIT_TIME);
            if (beginVisitTime != null) {
                log.info("接口访问主机：{}", exchange.getRequest().getURI().getHost());
                log.info("接口访问端口：{}", exchange.getRequest().getURI().getPort());
                log.info("接口访问路径：{}", exchange.getRequest().getURI().getPath());
                log.info("接口访问URL参数：{}", exchange.getRequest().getURI().getRawQuery());
                log.info("接口访问时长：{} ms", System.currentTimeMillis() - beginVisitTime);
                log.info("=========================分割线=========================");
            }
        }));
    }

    /**
     * 数字越小优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
