package com.codepiano.handlers;

import com.codepiano.models.Imitation;
import com.codepiano.services.ImitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ImitationHandler {

    private static final Logger logger = LoggerFactory.getLogger(MockHandler.class);

    @Autowired
    private ImitationService imitationService;

    public Mono<ServerResponse> imitation(ServerRequest request) {
        request.bodyToMono(String.class);
        Imitation imitation = new Imitation();
        return imitationService.imitation(imitation);
    }
}
