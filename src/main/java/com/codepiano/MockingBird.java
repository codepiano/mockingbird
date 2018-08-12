package com.codepiano;

import com.codepiano.exceptions.GlobalExceptionHandler;
import com.codepiano.handlers.MockHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class MockingBird implements WebFluxConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MockingBird.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes(MockHandler mockHandler) {
        return route(RequestPredicates.headers(headers -> Collections.singletonList("kill").equals(headers.header("mockingbird"))).negate(), mockHandler::mock)
                .andRoute(RequestPredicates.path("/a/*"), mockHandler::mock1);
    }

    @Bean
    @Order(-2)
    public WebExceptionHandler WebExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public DefaultDataBufferFactory defaultDataBufferFactory() {
        return new DefaultDataBufferFactory();
    }

}