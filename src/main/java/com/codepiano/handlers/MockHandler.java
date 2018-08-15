package com.codepiano.handlers;

import com.codepiano.models.MockRequest;
import com.codepiano.services.MockService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MockHandler {

    private static final String HOST_HEADER = "mock-host";
    private static final Logger logger = LoggerFactory.getLogger(MockHandler.class);

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

        var mockResponse = mockService.mock(mockRequest);

        return ServerResponse.ok().body(Mono.just(request.path()), String.class);
    }

}
