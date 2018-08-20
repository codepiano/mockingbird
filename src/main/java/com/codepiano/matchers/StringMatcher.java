package com.codepiano.matchers;

import com.codepiano.models.Rule;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringMatcher implements Matcher {

    private static final Logger logger = LoggerFactory.getLogger(StringMatcher.class);

    private Map<String, Rule> ruleMap = new ConcurrentHashMap<>();

    public Optional<Rule> match(Object data) {
        var next = ruleMap.get(data);
        return Optional.ofNullable(next);
    }

    @Override
    public boolean addData(String text, Rule rule) {
        Rule overridedRule = ruleMap.put(text, rule);
        if (overridedRule != null) {
            logger.info("rule override, key: {}, rule: {} by new rule: {}", text, overridedRule, rule);
        }
        return true;
    }
}
