package com.kgyam.ratelimiter.config.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgyam.ratelimiter.config.RuleConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser implements ConfigParser {
    public RuleConfig load(InputStream in) {
        ObjectMapper objectMapper = new ObjectMapper();
        Properties p = new Properties();
        try {
            p.load(in);

            return new RuleConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
