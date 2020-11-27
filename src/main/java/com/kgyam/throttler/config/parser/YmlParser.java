package com.kgyam.throttler.config.parser;

import com.kgyam.throttler.config.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YmlParser implements ConfigParser {
    public RuleConfig load(InputStream in) {
        if (in == null) {
            return null;
        }
        return new Yaml().loadAs(in, RuleConfig.class);
    }
}
