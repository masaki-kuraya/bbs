package name.kuraya.masaki.bbs.ui.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
class AuthenticationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        switch (exchange.getRequest().getPath().value()) {
        case "/":
            return exchange.getSession().flatMap(session -> {
                if (session.getAttribute("userInfo") == null) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(URI.create("/signin.html"));
                    return response.setComplete();
                } else {
                    return chain.filter(exchange);
                }
            });
        default:
            return chain.filter(exchange);
        }
    }

}
