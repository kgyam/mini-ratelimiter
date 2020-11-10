package com.kgyam.ratelimiter.config;

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
