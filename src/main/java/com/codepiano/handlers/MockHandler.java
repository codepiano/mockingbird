package com.codepiano.handlers;

import com.codepiano.models.MockRequest;
import com.codepiano.services.MockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.codepiano.MockingBird.DEFAULT_HOST;
import static com.codepiano.MockingBird.HOST;
import static com.codepiano.MockingBird.PATH;

@Component
@Slf4j
public class MockHandler {

    private static final String HOST_HEADER = "mock-host";

    @Autowired
    private MockService mockService;

    public Mono<ServerResponse> mock(ServerRequest request) {
        var mockRequest = new MockRequest();

        var mockHostHeader = request.headers().header(HOST_HEADER);
        if (!mockHostHeader.isEmpty()) {
            mockRequest.add(HOST, mockHostHeader.get(0));
        } else {
            var host = request.headers().host().getHostString();
            if (StringUtils.isBlank(host)) {
                host = DEFAULT_HOST;
            }
            mockRequest.add(HOST, host);
        }

        var path = request.path();
        mockRequest.add(PATH, path);

        var mockResponse = mockService.mock(mockRequest);

        return mockResponse;
    }
}
