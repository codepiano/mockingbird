package com.codepiano.services;

import com.codepiano.exceptions.UnsupportedMatcherException;
import com.codepiano.matchers.Matcher;
import com.codepiano.models.Imitation;
import com.codepiano.models.MockRequest;
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

import static org.springframework.http.ResponseCookie.from;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Service
public class ImitationService {

    @Autowired
    private MatcherService matcherService;

    public Optional<Imitation> initImitation(JsonObject jsonObject) {
        var imitation = new Imitation();
        var host = jsonObject.get("host").getAsString();
        if (StringUtils.isBlank(host)) {
            imitation.setHost(Imitation.DEFAULT_HOST);
        } else {
            imitation.setHost(host);
        }
        var response = generateResponse(jsonObject);

        var path = jsonObject.get("body").getAsJsonObject();
        if (path != null) {
            Rule rule = new Rule();
            rule.setMatchType(jsonObject.get("match-type").getAsString());
            rule.setAccess(MockRequest::getBody);
            Optional<Matcher> matcher = matcherService.getMatcherByType(rule.getMatchType());
            if (matcher.isPresent()) {
            } else {
                throw new UnsupportedMatcherException();
            }
        }
        return Optional.of(imitation);
    }

    /**
     * 构造 mock 响应体
     *
     * @param reponseData
     * @return
     */
    private Mono<ServerResponse> generateResponse(JsonObject reponseData) {
        // 处理响应码
        var status = reponseData.get("status").getAsInt();
        var body = reponseData.get("body").getAsString();
        ServerResponse.BodyBuilder responseBuilder;
        if (HttpStatus.resolve(status) != null) {
            status = HttpStatus.OK.value();
        } else if (StringUtils.isBlank(body)) {
            // 没有指定消息体，修改响应头
            status = HttpStatus.NO_CONTENT.value();
        }
        responseBuilder = ServerResponse.status(status);
        // 处理响应头
        Flux.fromIterable(reponseData.getAsJsonObject("headers").entrySet())
            .subscribe(header -> responseBuilder.header(header.getKey(), headerValues(header.getValue())));
        // 处理 cookie
        Flux.fromIterable(reponseData.getAsJsonObject("cookies").entrySet())
            .subscribe(
                cookie -> responseBuilder.cookie(from(cookie.getKey(), headerValues(cookie.getValue())).build()));
        // 处理响应体
        if (StringUtils.isNoneBlank(body)) {
            return responseBuilder.body(fromObject(body));
        } else {
            return responseBuilder.build();
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
