package com.kgyam.throttler.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 限流配置类
 */
public class RuleConfig {

    private AlgConfig algConfig;
    List<AppRuleConfig> appConfigs = new ArrayList<>();

    public RuleConfig() {
    }


    public AlgConfig getAlgConfig() {
        return algConfig;
    }

    public void setAlgConfig(AlgConfig algConfig) {
        this.algConfig = algConfig;
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
     *
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
