package com.codepiano.models;

import com.codepiano.matchers.Matcher;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rule {

    private String matchType;
    private String mockEntry;
    private Matcher matcher;
    private Mono<ServerResponse> response;

    public Optional<Rule> match(List<String> data) {
        return this.matcher.match(data);
    }
}
