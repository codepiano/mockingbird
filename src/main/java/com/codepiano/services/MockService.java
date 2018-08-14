package com.codepiano.services;

import com.codepiano.exceptions.NoHostRuleException;
import com.codepiano.exceptions.UnsupportedMatcherException;
import com.codepiano.matchers.Matcher;
import com.codepiano.models.MockRequest;
import com.codepiano.models.Rule;
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
    Map<String, Matcher> matchers = new ConcurrentHashMap<>();

    public Mono<ServerResponse> mock(MockRequest mockRequest) {
        var hostRules = rules.get(mockRequest.getMockHost());
        if (hostRules == null || hostRules.isEmpty()) {
            throw new NoHostRuleException();
        }
        // 优先处理路径
        var pathRules = hostRules.get("path");
        Optional<String> result = this.match(pathRules, mockRequest);
        var serverResponse = ServerResponse.ok();
        return serverResponse.build();
    }

    private Optional<String> match(Rules rules, MockRequest mockRequest) {
        for (Rule rule : rules.getRuleList()) {
            var matcher = matchers.get(rule.getMatchType());
            if (matcher == null) {
                logger.error("matcher %s not found!", rule.getMatchType());
                throw new UnsupportedMatcherException();
            }
            var result = matcher.match(rule, mockRequest);
            if (result) {
                return Optional.of(rule.getMockResult());
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
