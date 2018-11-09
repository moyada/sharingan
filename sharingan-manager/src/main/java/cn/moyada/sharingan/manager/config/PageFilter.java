package cn.moyada.sharingan.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Configuration
public class PageFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/")) {
            return chain.filter(exchange.mutate()
                    .request(exchange.getRequest().mutate().path("/index.html").build())
                    .build());
        }

        return chain.filter(exchange);
    }
}
