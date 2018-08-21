package com.codepiano.services;

import com.codepiano.exceptions.NoHostRuleException;
import com.codepiano.exceptions.NoMatchedMockException;
import com.codepiano.models.MockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.codepiano.MockingBird.HOST;
import static com.codepiano.MockingBird.PATH;

@Service
@Slf4j
public class MockService {

    @Autowired
    private RuleService ruleService;

    public Mono<ServerResponse> mock(MockRequest mockRequest) {
        var hostRules = ruleService.getRules(mockRequest.getFirst(HOST));
        if (hostRules == null || hostRules.isEmpty()) {
            throw new NoHostRuleException();
        }
        // 优先处理路径
        var pathRules = hostRules.get(PATH);
        var result = pathRules.process(mockRequest);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NoMatchedMockException();
        }
    }
}
