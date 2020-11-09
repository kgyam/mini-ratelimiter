package com.kgyam.ratelimiter.config.parser;

import com.kgyam.ratelimiter.config.RuleConfig;

import java.io.InputStream;

public interface ConfigParser {
    RuleConfig load(InputStream in);
}
