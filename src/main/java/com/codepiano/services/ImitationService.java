package com.codepiano.services;

import com.codepiano.exceptions.IllegalMockResponseFormatException;
import com.codepiano.exceptions.UnsupportedMatcherException;
import com.codepiano.models.Imitation;
import com.codepiano.models.Rule;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.codepiano.MockingBird.BODY;
import static com.codepiano.MockingBird.COOKIES;
import static com.codepiano.MockingBird.DEFAULT_HOST;
import static com.codepiano.MockingBird.HEADERS;
import static com.codepiano.MockingBird.HOST;
import static com.codepiano.MockingBird.RESPONSE;
import static com.codepiano.MockingBird.STATUS;
import static org.springframework.http.ResponseCookie.from;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Service
public class ImitationService {

    private static final List<String> ORDER = List.of("body", "cookies", "path");

    @Autowired
    private MatcherService matcherService;

    @Autowired
    private RuleService ruleService;

    public Optional<Imitation> initImitation(JsonObject jsonObject) {
        var imitation = new Imitation();
        String host;
        if (jsonObject.has(HOST)) {
            host = jsonObject.get(HOST).getAsString();
        } else {
            host = DEFAULT_HOST;
        }
        imitation.setHost(host);

        var finalRule = new Rule();
        var response = initResponse(jsonObject.getAsJsonObject(RESPONSE));
        if (response.isPresent()) {
            finalRule.setResponse(response.get());
        } else {
            throw new IllegalMockResponseFormatException();
        }
        Flux.fromIterable(ORDER)
            .reduce(
                finalRule,
                (previousRule, type) -> {
                    var rule = initRule(jsonObject, type, previousRule);
                    return rule.orElse(previousRule);
                })
            .subscribe(rule -> ruleService.addRuleToHost(host, rule));

        return Optional.of(imitation);
    }

    private Optional<Rule> initRule(JsonObject jsonObject, String key, Rule previousRule) {
        if (jsonObject.has(key)) {
            var value = jsonObject.get(key).getAsJsonObject();
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
     * @param responseData
     * @return
     */
    private Optional<Mono<ServerResponse>> initResponse(JsonObject responseData) {
        // 处理响应码
        var status = HttpStatus.OK.value();
        if (responseData.has(STATUS)) {
            status = responseData.get(STATUS).getAsInt();
            if (HttpStatus.resolve(status) == null) {
                status = HttpStatus.OK.value();
            }
        }
        // 处理 body
        String body = "";
        if (responseData.has(BODY)) {
            body = responseData.get(BODY).getAsString();
            if (StringUtils.isBlank(body)) {
                // 没有指定消息体，修改响应头
                status = HttpStatus.NO_CONTENT.value();
            }
        } else {
            status = HttpStatus.NO_CONTENT.value();
        }
        // 生成响应体
        ServerResponse.BodyBuilder responseBuilder = ServerResponse.status(status);
        // 处理响应头
        if (responseData.has(HEADERS)) {
            Flux.fromIterable(responseData.getAsJsonObject(HEADERS).entrySet())
                .subscribe(header -> responseBuilder.header(header.getKey(), headerValues(header.getValue())));
        }
        // 处理 cookie
        if (responseData.has(COOKIES)) {
            Flux.fromIterable(responseData.getAsJsonObject(COOKIES).entrySet())
                .subscribe(
                    cookie -> responseBuilder.cookie(from(cookie.getKey(), headerValues(cookie.getValue())).build()));
        }
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
