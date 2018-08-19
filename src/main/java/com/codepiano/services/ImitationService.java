package com.codepiano.services;

import com.codepiano.MockingBird;
import com.codepiano.exceptions.IllegalMockResponseFormatException;
import com.codepiano.exceptions.UnsupportedMatcherException;
import com.codepiano.models.Imitation;
import com.codepiano.models.Rule;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.codepiano.MockingBird.*;
import static org.springframework.http.ResponseCookie.from;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Service
public class ImitationService {

    private static final List<String> ORDER = List.of("body", "cookies", "path");
    @Autowired
    private MatcherService matcherService;

    public Optional<Imitation> initImitation(JsonObject jsonObject) {
        var imitation = new Imitation();
        var host = jsonObject.get("host").getAsString();
        if (StringUtils.isBlank(host)) {
            imitation.setHost(MockingBird.DEFAULT_HOST);
        } else {
            imitation.setHost(host);
        }

        var finalRule = new Rule();
        var response = initResponse(jsonObject);
        if (response.isPresent()) {
            finalRule.setResponse(response.get());
        } else {
            throw new IllegalMockResponseFormatException();
        }
        Flux.fromIterable(ORDER).reduce(finalRule, (previousRule, type) -> {
            var rule = initRule(jsonObject, type, previousRule);
            return rule.orElse(previousRule);
        }).subscribe();

        return Optional.of(imitation);
    }

    private Optional<Rule> initRule(JsonObject jsonObject, String key, Rule previousRule) {
        var value = jsonObject.get(key).getAsJsonObject();
        if (value != null) {
            var rule = new Rule();
            rule.setMatchType(value.get("match-type").getAsString());
            var matcher = matcherService.getMatcherByType(rule.getMatchType());
            if (matcher.isPresent()) {
                matcher.get().addData(value.get("value").getAsString(), previousRule);
                rule.setMatcher(matcher.get());
                return Optional.of(rule);
            } else {
                throw new UnsupportedMatcherException();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 构造 mock 响应体
     *
     * @param reponseData
     * @return
     */
    private Optional<Mono<ServerResponse>> initResponse(JsonObject reponseData) {
        // 处理响应码
        var status = reponseData.get(STATUS).getAsInt();
        var body = reponseData.get(BODY).getAsString();
        ServerResponse.BodyBuilder responseBuilder;
        if (HttpStatus.resolve(status) != null) {
            status = HttpStatus.OK.value();
        } else if (StringUtils.isBlank(body)) {
            // 没有指定消息体，修改响应头
            status = HttpStatus.NO_CONTENT.value();
        }
        responseBuilder = ServerResponse.status(status);
        // 处理响应头
        Flux.fromIterable(reponseData.getAsJsonObject(HEADERS).entrySet())
                .subscribe(header -> responseBuilder.header(header.getKey(), headerValues(header.getValue())));
        // 处理 cookie
        Flux.fromIterable(reponseData.getAsJsonObject(COOKIES).entrySet())
                .subscribe(
                        cookie -> responseBuilder.cookie(from(cookie.getKey(), headerValues(cookie.getValue())).build()));
        // 处理响应体
        if (StringUtils.isNoneBlank(body)) {
            return Optional.of(responseBuilder.body(fromObject(body)));
        } else {
            return Optional.of(responseBuilder.build());
        }
    }

    private String headerValues(JsonElement value) {
        String result;
        if (value.isJsonArray()) {
            List<String> valueList = new ArrayList<>();
            value.getAsJsonArray().iterator().forEachRemaining(x -> valueList.add(x.getAsString()));
            result = String.join(",", valueList);
        } else {
            result = value.getAsString();
        }
        return result;
    }
}
