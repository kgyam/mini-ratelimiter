package com.kgyam.ratelimiter;

import com.kgyam.ratelimiter.alg.*;
import com.kgyam.ratelimiter.config.*;
import com.kgyam.ratelimiter.config.parser.ConfigParser;
import com.kgyam.ratelimiter.config.parser.ParserDelegate;
import com.kgyam.ratelimiter.enums.AlgType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    /*
    规则配置
     */
    private RuleConfig ruleConfig;
    /*
    配置解析器
     */
    private ConfigParser parser;
    /*
    计算器容器
     */
    private static Map<String, LimitAlg> counterMap = new ConcurrentHashMap<>();
    /*
    限流器开关
     */
    private boolean enable;

    public RateLimiter() {
        init();
    }


    private void init() {
        parser = new ParserDelegate();
        ruleConfig = parser.load(null);
        if (ruleConfig != null) {
            enable = true;
        }
        initCounterMap();
    }


    private void initCounterMap() {
        if (ruleConfig == null) {
            return;
        }
        List<AppRuleConfig> appLst = ruleConfig.getAppConfigs();
        for (AppRuleConfig appRuleConfig : appLst) {
            String appId = appRuleConfig.getAppId();
            List<ApiLimitConfig> apiLimitLst = appRuleConfig.getApiConfigs();
            for (ApiLimitConfig apiLimitConfig : apiLimitLst) {
                putCounter(appId, apiLimitConfig);
            }
        }
    }


    /**
     * 插入到计算器容器
     *
     * @param appId
     * @param apiLimitConfig
     * @return
     */
    private String putCounter(String appId, ApiLimitConfig apiLimitConfig) {
        String key = createKey(appId, apiLimitConfig.getUrl());
        LimitAlg alg = getAlg(apiLimitConfig);
        counterMap.putIfAbsent(key, alg);
        return key;
    }


    private String createKey(String appId, String url) {
        return appId + ":[" + url + "]";
    }

    /**
     * 生成api对应的计算器
     *
     * @param apiLimitConfig
     * @return
     */
    private LimitAlg getAlg(ApiLimitConfig apiLimitConfig) {
        AlgConfig algConfig = apiLimitConfig.getAlgConfig();
        String type = algConfig.getAlgType();
        LimitAlg alg = null;
        if (AlgType.FIXED.name().equalsIgnoreCase(type)) {
            alg = new FixedAlg(apiLimitConfig.getLimit());
        } else if (AlgType.SLIDING.name().equalsIgnoreCase(type)) {
            SlidingAlgConfig _slidingAlgConfig = (SlidingAlgConfig) algConfig;
            alg = new SlidingAlg(apiLimitConfig.getLimit(), _slidingAlgConfig.getSpan());
        } else if (AlgType.TOKEN.name().equalsIgnoreCase(type)) {
            TokenBucketAlgConfig _tokenBucketAlgConfig = (TokenBucketAlgConfig) algConfig;
            alg = new TokenAlg(apiLimitConfig.getLimit(), _tokenBucketAlgConfig.getPermit());
        } else if (AlgType.LEAKY.name().equalsIgnoreCase(type)) {
        }
        return alg;
    }

    private boolean isEnabled() {
        return enable;
    }


    /**
     * 限流算法入口
     *
     * @param appId
     * @param url
     * @return
     */
    public boolean tryAcquire(String appId, String url) {
        if (!isEnabled()) {
            return true;
        }
        ApiLimitConfig apiLimitConfig = ruleConfig.getApiLimit(appId, url);
        if (apiLimitConfig == null) {
            return true;
        }
        String key = createKey(appId, url);
        LimitAlg counter = counterMap.get(key);
        if (counter == null) {
            //算法找不到，重新设置
            key = putCounter(appId, apiLimitConfig);
            counter = counterMap.get(key);
        }

        return counter.tryAcquire();
    }

}
