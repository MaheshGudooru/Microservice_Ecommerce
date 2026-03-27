package com.techouts.api_gateway.filter;

import com.techouts.api_gateway.utils.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println ("API GATEWAY IS WORKING!!!!!!!!");

        String path = exchange.getRequest ().getURI ().getPath ();

        // public endpoints don't need user login
        List<String> excludedURIs = List.of ("/user/login", "/user/register", "/products");

        // skip the check for public endpoints
        if (excludedURIs.stream ().anyMatch ((s) -> path.startsWith (s))) {
            return chain.filter (exchange);
        }

        String authHeader = exchange.getRequest ().getHeaders ().getFirst (HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith ("Bearer ")) {
            exchange.getResponse ().setStatusCode (HttpStatus.UNAUTHORIZED);
            return exchange.getResponse ().setComplete ();
        }

        String JWTtoken = authHeader.substring (7);

        try {
            if (!jwtUtil.validateToken (JWTtoken)) {
                exchange.getResponse ().setStatusCode (HttpStatus.UNAUTHORIZED);
                return exchange.getResponse ().setComplete ();
            }
        } catch (Exception e) {
            exchange.getResponse ().setStatusCode (HttpStatus.UNAUTHORIZED);
            return exchange.getResponse ().setComplete ();
        }

        String[] userDetails = jwtUtil.extractUserIdAndName (JWTtoken);

        ServerHttpRequest modifiedRequest = exchange.getRequest ()
                .mutate ()
                .header ("X-User-ID", userDetails[0])
                .header ("X-Username", userDetails[1])
                .build ();

        return chain.filter (exchange.mutate ().request (modifiedRequest).build ());

    }
}
