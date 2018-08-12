package com.codepiano.handlers;

import com.codepiano.models.MockRequest;
import com.codepiano.services.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MockHandler {

    private static final String HOST_HEADER = "mock-host";

    @Autowired
    private MockService mockService;

    public Mono<ServerResponse> mock(ServerRequest request) {
        MockRequest mockRequest = new MockRequest();

        List<String> mockHostHeader = request.headers().header(HOST_HEADER);
        if (!mockHostHeader.isEmpty()) {
            mockRequest.setMockHost(mockHostHeader.get(0));
        } else {
            mockRequest.setMockHost(request.headers().host().getHostString());
        }

        String path = request.path();
        mockRequest.setPath(path);

        mockService.mock(mockRequest);

        return ServerResponse.ok().body(Mono.just(request.path()), String.class);
    }

    public Mono<ServerResponse> mock1(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just(request.methodName()), String.class);
    }
}
