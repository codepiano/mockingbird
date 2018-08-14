package com.codepiano.matchers;

import com.codepiano.models.MockRequest;
import com.codepiano.models.MockResponse;
import com.codepiano.models.Rule;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class StringMatcher implements Matcher {

    Map<String, Rule> ruleMap = new ConcurrentHashMap<>();

    @Override
    public Optional<MockResponse> match(Rule rule, MockRequest mockRequest) {
        var content = (String) rule.getAccess().apply(mockRequest);
        if (ruleMap.containsKey(content)) {
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }
}
