package com.giansiccardi.springcloud.msvc_gateway_eventos.filter;

import com.giansiccardi.springcloud.msvc_gateway_eventos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

import com.giansiccardi.springcloud.msvc_gateway_eventos.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;

    private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }



    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Falta encabezado de autorización");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    Claims claims = jwtUtil.validateToken(authHeader);

                    String path = exchange.getRequest().getPath().toString();
                    HttpMethod method = exchange.getRequest().getMethod();
                    log.info("Solicitud recibida: Método - {}, Ruta - {}", method, path);

                    if (!hasRequiredRole(method, path, claims,exchange)) {
                        throw new RuntimeException("Acceso denegado");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Loggear la excepción
                    throw new RuntimeException("Acceso denegado");
                }
            }

            return chain.filter(exchange);
        };
    }

    private boolean hasRequiredRole(HttpMethod method, String path, Claims claims, ServerWebExchange exchange) {        List<String> roles = (List<String>) claims.get("roles");


        String fullPath = exchange.getRequest().getURI().toString();


        if (path.startsWith("/eventos")) {

            if (method == HttpMethod.POST || method == HttpMethod.DELETE || method == HttpMethod.PUT) {
                log.info("Método {} detectado para /eventos", method);
                if (roles.contains("ADMIN")) {
                    log.info("Acceso permitido para ADMIN");
                    return true;
                } else {
                    log.warn("Acceso denegado para usuario con roles: {}", roles);
                    return false;
                }
            }
        }

        if (path.startsWith("/ubicaciones")) {
            if (method == HttpMethod.POST || method == HttpMethod.DELETE || method == HttpMethod.PUT) {
                log.info("Método {} detectado para /ubicaciones", method);
                if (roles.contains("ADMIN")) {
                    log.info("Acceso permitido para ADMIN");
                    return true;
                } else {
                    log.warn("Acceso denegado para usuario con roles: {}", roles);
                    return false;
                }
            }
        }

        if (path.startsWith("/usuarios")) {
            if (method == HttpMethod.GET|| method == HttpMethod.DELETE) {
                log.info("Método {} detectado para /usuarios", method);
                if (roles.contains("ADMIN")) {
                    log.info("Acceso permitido para ADMIN");
                    return true;
                } else {
                    log.warn("Acceso denegado para usuario con roles: {}", roles);
                    return false;
                }
            }
        }



                log.info("Ruta o método no necesita verificación específica, acceso permitido.");
        return true;
    }

    public static class Config {}
}