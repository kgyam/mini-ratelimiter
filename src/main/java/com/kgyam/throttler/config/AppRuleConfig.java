package com.kgyam.throttler.config;

import java.util.ArrayList;
import java.util.List;

/**
 * App的配置类
 */
public class AppRuleConfig {

    /*
    用户id,外部得请求需要有对应得appid才能请求
     */

    private String appId;

    /*
    存放app下对应得api限流配置
     */

    List<ApiLimitConfig> apiConfigs = new ArrayList<>();

    public AppRuleConfig() {
    }


    public AppRuleConfig(String appId, List<ApiLimitConfig> apiConfigs) {
        this.appId = appId;
        this.apiConfigs = apiConfigs;
    }

    public List<ApiLimitConfig> getApiConfigs() {
        return apiConfigs;
    }

    public void setApiConfigs(List<ApiLimitConfig> apiConfigs) {
        this.apiConfigs = apiConfigs;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


}
