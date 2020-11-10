package com.kgyam.ratelimiter.config.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgyam.ratelimiter.config.RuleConfig;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser implements ConfigParser {
    @Override
    public RuleConfig load(InputStream in) {
        ObjectMapper objectMapper = new ObjectMapper();
        RuleConfig ruleConfig = null;
        try {
            ruleConfig = objectMapper.readValue(in, RuleConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ruleConfig;
    }
}
