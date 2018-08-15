package com.codepiano.services;

import com.codepiano.models.Imitation;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ImitationService {

    public Mono<ServerResponse> imitation(Imitation imitation) {
        return ServerResponse.ok().build();
    }
}
