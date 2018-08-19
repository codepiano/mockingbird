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
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rule {

    private String matchType;
    private Matcher matcher;
    private Mono<ServerResponse> response;

    public Optional<Rule> match(MockRequest mockRequest) {
        return this.matcher.match(mockRequest);
    }

}
