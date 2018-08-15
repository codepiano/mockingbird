package com.codepiano.services;

import com.codepiano.exceptions.NoHostRuleException;
import com.codepiano.models.MockRequest;
import com.codepiano.models.MockResponse;
import com.codepiano.models.Rules;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class MockService {

    private static final Logger logger = LoggerFactory.getLogger(MockService.class);

    Map<String, Map<String, Rules>> rules = new ConcurrentHashMap<>();

    public Mono<ServerResponse> mock(MockRequest mockRequest) {
        var hostRules = rules.get(mockRequest.getMockHost());
        if (hostRules == null || hostRules.isEmpty()) {
            throw new NoHostRuleException();
        }
        // 优先处理路径
        var pathRules = hostRules.get("path");
        Optional<MockResponse> result = pathRules.process(mockRequest);
        var serverResponse = ServerResponse.ok();
        return serverResponse.build();
    }
}
