package com.codepiano.handlers;

import com.codepiano.services.ImitationService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ImitationHandler {

    private static final Logger logger = LoggerFactory.getLogger(MockHandler.class);

    @Autowired
    private ImitationService imitationService;

    @Autowired
    private JsonParser parser;

    public Mono<ServerResponse> imitation(ServerRequest request) {
        request.cookies().get
        return request
            .bodyToMono(String.class)
            .map(parser::parse)
            .map(JsonElement::getAsJsonObject)
            .map(jsonObject -> imitationService.initImitation(jsonObject))
            .flatMap(imitation -> ServerResponse.ok().build());
    }
}
