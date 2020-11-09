package com.kgyam.ratelimiter.config;

import java.util.List;

/**
 * 整体的限流配置类
 */
public class RuleConfig {
    private String algType;
    List<AppRuleConfig> appConfigs;

    public RuleConfig() {
    }

    public RuleConfig(String algType, List<AppRuleConfig> appConfigs) {
        this.algType = algType;
        this.appConfigs = appConfigs;
    }

    public String getAlgType() {
        return algType;
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }

    public List<AppRuleConfig> getAppConfigs() {
        return appConfigs;
    }

    public void setAppConfigs(List<AppRuleConfig> appConfigs) {
        this.appConfigs = appConfigs;
    }

    @Override
    public String toString() {
        return "RuleConfig{" +
                "algType=" + algType +
                ", appConfigs=" + appConfigs +
                '}';
    }
}
