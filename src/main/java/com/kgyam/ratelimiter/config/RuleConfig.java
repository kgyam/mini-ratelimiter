package com.kgyam.ratelimiter.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.TrieUtils;

import java.util.List;

/**
 * 整体的限流配置类
 */
public class RuleConfig {

    List<AppRuleConfig> appConfigs;

    public RuleConfig() {
    }

    public RuleConfig(List<AppRuleConfig> appConfigs) {
        this.appConfigs = appConfigs;
    }

    public List<AppRuleConfig> getAppConfigs() {
        return appConfigs;
    }

    public void setAppConfigs(List<AppRuleConfig> appConfigs) {
        this.appConfigs = appConfigs;
    }


    /**
     * 优化，匹配字符串效率低
     * @param appId
     * @param url
     * @return
     */
    public ApiLimitConfig getApiLimit(String appId, String url) {
        for (AppRuleConfig appConfig : appConfigs) {
            String configAppId = appConfig.getAppId();
            if (configAppId.equalsIgnoreCase(appId)) {
                List<ApiLimitConfig> apiLimitConfigs = appConfig.getApiConfigs();
                for (ApiLimitConfig apiLimitConfig : apiLimitConfigs) {
                    String configUrl = apiLimitConfig.getUrl();
                    if (configUrl.equalsIgnoreCase(url)) {
                        return apiLimitConfig;
                    }
                }
            }
        }
        return null;
    }
}
