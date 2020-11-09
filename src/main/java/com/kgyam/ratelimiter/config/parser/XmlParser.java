package com.kgyam.ratelimiter.config.parser;

import com.kgyam.ratelimiter.config.ApiLimitConfig;
import com.kgyam.ratelimiter.config.AppRuleConfig;
import com.kgyam.ratelimiter.config.RuleConfig;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;

public class XmlParser implements ConfigParser {
    public RuleConfig load(InputStream in) {

        XStream xStream = new XStream();
        //将别名与xml名字对应
        xStream.alias("ruleConfig", RuleConfig.class);
        xStream.alias("appConfig", AppRuleConfig.class);
        xStream.alias("apiConfig", ApiLimitConfig.class);
        Object o = xStream.fromXML(in);
        return null;
    }
}
