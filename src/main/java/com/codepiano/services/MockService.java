package com.codepiano.services;

import com.codepiano.exceptions.MockingbirdException;
import com.codepiano.exceptions.NoHostRuleException;
import com.codepiano.models.MockRequest;
import com.codepiano.models.MockResponse;
import com.codepiano.models.Rules;
import java.net.NoRouteToHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockService {

    private static final Logger logger = LoggerFactory.getLogger(MockService.class);

    Map<String, Map<String, Rules>> rules = new ConcurrentHashMap<>();

    public MockResponse mock(MockRequest mockRequest) {
        Map<String, Rules> hostRules = rules.get(mockRequest.getMockHost());
        if (hostRules == null || hostRules.isEmpty()) {
            throw new NoHostRuleException("11");
        }
        return new MockResponse();
    }
}
