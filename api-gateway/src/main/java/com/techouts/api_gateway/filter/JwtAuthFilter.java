package com.techouts.api_gateway.filter;

import com.techouts.api_gateway.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthFilter() {
        System.out.println("JwtAuthFilter CREATED");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {

        System.out.println("API GATEWAY IS WORKING!!!!!!!!");

        String path = exchange.getRequest().getURI().getPath();

        // public endpoints don't need user login
        List<String> excludedURIs = List.of(
                "/api/products",
                "/api/user/login",
                "/api/user/register"
        );

        // skip the check for public endpoints
        if (excludedURIs.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendError(exchange, "Missing or invalid Authorization header");
        }

        System.out.println("AUTH HEADER: " + authHeader);

        String JWTtoken = authHeader.substring(7);

        System.out.println(JWTtoken);

        try {
            if (!jwtUtil.validateToken(JWTtoken)) {
                return sendError(exchange, "Invalid Authorization header");
            }
        } catch (ExpiredJwtException ex) {
            return sendError(exchange, "JWT token expired");
        } catch (JwtException e) {
            return sendError(exchange, "Invalid token");
        } catch (Exception e) {
            return sendError(exchange, "");
        }

        Integer extractedUserId = jwtUtil.extractUserId(JWTtoken);

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-Id", String.valueOf(extractedUserId))
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());

    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> sendError(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String responseBody = "{ \"error\" : \"" + message + "\" }";
        DataBuffer buffer = exchange
                .getResponse()
                .bufferFactory()
                .wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));

    }

}
