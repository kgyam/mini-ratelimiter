package com.kgyam.ratelimiter.config.parser;

import com.kgyam.ratelimiter.config.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YmlParser implements ConfigParser {
    public RuleConfig load(InputStream in) {
        Yaml yaml = new Yaml();
        RuleConfig ruleConfig = yaml.loadAs(in, RuleConfig.class);
        System.out.println(ruleConfig.getClass());
        System.out.println(ruleConfig);
        return ruleConfig;
    }
}
