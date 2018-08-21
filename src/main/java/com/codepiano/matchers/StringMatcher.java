package com.codepiano.matchers;

import com.codepiano.exceptions.NoRequestDataException;
import com.codepiano.models.Rule;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringMatcher implements Matcher {

    private Map<String, Rule> ruleMap = new ConcurrentHashMap<>();

    public Optional<Rule> match(List<String> data) {
        if (data.isEmpty()) {
            throw new NoRequestDataException();
        }
        var next = ruleMap.get(data.get(0));
        return Optional.ofNullable(next);
    }

    @Override
    public boolean addData(String text, Rule rule) {
        Rule overridedRule = ruleMap.put(text, rule);
        if (overridedRule != null) {
            log.info("rule override, key: {}, rule: {} by new rule: {}", text, overridedRule, rule);
        }
        return true;
    }
}
