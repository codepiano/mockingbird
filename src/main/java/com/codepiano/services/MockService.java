package com.codepiano.services;

import com.codepiano.exceptions.MockingbirdException;
import com.codepiano.models.MockRequest;
import com.codepiano.models.MockResponse;
import com.codepiano.models.Rules;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockService {

    Map<String, Map<String, Rules>> rules = new ConcurrentHashMap<>();

    public MockResponse mock(MockRequest mockRequest) {
        Map<String, Rules> hostRules = rules.get(mockRequest.getMockHost());
        if (hostRules == null || hostRules.isEmpty()) {
            throw new MockingbirdException();
        }
        return new MockResponse();
    }
}
