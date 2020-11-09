package com.kgyam.ratelimiter;

import com.kgyam.ratelimiter.alg.*;
import com.kgyam.ratelimiter.config.RuleConfig;
import com.kgyam.ratelimiter.config.parser.ConfigParser;
import com.kgyam.ratelimiter.config.parser.ParserDelegate;
import com.kgyam.ratelimiter.enums.AlgType;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    private RuleConfig ruleConfig;
    private ConfigParser parser;
    private static Map<AlgType, LimterAlg> algMap;
    private boolean enable;

    static {
        algMap = new HashMap<>();
        algMap.put(AlgType.Fixed, new FixedAlg());
        algMap.put(AlgType.Sliding, new SlidingAlg());
        algMap.put(AlgType.Token, new TokenAlg());
        algMap.put(AlgType.Leaky, new LeakyAlg());
    }

    public RateLimiter(LimterAlg alg) {
        algMap.put(AlgType.Customer, alg);
        init();
    }


    public RateLimiter() {
        init();

    }


    private void init() {
        parser = new ParserDelegate();
        ruleConfig = parser.load(null);
        if (ruleConfig != null) {
            enable = true;
        }
        // TODO: 2020/11/9  初始化
    }


    private boolean isEnabled() {
        return enable;
    }

    public boolean limit(String appId, String url) {
        if (!isEnabled()) {
            return true;
        }

        return false;
    }

}
