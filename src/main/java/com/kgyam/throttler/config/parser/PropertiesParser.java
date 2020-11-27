package com.kgyam.throttler.config.parser;

import com.kgyam.throttler.config.RuleConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser implements ConfigParser {
    public RuleConfig load(InputStream in) {
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
