package com.codepiano;

import com.codepiano.exceptions.GlobalExceptionHandler;
import com.codepiano.handlers.ImitationHandler;
import com.codepiano.handlers.MockHandler;
import com.google.gson.JsonParser;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.headers;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class MockingBird implements WebFluxConfigurer {

    public static final String DEFAULT_HOST = "mockingbird";
    public static final String HOST = "host";
    public static final String BODY = "body";
    public static final String PATH = "path";
    public static final String STATUS = "status";
    public static final String HEADERS = "headers";
    public static final String COOKIES = "cookies";
    public static final String RESPONSE = "response";

    @Autowired
    private MockHandler mockHandler;

    @Autowired
    private ImitationHandler imitationHandler;

    public static void main(String[] args) {
        SpringApplication.run(MockingBird.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {

        return route(
                headers(headers -> Collections.singletonList("kill").equals(headers.header("mockingbird")))
                    .and(POST("/initImitation")),
                imitationHandler::imitation)
            .andRoute(path("/**"), mockHandler::mock);
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

    @Bean
    public JsonParser parser() {
        return new JsonParser();
    }
}
