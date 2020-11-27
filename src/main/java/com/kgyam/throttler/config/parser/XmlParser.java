package com.kgyam.throttler.config.parser;

import com.kgyam.throttler.config.RuleConfig;

import java.io.InputStream;

public class XmlParser implements ConfigParser {
    private static final String RULE_CONFIG = "ruleConfig";
    private static final String APP_CONFIG = "appConfig";

    public RuleConfig load(InputStream in) {
        return null;
    }

}
