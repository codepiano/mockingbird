package com.codepiano.services;

import com.codepiano.models.Rule;
import com.codepiano.models.Rules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    Map<String, Map<String, Rules>> rulesMap = new ConcurrentHashMap<>();

    public Map<String, Rules> getRules(String host) {
        return rulesMap.get(host);
    }

    public boolean addRuleToHost(String host, Rule rule) {
        var hostRules = rulesMap.get(host);
        if (hostRules == null) {
            List<Rule> ruleList = new ArrayList<>();
            ruleList.add(rule);
            Rules rules = new Rules();
            rules.setRuleList(ruleList);
            hostRules = new HashMap<>();
            hostRules.put(rule.getMatchType(), rules);
            rulesMap.put(host, hostRules);
        } else {
            Rules rules = hostRules.get(rule.getMatchType());
            if(rules == null) {
                List<Rule> ruleList = new ArrayList<>();
                ruleList.add(rule);
                rules = new Rules();
                rules.setRuleList(ruleList);
                hostRules.put(rule.getMatchType(), new Rules());
            } else {
                rules.getRuleList().add(rule);
            }
        }
        return true;
    }
}
