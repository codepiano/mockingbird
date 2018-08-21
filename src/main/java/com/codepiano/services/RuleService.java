package com.codepiano.services;

import com.codepiano.models.Rule;
import com.codepiano.models.Rules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RuleService {

    Map<String, Map<String, Rules>> rulesMap = new ConcurrentHashMap<>();

    public Map<String, Rules> getRules(String host) {
        return rulesMap.get(host);
    }

    public boolean addRuleToHost(String host, Rule rule) {
        var hostRules = rulesMap.computeIfAbsent(host, k -> new HashMap<>());
        var rules = hostRules.computeIfAbsent(rule.getMockEntry(), k -> new Rules());
        var ruleList = rules.getRuleList();
        if (ruleList == null) {
            ruleList = new ArrayList<>();
            ruleList.add(rule);
            rules.setRuleList(ruleList);
        } else {
            ruleList.add(rule);
        }
        return true;
    }
}
