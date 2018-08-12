package com.codepiano.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class GlobalExceptionHandler extends WebFluxResponseStatusExceptionHandler {

    @Autowired
    private DefaultDataBufferFactory dataBufferFactory;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);
        if (status != null && exchange.getResponse().setStatusCode(status)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer buf = dataBufferFactory.wrap("Hello from exchange".getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buf));
        }
        return Mono.error(ex);
    }

    @Nullable
    private HttpStatus resolveStatus(Throwable ex) {
        HttpStatus status = determineStatus(ex);
        if (status == null) {
            Throwable cause = ex.getCause();
            if (cause != null) {
                status = resolveStatus(cause);
            }
        }
        return status;
    }

}
