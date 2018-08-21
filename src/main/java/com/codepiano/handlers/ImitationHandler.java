package com.codepiano.handlers;

import com.codepiano.services.ImitationService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ImitationHandler {

    @Autowired
    private ImitationService imitationService;

    @Autowired
    private JsonParser parser;

    public Mono<ServerResponse> imitation(ServerRequest request) {
        return request
            .bodyToMono(String.class)
            .map(parser::parse)
            .map(JsonElement::getAsJsonObject)
            .map(jsonObject -> imitationService.initImitation(jsonObject))
            .flatMap(imitation -> ServerResponse.ok().build());
    }
}
