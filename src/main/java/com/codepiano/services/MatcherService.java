package com.codepiano.services;

import com.codepiano.matchers.Matcher;
import com.codepiano.matchers.StringMatcher;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatcherService {
    private static final Map<String, Matcher> matchers =
        new ConcurrentHashMap<>() {
            {
                put("string", new StringMatcher());
            }
        };

    public Optional<Matcher> getMatcherByType(String type) {
        return Optional.ofNullable(matchers.get(type));
    }
}
