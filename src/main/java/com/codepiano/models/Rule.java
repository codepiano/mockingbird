package com.codepiano.models;

import com.codepiano.matchers.Matcher;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rule {

    private String matchType;
    private Matcher matcher;
    private MockResponse mockResponse;
    private Function<MockRequest, Object> access;

    public Optional<Rule> match(MockRequest mockRequest) {
        var content = (String) this.getAccess().apply(mockRequest);
        return this.matcher.match(content);
    }
}
