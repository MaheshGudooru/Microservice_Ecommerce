package com.techouts.api_gateway.filter;

import com.techouts.api_gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthFilter() {
        System.out.println("JwtAuthFilter CREATED");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println ("API GATEWAY IS WORKING!!!!!!!!");

        String path = exchange.getRequest ().getURI ().getPath ();

        // public endpoints don't need user login
        List<String> excludedURIs = List.of ("/user-service/user/login", "/user-service/user/register", "/products");

        // skip the check for public endpoints
        if (excludedURIs.stream ().anyMatch ((s) -> path.startsWith (s))) {
            return chain.filter (exchange);
        }

        String authHeader = exchange.getRequest ().getHeaders ().getFirst (HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header: " + authHeader);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        System.out.println("AUTH HEADER: " + authHeader);

        String JWTtoken = authHeader.substring (7);

        System.out.println (JWTtoken);

        try {
            if (!jwtUtil.validateToken(JWTtoken)) {
                System.out.println("TOKEN INVALID");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception e) {
            System.out.println("TOKEN ERROR: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Integer extractedUserId = jwtUtil.extractUserId (JWTtoken);

        ServerHttpRequest modifiedRequest = exchange.getRequest ()
                .mutate ()
                .header ("X-User-ID", String.valueOf (extractedUserId))
                .build ();

        return chain.filter (exchange.mutate ().request (modifiedRequest).build ());

    }

    @Override
    public int getOrder() {
        return -1;
    }
}
