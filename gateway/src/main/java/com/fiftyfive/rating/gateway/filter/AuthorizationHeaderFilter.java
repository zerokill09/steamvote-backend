package com.fiftyfive.rating.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    SecretKey secretKey;

    public AuthorizationHeaderFilter (@Value("${jwt.password}") String key) {
        super(Config.class);
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return ((exchange, chain) -> {
          ServerHttpRequest request = exchange.getRequest();

          if(request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
              String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
              if(authorizationHeader.isEmpty()) { return chain.filter(exchange); }
              String jwt = authorizationHeader.replace("bearer ", "");

              if (!isValid(jwt)) {
                  return onError(exchange, "JWT token is no valid", HttpStatus.UNAUTHORIZED);
              }

              String userId = getUserId(jwt);
              request = exchange.getRequest().mutate()
                      .header("X-User-Id", userId)
                      .build();
              ServerWebExchange modifiedExchange = exchange.mutate().request(request).build();

              return chain.filter(modifiedExchange);
          } else {
              return chain.filter(exchange);
          }
        });
    }

    private boolean isValid(String jwt) {
        boolean result = true;

        String subject = null;
        subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt).getPayload().getSubject();

        if(subject == null || subject.isEmpty()) {
            result = false;
        }

        return result;
    }

    public String getUserId(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload().get("user_id", Integer.class).toString();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String noAuthorizationHeader, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
