package com.codepiano.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rules {

    private static final Logger logger = LoggerFactory.getLogger(Rules.class);

    private List<Rule> ruleList = new ArrayList<>();

    public Optional<Mono<ServerResponse>> process(MockRequest mockRequest) {
        for (Rule rule : this.getRuleList()) {
            Optional<Rule> result = pecking(rule, mockRequest);
            return Optional.of(result.get().getResponse());
        }
        return Optional.empty();
    }

    private Optional<Rule> pecking(Rule rule, MockRequest mockRequest) {
        if (rule != null) {
            if (rule.getResponse() != null) {
                return Optional.of(rule);
            } else {
                var data = mockRequest.get(rule.getMockEntry());
                Optional<Rule> nextRule = rule.match(mockRequest);
                if (nextRule.isPresent()) {
                    return pecking(nextRule.get(), mockRequest);
                }
            }
        }
        return Optional.empty();
    }
}
