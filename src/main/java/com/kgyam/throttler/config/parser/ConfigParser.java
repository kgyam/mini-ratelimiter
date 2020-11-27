package com.kgyam.throttler.config.parser;

import com.kgyam.throttler.config.RuleConfig;

import java.io.InputStream;

public interface ConfigParser {
    RuleConfig load(InputStream in);
}
