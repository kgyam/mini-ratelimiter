package com.kgyam.ratelimiter.config.parser;

import com.kgyam.ratelimiter.config.ApiLimitConfig;
import com.kgyam.ratelimiter.config.AppRuleConfig;
import com.kgyam.ratelimiter.config.RuleConfig;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;

public class XmlParser implements ConfigParser {
    private static final String RULE_CONFIG = "ruleConfig";
    private static final String APP_CONFIG = "appConfig";
    private static final String API_CONFIG = "apiConfig";

    public RuleConfig load(InputStream in) {
        XStream xStream = new XStream();
        //将别名与xml名字对应
        xStream.alias(RULE_CONFIG, RuleConfig.class);
        xStream.alias(APP_CONFIG, AppRuleConfig.class);
        xStream.alias(API_CONFIG, ApiLimitConfig.class);
        RuleConfig ruleConfig = (RuleConfig) xStream.fromXML(in);
        return ruleConfig;
    }
}
