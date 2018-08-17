package com.codepiano.matchers;

import com.codepiano.models.Rule;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class StringMatcher implements Matcher {

    private Map<String, Rule> ruleMap = new ConcurrentHashMap<>();

    public Optional<Rule> match(Object data) {
        var next = ruleMap.get(data);
        if (next == null) {
            return Optional.empty();
        } else {
            return Optional.of(next);
        }
    }
}
