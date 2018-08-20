package com.codepiano.services;

import com.codepiano.exceptions.NoHostRuleException;
import com.codepiano.models.MockRequest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.codepiano.MockingBird.HOST;
import static com.codepiano.MockingBird.PATH;

@Service
public class MockService {

    private static final Logger logger = LoggerFactory.getLogger(MockService.class);

    @Autowired
    private RuleService ruleService;

    public Mono<ServerResponse> mock(MockRequest mockRequest) {
        var hostRules = ruleService.getRules(mockRequest.getFirst(HOST));
        if (hostRules == null || hostRules.isEmpty()) {
            throw new NoHostRuleException();
        }
        // 优先处理路径
        var pathRules = hostRules.get(PATH);
        Optional<Mono<ServerResponse>> result = pathRules.process(mockRequest);
        var serverResponse = ServerResponse.ok();
        return serverResponse.build();
    }
}
