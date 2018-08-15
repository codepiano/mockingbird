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

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rules {

    private static final Logger logger = LoggerFactory.getLogger(Rules.class);

    private List<Rule> ruleList = new ArrayList<>();

    public Optional<MockResponse> process(MockRequest mockRequest) {
        for (Rule rule : this.getRuleList()) {
            pecking(rule.match(mockRequest), mockRequest);
        }
        return Optional.empty();
    }

    private Optional<Rule> pecking(Optional<Rule> rule, MockRequest mockRequest) {
        if (rule.isPresent()) {
            rule = rule.get().match(mockRequest);
            return pecking(rule, mockRequest);
        } else {
            return Optional.empty();
        }
    }
}
