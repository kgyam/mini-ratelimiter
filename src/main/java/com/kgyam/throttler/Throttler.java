package com.kgyam.throttler;

import com.kgyam.throttler.alg.*;
import com.kgyam.throttler.config.*;
import com.kgyam.throttler.config.parser.ConfigParser;
import com.kgyam.throttler.config.parser.ParserDelegate;
import com.kgyam.throttler.enums.AlgType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流器
 */
public class Throttler {
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

    public Throttler() {
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
     * 将计算器放到容器
     *
     * @param appId
     * @param apiLimitConfig
     * @return
     */
    private synchronized String putCounter(String appId, ApiLimitConfig apiLimitConfig) {

        String key = createKey(appId, apiLimitConfig.getUrl());
        LimitAlg alg = getAlg(apiLimitConfig.getLimit());
        if (alg != null) {
            counterMap.putIfAbsent(key, alg);
        }
        return key;
    }


    private String createKey(String appId, String url) {
        return appId + ":[" + url + "]";
    }

    /**
     * 生成api对应的计算器
     *
     * @return
     */
    private LimitAlg getAlg(int limit) {

        if (ruleConfig == null) {
            return null;
        }
        AlgConfig algConfig = ruleConfig.getAlgConfig();
        if (algConfig == null) {
            return null;
        }
        AlgType algType = algConfig.getAlgType();
        LimitAlg alg = null;
        if (AlgType.FIXED.equals(algType)) {
            alg = new FixedAlg(limit);
        } else if (AlgType.SLIDING.equals(algType)) {
            alg = new SlidingAlg(limit, algConfig.getSpan());
        } else if (AlgType.TOKEN_BUCKET.equals(algType)) {
            alg = new TokenBucketAlg(limit, algConfig.getPermit());
        } else if (AlgType.LEAKY.equals(algType)) {
            alg = new LeakyAlg(limit, algConfig.getRate());
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
            //算法找不到，重新设置，这里有并发问题。putCounter只能控制进程内并发问题
            key = putCounter(appId, apiLimitConfig);
            counter = counterMap.get(key);
        }
        return counter.tryAcquire();
    }

}
