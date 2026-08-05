package com.ms.api.gateway.api_gateway.filter;

import com.ms.api.gateway.api_gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("JWT FILTER HIT");
        String path = exchange.getRequest()
                .getURI()
                .getPath();
        System.out.println("PATH = " + path);

        String authHeader =
        exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        String method = exchange.getRequest().getMethod().name();
        if("POST".equals(method) && path.startsWith("/user-api/user")) {
            return chain.filter(exchange);
        }
        if("/auth-api/login".equals(path)
                || "/auth-api/register".equals(path)) {
            return chain.filter(exchange);
        }
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);
        System.out.println("PATH = " + path);
        System.out.println("HEADER = " + authHeader);
        System.out.println("TOKEN = " + token);
        boolean valid = jwtService.validateToken(token);
        System.out.println("VALID = " + valid);

        if(!jwtService.validateToken(token)) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String username = jwtService.extractUsername(token);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        AuthorityUtils.NO_AUTHORITIES);

        return chain.filter(exchange)
                .contextWrite(
                        ReactiveSecurityContextHolder
                                .withAuthentication(auth));
    }
}
