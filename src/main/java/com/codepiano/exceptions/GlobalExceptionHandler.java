package com.codepiano.exceptions;

import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
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

@Component
public class GlobalExceptionHandler extends WebFluxResponseStatusExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR = "internal server error";

    @Autowired
    private DefaultDataBufferFactory dataBufferFactory;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        if (status != null && response.setStatusCode(status)) {
            String content;
            if (ex instanceof MockingbirdException) {
                MockingbirdException exception = (MockingbirdException) ex;
                JsonObject result = new JsonObject();
                result.addProperty("code", exception.getClass().getAnnotation(Code.class).value());
                result.addProperty("reason", exception.getReason());
                content = result.toString();
            } else {
                content = INTERNAL_SERVER_ERROR;
            }
            DataBuffer buf = dataBufferFactory.wrap(content.getBytes(StandardCharsets.UTF_8));
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
